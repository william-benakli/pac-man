#ifndef LIST_GAME_H
#define LIST_GAME_H

#include "game_settings.h"

struct list_game{
    struct game *game;
    struct list_game *next_game;

};

struct list_game * init_list_game();
int add_game(struct game *game,struct list_game *list);
int remove_game(struct game *game, struct list_game *list);
uint8_t size_game_available(struct list_game *list);

#endif