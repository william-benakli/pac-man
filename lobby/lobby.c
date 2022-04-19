#include "../include/game_settings.h"

#define GAME_CREATION_SUCCESS 0 
#define PLAYERS_READY 1
#define PLAYERS_NOT_READY 0

int id_games = 0;

int init_game(struct game *_game, int hauteur, int largeur, char **labyrinth){
    
    _game->id_partie = id_games; //TODO STATIC ALLOCATION OF ID 
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->max_player = 0;
    _game->labyrinth = labyrinth; //TODO: gerer le transision
    _game->joueurs = NULL;
    //TODO: METTRE UN VERROU
    id_games++;
    return GAME_CREATION_SUCCESS;
}       


int check_ready(struct game *_game){
    
    struct participant *cpy = _game->joueurs;
    
    while (cpy != NULL){
        if (cpy->player_ready == 0){
            //TODO: snprintf le pseudo de ceux qui sont pas prets sur un buffer
            return PLAYERS_NOT_READY;
        }
        cpy = cpy->next;
    }
    return PLAYERS_READY;
}