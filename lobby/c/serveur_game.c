#include "../../include/serveur_game.h"

int gameInput(int socketclient, struct participant *partcipant_ingame, struct game *game_courant){

  while(check_endgame(game_courant) == NOT_FINISH){
    printlabyrinth(game_courant);

    size_t size_buffer_first = SIZE_INPUT_DEFAULT;
    char buffer[size_buffer_first + 1];
    buffer[size_buffer_first] = '\0';

    int count_fst = read(socketclient, buffer, size_buffer_first);

    if(count_fst != size_buffer_first){
      printf("[GAME] Erreur de taille count: [%d] / buffer: [%ld] #1\n", count_fst, size_buffer_first);
      return -1;
    }

    /* JOUEUR QUIT LA PARTIE */
    if(strcmp(buffer, "IQUIT") == 0){
        int rep_stars = readStars(socketclient);
        if(rep_stars == -1){
            sendDunno(socketclient, "IQUIT but '***' miss");
            continue;
        }else{
            break;
        }
    }

    if(strcmp(buffer, "RIMOV") != 0 || strcmp(buffer, "UPMOV") != 0 || strcmp(buffer, "DOMOV") != 0 || strcmp(buffer, "LEMOV") !=0){
        sendDunno(socketclient, "ERROR NOT GOOD SYNTAXE");
        continue;
    }
    
    size_t size_buffer_snd = SIZE_ONE_SPACE + SIZE_DISTANCE + SIZE_INPUT_STAR;
    char buffer_movement[size_buffer_snd + 1];
    buffer_movement[size_buffer_snd] = '\0';

    int count_snd = read(socketclient, buffer_movement, size_buffer_snd);

    if(count_snd != size_buffer_snd){
      printf("[GAME] Erreur de taille count: [%d] / buffer: [%ld] #2\n", count_snd, size_buffer_snd);
      return -1;
    }

    char distance[SIZE_DISTANCE];
    char stars[SIZE_INPUT_STAR+1];
    stars[SIZE_INPUT_STAR] = '\0';

    memmove(distance, buffer_movement+SIZE_ONE_SPACE, SIZE_DISTANCE);
    memmove(stars, buffer_movement+SIZE_ONE_SPACE+SIZE_DISTANCE, SIZE_INPUT_STAR);

    if(strcmp("***", stars) != 0){
        sendDunno(socketclient, "MOV but '***' miss");
        continue;
    }
    move_by_action(buffer, distance, game_courant, partcipant_ingame);    
  }

  /*SORTIE DU BREAK */
  sendGodBye(socketclient);
  free(partcipant_ingame);
  close(socketclient);
  return 0;
}

void move_by_action(char * direction, char * distance,  struct game *game_courant, struct participant * partcipant_ingame){
 if(strcmp(direction, CMD_RIMOV) == 0){      
      move(MOVERIGHT, distance, game_courant, partcipant_ingame);
    }else if(strcmp(direction, CMD_LEMOV) == 0){
      move(MOVELEFT, distance, game_courant, partcipant_ingame);
    }else if(strcmp(direction, CMD_UPMOV) == 0){
      move(MOVEUP, distance, game_courant, partcipant_ingame);
    }else if(strcmp(direction, CMD_DOMOV) == 0){
      move(MOVEDOWN, distance, game_courant, partcipant_ingame);
    }else{
      sendDunno(partcipant_ingame->tcp_sock, "[GAME ACTION] Argument introuvable");
    }  
}