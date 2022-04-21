# COMPILATION DES FICHIERS JAVA #####################
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) -cp lobby/java $<

CLASSES_JAVA = \
        lobby/java/Client_TCP.java\
        lobby/java/Client_UDP.java\
	lobby/java/Client_Main.java

# COMPILATION DES FICHIERS C #########################
CC = gcc
CFLAGS = -pthread -Wall -I $(INCLUDE_DIR)
CPATH = lobby/c/
CPATH_GAME = game/
INCLUDE_DIR = include


#.SUFFIXES_C: .c .o
#.c.o:
#	$(CC) $(CFLAGS) -o serveur -c lobby/c $<

CLASSES_C = $(CPATH)serveur.c $(CPATH)player_lobby.c $(CPATH)list_game.c $(CPATH_GAME)lobby.c

# LANCE LA COMPILATION DES FICHIERS ##################
default: java c

java: $(CLASSES_JAVA:.java=.class)
#c: $(CLASSES_C:.c=.o)
c: $(CLASSES_C)
	$(CC) $(CFLAGS) -o serveur $(CLASSES_C)

# NETOYAGE DES FICHIERS INUTILES #####################
clean:
	$(RM) lobby/c/-f *.o
	$(RM) lobby/java/*.class

######################################################
