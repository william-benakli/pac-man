#ifndef REPONSE_GAME_H
#define REPONSE_GAME_H

#include "../serveur.h"

int sendSize(int socketclient);
int sendList(int socketclient);
int sendUnRegOk(int socketclient, u_int8_t id_partie);
int sendRegOk(int socketclient, u_int8_t id_partie);

#endif