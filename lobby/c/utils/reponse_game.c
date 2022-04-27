#include "../../../include/utils/reponse_game.h"

int sendSize(int socketclient){

  size_t size_buffer_max = SIZE_ONE_SPACE + sizeof(uint8_t)+SIZE_INPUT_STAR;
  char buffer_reception [size_buffer_max];
  int count_read = read(socketclient, buffer_reception, size_buffer_max);
  if(count_read != size_buffer_max)return -1;

  buffer_reception[SIZE_ONE_SPACE + sizeof(uint8_t)+SIZE_INPUT_STAR] = '\0';
 
  printf("-------%s\n", buffer_reception);
  
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
  
  memmove(buffer_envoie, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE, &game_courant->id_partie, sizeof(uint8_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE, &game_courant->hauteur, sizeof(uint16_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+2+sizeof(uint16_t), &game_courant->largeur, sizeof(uint16_t));
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
  printf("Id reconstitué: %d...\n", id_partie);
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
  
  struct participant *participant_courant = game_courant->joueurs;
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