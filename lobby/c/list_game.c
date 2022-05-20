#include "../../include/list_game.h"
#include "../../include/utils/variables_mutex.h"

#define GAME_CREATED_SUCCESSFULLY 0 
#define GAME_FAILED_CREATION 1
#define GAME_FAILED_REMOVAL 1 
#define GAME_REMOVED_SUCCESSFULLY 0

struct list_game * init_list_game(){
    struct list_game * game = malloc(sizeof(struct list_game));
    game->game = NULL;
    game->next_game=NULL;
    return game;
}

void free_list_game(struct list_game *games){
    struct list_game *copy = games;
    struct list_game *next = NULL;

    while(copy != NULL){
        next = copy->next_game;
        copy = NULL;
        free_game(copy->game);
        copy= next;
    }
    free(games);

}

int add_game(struct game *addgame, struct list_game *list){
    pthread_mutex_lock(&verrou1);

        struct list_game *copy = list;
        if(copy->game == NULL){
            list->game = addgame;
            pthread_mutex_unlock(&verrou1);
            return GAME_CREATED_SUCCESSFULLY;
        } 

        while(copy->next_game != NULL){
            copy = copy->next_game;
        }
        struct list_game * list_next = init_list_game();
        list_next->game = addgame;

        copy->next_game = list_next;

    pthread_mutex_unlock(&verrou1);
    return GAME_CREATED_SUCCESSFULLY;
}

//supprime un jeu de liste
int remove_game(struct game *rem_game, struct list_game *list){
    pthread_mutex_lock(&verrou1);
    struct list_game *copy = list;

    if(copy->game == NULL){
        pthread_mutex_unlock(&verrou1);
        return GAME_FAILED_REMOVAL;
    }

    if(copy->game->id_partie == rem_game->id_partie){
        list->next_game = list->next_game->next_game;
        pthread_mutex_unlock(&verrou1);
        return GAME_REMOVED_SUCCESSFULLY;
    }

    while(copy->next_game != NULL){
        if (copy->next_game->game->id_partie == rem_game->id_partie){
            copy->next_game = copy->next_game->next_game;
            pthread_mutex_unlock(&verrou1);
            return GAME_REMOVED_SUCCESSFULLY;
        }
    }
    pthread_mutex_unlock(&verrou1);
    return GAME_FAILED_REMOVAL;
}

uint8_t size_game_available(struct list_game *list){
    pthread_mutex_lock(&verrou1);
    struct list_game *copy = list;
    uint8_t nombre_party_available = 0;
    if(copy->game == NULL){
        pthread_mutex_unlock(&verrou1);
        return 0;
    }
    while(copy != NULL){
        if (copy->game->status == STATUS_AVAILABLE)nombre_party_available++;
        copy = copy->next_game;
    }
    pthread_mutex_unlock(&verrou1);
    return nombre_party_available;
}