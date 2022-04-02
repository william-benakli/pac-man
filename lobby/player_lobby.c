#include "../include/player_lobby.h"

#include <string.h>

int init_player(struct player *client, char identifiant[8], int udp_sock, int tcp_sock) {

	// TODO: Verifier si l'ID n'est pas deja utilise
	// TODO: Verifier si udp et tcp sock ne sont pas deja utilisé
	strcpy(client->identifiant,identifiant);
	if(udp_sock<0) {
		return -1;
	}
	client->udp_sock = udp_sock;
	if(tcp_sock<0) {
		return -1;
	}
	client->tcp_sock = tcp_sock;

	client->is_in_game = NOT_IN_GAME;
	return 0;
}