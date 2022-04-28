#include "../../include/labyrinthlogic.h"


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
            if(player->pos_x - i < 0){
                break;
            } 
            if (game->labyrinth[player->pos_y][player->pos_x - i] != '0'){
               if (game->labyrinth[player->pos_y][player->pos_x - i] != 'f' ){
                    break;
               }
            } 
            if (game->labyrinth[player->pos_y][player->pos_x - i] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_y][player->pos_x - i] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y][player->pos_x - stepsmoved] = '1';
        if(stepsmoved != 0){
            game->labyrinth[player->pos_y][player->pos_x] = '0';
        }
        player->pos_x = player->pos_x - stepsmoved;
        break;

    case MOVERIGHT: 
       for(int i = 1; i <= steps; i++){
           if(player->pos_y + i > game->largeur -1){
                break;
            } 
            if (game->labyrinth[player->pos_y][player->pos_x + i] != '0'){
               if (game->labyrinth[player->pos_y][player->pos_x + i] != 'f' ){
                    break;
               }
            } 
            if (game->labyrinth[player->pos_y][player->pos_x + i] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_y][player->pos_x + i] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y][player->pos_x + stepsmoved] = '1';
        if(stepsmoved != 0){
            game->labyrinth[player->pos_y][player->pos_x] = '0';
        }
        player->pos_x = player->pos_x + stepsmoved;
        break;

    case MOVEUP:
        for(int i = 1; i <= steps; i++){
            if(player->pos_y - i < 0){
                break;
            } 
            if ( game->labyrinth[player->pos_y - i][player->pos_x] != '0'){ 
                if(game->labyrinth[player->pos_y - i][player->pos_x] != 'f' ){
                    break;
                }
            }
            if (game->labyrinth[player->pos_y - i][player->pos_x] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_y - i][player->pos_x] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y - stepsmoved][player->pos_x] = '1';
        if(stepsmoved != 0){
            game->labyrinth[player->pos_y][player->pos_x] = '0';
        }
        player->pos_y = player->pos_y - stepsmoved;
        break;
    
    case MOVEDOWN: 
        for(int i = 1; i <= steps; i++){
            if (player->pos_y - i > game->hauteur - 1 ){
                break;
            }
            if (game->labyrinth[player->pos_y + i][player->pos_x] != '0'){ 
                if(game->labyrinth[player->pos_y + i][player->pos_x] != 'f' ){
                    break;
                }
            }
            if (game->labyrinth[player->pos_y + i][player->pos_x] == 'f'){
                ghostfound = 1;
                game->labyrinth[player->pos_y + i][player->pos_x] = '0';
                //TODO: ENVOYER UDP A TOUT LE MONDE
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y + stepsmoved][player->pos_x] = '1';
        if(stepsmoved != 0){
            game->labyrinth[player->pos_y][player->pos_x] = '0';
        }
        player->pos_y = player->pos_y + stepsmoved;
        break;

    default:
        break;
    }

    int ret = (ghostfound == 1) ? stepsmoved + FANTOME : stepsmoved;
    return ret;
}

void printlabyrinth(struct game *_game){
    for (int i = 0; i < 5; i++){
        printf("%s\n",_game->labyrinth[i]);
    }
}

/*
int main(){
    
    struct participant *player = (struct participant*) malloc(sizeof(struct participant));
    player->pos_x = 1; 
    player->pos_y = 1;
    struct game *_game = (struct game*) malloc(sizeof(struct game));
    _game->hauteur = 5;
    _game->largeur = 5;
    _game->labyrinth = initlabirynth(_game->hauteur,_game->largeur);
    
    strcpy(_game->labyrinth[0],"#####");
    for (int i = 1 ; i <= 3; i++){
        strcpy(_game->labyrinth[i],"#0f0#");
    } 
    strcpy(_game->labyrinth[4],"#####");
    _game->labyrinth[player->pos_x][player->pos_y] = '1';

    //printf("%d\n",(player->pos_x - 1 < 0 )?1:0);
    //printf("%d\n",(_game->labyrinth[player->pos_x + 1][player->pos_y] != '0' )?1:0);
    //printf("%d\n",(_game->labyrinth[player->pos_x + 1][player->pos_y] != 'f' )?1:0);

    printlabyrinth(_game);
    printf("----------\n");
    int x  = moveinlabyrinth(MOVEDOWN,1,_game,player);
    printlabyrinth(_game);
    printf("%d",x);
    printf("%d",player->pos_x);
    printf("%d",player->pos_y);
    printf("----------\n");
    x = moveinlabyrinth(MOVERIGHT,5,_game,player);
    printlabyrinth(_game);
    printf("%d",x);
    printf("%d",player->pos_x);
    printf("%d",player->pos_y);
    printf("----------\n");

    free(player);
    freelabirynth(_game);
    free(_game);

    return 0;
}*/