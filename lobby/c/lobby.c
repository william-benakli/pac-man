#include "../../include/game_settings.h"

uint8_t id_games_static = 0;

int init_game(struct game *_game, uint16_t hauteur, uint16_t largeur, char **labyrinth){
    
    _game->id_partie = id_games_static; //TODO STATIC ALLOCATION OF ID 
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->max_player = 0;
    _game->labyrinth = labyrinth; //TODO: gerer le transision
    _game->joueurs = NULL;
    //TODO: METTRE UN VERROU
    if(id_games_static > 255){
        return GAME_CREATION_FAILED;
    }
    id_games_static++;
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