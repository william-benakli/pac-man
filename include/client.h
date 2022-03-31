#ifndef CLIENT_H
#define CLIENT_H

#include <stdio.h>
#include <stdlib.h>

int create_client(struct* client client, char[8] identifiant, int udp_sock, int tcp_sock);

struct client {
	char[8] identifiant;
	int udp_sock;
	int tcp_sock;
};

#endif
