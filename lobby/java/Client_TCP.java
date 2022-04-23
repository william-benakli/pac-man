import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Client_TCP implements Runnable {
	Socket socket;
	static int is_the_game_started = 0;

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
			while (is_the_game_started == 0) {
				System.out.println("\nInfo_commandes_reponses? tapez: INFOS***");
				System.out.println("Info_variables? tapez: VARSE***");
				System.out.print("Votre msg TCP: ");
				msg = sc.nextLine();
				Command_Check(msg, is, os);
			}
			System.out.println("LETS GO!");
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
		if (val.equals("")) {
			return -1;
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

	public static boolean Check_if_integer_zero_nine(byte b) {
		try {
			String crashtest = "";
			crashtest += String.valueOf((int) b);
			int valideoupas = Integer.valueOf(crashtest);
			if (valideoupas >= 0 && valideoupas <= 9)
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
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
						Command_regis_player(msg, is, os);
						break;
					case "UNREG":
						Command_unreg_player(msg, is, os);
						break;
					case "SIZE?":
						Command_size_player(msg, is, os);
						break;
					case "LIST?":
						Command_list_player(msg, is, os);
						break;
					case "GAME?":
						try {
							// Envoie msg - message en TCP
							os.write(msg.getBytes());
							System.out.println("J'ai envoye en TCP: " + msg);
							// Recois msg - message en TCP
							Command_games_number(is);
						} catch (Exception e) {
							System.out.println("ERREUR GAME? (COMMAND_CHECK)");
						}
						break;
					case "START":
						try {
							// Envoie msg - message en TCP
							String start = "START";
							os.write(start.getBytes());
							System.out.println("J'ai envoye en TCP: " + msg);
							is_the_game_started = 1;
						} catch (Exception e) {
							System.out.println("ERREUR START (COMMAND_CHECK)");
						}
						return;
					default:
						System.out.println("COMMANDE NON RECONNUE! (COMMAND_CHECK)");
						System.out.println("Entrer INFOS ou VARSE pour voir les commandes detailles");
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

	// COMMANDE_SEND: [LIST?␣m***] m sur un byte
	public static void Command_list_player(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
			for (int i = 0; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i))) {
					if (Check_int_value(str, i) > 255 || Check_int_value(str, i) < 0) {
						System.out
								.println("Numero de partie invalide car sous uint8 on ne depasse pas 255 [LIST?␣m***]");
						return;
					}
					byte_out.write((byte) Check_int_value(str, i));
					String str_tmp = String.valueOf(Check_int_value(str, i));
					i += str_tmp.length() - 1;
				} else {
					byte_out.write((byte) str.charAt(i));
				}
			}
			byte[] to_send = byte_out.toByteArray();
			System.out.println("LENGTH DU BYTE BUFFER: " + to_send.length);
			os.write(to_send);
			System.out.println("J'ai envoye en TCP: " + str);

			// Recois msg - message en TCP
			String msg = "";
			String receptacle = null;
			int read;
			String str_nombre_de_joueur = "";
			int nombre_de_joueur = 0;
			byte[] msg_byte = new byte[5];
			if ((read = is.read(msg_byte)) != -1) {
				receptacle = new String(msg_byte, 0, read);
			}
			for (int i = 0; i < msg_byte.length; i++) {
				byte b = (byte) msg_byte[i];
				msg += (char) b;
			}
			if (msg.equals("LIST!")) {
				byte[] msg_byte_OK = new byte[7];
				if ((read = is.read(msg_byte_OK)) != -1) {
					receptacle = new String(msg_byte_OK, 0, read);
				}
				for (int i = 0; i < msg_byte_OK.length; i++) {
					byte b = (byte) msg_byte_OK[i];
					if (i == 1 || i == 3) {
						msg += String.valueOf((int) b);
						if (i == 3) {
							str_nombre_de_joueur += String.valueOf((int) b);
							nombre_de_joueur = Integer.valueOf(str_nombre_de_joueur);
						}
					} else {
						msg += (char) b;
					}
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				// la partie suivi de s messages de la forme [PLAYR␣id***]
				if (nombre_de_joueur == 0) {
					System.out.println("Il n'y a aucun joueur dans cette partie.");
				} else {
					for (int i = 0; i < nombre_de_joueur; i++) {
						msg = "";
						byte[] msg_byte_joueur = new byte[17];
						if ((read = is.read(msg_byte_joueur)) != -1) {
							receptacle = new String(msg_byte_joueur, 0, read);
						}
						for (int j = 0; j < msg_byte_joueur.length; j++) {
							byte b = (byte) msg_byte_joueur[j];
							if (Check_if_integer_zero_nine(b)) {
								msg += String.valueOf((int) b);
							} else {
								msg += (char) b;
							}
						}
						System.out.println("J'ai reçu en TCP: " + msg);
					}
				}
			} else if (msg.equals("DUNNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				System.out.println("J'ai reçu en TCP: " + msg);
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[LIST?␣m***]");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("ERREUR LIST");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [SIZE?␣m***] m sur un byte
	public static void Command_size_player(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
			for (int i = 0; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i))) {
					if (Check_int_value(str, i) > 255 || Check_int_value(str, i) < 0) {
						System.out
								.println("Numero de partie invalide car sous uint8 on ne depasse pas 255 [SIZE?␣m***]");
						return;
					}
					byte_out.write((byte) Check_int_value(str, i));
					String str_tmp = String.valueOf(Check_int_value(str, i));
					i += str_tmp.length() - 1;
				} else {
					byte_out.write((byte) str.charAt(i));
				}
			}
			byte[] to_send = byte_out.toByteArray();
			System.out.println("LENGTH DU BYTE BUFFER: " + to_send.length);
			os.write(to_send);
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
			if (msg.equals("SIZE!")) {
				byte[] msg_byte_OK = new byte[11];
				if ((read = is.read(msg_byte_OK)) != -1) {
					receptacle = new String(msg_byte_OK, 0, read);
				}
				for (int i = 0; i < msg_byte_OK.length; i++) {
					byte b = (byte) msg_byte_OK[i];
					// Attention [SIZE!␣m␣h␣w***] h et w sur 2 bytes et m sur 1 byte
					// et code en little endian (protocol)
					switch (i) {
					case 1:
						msg += String.valueOf((int) b);
						break;
					case 3:
						byte[] dbyte_h = new byte[2];
						dbyte_h[0] = (byte) msg_byte_OK[i];
						dbyte_h[1] = (byte) msg_byte_OK[i + 1];
						i++;
						int x_h = java.nio.ByteBuffer.wrap(dbyte_h).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
						msg += String.valueOf(x_h);
						break;
					case 6:
						byte[] dbyte_w = new byte[2];
						dbyte_w[0] = (byte) msg_byte_OK[i];
						dbyte_w[1] = (byte) msg_byte_OK[i + 1];
						i++;
						int x_w = java.nio.ByteBuffer.wrap(dbyte_w).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
						msg += String.valueOf(x_w);
						break;
					default:
						msg += (char) b;
					}
				}
			} else if (msg.equals("DUNNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[SIZE?␣m***]");
				System.out.println(msg);
			}
			System.out.println("J'ai reçu en TCP: " + msg);
		} catch (

		Exception e) {
			System.out.println("ERREUR SIZE");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [UNREG***]
	public static void Command_unreg_player(String str, InputStream is, OutputStream os) {
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
			if (msg.equals("UNROK")) {
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
			} else if (msg.equals("DUNNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[UNREG***]");
				System.out.println("MESSAGE RECU: " + msg);
			}
			System.out.println("J'ai reçu en TCP: " + msg);
		} catch (Exception e) {
			System.out.println("ERREUR UNREG");
			e.printStackTrace();
		}
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
			if (msg.equals("REGOK")) {
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
			} else if (msg.equals("REGNO")) {
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
				System.out.println("MESSAGE RECU: " + msg);
			}
			System.out.println("J'ai reçu en TCP: " + msg);
		} catch (Exception e) {
			System.out.println("ERREUR NEWPL");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [REGIS␣id␣port␣m***] m en byte
	public static void Command_regis_player(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
			for (int i = 0; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i)) && i >= 20) {
					if (Check_int_value(str, i) > 255 || Check_int_value(str, i) < 0) {
						System.out
								.println("Numero de partie invalide car sous uint8 on ne depasse pas 255 [REGIS␣id␣port␣m***]");
						return;
					}
					byte_out.write((byte) Check_int_value(str, i));
					String str_tmp = String.valueOf(Check_int_value(str, i));
					i += str_tmp.length() - 1;
				} else {
					byte_out.write((byte) str.charAt(i));
				}
			}
			byte[] to_send = byte_out.toByteArray();
			System.out.println("LENGTH DU BYTE BUFFER: " + to_send.length);
			os.write(to_send);
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
			if (msg.equals("REGOK")) {
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
			} else if (msg.equals("REGNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[REGIS␣id␣port␣m***]");
				System.out.println("MESSAGE RECU: " + msg);
			}
			System.out.println("J'ai reçu en TCP: " + msg);
		} catch (Exception e) {
			System.out.println("ERREUR REGIS");
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
				System.out.println("MESSAGE RECU: " + msg);
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
					System.out.println("MESSAGE RECU: " + msg);
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
