#ifndef PLAYER_LOBBY_H
#define PLAYER_LOBBY_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct player {
	int udp_port;
	int tcp_sock;
    enum {IN_GAME, IN_LOBBY} status_game;
	uint8_t game_id;
};

int init_player(struct player *player, int tcp_sock);

#endif