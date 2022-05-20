#include "../../include/leave_game.h"
#include "../../include/utils/variables_mutex.h"

int playerleave(struct game *_game, struct player *player){
    pthread_mutex_lock(&verrou2);
    struct game *copy = _game;
    if (copy->participants->tcp_sock == player->tcp_sock){
        _game->participants = _game->participants->next;
        _game->players--;
        pthread_mutex_unlock(&verrou2);
        return GAME_LEAVE_SUCCESS;
    }
    while(copy->participants->next->next != NULL){
        if(copy->participants->next->tcp_sock == player->tcp_sock){
            copy->participants->next = copy->participants->next->next;
            _game->players--;
            pthread_mutex_unlock(&verrou2);
            return GAME_LEAVE_SUCCESS;
        }
    }
    pthread_mutex_unlock(&verrou2);
    return GAME_LEAVE_FAILURE;
}

int unregis(struct player *client, struct list_game *games){

    if (client->status_game == IN_LOBBY){
        printf("player not in party\n");
        return PLAYER_UNREG_FAILURE;
    }

    struct game *target_game = search_game(client->game_id,games);
    if (target_game == NULL){
        printf("game not found\n");
        return PLAYER_UNREG_FAILURE;
    }

    if (target_game == NULL || games == NULL){ //GAME_LEAVE_SUCCESS fait un erreur ici pour quelque raison?
        printf("failed to join\n");
        return PLAYER_UNREG_FAILURE;
    }
    playerleave(target_game,client);
    client->status_game = IN_LOBBY;
    client->game_id = -1;
    return PLAYER_UNREG_SUCCESS;

}