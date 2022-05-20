#ifndef LABYRINTH_LOGIC_H
#define LABYRINTH_LOGIC_H

#include "../include/game_settings.h"
#include "udp_functions.h"

#define CASEVIDE '0'
#define MUR '#'
#define JOUEUR '1'
#define FANTOME 'f'

#define MOVELEFT 1 
#define MOVERIGHT 2 
#define MOVEUP 3 
#define MOVEDOWN 4

#define NOMBRE_FANTOME 10

#define FINISH 0
#define NOT_FINISH 1

char  **initlabirynth(uint16_t x, uint16_t y);
void printlabyrinth(struct game *_game);
void freelabirynth(struct game *game);
int moveinlabyrinth(int direction, int steps, struct game *game, struct participant *player);
int spawnJoueur(struct game *game, struct participant *participant);
int spawnFantomes(struct game *game);
char getElementAtPos(struct game *game, int x, int y);
int setElementAtPos(struct game *game, char c, int x, int y);
int setParticipantAtPos(struct game *game, struct participant *participant, int x, int y);
int check_endgame(struct game *game);
struct participant* find_winner(struct game *game);

#endif