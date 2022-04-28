#include "../../include/movement.h"

int ingame(struct player *player, struct list_game *_games){
    return 0;
}

int move(int direction, struct game *_game, struct participant *player){
    char movementbuffer[7];
    int recu = recv(player->tcp_sock,movementbuffer,7*sizeof(char),0);
    if(recu < 0){
        perror("recv function failed in move function in movement.c");
        return -1;
    }
    //verifier que la dernier trois  = ***
    if(strcmp("***",movementbuffer + 4) != 0){
        return -1; //protocol pas respecté
    }
    char dist[3];
    memmove(dist,movementbuffer+1,3);
    int movement = moveinlabyrinth(direction,atoi(dist),_game,player);
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

int main(){
    return 0;
}