init: c java

c: lobby/serveur.c
	gcc -c -Wall lobby/player_lobby.c
	gcc -pthread -Wall -g -o serveur lobby/serveur.c
	gcc -c -Wall game/labyrinthe.c
	gcc -c -Wall game/lobby.c

java: lobby/Client_Main.java
	javac lobby/*.java

distclean:
	rm -f *.o
