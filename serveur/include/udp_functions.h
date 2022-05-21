#ifndef UDP_FUNCTIONS_H
#define UDP_FUNCTIONS_H

#include "serveur.h"

#define ADDRESS_MULTICAST "250.0.0.1"
extern int __UDP_PORT;

int mutilcast_message(struct game *_game, char *buf);
int score_message(struct game *_game, struct participant *player, int fantom_x , int fantom_y);
int ghost_message(struct game *_game, int fantom_x , int fantom_y);
int end_message(struct game *_game, struct participant *winner);
int group_message(struct game *_game, struct participant *sender, char *message);
int private_message(struct game *_game, char *target_identifiant, char *message, struct participant *sender);
int get_mall_message(int clientsocket, char * message_buffer);
int get_private_message(int clientsocket, char *target_identifiant, char * message_buffer);
#endif