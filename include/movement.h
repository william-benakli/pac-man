#ifndef MOVEMENT_H
#define MOVEMENT_H

#include "serveur.h"
#include "list_game.h"
#include "join_game.h"
#include "labyrinthlogic.h"

int ingame(struct player *player, struct list_game *_games);
int move(int direction, char * distance, struct game * game, struct participant *player);

#endif