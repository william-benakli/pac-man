#ifndef CLIENT_H
#define CLIENT_H

#include <stdio.h>
#include <stdlib.h>

struct player {
	char identifiant[8];
	int udp_sock;
	int tcp_sock;
};


int init_player(struct player *client, char identifiant[8], int udp_sock, int tcp_sock);




#endif
