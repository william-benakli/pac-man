#include "../../include/join_game.h"

int player_join(struct game *_game, struct player *player, struct participant *new_player_ingame){
    new_player_ingame->pos_x = 0;
    new_player_ingame->pos_y = 0;
    new_player_ingame->score = 0;
    new_player_ingame->player_ready = 0;
    new_player_ingame->tcp_sock = player->tcp_sock;
    new_player_ingame->udp_port = player->udp_port;
    new_player_ingame->next = _game->joueurs;
    _game->joueurs = new_player_ingame;
    return PLAYER_JOIN_SUCCESS;
}

int register_game(struct player *client, char * identifiant, uint8_t room_id_game, struct list_game *games){
     
    struct game *target_game = search_game(room_id_game,games);
    if (target_game == NULL){
        printf("game not found\n");
        return PLAYER_REGISTER_FAILURE;
    }
    printf("pre malloc participant \n");

    struct participant *joining_player = malloc(sizeof(struct participant));
    strcpy(joining_player->identifiant, identifiant);

    printf("post malloc participant \n");

    if (joining_player == NULL || target_game == NULL){
        printf("failed to join game \n");
        if(joining_player == NULL){
            printf("NULL joining player \n");
        }
        return PLAYER_REGISTER_FAILURE;
    }
    printf("pre join participant \n");
    player_join(target_game,client,joining_player);
    printf("post join participant \n");

    client->status_game = IN_GAME;
    client->game_id = room_id_game;
    return PLAYER_REGISTER_SUCCESS;
}

void * search_game(uint8_t id, struct list_game *list){
 
   struct list_game *copy = list;
    
    while(copy->game != NULL){
        printf("Partie lu %d\n", copy->game->id_partie);
        printf("parti attendu  %d\n", id);

        if (copy->game->id_partie == id){
                printf("trouvé\n");
            return copy->game;
        }
        copy = copy->next_game;
    }

    return NULL;
}
