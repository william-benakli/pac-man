#include "../../include/serveur.h"

struct list_game * _games;

int main(int argc, char const *argv[]) {
  _games = init_list_game();
   srand(time(NULL));

  int port = atoi(argv[1]);
  if(port == 0){
    perror("Erreur argument invalide port");
    exit(EXIT_FAILURE);
  }
  
//TODO verifier le socket de connexion DATAGRAM 
  int connexion = socket(PF_INET,SOCK_STREAM,0);
  struct sockaddr_in address_sock;
  address_sock.sin_family= AF_INET;
  address_sock.sin_port= htons(port);
  address_sock.sin_addr.s_addr= htonl(INADDR_ANY);


  int r = bind(connexion, (struct sockaddr *)&address_sock ,sizeof(struct  sockaddr_in));
  if(r == -1){
    perror("Erreur à blind, port invalide ");
    exit(EXIT_FAILURE);
  }

  int listenning = listen(connexion, 0);
  if(listenning == -1){
    perror("Erreur à l'écoute (listen) ");
    exit(EXIT_FAILURE);
  }

  while(1){
    struct player *client_connexion = malloc(sizeof(struct player));
    struct sockaddr_in caller;
    socklen_t size = sizeof(caller);
    int socketclient = accept(connexion,(struct sockaddr *)&caller,&size);
    init_player(client_connexion, socketclient);
    pthread_t client;
    pthread_create(&client,NULL, clientConnexion, client_connexion);
  }
  return 0;
}

void *clientConnexion(void * client_connect){

  int socket = ((struct player *)client_connect)->tcp_sock;

  int rep_party = sendgames(socket);
  if(rep_party == -1){
    perror("Erreur games invalide");
    goto erreur;
  }

  int reponse_register = registerInput((struct player *)client_connect);

  if(reponse_register == -1)goto erreur;
  else if(reponse_register == 0)goto success;
  
  close(socket);
  free(client_connect);
  return NULL;

  erreur:
    close(socket);
    free(client_connect);
    exit(EXIT_FAILURE);
    return NULL;

  success:
    close(socket);
    free(client_connect);
    exit(EXIT_SUCCESS);
    return NULL;
}

int registerInput(struct player * player){
    int socketclient = player->tcp_sock;

    char buffer[SIZE_INPUT_DEFAULT+1];
    buffer[SIZE_INPUT_DEFAULT] = '\0';
    read(socketclient, buffer, SIZE_INPUT_DEFAULT);
    
    if(strcmp(buffer, CMD_NEW_PARTY) == 0){
        int rep_create = creategame(player, _games); 
        if(rep_create == -1){
          sendRegNo(socketclient);
          registerInput(player);
        }else{
          sendRegOk(socketclient, player->game_id);
          registerInput(player);
        }
    }else if(strcmp(buffer, CMD_REGISTER) == 0){
        int rep_regis = regisgame(player, _games); 
        if(rep_regis == -1){
          sendRegNo(socketclient);
          registerInput(player);
        }else{
          sendRegOk(socketclient, player->game_id);
          registerInput(player);
        } 
    //TODO: Command en partie : on devrait peut être separer en deux fonctions ?
    }else if(strcmp(buffer, CMD_UNREG) == 0){

      int check_stars = readStars(socketclient);
      if(check_stars == -1){
        sendDunno(socketclient, "CMD UNREG READSTARS ERROR");
        registerInput(player);
      }

      if(player->status_game == IN_LOBBY){
        sendDunno(socketclient, "UNREG ERREUR PLAYER EN LOBBY");
        registerInput(player);
      }

      u_int8_t id_partie = player->game_id;
      int rep_unreg = unregis(player, _games);
      if(rep_unreg == -1){
        sendDunno(socketclient, "UNREG ERREUR UNREG PLAYER LIST");
        registerInput(player);
      }
      int rep_regOk = sendUnRegOk(socketclient, id_partie);
      if(rep_regOk == -1){
        sendDunno(socketclient, "UNREG ERREUR UNREGOK FAIL");
        registerInput(player);
      }
      registerInput(player);
    }else if(strcmp(buffer, CMD_GAME) == 0){
      int check_stars = readStars(socketclient);
      if(check_stars == -1){
        sendDunno(socketclient, "CMD GAME READSTART ERROR");
        registerInput(player);
      }
      int rep_party = sendgames(socketclient);
      if(rep_party == -1){
        sendDunno(socketclient, "CMD GAME ENVOIE GAME ECHEC");
        registerInput(player);
      }
      registerInput(player);
    }else if(strcmp(buffer, CMD_SIZE) == 0){

      int rep_size = sendSize(socketclient, _games);
      if(rep_size == -1){
        sendDunno(socketclient, "CMD SIZE sendSIZE FAIL");
        registerInput(player);
      }
      registerInput(player);

    }else if(strcmp(buffer, CMD_LIST) == 0){

      int rep_list = sendList(socketclient, _games);
      if(rep_list == -1){
        sendDunno(socketclient,"CMD LIST sendList fail");
        registerInput(player);
      }
      registerInput(player);
    }else if(strcmp(buffer, CMD_START) == 0){

      int rep_list = start(player, _games);
      if(rep_list == -1){
        sendDunno(socketclient,"CMD START FAILED");
        registerInput(player);
      }

      //On recupere la partie
      //On recupere le joueur dans la partie
      struct game * target_game = search_game(player->game_id, _games);
      if(target_game == NULL) registerInput(player);
      
      struct participant * partcipant_lobby = search_player_in_game(target_game, player);
      if(partcipant_lobby == NULL) registerInput(player);

        sendWelcome(target_game, partcipant_lobby, _games);
        spawnFantomes(target_game);
        printf("Positiosement des fantomes\n");
        spawnJoueur(target_game, partcipant_lobby);
        printf("Positiosement du joueur\n");
        gameInput(partcipant_lobby, target_game);
    }else{
      perror("Erreur arguments non conforme");
      sendDunno(socketclient, "ERREUR ENTREE INCONNUE");
      registerInput(player);
    }
  return 0;
  }

