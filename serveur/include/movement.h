#ifndef MOVEMENT_H
#define MOVEMENT_H

#include "list_game.h"
#include "join_game.h"
#include "labyrinthlogic.h"
#include "serveur.h"
#include "game_settings.h"

int move(int direction, char * distance, struct game * game, struct participant *player);
int deplace_fantom(struct game *game);


#endif