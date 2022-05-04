#include "../../include/labyrinthlogic.h"

char  **initlabirynth(uint16_t x, uint16_t y){
    char **labyrinth = (char **) malloc(y * sizeof(char *));
    for (uint16_t i = 0; i < y; i++){
        labyrinth[i] = (char *) malloc(x * sizeof(char));
    }

    for (uint16_t i = 0; i < y; i++){
        for (uint16_t j = 0; j < x; j++){
            labyrinth[i][j]= '0';
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
                checkFinish(game);
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
                checkFinish(game);
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
                checkFinish(game);
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
                checkFinish(game);
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
    game->labyrinth[x][y] = '1';
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
    
    //random le labyrinth jusqu'a trouver une case ou il n'y pas 
    //de fantom de mur ou de joueur donc une case avec 0;

    uint16_t largeur = game->largeur;
    uint16_t hauteur = game->hauteur;

    int spawn_location_x = rand() % (largeur)-1;       // % => Reste de la division entière
    int spawn_location_y = rand() % (hauteur)-1;       // % => Reste de la division entière

    uint16_t possibilite = 0;

    char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
    while(c != '0'){
        spawn_location_x = rand() % (largeur)-1;       // % => Reste de la division entière
        spawn_location_y = rand() % (hauteur)-1;  
        c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        if(possibilite > largeur*hauteur){
            printf("Aucune place disponibile dans un temps acceptable \n");
            return -1;
        }
        possibilite++;
    }

    int reponse = setParticipantAtPos(game, participant, spawn_location_x, spawn_location_y);
    if(reponse == -1){
        printf("Ajout du participan problematique\n");
        return -1;
    }
    return 0;
}

int spawnFantomes(struct game *game){

    uint16_t largeur = game->largeur;
    uint16_t hauteur = game->hauteur;

    int spawn_location_x = rand() % (largeur)-1;       // % => Reste de la division entière
    int spawn_location_y = rand() % (hauteur)-1;       // % => Reste de la division entière
    uint8_t nombre_fantome_courant = 0;

    while(nombre_fantome_courant <= game->nb_fantome){

        char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        while( c != '0'){
            spawn_location_x = rand() % (largeur);       // % => Reste de la division entière
            spawn_location_y = rand() % (hauteur);  
            printf("on entre ici 2 while %d et %d je trouve %c\n", spawn_location_x, spawn_location_y, c);
            c = getElementAtPos(game, spawn_location_x, spawn_location_y);
        }

        char fantom = 'f';
        int reponse = setElementAtPos(game, fantom, spawn_location_x, spawn_location_y);
        if(reponse == -1){
            printf("Ajout des fantomes problematique\n");
            return -1;
        }
        nombre_fantome_courant++;
        printf("on entre ici premier while \n");
    }

    if(nombre_fantome_courant != game->nb_fantome)return -1; 
    return 0;
}

/*
 Cette fonction sera appelé à deux moments: 
  Quand un joueur va faire IQUIT 
  Quand un fantome sera mangé
*/
int checkFinish(struct game *game){
    if(game->nb_fantome > 0 || game->players >= 1){//ici on devra verifier 2
        return FINISH;
    }
    return NOT_FINISH;
}

void printlabyrinth(struct game *game){
    printf("------------------\n");
    for (uint16_t i = 0; i < game->hauteur; i++){
            for (uint16_t j = 0; j < game->largeur; j++){
                printf("| %c",game->labyrinth[i][j]);
            }
            printf("\n");
    }
    printf("--------------------\n");
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