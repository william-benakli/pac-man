#ifndef REPONSE_GAME_H
#define REPONSE_GAME_H

#include "../serveur.h"

int sendSize(int socketclient, struct list_game *_games);
int sendList(int socketclient, struct list_game *_games);
int sendUnRegOk(int socketclient, u_int8_t id_partie);
int sendRegOk(int socketclient, u_int8_t id_partie);
int sendGlist(int socketclient, struct game *current_game);
int sendWelcome(struct game *game, struct participant *participant, struct list_game *list_game);
int sendPosit(int client_socket, struct game *game , struct participant *participant);

#endif