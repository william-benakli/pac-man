#include "../../include/serveur.h"
#include "../../include/utils/variables_mutex.h"

struct list_game * _games;
int __UDP_PORT;

pthread_mutex_t verrou1;
pthread_mutex_t verrou2;

int main(int argc, char const *argv[]) {
  pthread_mutex_init(&verrou1,NULL);
  pthread_mutex_init(&verrou2,NULL);
  __UDP_PORT = 4242;
  _games = init_list_game();
   srand(time(NULL));

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
    init_player(client_connexion, socketclient, &caller);
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
    goto close;
  }

  registerInput((struct player *)client_connect);
  close(socket);
  free(client_connect);
  return NULL;
  
  close: 
    close(socket);
    free(client_connect);
    return NULL;
}

int readStars(int socketclient){
  char stars[4];
  stars[3] = '\0';
  int count = read(socketclient, stars, SIZE_INPUT_STAR);
  if(strcmp(stars, "***") != 0)return -1;
  return count == (SIZE_INPUT_STAR) ? 0 : -1 ;
}