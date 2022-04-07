#include "../include/game_settings.h"

#define GAME_CREATION_SUCCESS 0 
#define PLAYER_JOIN_SUCCESS 0
#define PLAYERS_READY 1
#define PLAYERS_NOT_READY 0


int init_game(struct game *_game, int hauteur, int largeur, char **labyrinth){
    _game->id_partie = 0; //TODO STATIC ALLOCATION OF ID 
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->players = 0;
    _game->labyrinth = labyrinth; //TODO: gerer le transision
    _game->joueurs = NULL;
    return GAME_CREATION_SUCCESS;
}       

int player_join(struct game *_game, struct player player, struct participant *new_player_ingame){
    //struct participant *new_player = (struct participant *)malloc(sizeof(struct participant));
    new_player_ingame->pos_x = 0;
    new_player_ingame->pos_y = 0;
    new_player_ingame->score = 0;
    new_player_ingame->player_ready = 0;
    new_player_ingame->tcp_sock = player.tcp_sock;
    new_player_ingame->next = _game->joueurs;
    _game->joueurs = new_player_ingame;
    return PLAYER_JOIN_SUCCESS;
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