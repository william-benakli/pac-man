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

#include "player_lobby.h"
#include "game_settings.h"
#include "list_game.h"
#include "join_game.h"
#include "leave_game.h"

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
int sendGodBye(int socketclient);
int sendError(int socketclient);

extern struct list_game *_games;

pthread_mutex_t verrou= PTHREAD_MUTEX_INITIALIZER;

#endif