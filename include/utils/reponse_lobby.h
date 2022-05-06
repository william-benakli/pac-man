#ifndef REPONSE_LOBBY_H
#define REPONSE_LOBBY_H

#include "../serveur.h"

int creategame(struct player * player, struct list_game * games);
int sendgames(int socketclient);
int regisgame(struct player *client,struct list_game *games);
int player_game_accept(struct player *player, struct list_game *list);

#endif