#include "../../include/udp_functions.h"

int broadcast_message(struct game *_game, char *buf) {

	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct addrinfo *first_info;
	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	char portbuffer[5];
	int bytes_written = sprintf(portbuffer, "%d", _game->port_udp);
	if (bytes_written < 0) {
		return -1;
	}

	int r = getaddrinfo(_game->address_udp, portbuffer, &hints, &first_info);
	if (r == 0) {
		if (first_info != NULL) {
			struct sockaddr *saddr = first_info->ai_addr;
			sendto(sock, buf, strlen(buf), 0, saddr,
					(socklen_t) sizeof(struct sockaddr_in));
		}
	}
	return 0;
}

int score_message(struct game *_game, struct participant *player, int fantom_x,
		int fantom_y) {
	char score_buffer[30];
	int bytes_written = sprintf(score_buffer, "SCORE %s %04d %03d %03d+++",
			player->identifiant, player->score, fantom_x, fantom_y);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = broadcast_message(_game, score_buffer);
	return ret;
}

int ghost_message(struct game *_game, int fantom_x, int fantom_y) {
	char ghost_buffer[20];
	int bytes_written = sprintf(ghost_buffer, "GHOST %03d %03d+++", fantom_x,
			fantom_y);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = broadcast_message(_game, ghost_buffer);
	return ret;
}

int end_message(struct game *_game, struct participant *winner) {
	char end_buffer[30];
	int bytes_written = sprintf(end_buffer, "ENDGA %s %04d+++",
			winner->identifiant, winner->score);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = broadcast_message(_game, end_buffer);
	return ret;
}

int group_message(struct game *_game, struct participant *sender,
		char *message) {
	char message_buffer[220];
	int bytes_written = sprintf(message_buffer, "MESSA %s %s+++",
			sender->identifiant, message);
	if (bytes_written < 0) {
		return -1;
	}
	int ret = broadcast_message(_game, message_buffer);
	return ret;
}

int private_message(struct game *_game, char *target_identifiant, char *message,
		struct participant *sender) {
	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct addrinfo *first_info;
	struct addrinfo hints;
	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	struct participant *copy_players = _game->participants;
	while (strcmp(copy_players->identifiant, target_identifiant) != 0) {
		copy_players = copy_players->next;
	}

	if (copy_players == NULL) {
		return -1;
	}

	char private_buffer[230];
	int bytes_written = sprintf(private_buffer, "MESSP %s %s+++",
			sender->identifiant, message);
	if (bytes_written < 0) {
		return -1;
	}

	char portbuffer[5];
	bytes_written = sprintf(portbuffer, "%d", copy_players->udp_port);
	if (bytes_written < 0) {
		return -1;
	}

	int r = getaddrinfo(copy_players->address, portbuffer, &hints, &first_info);
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

