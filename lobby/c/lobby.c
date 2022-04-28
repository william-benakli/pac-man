#include "../../include/game_settings.h"

uint8_t id_games_static = 0;

int init_game(struct game *_game, uint16_t hauteur, uint16_t largeur, char **labyrinth){
    
    _game->id_partie = id_games_static; //TODO STATIC ALLOCATION OF ID 
    id_games_static++;
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->max_player = 0;
    _game->labyrinth = labyrinth; //TODO: gerer le transision
    _game->participants = NULL;
    //TODO: METTRE UN VERROU
    if(id_games_static > 255){
        return GAME_CREATION_FAILED;
    }
    return GAME_CREATION_SUCCESS;
}       

int free_game(struct game *game){
    int rep = free_participant(game->participants);
    if(rep == -1)return -1;
    game = NULL;
    free(game);
    return 0;
}

int free_participant(struct participant *premier){
	struct participant *copy = premier;
	struct participant *next = NULL;

	while(copy != NULL){
		next = copy->next;
        copy = NULL;
		free(copy);
		copy = next;
	}
    return 0;
}

int check_ready(struct game *_game){
    
    struct participant *cpy = _game->participants;
    
    while (cpy != NULL){
        if (cpy->player_ready == 0){
            //TODO: snprintf le pseudo de ceux qui sont pas prets sur un buffer
            return PLAYERS_NOT_READY;
        }
        cpy = cpy->next;
    }
    return PLAYERS_READY;
}