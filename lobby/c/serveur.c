#include "../../include/serveur.h"

struct list_game * _games;

//pour compiler gcc -pthread -Wall -o serveur serveur.c player_lobby.c list_game.c ../game/lobby.c

int main(int argc, char const *argv[]) {
  _games = init_list_game();

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

  printf("Ici tout est ok\n");
  int rep_party = sendgames(socket);
  if(rep_party == -1){
    perror("Erreur games invalide");
    goto erreur;
  }

  printf("Ici tout est ok\n");
  int reponse_register = registerInput(socket);
  if(reponse_register == -1){
    goto erreur;
  }else if(reponse_register == 0){
    goto success;
  }

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

int registerInput(int socketclient){
    char buffer[7];
    buffer[6] = '\0';
    read(socketclient, buffer, SIZE_INPUT_DEFAULT);

    if(strcmp(buffer, CMD_NEW_PARTY) == 0){
      int rep_create = creategame(socketclient); 
      if(rep_create == -1){
        sendError(socketclient);
        registerInput(socketclient);
      } 
      //faire renjoindre la party au joueur
    }else if(strcmp(buffer, CMD_REGISTER) == 0){

    }else if(strcmp(buffer, CMD_IQUIT) == 0){
      sendGodBye(socketclient);
      return 0;
    }else{
      perror("Erreur arguments non conforme");
      sendError(socketclient);
      registerInput(socketclient);
    }
  return 0;
  }

int sendError(int socketclient){
  char * buffer = "DUNNO***";
  int count = write(socketclient, buffer, SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR -1);
  return count == (SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR -1) ? -1:0;
}

int sendGodBye(int socketclient){
  char * buffer = "GOBYE***";
  int count = write(socketclient, buffer, SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR -1);
  return count == (SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR -1) ? -1:0;
}


int sendgames(int socketclient){
  int nombre_games = size_game_available(_games);
  int size_games = SIZE_INPUT_DEFAULT + sizeof(uint8_t) + SIZE_INPUT_STAR;
  int size_ogame = SIZE_INPUT_DEFAULT + sizeof(uint8_t)*2 + 1 + SIZE_INPUT_STAR;
  int size_max = size_games + nombre_games*(size_ogame);
  char mess_game[size_max];
  //On deplace [GAMES n***] dans le buffer
  memmove(mess_game, "GAMES ", SIZE_INPUT_DEFAULT+1);
  memmove(mess_game+(SIZE_INPUT_DEFAULT), &nombre_games, sizeof(uint8_t));
  memmove(mess_game+(SIZE_INPUT_DEFAULT) + sizeof(uint8_t), "***", SIZE_INPUT_STAR);

  struct list_game *listgames_courant = _games;

  if(listgames_courant->game != NULL){
      int it_games = 0;
      while(listgames_courant->game != NULL){
        //Pour chaque partie on enrengistre [OGAME m s***]
        if(listgames_courant->game->status == STATUS_AVAILABLE){
          memmove(mess_game+size_games + (it_games*size_ogame), "OGAME ", SIZE_INPUT_DEFAULT);
          memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT), &listgames_courant->game->id_partie, sizeof(uint8_t));
          memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT)+1, " ", sizeof(char)*1);
          memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT)+sizeof(uint8_t)+1, &listgames_courant->game->players,sizeof(uint8_t));
          memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT)+sizeof(uint8_t)+1+sizeof(uint8_t), "***", SIZE_INPUT_STAR);
          it_games++;
        }
       listgames_courant = listgames_courant->next_game;
      }
  }
  printf("%d NOMBRE GAME CREATION\n", nombre_games);
  int count =  write(socketclient, mess_game, size_max);
  return count == size_max ? 0 : -1;
}

int creategame(int socketclient){

    char arguments[SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR];
    int count = read(socketclient, arguments, SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR);
    if(count != SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR)return -1;
    
    char identifiant[SIZE_IDENTIFIANT+1];
    identifiant[SIZE_IDENTIFIANT] = '\0';
    char port[SIZE_PORT+1];
    port[SIZE_PORT] = '\0';
    char stars[SIZE_INPUT_STAR+1];
    stars[SIZE_INPUT_STAR] = '\0';

    memmove(identifiant, arguments, SIZE_IDENTIFIANT);
    memmove(port, arguments+SIZE_IDENTIFIANT, sizeof(int));
    memmove(stars, arguments+SIZE_IDENTIFIANT+sizeof(int), SIZE_INPUT_STAR);

    if(strcmp(stars, "***") != 0)return -1;
    
    struct game *game = malloc(sizeof(struct game));
    int rep_init = init_game(game, 10, 10, NULL);
    if(rep_init == -1)return -1;
    
    printf("[CREATE PARTY] Joueur %s Port %s", identifiant, port);
    //TODO: AJOUTER UN PLAYER
    int rep_add = add_game(game, _games);
    if(rep_add == -1)return -1;
    
    return 0;
}
