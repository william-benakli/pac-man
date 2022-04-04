#include "../include/serveur.h"

int main(int argc, char const *argv[]) {

  int port = atoi(argv[1]);
  if(port == 0){
    perror("Erreur argument invalide port");
    exit(EXIT_FAILURE);
  }

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

    //create_client(&client_connexion);

    pthread_t client;
    pthread_create(&client,NULL, clientConnexion, client_connexion);
  }
  return 0;
}

void *clientConnexion(void * client_connect){

  int socket = ((struct player *)client_connect)->tcp_sock;
  char pseudo[MAX_IDENTIFIANT];

  int rep_party = sendParty(socket);
  if(rep_party == -1){
    perror("Erreur party invalide");
    goto erreur;
  }

  listGames();
  char buffer[6];
  buffer[5] = '\0';

  read(socket, buffer, 5);

  if(strcmp(buffer,"REGIS") == 0){
    //inscription à une partie  
    registerGame();
  }else if(strcmp(buffer,"NEWPL") == 0){
      //creation de la m!
  }else if(strcmp(buffer,"START") == 0){
      //Lancement de la partie s'il y'en a 
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

int sendGame(int socketclient){
    //TODO: envoyer la liste des parties disponibles au joueur
    while(listOfLobby->next != NULL){
        write(socketclient, "GAMES", 10);
    }
}
