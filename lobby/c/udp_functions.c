#include "../../include/udp_functions.h"

int mutilcast_message(struct game *_game, char *buf) {

	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct addrinfo *first_info;
	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	char private_buffer[strlen(buf) + 4];
	int bytes_written = sprintf(private_buffer, "%s+++", buf);
	if (bytes_written < 0) {
		return -1;
	}

	char port[5];
	sprintf(port, "%d", _game->port_udp);
	int r = getaddrinfo(_game->address_udp, port, &hints, &first_info);

	// private_buffer[strlen(private_buffer)] = '\0';

	printf("LE PORT UDP DE LA GAME: %s\n", _game->address_udp);
	printf("LE STRLEN: %ld\n", strlen(buf));
	printf("LE BUFFER: %s\n", buf);
	printf("R EST IL VALIDE: %d\n", r);

	if (r == 0) {
		if (first_info != NULL) {
			struct sockaddr *saddr = first_info->ai_addr;
			sendto(sock, private_buffer, strlen(private_buffer), 0, saddr,
					(socklen_t) sizeof(struct sockaddr_in));
		}
	}

	return 0;
}

int score_message(struct game *_game, struct participant *player, int fantom_x,
		int fantom_y) {

	char sender_id[9];
	memcpy(sender_id, player->identifiant, 8);
	sender_id[8] = '\0';

	char score_buffer[28];
	int bytes_written = sprintf(score_buffer, "SCORE %s %04d %03d %03d",
			sender_id, player->score, fantom_x, fantom_y);
	if (bytes_written < 0) {
		return -1;
	}

	int ret = mutilcast_message(_game, score_buffer);

	return ret;
}

int ghost_message(struct game *_game, int fantom_x, int fantom_y) {
	char ghost_buffer[14];
	int bytes_written = sprintf(ghost_buffer, "GHOST %03d %03d", fantom_x,
			fantom_y);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = mutilcast_message(_game, ghost_buffer);
	return ret;
}

int end_message(struct game *_game, struct participant *winner) {

	char sender_id[9];
	memcpy(sender_id, winner->identifiant, 8);
	sender_id[8] = '\0';

	char end_buffer[20];
	int bytes_written = sprintf(end_buffer, "ENDGA %s %04d",
			sender_id, winner->score);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = mutilcast_message(_game, end_buffer);
	return ret;
}

int group_message(struct game *_game, struct participant *sender,
		char *message) {
	char message_buffer[220];
	int bytes_written = sprintf(message_buffer, "MESSA %s %s",
			sender->identifiant, message);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = mutilcast_message(_game, message_buffer);
	return ret;
}

struct participant* find_player(struct game *game, char *id) {
	struct game *courant = game;
	if (courant->participants == NULL) {
		printf("PAS DE JOUEUR\n");
		return NULL;
	}

	struct participant *joueur = courant->participants;

	char id_cours[9];
	memcpy(id_cours, joueur->identifiant, 8);
	id_cours[8] = '\0';

	if (strcmp(id_cours, id) == 0) {
		return joueur;
	}

	memset(id_cours, 0, 9);

	while (courant->participants->next != NULL) {
		memcpy(id_cours, joueur->identifiant, 8);
		id_cours[8] = '\0';

		if (strcmp(id_cours, id) == 0) {
			return joueur;
		}
		memset(id_cours, 0, 9);

		courant->participants = courant->participants->next;
	}

	memcpy(id_cours, joueur->identifiant, 8);
	id_cours[8] = '\0';
	if (strcmp(id_cours, id) == 0) {
		return joueur;
	}

	printf("PAS TROUVE LE JOUEUR\n");
	return NULL;
}

int private_message(struct game *_game, char *target_id, char *message,
		struct participant *sender) {
	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct addrinfo *first_info;
	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	struct participant *joueur = find_player(_game, target_id);

	if (joueur == NULL) {
		return -1;
	}

	char sender_id[9];
	memcpy(sender_id, sender->identifiant, 8);
	sender_id[8] = '\0';

	char private_buffer[strlen(message) + 15];

	int bytes_written = sprintf(private_buffer, "%s: %s+++", sender_id,
			message);
	if (bytes_written < 0) {
		return -1;
	}

	char portbuffer[5];
	bytes_written = sprintf(portbuffer, "%d", joueur->udp_port);
	if (bytes_written < 0) {
		return -1;
	}

	//private_buffer[strlen(private_buffer)] = '\0';
	int r = getaddrinfo(joueur->address, portbuffer, &hints, &first_info);
	printf("LE PORT UDP DU JOUEUR: %s\n", portbuffer);
	printf("LE STRLEN: %ld\n", strlen(private_buffer));
	printf("LE BUFFER: %s\n", private_buffer);
	printf("R EST IL VALIDE: %d\n", r);

	if (r == 0) {
		if (first_info != NULL) {
			struct sockaddr *saddr = first_info->ai_addr;
			sendto(sock, private_buffer, strlen(private_buffer), 0, saddr,
					(socklen_t) sizeof(struct sockaddr_in));
		}
	}
	return 0;
}

int get_mall_message(int clientsocket, char *message_buffer) {
	char mall_buffer[210];
	int recieved = recv(clientsocket, mall_buffer, 204, 0);
	char star_buffer[4];
	memmove(message_buffer, mall_buffer + 1, recieved - 4); //ATTENTION A CA 
	message_buffer[recieved - 4] = '\0';
	char *s = strstr(message_buffer, "***");
	if (s != NULL) {
		return -1;
	}
	memmove(star_buffer, mall_buffer + (recieved - 3), 3);
	star_buffer[3] = '\0';
	if (strcmp(star_buffer, "***") != 0) {
		return -1;
	}
	return 0;
}

int get_private_message(int clientsocket, char *target_identifiant,
		char *message_buffer) {
	char send_buffer[220];
	int recieved = recv(clientsocket, send_buffer, 213, 0);
	char star_buffer[4];
	memmove(star_buffer, send_buffer + (recieved - 3), 3);
	star_buffer[3] = '\0';
	if (strcmp(star_buffer, "***") != 0) {
		return -1;
	}
	memmove(target_identifiant, send_buffer + 1, 8);
	memmove(message_buffer, send_buffer + 10, recieved - 13);
	message_buffer[recieved - 13] = '\0';
	char *s = strstr(message_buffer, "***");
	if (s != NULL) {
		return -1;
	}
	return 0;
}

