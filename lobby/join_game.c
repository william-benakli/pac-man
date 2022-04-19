#include "../include/game_settings.h"
#include "../include/join_game.h"

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

int regis(struct player *client,struct list_game *games){
    
    int socketclient = client->tcp_sock;
    
    char regis_buffer[SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR];
 
    int count = read(socketclient, regis_buffer, SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR);
    if(count != SIZE_IDENTIFIANT+SIZE_PORT+SIZE_INPUT_STAR){
        printf("missing argument\n");
        return PLAYER_REGISTER_FAILURE;
    }

    char id_buffer[8];
    char port_buffer[4];
    char room_buffer[1];

    if(client->status_game == IN_GAME){
      printf("player already in party\n");
      return PLAYER_REGISTER_FAILURE;
    }

    memmove((void *)id_buffer,regis_buffer,8);
    memmove((void *)port_buffer,regis_buffer+9,4);
    memmove((void *)room_buffer,(char *)regis_buffer+8+1+4+1,1);
    
    int protocol_respected = strcmp(regis_buffer+15,"***");
    if(protocol_respected != 0){
      printf("invalid protocol\n");
      return PLAYER_REGISTER_FAILURE;
    }
    
    struct game *target_game;
    int sg = search_game(*((int *)room_buffer),games, target_game);
    if (sg == GAME_NOT_FOUND){
        printf("game not found\n");
        return PLAYER_REGISTER_FAILURE;
    }

    struct participant *joining_player = malloc(sizeof(struct participant));

    strcpy(joining_player->identifiant,id_buffer);

    if (joining_player == NULL || target_game == NULL){
        printf("failed to join game\n");
        return PLAYER_REGISTER_FAILURE;
    }
    player_join(&target_game,client,joining_player);

    client->status_game = IN_GAME;
    client->game_id = *((int *)room_buffer);
    return PLAYER_REGISTER_SUCCESS;
}

int search_game(uint8_t id, struct list_game *list, struct game *ret){
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
