#include "../../../include/utils/reponse_lobby.h"

int creategame(struct player * player, struct list_game * games){
    int socketclient = player->tcp_sock;

    if(player->status_game == IN_GAME){
      printf("player already in party\n");
      return -1;
    }
    size_t size_max_args = SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+SIZE_PORT+SIZE_INPUT_STAR;
    char arguments[size_max_args];

    int count = read(socketclient, arguments, size_max_args);
    if(count != size_max_args)return -1;
    
    char identifiant[SIZE_IDENTIFIANT+1];
    identifiant[SIZE_IDENTIFIANT] = '\0';
    char port[SIZE_PORT+1];
    port[SIZE_PORT] = '\0';
    char stars[SIZE_INPUT_STAR+1];
    stars[SIZE_INPUT_STAR] = '\0';

    memmove(identifiant, arguments+SIZE_ONE_SPACE, SIZE_IDENTIFIANT);
    memmove(port, arguments+SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE, sizeof(int));
    memmove(stars, arguments+SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+sizeof(int), SIZE_INPUT_STAR);

    if(strcmp(stars, "***") != 0){
      printf("Argument incorrect %s %s %s\n", identifiant, port, stars);
      return -1;
    }

    struct game *game = malloc(sizeof(struct game));
    int rep_init = init_game(game, 10, 10, NULL);
    if(rep_init == -1)return -1;

    int rep_add = add_game(game, _games);
    if(rep_add == -1)return -1;
    player->game_id = game->id_partie;

    int rep_join = register_game(player, identifiant, game->id_partie, _games);
    if(rep_join == -1)return -1;

    printf("[CREATE GAME] Joueur %s Port %s\n", identifiant, port);
    return 0;
}

int sendgames(int socketclient){

  uint8_t nombre_games = size_game_available(_games);

  int size_games = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR;
  int size_ogame = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t)*2 + SIZE_ONE_SPACE + SIZE_INPUT_STAR;
  int size_max = size_games + nombre_games*(size_ogame);
  char mess_game[size_max];
  //On deplace [GAMES n***] dans le buffer
  memmove(mess_game, "GAMES ", SIZE_INPUT_DEFAULT_SPACE);
  memmove(mess_game+(SIZE_INPUT_DEFAULT)+SIZE_ONE_SPACE, &nombre_games, sizeof(uint8_t));
  memmove(mess_game+(SIZE_INPUT_DEFAULT) + SIZE_ONE_SPACE + sizeof(uint8_t), "***", SIZE_INPUT_STAR);

  struct list_game *listgames_courant = _games;

  if(listgames_courant->game != NULL){
      int it_games = 0;
      while(listgames_courant != NULL){
        if(listgames_courant->game->status == STATUS_AVAILABLE){
          memmove(mess_game+size_games + (it_games*size_ogame), "OGAME ", SIZE_INPUT_DEFAULT_SPACE);
          memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT_SPACE), &listgames_courant->game->id_partie, sizeof(uint8_t));
          memmove(mess_game+size_games + (it_games*size_ogame) +(SIZE_INPUT_DEFAULT_SPACE)+SIZE_ONE_SPACE, " ", sizeof(char)*1);
          memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT_SPACE)+sizeof(uint8_t)+SIZE_ONE_SPACE, &listgames_courant->game->players,sizeof(uint8_t));
          memmove(mess_game+size_games + (it_games*size_ogame)+(SIZE_INPUT_DEFAULT_SPACE)+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint8_t), "***", SIZE_INPUT_STAR);
          it_games++;
        }
        listgames_courant = listgames_courant->next_game;
      }
      printf("---- %s ---- \n",mess_game);

  }
  printf("Nombre de game jouable %d:\n", nombre_games);
  int count =  write(socketclient, mess_game, size_max);
  return count == size_max ? 0 : -1;
}


int regisgame(struct player *client,struct list_game *games){
    
    int socketclient = client->tcp_sock;
    
    size_t size_buffer_max = SIZE_ONE_SPACE+ SIZE_IDENTIFIANT+SIZE_ONE_SPACE+SIZE_PORT+SIZE_ONE_SPACE+sizeof(uint8_t)+SIZE_INPUT_STAR;
    char regis_buffer[size_buffer_max];
 
    int count = read(socketclient, regis_buffer, size_buffer_max);
    if(count != size_buffer_max){
        printf("missing argument\n");
        return PLAYER_REGISTER_FAILURE;
    }

    char id_buffer[8];
    char port_buffer[4];
    uint8_t room_buffer;
    char stars[4];
    stars[3] = '\0';
    if(client->status_game == IN_GAME){
      printf("player already in party\n");
      return PLAYER_REGISTER_FAILURE;
    }

    memmove(id_buffer,regis_buffer+SIZE_ONE_SPACE, SIZE_IDENTIFIANT);
    memmove(port_buffer,regis_buffer+SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE,sizeof(int));
    memmove(&room_buffer,regis_buffer+SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+sizeof(int), sizeof(uint8_t));
    memmove(stars,regis_buffer+SIZE_ONE_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+sizeof(int) + sizeof(uint8_t), SIZE_INPUT_STAR);

    printf("Je recois id: %s , port %s, l'id game %d, les etoiles %s\n", id_buffer, port_buffer, room_buffer, stars);
    
    int protocol_respected = strcmp(stars,"***");
    if(protocol_respected != 0){
      printf("invalid protocol etoile miss\n");
      return PLAYER_REGISTER_FAILURE;
    }
   
    int protocol_register_game = register_game(client, id_buffer, room_buffer, games);
    if(protocol_register_game != 0){
      printf("invalid protocol to the game\n");
      return PLAYER_REGISTER_FAILURE;
    }
    
    return PLAYER_REGISTER_SUCCESS;
}