#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

// UDP SERVEUR C

int main(int argc, char *argv[]) {
	int port = atoi(argv[1]);
	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct sockaddr_in address_sock;
	address_sock.sin_family = AF_INET;
	address_sock.sin_port = htons(port);
	address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
	int r = bind(sock, (struct sockaddr*) &address_sock,
			sizeof(struct sockaddr_in));
	if (r == 0) {
		char tampon[100];
		while (1) {
			int rec = recv(sock, tampon, 100, 0);
			if (rec < 0) {
				perror("Message non valide.");
			}
			printf("Message recu: %s", tampon);
			memset(tampon,0,strlen(tampon));
		}
	} else {
		perror("ERREUR DE MESSAGE DANS SERVEUR UDP C.");
	}
	
	return 0;
}
