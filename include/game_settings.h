#ifndef GAME_SETTINGS_H
#define GAME_SETTINGS_H

#include "player_ingame.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct game{
    u_int8_t id_partie;
    u_int8_t players;
    u_int8_t max_player;
    int hauteur;
    int largeur;
    char **labyrinth;
    struct participant *joueurs;
    enum { STATUS_AVAILABLE, STATUS_UNAVAILABLE} status;
};


int init_game(struct game *_game, int hauteur, int largeur, char **labyrinth);
int player_join(struct game *_game, struct player player, struct participant *new_player);
int check_ready(struct game *_game);

#endif