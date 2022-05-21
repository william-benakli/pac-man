#################### README_PROJET_GHOSTLAB ####################

GROUPE NUMERO 5:
- SHAN Michel
- ALKHAER Mohammad Hadi
- BENAKLI RABHA William

#################### PARTIE_CLIENT ####################

# Lancer le client: 

Tout d'abord assurez vous de n'avoir aucune fichier du type '.class'
pour cela il y'a la rubrique __#Nettoyer les .class du client__

Depuis le dossier (client) executer la commande:
**javac src/ghostlab/Client_Main.java**

Puis depuis ce même dossier faire la commande:
**java src.ghostlab.Client_Main [adresse_ip] [numéro_de_port]**

L'utilisation d'un makefile étant recommandé nous l'avons fait, mais par soucis
de sureté executer les commandes à la mains nous semble plus sécurisée.

# Nettoyer les .class du client:
Depuis le dossier (client) executer la commande:
**rm $(find -name "*.class")**
 
#################### PARTIE_SERVEUR ####################

# Lancer le serveur:
Depuis la racine du projet lancer la commande: **make**
+ En cas de problème de compatibilité de version lancer avant: **make clean**
Puis lancer la commande: **./serveur/lobby/c/out/serveur [numéro_de_port]**

# Nettoyer les fichiers générés par le serveur:
Depuis la racine du projet lancer la commande: **make clean**

################################################################

# Lancer correctement le projet
Il faut bien-sur **lancer le serveur** 
avant de **lancer le client**
sur **un même [numéro_de_port]**


