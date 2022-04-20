#ifndef LEAVE_GAME_H
#define LEAVE_GAME_H

#include "list_game.h"
#include "join_game.h"

#define GAME_LEAVE_SUCCESS 0;
#define GAME_LEAVE_FAILURE 1;
#define PLAYER_UNREG_SUCCESS 0;
#define PLAYER_UNREG_FAILURE 1;

int player_leave(struct game *_game, struct player *player);
int unregis(struct player *client, struct list_game *games);

#endif