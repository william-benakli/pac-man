#include "../include/player_nolobby.h"

int create_client(struct player *player, char * identifiant, int udp_sock, int tcp_sock) {
	// TODO: Verifier si l'ID n'est pas deja utilise
	// TODO: Verifier si udp et tcp sock ne sont pas deja utilisé
    memmove(player->identifiant, identifiant,8);
	if(udp_sock<0) {
		return -1;
	}
	player->udp_sock = udp_sock;
	if(tcp_sock<0) {
		return -1;
	}
	player->tcp_sock = tcp_sock;
}