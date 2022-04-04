#include "../include/list_game.h"

#define GAME_CREATED_SUCCESSFULLY 0 
#define GAME_FAILED_CREATION 1
#define GAME_FAILED_REMOVAL 1 
#define GAME_REMOVED_SUCCESSFULLY 0
#define GAME_FOUND 0
#define GAME_NOT_FOUND 1


//ajout un jeu a la fin de liste
int add_game(struct game *game,struct list_game *list){
    struct list_game *add;
    add->game = game;
    add->next_game = NULL;

    struct list_game *copy = list;
    if(copy->game == NULL){
        copy->game = game;
        return GAME_CREATED_SUCCESSFULLY;
    } 

    while(copy->next_game != NULL){
        copy = copy->next_game;
    }

    copy->next_game = add;
    return GAME_CREATED_SUCCESSFULLY;
}

//supprime un jeu de liste
int remove_game(struct game *game, struct list_game *list){
    struct list_game *copy = list;

    if(copy->game == NULL){
        return GAME_FAILED_REMOVAL;
    }

    if(copy->game->id_partie == game->id_partie){
        list->game = list->next_game;
        return GAME_REMOVED_SUCCESSFULLY;
    }

    while(copy->next_game != NULL){
        if (copy->next_game->game->id_partie == game->id_partie){
            copy->next_game = copy->next_game->next_game;
            return GAME_REMOVED_SUCCESSFULLY;
        }
    }

    return GAME_FAILED_REMOVAL;
}

//parcourir une liste et remplir ret avec la jeu de meme id_valeur comme id 
int search_game(int id, struct list_game *list, struct game *ret){
    struct list_game *copy = list;

    while(copy != NULL){
        if (copy->game->id_partie == id){
            ret = copy->game;
            return GAME_FOUND;
        } 
        copy = copy->next_game;
    }

    return GAME_NOT_FOUND;
}