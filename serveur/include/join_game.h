#ifndef JOIN_GAME_H
#define JOIN_GAME_H

#include <regex.h>

#include "list_game.h"
#include "serveur.h"

#define GAME_FOUND 0
#define GAME_NOT_FOUND -1
#define PLAYER_JOIN_SUCCESS 0
#define PLAYER_REGISTER_SUCCESS 0
#define PLAYER_REGISTER_FAILURE -1


int player_join(struct game *_game, struct player *player, struct participant *new_player_ingame);
void * search_game(uint8_t id, struct list_game *list);
int register_game(struct player *client, char * identifiant, uint8_t room_id_game, struct list_game *games);
#endif