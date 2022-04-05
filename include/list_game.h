#ifndef LIST_GAME_H
#define LIST_GAME_H

#include "game_settings.h"

struct list_game{
    struct game *game;
    struct list_game *next_game;
};


int add_game(struct list_game *game,struct list_game *list);
int remove_game(struct game *game, struct list_game *list);
int search_game(int id, struct list_game *list, struct game *ret); 

#endif