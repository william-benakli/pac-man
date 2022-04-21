import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.*;

public class Client_TCP implements Runnable {
	Socket socket;

	public Client_TCP(Socket sock) {
		this.socket = sock;
	}

	public void run() {
		try {
			// PARAMETRAGE POUR CLIENT TCP
			Scanner sc = new Scanner(System.in);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			String msg;
			
			// Recois msg - message en TCP: [GAMES␣n***]
			Command_games_number(is);

			// AVANT LA PARTIE
			while (true) {
				System.out.println("Info_commandes_reponses? tapez: INFOS***");
				System.out.println("Info_variables? tapez: VARSE***");
				System.out.print("Votre msg TCP: ");
				msg = sc.nextLine();
				Command_Check(msg, is, os);

			}

			// TODO: LE DEBUT DE LA PARTIE

			// FERMETURE DES SOCKETS
			// pw.close();
			// socket.close();
			// return;
		} catch (

		Exception e) {
			if (!hostAvailabilityCheck(socket.getInetAddress().toString(), socket.getPort())) {
				System.out.println("Le serveur est deconnecte_TCP.");
			}
			e.printStackTrace();
			return;
		}
	}

	public static boolean Check_valide_message(String s) {
		try {
			if (s.length() < 3) {
				return false;
			}
			if (s.charAt(s.length() - 1) != '*') {
				return false;
			}
			if (s.charAt(s.length() - 2) != '*') {
				return false;
			}
			if (s.charAt(s.length() - 3) != '*') {
				return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("ERREUR CHECK VALIDE MESSAGE");
			return false;
		}
	}

	// Renvoie un int a partir d'un String qui se suit
	public static int Check_int_value(String s, int pos) {
		String val = "";
		for (int i = pos; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				val += s.charAt(i);
			} else {
				break;
			}
		}
		return Integer.valueOf(val);
	}

	// Renvoie le nombre de partie a partir d'une string selon protocole
	public static int Check_nb_game(String s) {
		if (Check_valide_message(s)) {
			String nb_reversed = "";
			String nb = "";
			// Ajoute a nb le nombre a l'envers
			for (int i = s.length() - 4; i >= 0; i--) {
				if (Character.isDigit(s.charAt(i))) {
					nb_reversed += s.charAt(i);
				} else {
					break;
				}
			}
			// Renverse nb
			for (int i = nb_reversed.length() - 1; i >= 0; i--) {
				nb += nb_reversed.charAt(i);
			}
			return Integer.valueOf(nb);
		}
		return -1;
	}

	// Renvoie aide pour les commandes du joueur dans le lobby
	public static void help_commands_lobby() {
		System.out.println("GUIDE_COMMANDES_LOBBY_JOUEURS:");
		System.out.println(" [NEWPL␣id␣port***] pour creer une partie");
		System.out.println(" [REGIS␣id␣port␣m***] pour rejoindre une partie");
		System.out.println(" [UNREG***] pour se desinscrire d'une partie");
		System.out.println(" [SIZE?␣m***] pour connaitre taille du labyrinthe");
		System.out.println(" [LIST?␣m***] pour demander la liste des joueurs");
		System.out.println(" [GAME?***] voir la liste des parties non commence");
		System.out.println(" [START***] pour etre pret (doit etre inscrit)");
		System.out.println("GUIDE_REPONSES_LOBBY_SERVEUR:");
		System.out.println(" [GAMES␣n***] nombre de partie non commence");
		System.out.println(" [OGAME␣m␣s***] infos de la partie");
		System.out.println(" [REGOK␣m***] inscription dans la partie ok");
		System.out.println(" [REGNO***] inscription dans la partie refusee");
		System.out.println(" [UNROK␣m***] desinscription acceptee");
		System.out.println(" [DUNNO***] pas inscrit / partie existe pas");
		System.out.println(" [SIZE!␣m␣h␣w***] infos du labyrinthe");
		System.out.println(" [PLAYR␣id***] info id joueur");
	}

	// Renvoie aide pour les explications des variable du joueur dans le lobby
	public static void help_understand_lobby() {
		System.out.println("GUIDE_VAR_LOBBY_JOUEURS:");
		System.out.println(" m: numero de partie");
		System.out.println(" id: identifiant du joueur");
		System.out.println(" port: port UDP");
		System.out.println("GUIDE_VAR_LOBBY_SERVEUR:");
		System.out.println(" n: nombre de partie");
		System.out.println(" m: numero de partie");
		System.out.println(" s: nombre de joueur inscrit");
		System.out.println(" id: identifiant du joueur");
		System.out.println(" h: hauteur du labyrinthe");
		System.out.println(" w: largeur du labyrinthe");
	}

