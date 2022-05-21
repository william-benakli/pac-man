#ifndef PLAYER_INGAME_H
#define PLAYER_INGAME_H

#include "player_lobby.h"

#include <stdio.h>
#include <stdlib.h>
struct participant{
	char identifiant[8];
	int udp_port;
	int tcp_sock;
    int player_ready;
    int pos_x;
    int pos_y;
    int score; 
    struct participant *next;
    char *address;
};

//int init_player_participant(struct player player, struct participant participant, int udp_sock, int tcp_sock, 
//                            int player_ready, int pos_x, int pos_y, int score);

#endif