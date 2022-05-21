#include "../../include/player_lobby.h"

int init_player(struct player *player, int tcp_sock, struct sockaddr_in *client){
	if(tcp_sock<0) {
		return -1;
	}
	player->tcp_sock = tcp_sock;
	player->status_game = IN_LOBBY;
	player->game_id = -1;
	player->player_address = inet_ntoa(client->sin_addr);
	return 0;
}
