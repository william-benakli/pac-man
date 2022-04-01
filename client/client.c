#include "../include/client.h"

int create_client(struct* client client, char[8] identifiant, int udp_sock, int tcp_sock) {
	// TODO: Verifier si l'ID n'est pas deja utilise
	// TODO: Verifier si udp et tcp sock ne sont pas deja utilisé
	client->identifiant = identifiant;
	if(udp_sock<0) {
		return -1;
	}
	client->udp_sock = udp_sock;
	if(tcp_sock<0) {
		return -1;
	}
	client->tcp_sock = tcp_sock;
}