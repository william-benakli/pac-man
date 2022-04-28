#include "../../../include/utils/reponse_game.h"

int sendSize(int socketclient){

  size_t size_buffer_max = SIZE_ONE_SPACE + sizeof(uint8_t)+SIZE_INPUT_STAR;
  char buffer_reception [size_buffer_max];
  int count_read = read(socketclient, buffer_reception, size_buffer_max);
  if(count_read != size_buffer_max)return -1;

  buffer_reception[SIZE_ONE_SPACE + sizeof(uint8_t)+SIZE_INPUT_STAR] = '\0';
  
  uint8_t id_partie;
  char stars[4];
  stars[3] = '\0';
  memmove(&id_partie, (buffer_reception+SIZE_ONE_SPACE), sizeof(uint8_t));
  memmove(stars, (buffer_reception+SIZE_ONE_SPACE+sizeof(uint8_t)), SIZE_INPUT_STAR);

  if(strcmp(stars, "***") != 0)return -1;
  struct game *game_courant = search_game(id_partie, _games);
  if(game_courant == NULL) return -1;
  
  size_t size_reponse = SIZE_INPUT_DEFAULT_SPACE+ sizeof(uint8_t) + (sizeof(uint16_t)*2 )+ 2 + SIZE_INPUT_STAR;
  char buffer_envoie[size_reponse];
  char * buffer_input = "SIZE! ";
  
  uint16_t hauteur = htons(game_courant->hauteur);
  uint16_t largeur = htons(game_courant->largeur);

  memmove(buffer_envoie, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE, &game_courant->id_partie, sizeof(uint8_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE, (&hauteur), sizeof(uint16_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+2+sizeof(uint16_t), (&largeur), sizeof(uint16_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+2+(sizeof(uint16_t)*2), "***", SIZE_INPUT_STAR);
  
  int count = write(socketclient, buffer_envoie, size_reponse);
  return count == (size_reponse) ? 0 : -1;
}

int sendList(int socketclient){

  size_t size_buffer_max = SIZE_ONE_SPACE + sizeof(uint8_t)+SIZE_INPUT_STAR;
  char buffer_reception [size_buffer_max];
  int count_read = read(socketclient, buffer_reception, size_buffer_max);
  if(count_read != size_buffer_max)return -1;

  uint8_t id_partie;
  char stars[4];
  stars[3] = '\0';
  memmove(&id_partie, (buffer_reception+SIZE_ONE_SPACE), sizeof(uint8_t));
  memmove(stars, (buffer_reception+SIZE_ONE_SPACE+sizeof(uint8_t)), SIZE_INPUT_STAR);
  if(strcmp(stars, "***") != 0)return -1;

  struct game *game_courant = search_game(id_partie, _games);
  if(game_courant == NULL){
    return -1;
  }

  size_t size_reponse = SIZE_INPUT_DEFAULT_SPACE+ (sizeof(uint8_t) * 2) + SIZE_ONE_SPACE + SIZE_INPUT_STAR + (SIZE_INPUT_DEFAULT_SPACE + SIZE_IDENTIFIANT +  SIZE_INPUT_STAR) * + game_courant->players;
  size_t size_list = SIZE_INPUT_DEFAULT_SPACE + (sizeof(uint8_t) * 2) + SIZE_ONE_SPACE + SIZE_INPUT_STAR;
  size_t size_playr = SIZE_INPUT_DEFAULT_SPACE + (sizeof(uint8_t)) + SIZE_INPUT_STAR;
  char buffer_envoie[size_reponse];

  u_int8_t size_joueur = game_courant->players;
  memmove(buffer_envoie,  "LIST! ", SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE,  &id_partie, sizeof(u_int8_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(u_int8_t),  " ", SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(u_int8_t)+SIZE_ONE_SPACE,  &size_joueur, sizeof(u_int8_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(u_int8_t)+SIZE_ONE_SPACE+sizeof(u_int8_t),  "***", SIZE_INPUT_STAR);
  
  struct participant *participant_courant = game_courant->participants;
  int it_games = 0;
  while(participant_courant != NULL){
    memmove(buffer_envoie+size_list + (it_games*size_playr), "PLAYR ", SIZE_INPUT_DEFAULT_SPACE);
    memmove(buffer_envoie+size_list + (it_games*size_playr) +(SIZE_INPUT_DEFAULT_SPACE), &participant_courant->identifiant, SIZE_IDENTIFIANT);
    memmove(buffer_envoie+size_list + (it_games*size_playr)+(SIZE_INPUT_DEFAULT_SPACE)+SIZE_IDENTIFIANT, "***", SIZE_INPUT_STAR);
    it_games++;
    participant_courant = participant_courant->next;
  }

  int count = write(socketclient, buffer_envoie, size_reponse);
  return count == (size_reponse) ? 0 : -1 ;
}

int sendUnRegOk(int socketclient, uint8_t id_partie){
  char * buffer_input = "UNROK ";
  char * stars = "***";

  char buffer_reponse[SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR];

  memmove(buffer_reponse, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE, &id_partie, sizeof(uint8_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), stars, SIZE_INPUT_STAR);

  int count = write(socketclient, buffer_reponse, SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR);
  return count == (SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR) ? 0 : -1;
}

int sendRegOk(int socketclient, uint8_t id_partie){
  char buffer_reponse[SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR];
  char * buffer_input = "REGOK ";
  char * stars = "***";

  memmove(buffer_reponse, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE, &id_partie, sizeof(uint8_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), stars, SIZE_INPUT_STAR);

  int count = write(socketclient, buffer_reponse, SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR);
  return count == (SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR) ? 0 : -1;
}

int sendGlist(struct player *player, struct list_game *list){

  int socketclient = player->tcp_sock;
  uint8_t gameid = player->game_id;
  struct game *target_game = search_game(gameid, list);
  if(target_game == NULL){
    printf("Error game not found \n");
    return -1;
  }

  uint8_t nombre_joueur = target_game->players; 

  int size_glist = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR;
  int size_plyr = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t)*2 + SIZE_ONE_SPACE + SIZE_INPUT_STAR;
  int size_max = size_glist + nombre_joueur*(size_plyr);
  char mess_game[size_max];

  memmove(mess_game, "GLIS! ", SIZE_INPUT_DEFAULT_SPACE);
  memmove(mess_game+(SIZE_INPUT_DEFAULT)+SIZE_ONE_SPACE, &nombre_joueur, sizeof(uint8_t));
  memmove(mess_game+(SIZE_INPUT_DEFAULT) + SIZE_ONE_SPACE + sizeof(uint8_t), "***", SIZE_INPUT_STAR);

  struct participant * participant_courant = target_game->participants;
  int it_players = 0;
  while(participant_courant != NULL){
    memmove(mess_game+size_glist + (it_players*size_plyr), "GPLYR ", SIZE_INPUT_DEFAULT_SPACE);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE), participant_courant->identifiant, SIZE_IDENTIFIANT);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
   // memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT+SIZE_ONE_SPACE, participant_courant->pos_x, sizeof());
    //memmove(mess_game+size_glist + (it_players*size_plyr)+(SIZE_INPUT_DEFAULT_SPACE)+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint8_t), "***", SIZE_INPUT_STAR);
    it_players++;
    participant_courant = participant_courant->next;
  }
  
  int count =  write(socketclient, mess_game, size_max);
  return count == size_max ? 0 : -1;
}