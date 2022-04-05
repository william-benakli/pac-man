#ifndef GAME_SETTINGS_H
#define GAME_SETTINGS_H

#include "player_ingame.h"

#include <stdio.h>
#include <stdlib.h>

struct game{
    int id_partie;
    int hauteur;
    int largeur;
    int max_players;
    char **labyrinth;
    struct participant *joueurs;
    struct game *next;
};


int init_game(struct game *_game, int hauteur, int largeur, int max_players, char **labyrinth);
int player_join(struct game *_game, struct player player, struct participant *new_player);
int check_ready(struct game *_game);

#endif