int readStars(int socketclient){
  char stars[4];
  stars[3] = '\0';
  int count = read(socketclient, stars, SIZE_INPUT_STAR);
  if(strcmp(stars, "***") != 0)return -1;
  return count == (SIZE_INPUT_STAR) ? 0 : -1 ;
}

void gameInput(struct participant *partcipant_ingame, struct game *game_courant){

  int socketclient = partcipant_ingame->tcp_sock;
  size_t size_buffer = SIZE_INPUT_DEFAULT_SPACE+SIZE_DISTANCE+SIZE_INPUT_STAR;
  char buffer[size_buffer];
  int count = read(socketclient, buffer, size_buffer);
  if(count != size_buffer){
    gameInput(partcipant_ingame, game_courant);
  }
  
  printlabyrinth(game_courant);

  char direction[SIZE_INPUT_DEFAULT + 1];
  direction[SIZE_INPUT_DEFAULT] = '\0';
  char distance[SIZE_DISTANCE];
  char stars[SIZE_INPUT_STAR];
  stars[SIZE_INPUT_STAR] = '\0';

  memmove(direction, buffer, SIZE_INPUT_DEFAULT);
  memmove(distance, buffer+SIZE_INPUT_DEFAULT+SIZE_ONE_SPACE, SIZE_DISTANCE);
  memmove(stars, buffer+SIZE_INPUT_DEFAULT+SIZE_ONE_SPACE+SIZE_DISTANCE, SIZE_INPUT_STAR);

  if(strcmp("***", stars) != 0){
    gameInput(partcipant_ingame, game_courant);
  } //protocol pas respecté

    if(strcmp(direction, CMD_RIMOV) == 0){
      int rep_move = move(MOVERIGHT, distance, game_courant, partcipant_ingame);
      if(rep_move == -1){
        gameInput(partcipant_ingame, game_courant);
      }
    }else if(strcmp(direction, CMD_LEMOV) == 0){
      int rep_move = move(MOVELEFT, distance, game_courant, partcipant_ingame);
      if(rep_move == -1){
        gameInput(partcipant_ingame, game_courant);
      }
    }else if(strcmp(direction, CMD_UPMOV) == 0){
      int rep_move = move(MOVEUP, distance, game_courant, partcipant_ingame);
      if(rep_move == -1){
        gameInput(partcipant_ingame, game_courant);
      }
    }else if(strcmp(direction, CMD_DOMOV) == 0){
      int rep_move = move(MOVEDOWN, distance, game_courant, partcipant_ingame);
      if(rep_move == -1){
        gameInput(partcipant_ingame, game_courant);
      }

/* Essayer de comprendre quand utiliser IQUIT */
 //   }else if(strcmp(buffer, CMD_IQUIT) == 0){
      //Premier verification toujours des personnes dans la partie
      //Deuxieme verification quand on joueur prend un fantome s'il reste dans la partie
    }else{
      perror("Erreur arguments en partie non conforme");
      gameInput(partcipant_ingame, game_courant);
    }    
}