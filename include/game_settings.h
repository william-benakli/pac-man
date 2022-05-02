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
    uint8_t id_partie;
    uint8_t players;
    uint8_t max_player;
    uint16_t hauteur;
    uint16_t largeur;
    
    char **labyrinth;
    enum { STATUS_AVAILABLE, STATUS_UNAVAILABLE} status;
    struct participant *participants;
};

int init_game(struct game * game, uint16_t hauteur, uint16_t largeur);
void * search_player_in_game(struct game * target_game, struct player *player);
int free_game(struct game *game);
int check_ready(struct game * game);

#endif