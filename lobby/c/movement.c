#include "../../include/movement.h"

int ingame(struct player *player, struct list_game *_games){
    return 0;
}

int move(int direction, char * distance, struct game *_game, struct participant *player){

    int movement = moveinlabyrinth(direction, atoi(distance), _game, player);
    if (movement < 10){
        char sendbuffer[19];
        sprintf(sendbuffer,"MOVE! %03d %03d***",player->pos_x,player->pos_y);
        int sent = send(player->tcp_sock,sendbuffer,19*sizeof(char),0);
        if (sent < 0){
            perror("send function failed in move function in movement.c");
            return -1;
        }
    } else {
        char sendbuffer[22];
        sprintf(sendbuffer,"MOVEF %03d %03d %d***",player->pos_x,player->pos_y,player->score);
         sprintf(sendbuffer,"MOVE! %03d %03d***",player->pos_x,player->pos_y);
        int sent = send(player->tcp_sock,sendbuffer,22*sizeof(char),0);
        if (sent < 0){
            perror("send function failed in move function in movement.c");
            return -1;
        }
    }

    return 0;

}