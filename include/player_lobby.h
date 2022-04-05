#ifndef PLAYER_LOBBY_H
#define PLAYER_LOBBY_H

#include <stdio.h>
#include <stdlib.h>
#define NOT_IN_GAME 0;
#define IN_GAME 0;

struct player {
	char identifiant[8];
	int udp_sock;
	int tcp_sock;
	int is_in_game;
};

int init_player(struct player *player, char identifiant[8], int udp_sock, int tcp_sock);

#endif