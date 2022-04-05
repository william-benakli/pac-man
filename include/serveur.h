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

#define SIZE_IDENTIFIANT 8
#define SIZE_PORT 4

void *clientConnexion(void * client);
int sendWelcome(int socketclient);

pthread_mutex_t verrou= PTHREAD_MUTEX_INITIALIZER;

#endif