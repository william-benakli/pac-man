#ifndef REPONSE_SERV_H
#define REPONSE_SERV_H

#include "../serveur.h"

int sendGodBye(int socketclient);
int sendDunno(int socketclient, char * s);
int sendRegNo(int socketclient);

#endif