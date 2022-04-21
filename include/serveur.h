#ifndef SERVEUR_H
#define SERVEUR_H

#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <time.h>
#include <pthread.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>

#include "list_game.h"
#include "leave_game.h"

#define SIZE_ONE_SPACE 1
#define SIZE_IDENTIFIANT 8
#define SIZE_PORT 4
#define SIZE_INPUT_DEFAULT 5
#define SIZE_INPUT_DEFAULT_SPACE 6 
#define SIZE_INPUT_STAR 3

#define CMD_REGISTER "REGIS"
#define CMD_START "START"
#define CMD_NEW_PARTY "NEWPL"
#define CMD_GAME "GAME?"
#define CMD_UNREG "UNREG"
#define CMD_SIZE "SIZE?"
#define CMD_LIST "LIST?"
#define CMD_IQUIT "IQUIT"

void *clientConnexion(void * client);
int registerInput(struct player *player);
int creategame(struct player * player, struct list_game * games);

int sendgames(int socketclient);
int sendSize(int socketclient);
int sendList(int socketclient);

int sendGodBye(int socketclient);
int sendDunno(int socketclient);

int sendRegNo(int socketclient);

int sendUnRegOk(int socketclient, u_int8_t id_partie);
int sendRegOk(int socketclient, u_int8_t id_partie);

int readStars(int socketclient);

extern struct list_game *_games;

//pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;

#endif