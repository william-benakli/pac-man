#include "../../include/movement.h"

int move(int direction, char * distance, struct game * game, struct participant *player){
        printf("MOUVEUMENT: %d",atoi(distance));
    int movement = moveinlabyrinth(direction, atoi(distance), game, player);
    if (movement != 0){
        char sendbuffer[17];
        sprintf(sendbuffer,"MOVE! %03d %03d***",player->pos_x,player->pos_y);
        printf("[GAME] envoie TCP %s\n", sendbuffer);
        int sent = send(player->tcp_sock,sendbuffer,strlen(sendbuffer),0);
        if (sent < 0){
            perror("send function failed in move function in movement.c");
            return -1;
        }
    } else {
        char sendbuffer[25];
        sprintf(sendbuffer,"MOVEF %03d %03d %04d***",player->pos_x,player->pos_y,player->score);
        //sprintf(sendbuffer,"MOVE! %03d %03d***",player->pos_x,player->pos_y); ????
        int sent = send(player->tcp_sock,sendbuffer,strlen(sendbuffer),0);
        if (sent < 0){
            perror("send function failed in move function in movement.c");
            return -1;
        }
    }

    return 0;

}
