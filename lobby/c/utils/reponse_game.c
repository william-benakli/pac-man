#include "../../../include/utils/reponse_game.h"

int sendSize(int socketclient, struct list_game *_games){

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
  
  uint16_t hauteur = ntohs(game_courant->hauteur);
  uint16_t largeur = ntohs(game_courant->largeur);

  memmove(buffer_envoie, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE, &game_courant->id_partie, sizeof(uint8_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE, (&hauteur), sizeof(uint16_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t), " ",  SIZE_ONE_SPACE);
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+2+sizeof(uint16_t), (&largeur), sizeof(uint16_t));
  memmove(buffer_envoie+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+2+(sizeof(uint16_t)*2), "***", SIZE_INPUT_STAR);
  
  int count = write(socketclient, buffer_envoie, size_reponse);
  printf("j'envoie %d\n", count);
  return count == (size_reponse) ? 0 : -1;
}

int sendList(int socketclient, struct list_game *_games){

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
  size_t size_playr = SIZE_INPUT_DEFAULT_SPACE + SIZE_IDENTIFIANT + SIZE_INPUT_STAR;
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
    memmove(buffer_envoie+size_list + (it_games*size_playr) +(SIZE_INPUT_DEFAULT_SPACE), participant_courant->identifiant, SIZE_IDENTIFIANT);
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

int sendGlist(int socketclient, struct game *current_game){

  uint8_t nombre_joueur = current_game->players; 

  //int size_glist = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) + SIZE_INPUT_STAR;
  //int size_plyr = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t)*2 + SIZE_ONE_SPACE + SIZE_INPUT_STAR;
  //int size_max = size_glist + nombre_joueur*(size_plyr);
  char glis_buffer[11];
  char gplyr_buffer[31]; // c'était 30
  char id_joueur[9];

  //memmove(mess_game, "GLIS! ", SIZE_INPUT_DEFAULT_SPACE);
  //memmove(mess_game+(SIZE_INPUT_DEFAULT)+SIZE_ONE_SPACE, &nombre_joueur, sizeof(uint8_t));
  //memmove(mess_game+(SIZE_INPUT_DEFAULT) + SIZE_ONE_SPACE + sizeof(uint8_t), "***", SIZE_INPUT_STAR);

  struct participant * participant_courant = current_game->participants;
  
  int it_players = 0;
  int bytes_written = 0;
  int accumulator = 0;
  int count = 0;
  memmove(glis_buffer,"GLIS! ",6);
  memmove(glis_buffer + 6,&nombre_joueur,1);
  memmove(glis_buffer + 7, "***", 3);
  count =  write(socketclient, glis_buffer, strlen(glis_buffer));
  
  if(count < strlen(glis_buffer)){
    return -1;
  }
  
  while(participant_courant != NULL){
	
    // memset(gplyr_buffer,0,24);
    // memset(trait,0,9);
    memcpy(id_joueur,participant_courant->identifiant,8);
    id_joueur[8] = '\0';
        
    bytes_written = sprintf(gplyr_buffer,"GPLYR %s %03d %03d %04d***", 
    id_joueur,participant_courant->pos_x,participant_courant->pos_y,participant_courant->score);
    
    count =  write(socketclient, gplyr_buffer, strlen(gplyr_buffer));
    if(count < strlen(glis_buffer)){
      return -1;
    }
        
    accumulator += bytes_written;
  /*memmove(mess_game+size_glist + (it_players*size_plyr), "GPLYR ", SIZE_INPUT_DEFAULT_SPACE);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE), participant_courant->identifiant, SIZE_IDENTIFIANT);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT+SIZE_ONE_SPACE, , );
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT+SIZE_ONE_SPACE, , );
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
    memmove(mess_game+size_glist + (it_players*size_plyr) +(SIZE_INPUT_DEFAULT_SPACE)+ SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
*/
    it_players++;
    participant_courant = participant_courant->next;
  }

  return 0;
}

int sendWelcome(struct game *game, struct participant *participant, struct list_game *list_game){

  size_t size_buffer = SIZE_INPUT_DEFAULT_SPACE + sizeof(uint8_t) *2 + sizeof(uint16_t)*2 + SIZE_IP_ADRESSE + SIZE_PORT + SIZE_INPUT_STAR + 5;
  char buffer_reponse[size_buffer];
  char * buffer_input = "WELCO ";
  uint8_t id_partie = game->id_partie;
  uint16_t hauteur = ntohs(game->hauteur);
  uint16_t largeur = ntohs(game->largeur);
  uint8_t nombre_fantome = game->nb_fantome;
  participant->address = "226.1.2.4";
  game -> address_udp = "226.1.2.4";
  char * ip = "226.1.2.4######";
  char port[5];
  sprintf(port, "%d", game->port_udp);
  char * stars = "***";

  memmove(buffer_reponse, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE, &id_partie, sizeof(uint8_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t), " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE, &hauteur, sizeof(uint16_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t), " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE, &largeur, sizeof(uint16_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t), " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE, &nombre_fantome, sizeof(uint8_t));
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint8_t), " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE, ip, SIZE_IP_ADRESSE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+SIZE_IP_ADRESSE, " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+SIZE_IP_ADRESSE+SIZE_ONE_SPACE, port, SIZE_PORT);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint16_t)+SIZE_ONE_SPACE+sizeof(uint8_t)+SIZE_ONE_SPACE+SIZE_IP_ADRESSE+SIZE_ONE_SPACE+SIZE_PORT, stars, SIZE_INPUT_STAR);

  printf("%s\n", buffer_reponse);

  int count = write(participant->tcp_sock, buffer_reponse, size_buffer);
  if(count != size_buffer){
    return -1;
  }
  return 0;
}

int sendPosit(int client_socket, struct game *game , struct participant *participant){
  size_t size_buffer = SIZE_INPUT_DEFAULT_SPACE + SIZE_IDENTIFIANT + SIZE_ONE_SPACE + SIZE_POS_X + SIZE_ONE_SPACE +SIZE_POS_Y + SIZE_INPUT_STAR;
  char buffer_reponse[size_buffer];
  char * buffer_input = "POSIT ";
  char * id_joueur = participant->identifiant;
  char pos_x[4];
  char pos_y[4];

  sprintf(pos_x,"%03d",participant->pos_x);
  printf("%c--------\n", pos_x[3]);

  sprintf(pos_y,"%03d",participant->pos_y);
  printf("%c--------\n", pos_y[3]);

  printf("on est passé\n");
  char *stars = "***";
  memmove(buffer_reponse, buffer_input, SIZE_INPUT_DEFAULT_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE, id_joueur, SIZE_IDENTIFIANT);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+SIZE_IDENTIFIANT, " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE, pos_x, SIZE_POS_X);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+SIZE_POS_X, " ", SIZE_ONE_SPACE);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+SIZE_POS_X+SIZE_ONE_SPACE, pos_y, SIZE_POS_Y);
  memmove(buffer_reponse+SIZE_INPUT_DEFAULT_SPACE+SIZE_IDENTIFIANT+SIZE_ONE_SPACE+SIZE_POS_X+SIZE_ONE_SPACE+SIZE_POS_Y, stars, SIZE_INPUT_STAR);
  
  int count = write(client_socket, buffer_reponse, size_buffer);
  if(count != size_buffer){
    return -1;
  }
  return 0;
}
