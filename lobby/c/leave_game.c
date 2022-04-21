#include "../include/game_settings.h"
#include "../include/join_game.h"
#include "../include/leave_game.h"
#include "./join_game.c"


int playerleave(struct game *_game, struct player *player){
    struct game *copy = _game;
    if (copy->joueurs->tcp_sock == player->tcp_sock){
        _game->joueurs = _game->joueurs->next;
        return GAME_LEAVE_SUCCESS;
    }
    while(copy->joueurs->next->next != NULL){
        if(copy->joueurs->next->tcp_sock == player->tcp_sock){
            copy->joueurs->next = copy->joueurs->next->next;
            return GAME_LEAVE_SUCCESS;
        }
    }
    return GAME_LEAVE_FAILURE;
}

int unregis(struct player *client, int socket,struct list_game *games, char *return_message){
    if (client->is_in_game == 0){
        strcpy(return_message,"player not in party");
        return PLAYER_UNREG_FAILURE;
    }

    struct game target_game;
    int sg = search_game(client->game_id,games,&target_game);
    if (sg == GAME_NOT_FOUND){
        strcpy(return_message,"game not found");
        return PLAYER_UNREG_FAILURE;
    }

    int *pl  = (int *) malloc(sizeof(int));
    *pl = playerleave(&target_game,client);

    if (*pl != 0){ //GAME_LEAVE_SUCCESS fait un erreur ici pour quelque raison?
        strcpy(return_message,"failed to join game");
        return PLAYER_UNREG_FAILURE;
    }

    client->is_in_game = 0;
    client->game_id = -1;
    free(pl);
    return PLAYER_UNREG_SUCCESS;

}

int main(){
    return 0;
}