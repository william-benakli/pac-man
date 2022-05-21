#include "../../include/labyrinthlogic.h"
#include "../../include/utils/variables_mutex.h"
#include <time.h>
#include <stdlib.h>

char** initlabirynth(uint16_t x, uint16_t y) {
	char **labyrinth = (char**) malloc(y * sizeof(char*));
	for (uint16_t i = 0; i < y; i++) {
		labyrinth[i] = (char*) malloc(x * sizeof(char));
	}

	// Génère un labyrinthe au hasard parmi 5 types différents

	srand (time(NULL));int
	nRandonNumber = rand() % (4);
	switch (nRandonNumber) {
	case 0: // Labirynthe sans aucun mur (Titre: RUNDOWN)
		printf("LABYRINTHE RUNDOWN\n");
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i == 0 || j == 0 || i == (x - 1) || j == (y - 1)) {
					labyrinth[i][j] = '#';
				} else {
					labyrinth[i][j] = '0';
				}
			}
		}
		break;
	case 1: // Labirynthe en X (Titre: XSPECIAL)
		printf("LABYRINTHE XSPECIAL\n");
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i == 0 || j == 0 || i == (x - 1) || j == (y - 1)) {
					labyrinth[i][j] = '#';
				} else if (i == j && i != 1 && i != y - 2 && j != 1
						&& j != x - 2) {
					labyrinth[i][j] = '#';
				} else if (i == x - j && i != 1 && i != y - 2 && j != 1
						&& j != x - 2) {
					labyrinth[i][j] = '#';
				} else {
					labyrinth[i][j] = '0';
				}
			}
		}
		break;
	case 2: // // Labyrinthe en dur (Titre: CHEPERD)
		printf("LABYRINTHE CHEPERD\n");
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i == 0 || j == 0 || i == (x - 1) || j == (y - 1)) {
					labyrinth[i][j] = '#';
				} else if (i % 3 == 0 && j != y / 2) {
					int tmp_mur = rand() % (3);
					if (tmp_mur != 2) {
						labyrinth[i][j] = '#';
					} else {
						labyrinth[i][j] = '0';
					}
				} else {
					labyrinth[i][j] = '0';
				}
			}
		}
		break;
	default: // Labyrinthe en une ligne (Titre: ONE V ONE)
		printf("LABYRINTHE ONE V ONE\n");
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				labyrinth[i][j] = '0';
			}
		}
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i > 0 && (j < x / 2 - 1 || j > x / 2 + 1) && i < x - 1
						&& (i > y / 2 + 1 || i < y / 2 - 1))
					labyrinth[i][j] = '#';
			}
		}
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i == 0 || j == 0 || i == (x - 1) || j == (y - 1)) {
					labyrinth[i][j] = '0';
				}
			}
		}
		for (uint16_t i = 0; i < y; i++) {
			for (uint16_t j = 0; j < x; j++) {
				if (i == 0 || j == 0 || i == (x - 1) || j == (y - 1)) {
					labyrinth[i][j] = '#';
				}
			}
		}
	}
	return labyrinth;
}

void freelabirynth(struct game *game) {
	pthread_mutex_lock (&verrou2);
	for (int i = 0; i < game->hauteur; i++) {
		free(game->labyrinth[i]);
	}
	free(game->labyrinth);
	pthread_mutex_unlock(&verrou2);
}

