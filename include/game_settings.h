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
};


int init_game(struct game *_game, int hauteur, int largeur, char **labyrinth);
int check_ready(struct game *_game);

#endif