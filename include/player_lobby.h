#ifndef PLAYER_LOBBY_H
#define PLAYER_LOBBY_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NOT_IN_GAME 0;
#define IN_GAME 0;

struct player {
	int udp_port;
	int tcp_sock;
	int is_in_game;
};

int init_player(struct player *player, int tcp_sock);

#endif