	// VEIRIFIE LA COMMANDE PUIS REDIRIGE VERS LA BONNE METHODE
	public static void Command_Check(String msg, InputStream is, OutputStream os) {
		String lecture = "";
		String commande = "";
		int pos_lecture = 0;
		// remplie un tab de char avec la string en parametre
		char[] msg_decrypte = new char[msg.length()];
		for (int i = 0; i < msg.length(); i++) {
			msg_decrypte[i] = msg.charAt(i);
		}
		// tant que on a pas tout lu on continue
		while (pos_lecture != msg_decrypte.length) {
			// lire le tab de char one by one char si c'est valide on gere
			for (int i = pos_lecture; i < msg_decrypte.length; i++) {
				lecture += msg_decrypte[i];
				if (Check_valide_message(lecture)) {
					commande = Command_Read(lecture);
					switch (commande) {
					case "INFOS":
						help_commands_lobby();
						break;
					case "VARSE":
						help_understand_lobby();
						break;
					case "NEWPL":
						Command_new_player(msg, is, os);
						break;
					case "REGIS":
						// TODO
						break;
					case "UNREG":
						// TODO
						break;
					case "SIZE?":
						// TODO
						break;
					case "LIST?":
						// TODO
						break;
					case "GAME?":
						// TODO
						break;
					case "START":
						// TODO
						break;
					default:
						System.out.println("COMMANDE NON RECONNUE! (COMMAND_CHECK)");
					}
					lecture = "";
				}
				pos_lecture++;
			}
			System.out.println("Derniere lecture: " + lecture);
		}
	}

	// Pour verifier la commande 5 premiers chars (commande)
	public static String Command_Read(String msg) {
		String res = "";
		for (int i = 0; i < 5; i++)
			res += msg.charAt(i);
		return res;
	}

	// COMMANDE_SEND: [NEWPL␣id␣port***]
	public static void Command_new_player(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);
			// Recois msg - message en TCP
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte = new byte[5];
			if ((read = is.read(msg_byte)) != -1) {
				receptacle = new String(msg_byte, 0, read);
			}
			for (int i = 0; i < msg_byte.length; i++) {
				byte b = (byte) msg_byte[i];
				msg += (char) b;
			}
			if (msg == "REGOK") {
				byte[] msg_byte_OK = new byte[5];
				if ((read = is.read(msg_byte_OK)) != -1) {
					receptacle = new String(msg_byte_OK, 0, read);
				}
				for (int i = 0; i < msg_byte_OK.length; i++) {
					byte b = (byte) msg_byte_OK[i];
					if (i == 1) {
						msg += String.valueOf((int) b);
					} else {
						msg += (char) b;
					}
				}
			} else if (msg == "REGNO") {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[NEWPL␣id␣port***]");
				System.out.println(msg);
			}
		} catch (Exception e) {
			System.out.println("ERREUR NEWPL");
			e.printStackTrace();
		}
	}

	// COMMANDE_READ: [GAMES␣n***]
	public static void Command_games_number(InputStream is) {
		try {
			// message [GAMES␣n***] où n indique le nombre de parties (non commence)
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte = new byte[10];
			if ((read = is.read(msg_byte)) != -1) {
				receptacle = new String(msg_byte, 0, read);
			}
			for (int i = 0; i < msg_byte.length; i++) {
				byte b = (byte) msg_byte[i];
				if (i != 6) {
					msg += (char) b;
				} else {
					msg += String.valueOf((int) b);
				}
			}
			if (!Check_valide_message(msg)) {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GAMES␣n***]");
				System.out.println(msg);
				return;
			}
			System.out.println("J'ai reçu en TCP: " + msg);
			// retourne le nombre n dans [GAMES␣n***]
			int nombre_de_partie = Check_nb_game(msg);
			// [OGAME␣m␣s***] m indique le numéro de partie et s le nombre de joueur
			if (Check_int_value(msg, 6) == 0) {
				System.out.println("Aucune partie en cours..");
			} else {
				System.out.println("Nombre de parties disponible: " + Check_int_value(msg, 6));
			}
			for (int j = 0; j < nombre_de_partie; j++) {
				byte[] msg_byte2 = new byte[12];
				if ((read = is.read(msg_byte2)) != -1) {
					receptacle = new String(msg_byte2, 0, read);
				}
				msg = "";
				for (int i = 0; i < msg_byte2.length; i++) {
					byte b = (byte) msg_byte2[i];
					// Printing the content of byte array
					if (i != 6 && i != 8) {
						msg += (char) b;
					} else {
						msg += String.valueOf((int) b);
					}
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[OGAME␣m␣s***]");
					System.out.println(msg);
					return;
				}
				System.out.println("J'ai reçu en TCP: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[GAMES␣n***]/[OGAME␣m␣s***]");
			// e.printStackTrace();
			return;
		}
	}

	// Renvoie si la connexion est coupe ou non
	public static boolean hostAvailabilityCheck(String adress, int port) {
		try (Socket s = new Socket(adress, port)) {
			return true;
		} catch (IOException ex) {
			System.out.println("Main_Client_TCP");
		}
		return false;
	}
}
