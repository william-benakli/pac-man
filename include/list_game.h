#ifndef LIST_GAME_H
#define LIST_GAME_H

#include "game_settings.h"

struct list_game{
    struct game *first_game;
    struct game *current_game;
};

int add_game(struct game *game,struct list_game *list);
int remove_game(struct game *game, struct list_game *list);
struct game *search_game(int id, struct list_game *list); 

#endif