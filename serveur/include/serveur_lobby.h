#ifndef SERVEURLOBBY_H
#define SERVEURLOBBY_H

#include "serveur.h"
#include "serveur_game.h"
#include "utils/variables_mutex.h"

int registerInput(struct player *player);
void launch_game(struct game * game_courant, struct participant * partcipant_lobby, struct list_game *_games);
int select_action(struct player * player, char *buffer);
int waiting_players(struct player *player);

#endif