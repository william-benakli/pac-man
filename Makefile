# COMPILATION DES FICHIERS JAVA #####################
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) -cp client/src/ $<

CLASSES_JAVA = \
		client/src/ghostlab/Client_Main.java

# COMPILATION DES FICHIERS C #########################
CC = gcc
CFLAGS = -pthread -Wall
CPATH = serveur/lobby/c/
CPATH_H = include/
CPATH_GAME = game/
CPATH_UTILS = $(CPATH)utils/
INCLUDE_DIR = include
OUTPATH_C = $(CPATH)out/


#.SUFFIXES_C: .c .o
#.c.o:
#	$(CC) $(CFLAGS) -o serveur -c lobby/c $<

CLASSES_C = $(CPATH)movement.c $(CPATH)labyrinthlogic.c $(CPATH)serveur.c $(CPATH)player_lobby.c $(CPATH)join_game.c $(CPATH)leave_game.c $(CPATH)list_game.c $(CPATH)game.c $(CPATH_UTILS)reponse_game.c $(CPATH_UTILS)reponse_lobby.c $(CPATH_UTILS)reponse_serveur.c $(CPATH)serveur_game.c $(CPATH)serveur_lobby.c $(CPATH)udp_functions.c  

# LANCE LA COMPILATION DES FICHIERS ##################
default: c

java: $(CLASSES_JAVA:.java=.class)
#c: $(CLASSES_C:.c=.o)
c: $(CLASSES_C)
	$(CC) $(CFLAGS) -o $(OUTPATH_C)serveur $(CLASSES_C)

# NETOYAGE DES FICHIERS INUTILES #####################
clean:
	$(RM) serveur/lobby/c/-f *.o
	$(RM) client/src/ghostlab/*.class

######################################################
