#include "../../include/list_game.h"

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
    if(games->next_game != NULL)
    free(games);
}

//ajout un jeu a la fin de liste
int add_game(struct game *game,struct list_game *list){
    struct list_game *copy = list;
    if(copy->game == NULL){
        list->game = game;
        return GAME_CREATED_SUCCESSFULLY;
    } 

    while(copy->next_game != NULL){
        copy = copy->next_game;
    }
    struct list_game * list_next = init_list_game();
    list_next->game = game;

    copy->next_game = list_next;

   // nombre_games++;
    return GAME_CREATED_SUCCESSFULLY;
}

//supprime un jeu de liste
int remove_game(struct game *rem_game, struct list_game *list){
    struct list_game *copy = list;

    if(copy->game == NULL){
        return GAME_FAILED_REMOVAL;
    }

    if(copy->game->id_partie == rem_game->id_partie){
        list->next_game = list->next_game->next_game;
        return GAME_REMOVED_SUCCESSFULLY;
    }

    while(copy->next_game != NULL){
        if (copy->next_game->game->id_partie == rem_game->id_partie){
            copy->next_game = copy->next_game->next_game;
            return GAME_REMOVED_SUCCESSFULLY;
        }
    }

    return GAME_FAILED_REMOVAL;
}

//parcourir une liste et remplir ret avec la jeu de meme id_valeur comme id 

/*
int main(){
    struct list_game *test = malloc(sizeof(struct list_game));
    test->game = NULL;
    test->next_game = NULL;

    struct game *game = malloc(sizeof(struct game));
    game->id_partie = 1;
    game->joueurs = NULL;
    game->hauteur = NULL;
    game->labyrinth = NULL;
    game->max_player = NULL;
    game->players = NULL;
    add_game(game,test);
    add_game(game,test);
    add_game(game,test);

    printf("%d",test->game->id_partie);
    printf("%d",test->next_game->game->id_partie);
    printf("%d",test->next_game->next_game->game->id_partie);

}
*/
