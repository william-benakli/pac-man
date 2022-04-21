#include "../include/game_settings.h"
#include "../include/join_game.h"

int player_join(struct game *_game, struct player *player, struct participant *new_player_ingame){
    //struct participant *new_player = (struct participant *)malloc(sizeof(struct participant));
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

int search_game(int id, struct list_game *list, struct game *ret){
    struct list_game *copy = list;

    while(copy != NULL){
        if (copy->game->id_partie == id){
            ret = copy->game;
            return GAME_FOUND;
        } 
        copy = copy->next_game;
    }

    return GAME_NOT_FOUND;
}

int regis(struct player *client, char *regis_buffer, int socket,struct list_game *games, char *return_message){
    
    char id_buffer[8];
    char port_buffer[4];
    char room_buffer[1];

    if(client->is_in_game == 1){
    strcpy(return_message,"player already in party");
      return PLAYER_REGISTER_FAILURE;
    }

    memmove((void *)id_buffer,regis_buffer,8);
    memmove((void *)port_buffer,regis_buffer+9,4);
    memmove((void *)room_buffer,(char *)regis_buffer+8+1+4+1,1);
    int protocol_respected = strcmp(regis_buffer+15,"***");
    if(protocol_respected != 0){
      strcpy(return_message,"invalid protocol");
      return PLAYER_REGISTER_FAILURE;
    }

    client->tcp_sock = socket;
    client->udp_port = atoi(port_buffer);

    struct game target_game;
    int sg = search_game(*((int *)room_buffer),games,&target_game);
    if (sg == GAME_NOT_FOUND){
        strcpy(return_message,"game not found");
        return PLAYER_REGISTER_FAILURE;
    }

    struct participant joining_player;
    strcpy(joining_player.identifiant,id_buffer);

    int *pj  = (int *) malloc(sizeof(int));
    *pj = player_join(&target_game,client,&joining_player);
    if (*pj != PLAYER_JOIN_SUCCESS){
        strcpy(return_message,"failed to join game");
        return PLAYER_REGISTER_FAILURE;
    }

    client->is_in_game = 1;
    client->game_id = *((int *)room_buffer);
    free(pj);
    return PLAYER_REGISTER_SUCCESS;

}
