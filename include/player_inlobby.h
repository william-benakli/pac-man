#include "./player_nolobby.h"

#include <stdio.h>
#include <stdlib.h>


struct participant{
	char identifiant[8];
	int udp_sock;
	int tcp_sock;
    int player_ready;
    int pos_x;
    int pos_y;
    int score; 
    struct participant *next; 
};

int init_player_participant(struct player client, struct participant participant, int udp_sock, int tcp_sock, 
                            int player_ready, int pos_x, int pos_y, int score);
