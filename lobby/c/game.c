#include "../../include/game_settings.h"
#include "../../include/serveur.h"
#include "../../include/utils/variables_mutex.h"

uint8_t id_games_static = 0;

int init_game(struct game *_game, uint16_t hauteur, uint16_t largeur){
    
    _game->id_partie = id_games_static; //TODO STATIC ALLOCATION OF ID 
    id_games_static++;
    _game->hauteur = hauteur;
    _game->largeur = largeur;
    _game->max_player = 0;
    _game->labyrinth = initlabirynth(hauteur, largeur);
    if(_game->labyrinth == NULL){
        return GAME_CREATION_FAILED;
    }
    _game->nb_fantome = 2 + rand() %10; // mettre en aléatoire
    _game->participants = NULL;
    _game->port_udp = __UDP_PORT++;
    _game->address_udp = ADDRESS_MULTICAST;
    _game->isGhost = NO;
    if(id_games_static > 255){
        return GAME_CREATION_FAILED;
   }
    return GAME_CREATION_SUCCESS;
}       

int free_game(struct game *_game){
    pthread_mutex_lock(&verrou2);
    int rep = free_participant(_game->participants);
    if(rep == -1){
        pthread_mutex_unlock(&verrou2);
        return -1;
    }
    pthread_mutex_unlock(&verrou2);
    _game = NULL;
    free(_game);
    return 0;
}

int free_participant(struct participant *premier){
	struct participant *copy = premier;
	struct participant *next = NULL;

	while(copy != NULL){
		next = copy->next;
        copy = NULL;
		free(copy);
		copy = next;
	}
    return 0;
}

int check_ready(struct game *_game){
    pthread_mutex_lock(&verrou2);
    struct participant *cpy = _game->participants;
    while (cpy != NULL){
        if (cpy->player_ready == 0){
            pthread_mutex_unlock(&verrou2);
            return PLAYERS_NOT_READY;
        }
        cpy = cpy->next;
    }
    pthread_mutex_unlock(&verrou2);
    return PLAYERS_READY;
}

void * search_player_in_game(struct game *target_game, struct player *player){
    pthread_mutex_lock(&verrou2);
    struct participant *participant_courant = target_game->participants;
    while(participant_courant != NULL){
        if(participant_courant->tcp_sock == player->tcp_sock){
            pthread_mutex_unlock(&verrou2);
            return participant_courant;
        }
        participant_courant = participant_courant->next;
    }
    pthread_mutex_unlock(&verrou2);
    return NULL;
}