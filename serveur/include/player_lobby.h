#ifndef PLAYER_LOBBY_H
#define PLAYER_LOBBY_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//#include "serveur.h"
struct player {
	int udp_port;
	int tcp_sock;
    enum {IN_GAME, IN_LOBBY} status_game;
	u_int8_t game_id;
	char *player_address; 
};

int init_player(struct player *player, int tcp_sock, struct sockaddr_in *client);

#endif