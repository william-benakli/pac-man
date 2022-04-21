#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <time.h>
#include <ctype.h>

#define MAX_NAME 10

void randomName(char *pseudo);

int main(int argc, char const *argv[]) {
  srand(time(NULL));

  if(argc < 3){
    perror("Erreur argument manquant : ./client1 [adress] [port] | exemple : ./client1 localhost 4545");
    exit(EXIT_FAILURE);
  } 

  int port = atoi(argv[2]);
  if(port == 0){
    perror("Erreur argument invalide port");
    exit(EXIT_FAILURE);
  }

  int socket_const = port;
  struct sockaddr_in adresse;
  adresse.sin_port = htons(socket_const);
  adresse.sin_family = AF_INET;
  inet_aton(argv[1],&adresse.sin_addr);

    int descr=socket(PF_INET,SOCK_STREAM,0);
    int connexion = connect(descr, (struct sockaddr *)&adresse, sizeof(struct sockaddr_in));

    if(connexion !=-1){
        
        while(1){
          char reponseClient[100];
          read(descr, reponseClient, 100);
          int t = 0;
          for(int i = 0; i < 100; i++){
              if(isalpha(reponseClient[i])){
                  printf("%c", reponseClient[i]);
                  //t++;
              }else{
                  if(reponseClient[i] == '*'){
                      printf("*");
                      t++;
                  }else if(reponseClient[i] == ' '){
                      printf(" ");
                      t++;
                  }else{
                      t++;
                      printf("%d", reponseClient[i]);
                  }
              }
          }
          printf("dzzzzdzd%ddsssdsd\n", t);
          char scanmsg[23];
          scanf("%s",scanmsg); 
          printf("Vous avez entré %s \n",scanmsg);
          //"NEWPL WILLIAM7 5454***"
          write(descr, scanmsg, 7+8+3+4);
        }
        printf("\n");
    }else{
         perror("Erreur de connexion au serveur");
     }
    close(descr);
    
  return 0;
}