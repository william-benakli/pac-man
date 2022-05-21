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
#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <time.h>

#include "list_game.h"
#include "leave_game.h"
#include "utils/reponse_lobby.h"
#include "utils/reponse_serveur.h"
#include "utils/reponse_game.h"
#include "movement.h"
#include "labyrinthlogic.h"
#include "serveur_lobby.h"
#include "serveur_game.h"
#include "udp_functions.h"

#define SIZE_ONE_SPACE 1
#define SIZE_IDENTIFIANT 8
#define SIZE_PORT 4
#define SIZE_INPUT_DEFAULT 5
#define SIZE_INPUT_DEFAULT_SPACE 6 
#define SIZE_INPUT_STAR 3
#define SIZE_DISTANCE 3
#define SIZE_IP_ADRESSE 15
#define SIZE_POS_Y 3
#define SIZE_POS_X 3

#define CMD_REGISTER "REGIS"
#define CMD_START "START"
#define CMD_NEW_PARTY "NEWPL"
#define CMD_GAME "GAME?"
#define CMD_UNREG "UNREG"
#define CMD_SIZE "SIZE?"
#define CMD_LIST "LIST?"

#define CMD_IQUIT "IQUIT"
#define CMD_RIMOV "RIMOV"
#define CMD_UPMOV "UPMOV"
#define CMD_DOMOV "DOMOV"
#define CMD_LEMOV "LEMOV"

void *clientConnexion(void * client);
int readStars(int socketclient);


extern struct list_game *_games;
extern int port_static;

#endif