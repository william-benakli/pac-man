#include "../../../include/utils/reponse_serveur.h"

int sendDunno(int socketclient, char * s){
  printf("Erreur du DUNNOO %s\n", s);
  char * buffer = "DUNNO***";
  int count = write(socketclient, buffer, SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR);
  return count == (SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR) ? 0 : -1;
}

int sendRegNo(int socketclient){
  char * buffer = "REGNO***";
  int count = write(socketclient, buffer, SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR);
  return count == (SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR) ? 0 : -1;
}

int sendGodBye(int socketclient){
  char * buffer = "GOBYE***";
  int count = write(socketclient, buffer, SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR);
  return count == (SIZE_INPUT_DEFAULT+SIZE_INPUT_STAR) ? 0 : -1 ;
}
