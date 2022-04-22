#include "../../include/player_lobby.h"

int init_player(struct player *client, int tcp_sock){
	if(tcp_sock<0) {
		return -1;
	}
	client->tcp_sock = tcp_sock;
	client->status_game = IN_LOBBY;
	client->game_id = -1;
	return 0;
}