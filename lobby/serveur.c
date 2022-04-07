#include "../include/serveur.h"

uint8_t nombre_games;
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

  int rep_party = sendgames(socket);
  if(rep_party == -1){
    perror("Erreur games invalide");
    goto erreur;
  }

  char buffer[7];
  buffer[6] = '\0';
  read(socket, buffer, SIZE_INPUT_DEFAULT);

  if(strcmp(buffer, CMD_NEW_PARTY) == 0){
    int rep = creategame(socket); 
    if(rep == -1){
      perror("Erreur creation de la game à échoué");
      goto erreur;
    }
  }else if(strcmp(buffer, CMD_REGISTER) == 0){

  }else if(strcmp(buffer,"START ") == 0){

  }else{ 
    perror("Erreur arguments non conforme");
    goto erreur;
  }

  close(socket);
  free(client_connect);
  return NULL;

  erreur:
    close(socket);
    free(client_connect);
    exit(EXIT_FAILURE);
    return NULL;
}

int sendgames(int socketclient){
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
      while(listgames_courant->next_game != NULL){
        //Pour chaque partie on enrengistre [OGAME m s***]
        memmove(mess_game+size_games + (it_games*size_ogame), "OGAME ", SIZE_INPUT_DEFAULT);
        memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT), &listgames_courant->game->id_partie, sizeof(uint8_t));
        memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT)+1, " ", sizeof(char)*1);
        memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT)+sizeof(uint8_t)+1, &listgames_courant->game->players,sizeof(uint8_t));
        memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT)+sizeof(uint8_t)+1+sizeof(uint8_t), "***", SIZE_INPUT_STAR);
        listgames_courant = listgames_courant->next_game;
        it_games++;
      }
  }
  printf("%d NOMBRE GAME CREATION\n", nombre_games);
  int count =  write(socketclient, mess_game, size_max);
  return count == size_max ? 0 : -1;
}

int creategame(int socketclient){

    char arguments[SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR];
    read(socketclient, arguments, SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR);
    
    char identifiant[SIZE_IDENTIFIANT+1];
    identifiant[SIZE_IDENTIFIANT] = '\0';
    char port[SIZE_PORT+1];
    port[SIZE_PORT] = '\0';
    memmove(identifiant, arguments, SIZE_IDENTIFIANT);
    memmove(port, arguments+SIZE_IDENTIFIANT, sizeof(int));

    //fausse partie
    struct game *game = malloc(sizeof(struct game));
    game->id_partie = 2;
    game->players = 5;
    int rep = add_game(game, _games);
    nombre_games++;
    printf("[CREATE PARTY] Joueur %s Port %s", identifiant, port);
    return rep;
}