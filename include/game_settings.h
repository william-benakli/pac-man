#ifndef GAME_SETTINGS_H
#define GAME_SETTINGS_H

#include "player_ingame.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct game{
    uint8_t id_partie;
    uint8_t players;
    uint8_t max_player;
    uint16_t hauteur;
    uint16_t largeur;
    
    char **labyrinth;
    enum { STATUS_AVAILABLE, STATUS_UNAVAILABLE} status;
    struct participant *joueurs;
};


int init_game(struct game *_game, int hauteur, int largeur, char **labyrinth);
int check_ready(struct game *_game);

#endif