int moveinlabyrinth(int direction, int steps, struct game *game,
		struct participant *player) {
	pthread_mutex_lock (&verrou2);
	int stepsmoved = 0;
	int ghostfound = 0;
	switch (direction) {
	case MOVELEFT:
		for (int i = 1; i <= steps; i++) {
			if (player->pos_x - i < 0) {
				break;
			}
			if (game->labyrinth[player->pos_y][player->pos_x - i] != '0') {
				if (game->labyrinth[player->pos_y][player->pos_x - i] != 'f') {
					break;
				}
			}
			if (game->labyrinth[player->pos_y][player->pos_x - i] == 'f') {
				ghostfound = 1;
				game->labyrinth[player->pos_y][player->pos_x - i] = '0';
				player->score++;
				game->nb_fantome--;

				score_message(game, player, player->pos_x - i, player->pos_y);

				check_endgame(game);
			}
			stepsmoved++;
		}
		game->labyrinth[player->pos_y][player->pos_x - stepsmoved] = 'p';
		if (stepsmoved != 0) {
			game->labyrinth[player->pos_y][player->pos_x] = '0';
		}
		player->pos_x = player->pos_x - stepsmoved;
		break;

	case MOVERIGHT:
		for (int i = 1; i <= steps; i++) {
			if (player->pos_x + i > game->largeur - 1) {
				break;
			}
			if (game->labyrinth[player->pos_y][player->pos_x + i] != '0') {
				if (game->labyrinth[player->pos_y][player->pos_x + i] != 'f') {
					break;
				}
			}
			if (game->labyrinth[player->pos_y][player->pos_x + i] == 'f') {
				ghostfound = 1;
				game->labyrinth[player->pos_y][player->pos_x + i] = '0';
				player->score++;
				game->nb_fantome--;

				score_message(game, player, player->pos_x + i, player->pos_y);

				check_endgame(game);
			}
			stepsmoved++;
		}
		game->labyrinth[player->pos_y][player->pos_x + stepsmoved] = 'p';
		if (stepsmoved != 0) {
			game->labyrinth[player->pos_y][player->pos_x] = '0';
		}
		player->pos_x = player->pos_x + stepsmoved;
		break;

	case MOVEUP:
		for (int i = 1; i <= steps; i++) {
			if (player->pos_y - i < 0) {
				break;
			}
			if (game->labyrinth[player->pos_y - i][player->pos_x] != '0') {
				if (game->labyrinth[player->pos_y - i][player->pos_x] != 'f') {
					break;
				}
			}
			if (game->labyrinth[player->pos_y - i][player->pos_x] == 'f') {
				ghostfound = 1;
				game->labyrinth[player->pos_y - i][player->pos_x] = '0';
				player->score++;
				game->nb_fantome--;

				score_message(game, player, player->pos_x, player->pos_y - i);

				check_endgame(game);
			}
			stepsmoved++;
		}
		game->labyrinth[player->pos_y - stepsmoved][player->pos_x] = 'p';
		if (stepsmoved != 0) {
			game->labyrinth[player->pos_y][player->pos_x] = '0';
		}
		player->pos_y = player->pos_y - stepsmoved;
		break;

	case MOVEDOWN:
		for (int i = 1; i <= steps; i++) {
			if (player->pos_y - i > game->hauteur - 1) {
				break;
			}
			if (game->labyrinth[player->pos_y + i][player->pos_x] != '0') {
				if (game->labyrinth[player->pos_y + i][player->pos_x] != 'f') {
					break;
				}
			}
			if (game->labyrinth[player->pos_y + i][player->pos_x] == 'f') {
				ghostfound = 1;
				game->labyrinth[player->pos_y + i][player->pos_x] = '0';
				player->score++;
				game->nb_fantome--;

				score_message(game, player, player->pos_x, player->pos_y + i);

				check_endgame(game);
			}
			stepsmoved++;
		}
		game->labyrinth[player->pos_y + stepsmoved][player->pos_x] = 'p';
		if (stepsmoved != 0) {
			printf("on change la valeur de 0\n");
			game->labyrinth[player->pos_y][player->pos_x] = '0';
		}
		player->pos_y = player->pos_y + stepsmoved;
		break;

	default:
		break;
	}

	int ret = (ghostfound == 1) ? 0 : 1;
	pthread_mutex_unlock(&verrou2);
	return ret;
}

char getElementAtPos(struct game *game, int x, int y) {
	pthread_mutex_lock (&verrou2);

	if (game->labyrinth == NULL) {
		pthread_mutex_unlock(&verrou2);
		return '-';
	}

	pthread_mutex_unlock(&verrou2);
	return game->labyrinth[x][y];
}

int setParticipantAtPos(struct game *game, struct participant *participant,
		int x, int y) {
	pthread_mutex_lock (&verrou2);
	if (game->labyrinth == NULL) {
		pthread_mutex_unlock(&verrou2);
		return -1;
	}
	printf("placement du joueur  x: %d  y: %d\n", x, y);
	game->labyrinth[y][x] = 'p';
	participant->pos_x = x;
	participant->pos_y = y;
	pthread_mutex_unlock(&verrou2);
	return 0;
}

