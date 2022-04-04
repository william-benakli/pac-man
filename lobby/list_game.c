#include "../include/list_game.h"

#define GAME_CREATED_SUCCESSFULLY 0 
#define GAME_FAILED_CREATION 1
#define GAME_FAILED_REMOVAL 1 
#define GAME_REMOVED_SUCCESSFULLY 0


int add_game(struct game *game,struct list_game *list){
    if(list->first_game == NULL){
        list->first_game = game;
        return GAME_CREATED_SUCCESSFULLY;
    } 

    while(list->current_game->next != NULL){
        list->current_game = list->current_game->next;
    }

    list->current_game->next = game;
    list->current_game = list->first_game;
    return GAME_CREATED_SUCCESSFULLY;
}

int remove_game(struct game *game, struct list_game *list){
    if(list->first_game == NULL){
        return GAME_FAILED_REMOVAL;
    }

    if(list->first_game->id_partie == game->id_partie){
        list->current_game = list->first_game->next;
        list->first_game = list->first_game->next;
        return GAME_REMOVED_SUCCESSFULLY;
    }

    while(list->current_game->next != NULL){
        if (list->current_game->next->id_partie == game->id_partie){
            list->current_game->next = list->current_game->next->next;
            list->current_game = list->first_game;
            return GAME_REMOVED_SUCCESSFULLY;
        }
    }

    return GAME_FAILED_REMOVAL;
}