#include "../../include/labyrinthlogic.h"

char  **initlabirynth(uint16_t x, uint16_t y){
    char **labyrinth = (char **) malloc(y * sizeof(char *));
    for (uint16_t i = 0; i < y; i++){
        labyrinth[i] = (char *) malloc(x * sizeof(char));
    }

    for (uint16_t i = 0; i < y; i++){
        for (uint16_t j = 0; j < x; j++){
            if(i == 0 || j == 0 || i == (x-1) || j == (y-1)){
                labyrinth[i][j]= '#';
            }else{
                labyrinth[i][j]= '0';
            }
        } 
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
                check_endgame(game);
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
                check_endgame(game);
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y][player->pos_x + stepsmoved] = '1';
        if(stepsmoved != 0){
            game->labyrinth[player->pos_y][player->pos_x] = '@';
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
                check_endgame(game);
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
                check_endgame(game);
            }
            stepsmoved++;
        }
        game->labyrinth[player->pos_y + stepsmoved][player->pos_x] = '1';
        if(stepsmoved != 0){
            printf("on change la valeur de 0\n");
            game->labyrinth[player->pos_y][player->pos_x] = '0';
        }
        player->pos_y = player->pos_y + stepsmoved;
        break;

    default:
        break;
    }

    int ret = (ghostfound == 1) ? stepsmoved + NOMBRE_FANTOME : stepsmoved;
    return ret;
}

char getElementAtPos(struct game *game, int x, int y){
    if(game->labyrinth == NULL){
        return '-';
    }
    return game->labyrinth[x][y];
}

int setParticipantAtPos(struct game *game, struct participant *participant, int x, int y){
    if(game->labyrinth == NULL){
        return -1;
    }
    printf("placement du joueur  x: %d  y: %d\n", x,y);
    game->labyrinth[y][x] = '1';
    participant->pos_x = x;
    participant->pos_y = y;
    return 0;
}

int setElementAtPos(struct game *game, char c, int x, int y){
    if(game->labyrinth == NULL){
        return -1;
    }
    if(c == '0' || c == 'f' || c == '#' || c == '1'){
        game->labyrinth[x][y] = c;
    }else{
        return -1;
    }
    return 0;
}

int spawnJoueur(struct game *game, struct participant *participant){
    uint16_t largeur = game->largeur;
    uint16_t hauteur = game->hauteur;
    int spawn_location_x = rand()%(hauteur-1);       // % => Reste de la division entière
    int spawn_location_y = rand()%(largeur-1);       // % => Reste de la division entière
    uint16_t possibilite = 0;
    char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
    while(c != '0'){
        spawn_location_x = rand()%(hauteur-1);       // % => Reste de la division entière
        spawn_location_y = rand()%(largeur-1);  
        c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        if(possibilite > (largeur*hauteur)){
            printf("Aucune place disponibile dans un temps acceptable \n");
            return -1;
        }
        possibilite++;
    }
    int reponse = setParticipantAtPos(game, participant, spawn_location_x, spawn_location_y);
    if(reponse == -1)return -1;
    
    return 0;
}

int spawnFantomes(struct game *game){

    uint16_t largeur = game->largeur;
    uint16_t hauteur = game->hauteur;

    int spawn_location_x = rand() % (hauteur-1);       // % => Reste de la division entière
    int spawn_location_y = rand() % (largeur-1);       // % => Reste de la division entière
    uint8_t nombre_fantome_courant = 0;

    while(nombre_fantome_courant <= game->nb_fantome){

        char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        while( c != '0'){
            spawn_location_x = rand() % (hauteur-1);       // % => Reste de la division entière
            spawn_location_y = rand() % (largeur-1);  
            c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        }

        char fantom = 'f';
        int reponse = setElementAtPos(game, fantom, spawn_location_x, spawn_location_y);
        if(reponse == -1)return -1;
        nombre_fantome_courant++;
    }
    if(nombre_fantome_courant != game->nb_fantome)return -1; 
    return 0;
}

int check_endgame(struct game *game){
    if(game->nb_fantome <= 0 || game->players == 0){//TODO: ici on devra verifier 2
        return FINISH;
    }
    return NOT_FINISH;
}

void printlabyrinth(struct game *game){
    printf("------------------\n");
    for (uint16_t i = 0; i < game->largeur; i++){
            for (uint16_t j = 0; j < game->hauteur; j++){
                printf("| %c",game->labyrinth[i][j]);
            }
            printf("\n");
    }
    printf("--------------------\n");
}