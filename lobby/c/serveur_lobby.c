#include "../../include/serveur_lobby.h"
#include<signal.h>

int registerInput(struct player *player) {
	int socketclient = player->tcp_sock;

	while (1) {

		char buffer[SIZE_INPUT_DEFAULT + 1];
		buffer[SIZE_INPUT_DEFAULT] = '\0';
		int count = read(socketclient, buffer, SIZE_INPUT_DEFAULT);

		if (count != SIZE_INPUT_DEFAULT) {
			while (1)
				sleep(1);
			pthread_exit(NULL);
			// Gère le SIGKILL du client ragequit
			// sendDunno(socketclient, "ERROR cmd not found");
			// continue;
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

		select_action(player, buffer);
	}

	/*SORTIE DU BREAK */
	sendGodBye(socketclient);
	free(player);
	close(socketclient);
	return 0;
}

int select_action(struct player *player, char *buffer) {
	int socketclient = player->tcp_sock;

	if (strcmp(buffer, CMD_NEW_PARTY) == 0) {
		int rep_create = creategame(player, _games);
		if (rep_create == -1)
			sendRegNo(socketclient);
		else
			sendRegOk(socketclient, player->game_id);
	} else if (strcmp(buffer, CMD_REGISTER) == 0) {
		int rep_regis = regisgame(player, _games);
		if (rep_regis == -1)
			sendRegNo(socketclient);
		else
			sendRegOk(socketclient, player->game_id);
	} else if (strcmp(buffer, CMD_UNREG) == 0) {

		int check_stars = readStars(socketclient);
		if (check_stars == -1)
			sendDunno(socketclient, "CMD UNREG READSTARS ERROR");

		if (player->status_game == IN_LOBBY)
			sendDunno(socketclient, "UNREG ERREUR PLAYER EN LOBBY");

		u_int8_t id_partie = player->game_id;
		int rep_unreg = unregis(player, _games);
		if (rep_unreg == -1)
			sendDunno(socketclient, "UNREG ERREUR UNREG PLAYER LIST");

		int rep_regOk = sendUnRegOk(socketclient, id_partie);
		if (rep_regOk == -1)
			sendDunno(socketclient, "UNREG ERREUR UNREGOK FAIL");

	} else if (strcmp(buffer, CMD_GAME) == 0) {
		int check_stars = readStars(socketclient);
		if (check_stars == -1)
			sendDunno(socketclient, "CMD GAME READSTART ERROR");
		int rep_party = sendgames(socketclient);
		if (rep_party == -1)
			sendDunno(socketclient, "CMD GAME ENVOIE GAME ECHEC");
	} else if (strcmp(buffer, CMD_SIZE) == 0) {
		int rep_size = sendSize(socketclient, _games);
		if (rep_size == -1)
			sendDunno(socketclient, "CMD SIZE sendSIZE FAIL");
	} else if (strcmp(buffer, CMD_LIST) == 0) {
		int rep_list = sendList(socketclient, _games);
		if (rep_list == -1)
			sendDunno(socketclient, "CMD LIST sendList fail");
	} else if (strcmp(buffer, CMD_START) == 0) {
		int rep_list = player_game_accept(player, _games);
		if (rep_list == -1)
			sendDunno(socketclient, "CMD START FAILED");
		int rep_stars = readStars(socketclient);
		if (rep_stars == -1)
			sendDunno(socketclient, "CMD START FAILED miss ***");
		int rep_waiting = waiting_players(player);
		if (rep_waiting)
			sendDunno(socketclient, "CMD WAITING OR LAUNCH FAILED");
	} else {
		sendDunno(socketclient, "ERREUR ENTREE INCONNUE");
	}
	return 0;
}

int waiting_players(struct player *player) {
	struct game *game_courant = search_game(player->game_id, _games);
	if (game_courant == NULL)
		return -1;

	struct participant *partcipant_lobby = search_player_in_game(game_courant,
			player);
	if (partcipant_lobby == NULL)
		return -1;

	partcipant_lobby->player_ready = 1;

	while (check_ready(game_courant) == PLAYERS_NOT_READY
			|| game_courant->players < 2) {
		sleep(1);
	}
	pthread_mutex_lock (&verrou1);
	spawnFantomes(game_courant);
	pthread_mutex_unlock(&verrou1);

	launch_game(game_courant, partcipant_lobby, _games);
	return 0;
}

void launch_game(struct game *game_courant,
		struct participant *partcipant_lobby, struct list_game *_games) {
	pthread_mutex_lock (&verrou1);
	sendWelcome(game_courant, partcipant_lobby, _games);
	spawnJoueur(game_courant, partcipant_lobby);
	pthread_mutex_unlock(&verrou1);
	sendPosit(partcipant_lobby->tcp_sock, game_courant, partcipant_lobby);
	gameInput(partcipant_lobby->tcp_sock, partcipant_lobby, game_courant);
}
