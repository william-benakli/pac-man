#include "./player_nolobby.h"
#include "./player_inlobby.h"

#include <stdio.h>
#include <stdlib.h>


struct lobby{
    int id_partie;
    int hauteur;
    int largeur;
    int max_players;
    char **labyrinth;
    struct participant *joueurs;
};


int init_lobby(struct lobby game, int hauteur, int largeur, int max_players, char **labyrinth);

