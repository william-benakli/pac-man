#ifndef SERVEURGAME_H
#define SERVEURGAME_H

#include "serveur.h"
#include "serveur_lobby.h"

int gameInput(int socketclient, struct participant *partcipant_ingame, struct game *game_courant);
void move_by_action(char * direction, char * distance,  struct game *game_courant, struct participant * partcipant_ingame);
int remove_player(struct participant *leaver, struct game *game);

#endif