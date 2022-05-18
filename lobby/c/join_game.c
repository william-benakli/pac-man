#include "../../include/join_game.h"
#include "../../include/utils/variables_mutex.h"

int player_join(struct game *_game, struct player *player, struct participant *new_player_ingame){
    pthread_mutex_lock(&verrou2);
    new_player_ingame->pos_x = 0;
    new_player_ingame->pos_y = 0;
    new_player_ingame->score = 0;
    new_player_ingame->player_ready = 0;
    new_player_ingame->tcp_sock = player->tcp_sock;
    new_player_ingame->udp_port = player->udp_port;
    new_player_ingame->address = player->player_address;
    new_player_ingame->next = _game->participants;
    _game->participants = new_player_ingame;
    _game->players++;
    pthread_mutex_unlock(&verrou2);
    return PLAYER_JOIN_SUCCESS;
}

int register_game(struct player *client, char * identifiant, uint8_t room_id_game, struct list_game *games){
     
    struct game *target_game = search_game(room_id_game,games);
    if (target_game == NULL){
        printf("party introuvable \n");
        return PLAYER_REGISTER_FAILURE;
    }

    struct participant *joining_player = malloc(sizeof(struct participant));
    strcpy(joining_player->identifiant, identifiant);

    if (joining_player == NULL || target_game == NULL) return PLAYER_REGISTER_FAILURE;
    player_join(target_game,client,joining_player);
    client->status_game = IN_GAME;
    client->game_id = room_id_game;
    return PLAYER_REGISTER_SUCCESS;
}

void * search_game(uint8_t id, struct list_game *list){

   pthread_mutex_lock(&verrou1);
   struct list_game *copy = list;
    while(copy != NULL){
        if(copy->game != NULL){
            if (copy->game->id_partie == id){
                pthread_mutex_unlock(&verrou1);
                return copy->game;
            }
        }
        copy = copy->next_game;
    }
    pthread_mutex_unlock(&verrou1);
    return NULL;
}
