#include "../../include/movement.h"
#include "../../include/utils/variables_mutex.h"

int move(int direction, char *distance, struct game *game,
		struct participant *player) {
	printf("%d\n", direction);
	printf("%d ON PASSE LAS-----\n", atoi(distance));

	int movement = moveinlabyrinth(direction, atoi(distance), game, player);
	if (movement != 0) {
		char sendbuffer[17];
		sprintf(sendbuffer, "MOVE! %03d %03d***", player->pos_x, player->pos_y);
		printf("[GAME] envoie TCP %s\n", sendbuffer);
		int sent = send(player->tcp_sock, sendbuffer, strlen(sendbuffer), 0);
		if (sent < 0) {
			perror("send function failed in move function in movement.c");
			return -1;
		}
	} else {
		char sendbuffer[25];
		sprintf(sendbuffer, "MOVEF %03d %03d %04d***", player->pos_x,
				player->pos_y, player->score);
		//sprintf(sendbuffer,"MOVE! %03d %03d***",player->pos_x,player->pos_y); ????
		int sent = send(player->tcp_sock, sendbuffer, strlen(sendbuffer), 0);
		if (sent < 0) {
			perror("send function failed in move function in movement.c");
			return -1;
		}
	}

	return 0;

}

int deplace_fantom(struct game *game) {
	pthread_mutex_lock (&verrou2);
	int search_x = rand() % (game->largeur - 1);
	int search_y = rand() % (game->hauteur - 1);

	for (int x = search_x + 1; x < game->largeur - 1; x++) {
		for (int y = search_y + 1; y < game->hauteur - 1; y++) {
			if (game->labyrinth[y][x] == 'f') {
				int direction = rand() % 4;
				int distance = (rand() % 2) + 1;

				switch (direction) {
				case 0: //direction gauche
					if (x - distance < 0) {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					if (game->labyrinth[y][x - distance] != '0') {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					game->labyrinth[y][x - distance] = 'f';
					game->labyrinth[y][x] = '0';
					ghost_message(game, x - distance, y);
					break;
				case 1: //direction droite
					if (x + distance >= game->largeur) {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					if (game->labyrinth[y][x + distance] != '0') {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					game->labyrinth[y][x + distance] = 'f';
					game->labyrinth[y][x] = '0';
					ghost_message(game, x + distance, y);
					break;
				case 2: //direction up
					if (y - distance < 0) {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					if (game->labyrinth[y - distance][x] != '0') {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					game->labyrinth[y - distance][x] = 'f';
					game->labyrinth[y][x] = '0';
					ghost_message(game, x, y - distance);
					break;
				default: //direction down
					if (y + distance >= game->largeur) {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					if (game->labyrinth[y + distance][x] != '0') {
						pthread_mutex_unlock(&verrou2);
						return 0;
					}
					game->labyrinth[y + distance][x] = 'f';
					game->labyrinth[y][x] = '0';
					ghost_message(game, x, y + distance);
				}
			}
		}
	}
	pthread_mutex_unlock(&verrou2);
	return 0;
}
