#ifndef GAME_SETTINGS_H
#define GAME_SETTINGS_H

#include "player_ingame.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define GAME_CREATION_SUCCESS 0
#define GAME_CREATION_FAILED -1 
#define PLAYERS_READY 1
#define PLAYERS_NOT_READY 0

struct game{
    u_int8_t id_partie;
    u_int8_t players;
    u_int8_t max_player;
    u_int16_t hauteur;
    u_int16_t largeur;
    
    char **labyrinth;
    enum { STATUS_AVAILABLE, STATUS_UNAVAILABLE} status;
    struct participant *joueurs;
};


int init_game(struct game *_game, uint16_t hauteur, uint16_t largeur, char **labyrinth);
int check_ready(struct game *_game);

#endif