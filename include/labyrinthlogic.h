#ifndef LABYRINTH_LOGIC_H
#define LABYRINTH_LOGIC_H

#include "../include/game_settings.h"

#define CASEVIDE = '0'
#define MUR = '#'
#define JOUEUR = '1'

#define MOVELEFT 1 
#define MOVERIGHT 2 
#define MOVEUP 3 
#define MOVEDOWN 4

#define FANTOME 10

char  **initlabirynth(int x, int y);
void freelabirynth(struct game *game);
int moveinlabyrinth(int direction, int steps, struct game *game, struct participant *player);


#endif