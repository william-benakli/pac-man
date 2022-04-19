#ifndef JOIN_GAME_H
#define JOIN_GAME_H

#include <regex.h>

#include "./list_game.h"
#include "./game_settings.h"
#include "./player_lobby.h"
#include "serveur.h"

#define GAME_FOUND 0
#define GAME_NOT_FOUND 1
#define PLAYER_JOIN_SUCCESS 0
#define PLAYER_REGISTER_SUCCESS 0
#define PLAYER_REGISTER_FAILURE 1


int player_join(struct game *_game, struct player *player, struct participant *new_player_ingame);
int search_game(int id, struct list_game *list, struct game *ret); 
int regis(struct player *client,struct list_game *games);

#endif