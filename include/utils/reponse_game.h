#ifndef REPONSE_GAME_H
#define REPONSE_GAME_H

#include "../serveur.h"

int sendSize(int socketclient, struct list_game *_games);
int sendList(int socketclient, struct list_game *_games);
int sendUnRegOk(int socketclient, u_int8_t id_partie);
int sendRegOk(int socketclient, u_int8_t id_partie);
int sendGlist(struct player *player, struct list_game *list);
int sendWelcome(struct game *game, struct list_game *list_game);

#endif