int setElementAtPos(struct game *game, char c, int x, int y) {
	pthread_mutex_lock (&verrou2);
	if (game->labyrinth == NULL) {
		pthread_mutex_unlock(&verrou2);
		return -1;
	}
	if (c == '0' || c == 'f' || c == '#' || c == 'p') {
		game->labyrinth[x][y] = c;
	} else {
		pthread_mutex_unlock(&verrou2);
		return -1;
	}
	pthread_mutex_unlock(&verrou2);
	return 0;
}

int spawnJoueur(struct game *game, struct participant *participant) {
	uint16_t largeur = game->largeur;
	uint16_t hauteur = game->hauteur;
	int spawn_location_x = rand() % (hauteur - 1); // % => Reste de la division entière
	int spawn_location_y = rand() % (largeur - 1); // % => Reste de la division entière
	// uint16_t possibilite = 0;
	char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
	while (c != '0') {
		spawn_location_x = rand() % (hauteur - 1); // % => Reste de la division entière
		spawn_location_y = rand() % (largeur - 1);
		c = getElementAtPos(game, spawn_location_x, spawn_location_y);
		/*
		 if (possibilite > (largeur * hauteur)) {
		 printf("Aucune place disponibile dans un temps acceptable \n");
		 return -1;
		 }
		 possibilite++;
		 */
	}
	int reponse = setParticipantAtPos(game, participant, spawn_location_x,
			spawn_location_y);
	if (reponse == -1)
		return -1;

	return 0;
}

int spawnFantomes(struct game *game) {
	if (game->isGhost == YES)
		return 0;

	int largeur = game->largeur;
	int hauteur = game->hauteur;

	int spawn_location_x = rand() % (hauteur - 1); // % => Reste de la division entière
	int spawn_location_y = rand() % (largeur - 1); // % => Reste de la division entière
	int nombre_fantome_courant = 0;

	while (nombre_fantome_courant < game->nb_fantome) {
		char c = getElementAtPos(game, spawn_location_x, spawn_location_y);
		while (c != '0') {
			spawn_location_x = rand() % (hauteur - 1); // % => Reste de la division entière
			spawn_location_y = rand() % (largeur - 1); // % => Reste de la division entière
			c = getElementAtPos(game, spawn_location_x, spawn_location_y);
		}

		char fantom = 'f';
		int reponse = setElementAtPos(game, fantom, spawn_location_x,
				spawn_location_y);
		if (reponse == -1) {
			printf("erreur spawn fantom_01\n");
			return -1;
		}
		nombre_fantome_courant++;
	}
	if (nombre_fantome_courant != game->nb_fantome) {
		printf("erreur spawn fantom_02\n");
		return -1;
	}
	game->isGhost = YES;
	return 0;
}

struct participant* find_winner(struct game *game) {
	struct game *courant = game;
	if (courant->participants == NULL) {
		printf("COPY EST NUL\n");
		return NULL;
	}

	struct participant *winner = courant->participants;
	int fscore = winner->score;
	printf("SCORE DU GAGNANT %d", winner->score);

	while (courant->participants->next != NULL) {
		if (courant->participants->score > fscore) {
			fscore = courant->participants->score;
			winner = courant->participants;
		}
		courant->participants = courant->participants->next;
	}
	return winner;
}

int check_endgame(struct game *game) {
	if (game->nb_fantome <= 0 || game->players == 1) { //TODO: ici on devra verifier 2
		if (game->status == STATUS_AVAILABLE) {
			game->status = STATUS_UNAVAILABLE;
			struct participant *winner = find_winner(game);
			if (winner != NULL)
				end_message(game, winner);
			else
				printf("ERREUR PARTICIPANT VIDE");
			return FINISH;
		} else {
			return FINISH;
		}
	}
	return NOT_FINISH;
}

void printlabyrinth(struct game *game) {
	printf("------------------\n");
	for (int i = 0; i < game->largeur; i++) {
		for (int j = 0; j < game->hauteur; j++) {
			char c = game->labyrinth[i][j];
			if (c == '#') {
				printf(" X ");
			} else if (c == 'f') {
				printf(" F ");
			} else if (c == '0') {
				printf("   ");
			} else {
				printf(" P ");
			}
		}
		printf("\n");
	}
	printf("--------------------\n");
}
