
#include "../include/game_settings.h"

#define CASEVIDE = '0'
#define MUR = '#'
#define JOUEUR = '1'

#define MOVELEFT 1 
#define MOVERIGHT 2 
#define MOVEUP 3 
#define MOVEDOWN 4

#define FANTOME 10

char  **initlabirynth(int x, int y){
    char **labyrinth = (char **) malloc(y * sizeof(char *));
    for (int i = 0; i < y; i++){
        labyrinth[i] = (char *) malloc(x * sizeof(char));
    }

    return labyrinth;
}

void freelabirynth(struct game *game){
    for (int i = 0; i < game->hauteur; i++){
        free(game->labyrinth[i]);
    }
    free(game->labyrinth);
}

int moveinlabyrinth(int direction, int steps, struct game *game, struct participant *player){
    int stepsmoved = 0;
    int ghostfound = 0;
    switch (direction)
    {
    case MOVELEFT:
        for(int i = 1; i <= steps; i++){
            if (player->pos_x - i < 0 || game->labyrinth[player->pos_x - i][player->pos_y] != '0' 
                || game->labyrinth[player->pos_x - i][player->pos_y] != 'f' ){
                break;
            } 
            if (game->labyrinth[player->pos_x - i][player->pos_y] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_x - i][player->pos_y] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_x - stepsmoved][player->pos_y] = '1';
        game->labyrinth[player->pos_x][player->pos_y] = '0';
        player->pos_x = player->pos_x - stepsmoved;
        break;

    case MOVERIGHT: 
        for(int i = 1; i <= steps; i++){
            if (player->pos_x - i < 0 || game->labyrinth[player->pos_x + i][player->pos_y] != '0' 
                || game->labyrinth[player->pos_x + i][player->pos_y] != 'f' ){
                break;
            }
            if (game->labyrinth[player->pos_x - i][player->pos_y] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_x - i][player->pos_y] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_x + stepsmoved][player->pos_y] = '1';
        game->labyrinth[player->pos_x][player->pos_y] = '0';
        player->pos_x = player->pos_x + stepsmoved;
        break;

    case MOVEUP:
        for(int i = 1; i <= steps; i++){
            if (player->pos_x - i < 0 || game->labyrinth[player->pos_x][player->pos_y - i] != '0' 
                || game->labyrinth[player->pos_x][player->pos_y - i] != 'f' ){
                break;
            }
            if (game->labyrinth[player->pos_x - i][player->pos_y] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_x - i][player->pos_y] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_x][player->pos_y - stepsmoved] = '1';
        game->labyrinth[player->pos_x][player->pos_y] = '0';
        player->pos_y = player->pos_y - stepsmoved;
        break;
    
    case MOVEDOWN: 
        for(int i = 1; i <= steps; i++){
            if (player->pos_x - i < 0 || game->labyrinth[player->pos_x][player->pos_y + i] != '0' 
                || game->labyrinth[player->pos_x][player->pos_y + i] != 'f' ){
                break;
            }
            if (game->labyrinth[player->pos_x - i][player->pos_y] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_x - i][player->pos_y] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_x][player->pos_y + stepsmoved] = '1';
        game->labyrinth[player->pos_x][player->pos_y] = '0';
        player->pos_y = player->pos_y + stepsmoved;
        break;

    default:
        break;
    }

    int ret = (ghostfound == 1) ? stepsmoved + FANTOME : stepsmoved;
    return ret;
}

int main(){
    return 0;
}