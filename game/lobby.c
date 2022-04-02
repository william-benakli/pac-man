#include "../include/lobby_settings.h"


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LOBBY_CREATION_SUCCESS 0 
#define PLAYER_JOIN_SUCCESS 0
#define PLAYERS_READY 1
#define PLAYERS_NOT_READY 0


int init_lobby(struct lobby game, int hauteur, int largeur, int max_players, char **labyrinth){
    game.id_partie = 0; //TODO STATIC ALLOCATION OF ID 
    game.hauteur = hauteur;
    game.largeur = largeur;
    game.max_players = max_players;
    game.labyrinth = labyrinth;
    game.joueurs = NULL;
    return LOBBY_CREATION_SUCCESS;
}       

int player_join(struct lobby game, struct player player){
    struct participant *new_player = (struct participant *)malloc(sizeof(struct participant));
    memmove(new_player->identifiant,player.identifiant,8);
    new_player->pos_x = 0;
    new_player->pos_y = 0;
    new_player->score = 0;
    new_player->player_ready = 0;
    new_player->tcp_sock = player.tcp_sock;
    new_player->udp_sock = player.udp_sock;
    new_player->next = game.joueurs;
    game.joueurs = new_player;
    return PLAYER_JOIN_SUCCESS;
}

int check_ready(struct lobby game){
    
    struct participant *cpy = game.joueurs;
    
    while (cpy != NULL){
        if (cpy->player_ready == 0){
            //TODO: snprintf le pseudo de ceux qui sont pas prets sur un buffer
            return PLAYERS_NOT_READY;
        }
        cpy = cpy->next;
    }
    return PLAYERS_READY;
}