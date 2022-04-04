#include "../include/game_settings.h"


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define GAME_CREATION_SUCCESS 0 
#define PLAYER_JOIN_SUCCESS 0
#define PLAYERS_READY 1
#define PLAYERS_NOT_READY 0


int init_game(struct game *_game, int hauteur, int largeur, int max_players, char **labyrinth){
    _game->id_partie = 0; //TODO STATIC ALLOCATION OF ID 
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->max_players = max_players;
    _game->labyrinth = labyrinth; //TODO: gerer le transision
    _game->joueurs = NULL;
    return GAME_CREATION_SUCCESS;
}       

int player_join(struct game *_game, struct player player, struct participant *new_player){
    //struct participant *new_player = (struct participant *)malloc(sizeof(struct participant));
    memmove(new_player->identifiant,player.identifiant,8);
    new_player->pos_x = 0;
    new_player->pos_y = 0;
    new_player->score = 0;
    new_player->player_ready = 0;
    new_player->tcp_sock = player.tcp_sock;
    new_player->udp_sock = player.udp_sock;
    new_player->next = _game->joueurs;
    _game->joueurs = new_player;
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

int main(){
    return 0;
}