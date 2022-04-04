#include "player_ingame.h"

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
struct lobbylist{
    struct lobby data;
    struct lobbylist *next;
}

struct lobbylist *listOfLobby;
int nombre_lobby;

int init_lobby(struct lobby game, int hauteur, int largeur, int max_players, char **labyrinth);
int new_game(struct lobby *game);
void* listGames();