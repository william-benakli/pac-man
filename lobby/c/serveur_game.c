#include "../../include/serveur_game.h"

int gameInput(int socketclient, struct participant *partcipant_ingame,
		struct game *game_courant) {

	//while (check_endgame(game_courant) == NOT_FINISH) {
	while (1) {
		deplace_fantom(game_courant);

		printlabyrinth(game_courant);

		size_t size_buffer_first = SIZE_INPUT_DEFAULT;
		char buffer[size_buffer_first + 1];
		buffer[size_buffer_first] = '\0';

		int count_fst = read(socketclient, buffer, size_buffer_first);

		if (game_courant->status == STATUS_UNAVAILABLE) {
			break;
		}

		if (count_fst == 0){
			int ret = remove_player(partcipant_ingame,game_courant);
			free(partcipant_ingame);
			close(socketclient);
			return ret;
		}

		printf("%s\n", buffer);
		printf("hi, %d\n", count_fst);
		if (count_fst != size_buffer_first) {
			printf("[GAME] Erreur de taille count: [%d] / buffer: [%ld] #1\n",
					count_fst, size_buffer_first);
			return -1;
		}

		/* JOUEUR QUIT LA PARTIE */
		if (strcmp(buffer, "IQUIT") == 0) {
			int rep_stars = readStars(socketclient);
			if (rep_stars == -1) {
				sendDunno(socketclient, "IQUIT but '***' miss");
				continue;
			} else {
				break;
			}
		}

		if (strcmp(buffer, "GLIS?") == 0) {
			int rep_stars = readStars(socketclient);
			if (rep_stars == -1) {
				sendDunno(socketclient,
						"GLIS? but '***' miss ligne 45 de serveur_game.c");
				continue;
			} else {
				sendGlist(socketclient, game_courant);
				continue;
			}
		}
		if (strcmp(buffer, "MALL?") == 0) {
			char message_buffer[201];
			message_buffer[200] = '\0';

			int test_mall = get_mall_message(socketclient, message_buffer);
			printf("MSGBUFFER: %s\n", message_buffer);

			if (test_mall != 0) {
				printf(
						"erreur remplir le buffer de message ligne 34 fichier serveur_game.c\n");
				send(socketclient, "NMALL***", 8 * sizeof(char), 0);
				continue;
			}

			test_mall = mutilcast_message(game_courant, message_buffer);

			if (test_mall != 0) {
				printf(
						"erreur envoyer le buffer de message ligne 38 fichier serveur_game.c\n");
				send(socketclient, "NMALL***", 8 * sizeof(char), 0);
				continue;
			}
			send(socketclient, "MALL!***", 8 * sizeof(char), 0);
			continue;
		}

		if (strcmp(buffer, "SEND?") == 0) {
			char target_identifiant[9];
			char message_buffer[201];
			target_identifiant[8] = '\0';
			message_buffer[200] = '\0';

			int test_private = get_private_message(socketclient,
					target_identifiant, message_buffer);

			printf("TARGET_ID: %s\n", target_identifiant);
			printf("MSGBUFFER: %s\n", message_buffer);

			if (test_private != 0) {
				printf(
						"erreur remplir le buffer de message ligne 50 fichier serveur_game.c\n");
				send(socketclient, "NSEND***", 8 * sizeof(char), 0);
				continue;
			}

			test_private = private_message(game_courant, target_identifiant,
					message_buffer, partcipant_ingame);

			if (test_private != 0) {
				printf(
						"erreur remplir le buffer de message ligne 56 fichier serveur_game.c\n");
				send(socketclient, "NSEND***", 8 * sizeof(char), 0);
				continue;
			}
			send(socketclient, "SEND!***", 8 * sizeof(char), 0);
			continue;
		}

		if (strcmp(buffer, "RIMOV") != 0 && strcmp(buffer, "UPMOV") != 0
				&& strcmp(buffer, "DOMOV") != 0
				&& strcmp(buffer, "LEMOV") != 0) {
			sendDunno(socketclient, "ERROR NOT GOOD SYNTAXE");
			continue;
		}

		size_t size_buffer_snd = SIZE_ONE_SPACE + SIZE_DISTANCE
				+ SIZE_INPUT_STAR;
		char buffer_movement[size_buffer_snd + 1];
		buffer_movement[size_buffer_snd] = '\0';

		int count_snd = read(socketclient, buffer_movement, size_buffer_snd);

		if (count_snd != size_buffer_snd) {
			printf("[GAME] Erreur de taille count: [%d] / buffer: [%ld] #2\n",
					count_snd, size_buffer_snd);
			sendDunno(socketclient, "ERROR NOT GOOD SYNTAXE");
			continue;
		}

		char distance[SIZE_DISTANCE];
		char stars[SIZE_INPUT_STAR + 1];
		stars[SIZE_INPUT_STAR] = '\0';

		memmove(distance, buffer_movement + SIZE_ONE_SPACE, SIZE_DISTANCE);
		memmove(stars, buffer_movement + SIZE_ONE_SPACE + SIZE_DISTANCE,
				SIZE_INPUT_STAR);

		if (strcmp("***", stars) != 0) {
			sendDunno(socketclient, "MOV but '***' miss");
			continue;
		}
		move_by_action(buffer, distance, game_courant, partcipant_ingame);
	}

	/*SORTIE DU BREAK */
	sendGodBye(socketclient);
	free(partcipant_ingame);
	close(socketclient);
	pthread_exit (NULL);

	return 0;
}

void move_by_action(char *direction, char *distance, struct game *game_courant,
		struct participant *partcipant_ingame) {
	if (strcmp(direction, CMD_RIMOV) == 0) {
		move(MOVERIGHT, distance, game_courant, partcipant_ingame);
	} else if (strcmp(direction, CMD_LEMOV) == 0) {
		move(MOVELEFT, distance, game_courant, partcipant_ingame);
	} else if (strcmp(direction, CMD_UPMOV) == 0) {
		move(MOVEUP, distance, game_courant, partcipant_ingame);
	} else if (strcmp(direction, CMD_DOMOV) == 0) {
		move(MOVEDOWN, distance, game_courant, partcipant_ingame);
	} else {
		sendDunno(partcipant_ingame->tcp_sock,
				"[GAME ACTION] Argument introuvable");
	}
}


int remove_player(struct participant *leaver, struct game *game){
	struct game *copy = game;

	if(copy->participants->tcp_sock == leaver->tcp_sock){
		game->participants->next = game->participants->next->next;
		game->players--;
	}

	while(copy->participants->next != NULL){
		if(copy->participants->next->tcp_sock == leaver->tcp_sock){
			copy->participants->next = copy->participants->next->next;
			game->players--;
		}
	}

	if (game->players <= 0){
		remove_game(game,_games);
	}

	return 0;
}