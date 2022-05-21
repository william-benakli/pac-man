package src.ghostlab.thread;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Client_TCP implements Runnable {
	Socket socket;
	static int is_the_game_started = 0;
	static int is_the_game_ended = 0;
	static String port_du_joueur;

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

			System.out.println("LETS GO! Veuillez attendre que tous les joueurs soit prêts.");

			// TOUS LES JOUEURS SONT PRETS
			String str_info_UDP = Command_welcome(is);
			if (str_info_UDP.equals("NOTINGAME")) {
				System.out.println("Vous n'êtes pas inscrit dans une partie vous allez être deconnecté.");
				this.socket.close();
				return;
			}

			// INFOS UDP
			String str_socket_UDP = Check_udp_port(str_info_UDP);
			String adress_ip_udp = Check_udp_ip(str_info_UDP);
			
			System.out.println("Voici le port_UDP de la partie: " + str_socket_UDP);
			System.out.println("Voici votre port_UDP de joueur: " + port_du_joueur);
			System.out.println("Voici votre ip_UDP: " + adress_ip_udp);
			
			// CREER LES CLIENTS UDP
			MulticastSocket client_udp_jeu = new MulticastSocket(Integer.valueOf(str_socket_UDP));
			MulticastSocket client_udp_joueur = new MulticastSocket(Integer.valueOf(port_du_joueur));
			
			// Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
			Client_UDP launcher_UDP_jeu = new Client_UDP(client_udp_jeu, adress_ip_udp);
			Client_UDP launcher_UDP_joueur = new Client_UDP(client_udp_joueur, adress_ip_udp);

			// CREER LES THREADS UDP
			Thread t_udp_jeu = new Thread(launcher_UDP_jeu);
			Thread t_udp_joueur = new Thread(launcher_UDP_joueur);

			// LANCE LES THREADS UDP
			t_udp_jeu.start();
			t_udp_joueur.start();

			// POSITION DE DEBUT DE PARTIE
			Command_posit(is);

			// PENDANT LA PARTIE
			while (is_the_game_ended == 0) {
				System.out.println("\nInfo_commandes_reponses? tapez: INFOS***");
				System.out.println("Info_variables? tapez: VARSE***");
				System.out.print("Votre msg TCP: ");
				msg = sc.nextLine();
				Command_Check_in_game(msg, is, os);
			}
			// FERMETURE DES SOCKETS FIN DE LA PARTIE
			socket.close();
			System.out.println("Votre partie est fini vous êtes allez être déconnecté.");
			
			// STOP LE THREAD UDP
			t_udp_jeu.interrupt();
			t_udp_joueur.interrupt();
			
			// FERME TOUT
			System.exit(1);
			
			return;
		} catch (Exception e) {
			if (!hostAvailabilityCheck(socket.getInetAddress().toString(), socket.getPort())) {
				System.out.println("Le serveur est deconnecte_TCP.");
			}
			e.printStackTrace();
			return;
		}
	}

	public static String Check_udp_port(String s) {
		String res = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '/') {
				res += s.charAt(i);
			} else
				break;
		}
		return res;
	}

	public static String Check_udp_ip(String s) {
		String res = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '/') {
				for (int j = i + 1; j < s.length(); j++) {
					if (s.charAt(j) != '#') {
						res += s.charAt(j);
					} else
						break;
				}
				break;
			}
		}
		return res;
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

	// Renvoie aide pour les commandes du joueur dans la partie
	public static void help_commands_in_game() {
		System.out.println("GUIDE_COMMANDES_INGAME_JOUEURS:");
		System.out.println("  [UPMOV␣d***] pour vous deplacer en haut");
		System.out.println("  [DOMOV␣d***] pour vous deplacer en bas");
		System.out.println("  [LEMOV␣d***] pour vous deplacer à gauche");
		System.out.println("  [RIMOV␣d***] pour vous deplacer à droite");
		System.out.println("  [IQUIT***] pour quitter le jeu");
		System.out.println("  [GLIS?***] pour demander la liste des joueurs");
		System.out.println("  [MALL?␣mess***] pour envoyer un message a tous les joueurs");
		System.out.println("  [SEND?␣id␣mess***] pour envoyer un message privé");
		System.out.println("GUIDE_REPONSES_INGAME_SERVEUR:");
		System.out.println("  [MOVE!␣x␣y***] nouvelle position du joueur");
		System.out.println("  [MOVEF␣x␣y␣p***] nouvelle position du joueur après avoir rencontré un fantome");
		System.out.println("  [GOBYE***] fermeture de la connection si le joueur quitte ou si la partie est terminée");
		System.out.println("  [GLIS!␣s***] nombre de joueur dans la partie");
		System.out.println("  [GPLYR␣id␣x␣y␣p***] informations des joueurs");
		System.out.println("  [MALL!***] votre message a été envoyé à tous les joueurs");
		System.out.println("  [SEND!***] votre message privé a été envoyé");
		System.out.println("  [NSEND***] votre message privé n'a pas été envoyé");
		System.out.println("  [DUNNO***] votre commande n'est pas reconnue");
	}

	// Renvoie aide pour les explications des variable du joueur dans la partie TODO
	public static void help_understand_in_game() {
		System.out.println("GUIDE_VAR_INGAME_JOUEURS:");
		System.out.println("  d: distance de déplacement [3 caractères]");
		System.out.println("  id: identifiant du joueur [8 caractères]");
		System.out.println("  mess: message jusqu'à 200 caractères sans *** et +++ [0-200 caractères]");
		System.out.println("GUIDE_VAR_INGAME_SERVEUR:");
		System.out.println("  x: position du joueur horizontalement [3 caractères");
		System.out.println("  y: position du joueur verticalement [3 caractères]");
		System.out.println("  p: nombre de points du joueur [3 caractères]");
		System.out.println("  s: nombre de joueur dans la partie [valeur numérique allant à MAX: 255]");
		System.out.println("  id: identifiant du joueur [8 caractères]");
	}

	// Renvoie aide pour les commandes du joueur dans le lobby
	public static void help_commands_lobby() {
		System.out.println("GUIDE_COMMANDES_LOBBY_JOUEURS:");
		System.out.println("  [NEWPL␣id␣port***] pour creer une partie");
		System.out.println("  [REGIS␣id␣port␣m***] pour rejoindre une partie");
		System.out.println("  [UNREG***] pour se desinscrire d'une partie");
		System.out.println("  [SIZE?␣m***] pour connaitre taille du labyrinthe");
		System.out.println("  [LIST?␣m***] pour demander la liste des joueurs");
		System.out.println("  [GAME?***] voir la liste des parties non commence");
		System.out.println("  [START***] pour etre pret (doit etre inscrit)");
		System.out.println("GUIDE_REPONSES_LOBBY_SERVEUR:");
		System.out.println("  [GAMES␣n***] nombre de partie non commence");
		System.out.println("  [OGAME␣m␣s***] infos de la partie");
		System.out.println("  [REGOK␣m***] inscription dans la partie ok");
		System.out.println("  [REGNO***] inscription dans la partie refusee");
		System.out.println("  [UNROK␣m***] desinscription acceptee");
		System.out.println("  [DUNNO***] pas inscrit / partie existe pas");
		System.out.println("  [SIZE!␣m␣h␣w***] infos du labyrinthe");
		System.out.println("  [PLAYR␣id***] info id joueur");
	}

	// Renvoie aide pour les explications des variable du joueur dans le lobby
	public static void help_understand_lobby() {
		System.out.println("GUIDE_VAR_LOBBY_JOUEURS:");
		System.out.println("  m: numero de partie [valeur numérique allant à MAX: 255]");
		System.out.println("  id: identifiant du joueur [8 caractères]");
		System.out.println("  port: port UDP");
		System.out.println("GUIDE_VAR_LOBBY_SERVEUR:");
		System.out.println("  n: nombre de partie [valeur numérique allant à MAX: 255]");
		System.out.println("  m: numero de partie [valeur numérique allant à MAX: 255]");
		System.out.println("  s: nombre de joueur inscrit [valeur numérique allant à MAX: 255]");
		System.out.println("  id: identifiant du joueur [8 caractères]");
		System.out.println("  h: hauteur du labyrinthe [valeur numérique allant à MAX: 1000]");
		System.out.println("  w: largeur du labyrinthe [valeur numérique allant à MAX: 1000]");
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

	// VEIRIFIE LA COMMANDE PUIS REDIRIGE VERS LA BONNE METHODE (AVANT LE DEBUT DE
	// LA PARTIE)
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
						Command_new_player(lecture, is, os);
						break;
					case "REGIS":
						Command_regis_player(lecture, is, os);
						break;
					case "UNREG":
						Command_unreg_player(lecture, is, os);
						break;
					case "SIZE?":
						Command_size_player(lecture, is, os);
						break;
					case "LIST?":
						Command_list_player(lecture, is, os);
						break;
					case "GAME?":
						try {
							// Envoie msg - message en TCP
							os.write(msg.getBytes());
							System.out.println("J'ai envoye en TCP: " + lecture);
							// Recois msg - message en TCP
							Command_games_number(is);
						} catch (Exception e) {
							System.out.println("ERREUR GAME? (COMMAND_CHECK)");
						}
						break;
					case "START":
						try {
							// Envoie msg - message en TCP
							String start = "START***";
							os.write(start.getBytes());
							System.out.println("J'ai envoye en TCP: " + lecture);
							is_the_game_started = 1;
							return;
						} catch (Exception e) {
							System.out.println("START ERROR (COMMAND_CHECK)");
							System.out.println("COMMAND: " + commande);
							break;
						}
					default:
						System.out.println("COMMANDE NON RECONNUE! (COMMAND_CHECK)");
						System.out.println("COMMAND: " + commande);
						System.out.println("LECTURE: " + lecture);
						System.out.println("Entrer INFOS ou VARSE pour voir les commandes detailles");
					}
					lecture = "";
				}
				pos_lecture++;
			}

			/*
			 * // ENVOIE COMME MEME POUR TESTER ROBUSTESSE DU SERVEUR if
			 * (!lecture.equals("")) { try {
			 * System.out.println("ATTENTION MSG NON CONFORME ENVOYE");
			 * System.out.println("MSG ENVOYE: " + lecture); os.write(lecture.getBytes());
			 * // Recois msg - message en TCP String msg_erreur = ""; String receptacle =
			 * null; int read; byte[] msg_byte = new byte[8]; if ((read = is.read(msg_byte))
			 * != -1) { receptacle = new String(msg_byte, 0, read); } for (int j = 0; j <
			 * msg_byte.length; j++) { byte b = (byte) msg_byte[j]; msg_erreur += (char) b;
			 * } System.out.println("MSG RECU: " + msg_erreur); } catch (Exception e) {
			 * System.out.println("ERREUR DEFAULT SWITCH"); e.printStackTrace(); } } // FIN
			 * TEST ROBUSTESSE DU SERVEUR
			 */

			System.out.println("Derniere lecture: " + lecture);
		}

	}

	// VEIRIFIE LA COMMANDE PUIS REDIRIGE VERS LA BONNE METHODE (PENDANT LA PARTIE)
	public static void Command_Check_in_game(String msg, InputStream is, OutputStream os) {
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
						help_commands_in_game();
						break;
					case "VARSE":
						help_understand_in_game();
						break;
					case "UPMOV":
						check_move_in_game(lecture, is, os);
						break;
					case "DOMOV":
						check_move_in_game(lecture, is, os);
						break;
					case "LEMOV":
						check_move_in_game(lecture, is, os);
						break;
					case "RIMOV":
						check_move_in_game(lecture, is, os);
						break;
					case "IQUIT":
						quit_in_game(lecture, is, os);
						is_the_game_ended = 1;
						return;
					case "GLIS?":
						glist_in_game(lecture, is, os);
						break;
					case "MALL?":
						msg_all_in_game(lecture, is, os);
						break;
					case "SEND?":
						msg_send_in_game(lecture, is, os);
						break;
					default:
						System.out.println("COMMANDE NON RECONNUE! (COMMAND_CHECK_IN_GAME)");
						System.out.println("COMMAND: " + commande);
						System.out.println("LECTURE: " + lecture);
						System.out.println("Entrer INFOS ou VARSE pour voir les commandes detailles");
					}
					lecture = "";

				}
				pos_lecture++;
			}

			/*
			 * // ENVOIE COMME MEME POUR TESTER ROBUSTESSE DU SERVEUR if
			 * (!lecture.equals("")) { try {
			 * System.out.println("ATTENTION MSG NON CONFORME ENVOYE");
			 * System.out.println("MSG ENVOYE: " + lecture); os.write(lecture.getBytes());
			 * // Recois msg - message en TCP String msg_erreur = ""; String receptacle =
			 * null; int read; byte[] msg_byte = new byte[8]; if ((read = is.read(msg_byte))
			 * != -1) { receptacle = new String(msg_byte, 0, read); } for (int j = 0; j <
			 * msg_byte.length; j++) { byte b = (byte) msg_byte[j]; msg_erreur += (char) b;
			 * } System.out.println("MSG RECU: " + msg_erreur); } catch (Exception e) {
			 * System.out.println("ERREUR DEFAULT SWITCH"); e.printStackTrace(); } } // FIN
			 * TEST ROBUSTESSE DU SERVEUR
			 */

			System.out.println("Derniere lecture_in_game: " + lecture);
		}

	}

	// Pour verifier la commande 5 premiers chars (commande)
	public static String Command_Read(String msg) {
		String res = "";
		for (int i = 0; i < 5; i++) {
			res += msg.charAt(i);
		}
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

			// Recois msg - message en TCP [SIZE!␣m␣h␣w***]
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
					byte[] dbytes = new byte[2];
					ByteBuffer buffer;
					int little_endian;
					// Attention [SIZE!␣m␣h␣w***] h et w sur 2 bytes et m sur 1 byte
					// et code en little endian (protocol)
					switch (i) {
					case 1:
						msg += String.valueOf((int) b);
						break;
					case 3:
						dbytes[0] = (byte) msg_byte_OK[i];
						dbytes[1] = (byte) msg_byte_OK[i + 1];
						buffer = ByteBuffer.wrap(dbytes);
						buffer.order(ByteOrder.BIG_ENDIAN);
						little_endian = buffer.getShort();
						msg += String.valueOf(little_endian);
						i++;
						break;
					case 6:
						dbytes[0] = (byte) msg_byte_OK[i];
						dbytes[1] = (byte) msg_byte_OK[i + 1];
						buffer = ByteBuffer.wrap(dbytes);
						buffer.order(ByteOrder.BIG_ENDIAN);
						little_endian = buffer.getShort();
						msg += String.valueOf(little_endian);
						i++;
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
		} catch (Exception e) {
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
				port_du_joueur = str.substring(15, 19);
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
						System.out.println(
								"Numero de partie invalide car sous uint8 on ne depasse pas 255 [REGIS␣id␣port␣m***]");
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
				port_du_joueur = str.substring(15, 19);
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

	// COMMANDE_READ: [WELCO␣m␣h␣w␣f␣ip␣port***]
	// taille 5+1+1+1+2+1+2+1+1+1+15+1+4+3= 39bytes
	// taille 1 byte : m, f | taille 2 bytes: h, w
	// taille 4 bytes: port | taille 15 bytes: ip
	public static String Command_welcome(InputStream is) {
		try {
			// Verifie que le message est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("WELCO")) {
				byte[] msg_byte = new byte[34];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					if (i == 1 || i == 9) { // 1 byte (uint8)
						msg += String.valueOf((int) b);
					} else if (i == 3 || i == 6) { // 2 bytes (uint16)
						byte[] dbytes = new byte[2];
						ByteBuffer buffer;
						int little_endian;
						dbytes[0] = (byte) msg_byte[i];
						dbytes[1] = (byte) msg_byte[i + 1];
						buffer = ByteBuffer.wrap(dbytes);
						buffer.order(ByteOrder.BIG_ENDIAN);
						little_endian = buffer.getShort();
						msg += String.valueOf(little_endian);
						i++;
					} else {
						msg += (char) b;
					}
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[WELCO␣m␣h␣w␣f␣ip␣port***]_02");
					System.out.println("MESSAGE RECU: " + msg);
					return "NOTINGAME";
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
				String num_port = msg.substring(msg.length() - 7, msg.length() - 3);
				String ip_udp = msg.substring(msg.length() - 23, msg.length() - 8);
				return num_port + "/" + ip_udp;

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
				return "NOTINGAME";
			} else
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[WELCO␣m␣h␣w␣f␣ip␣port***]_01");
			System.out.println("MESSAGE RECU: " + msg);
			return "NOTINGAME";
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[WELCO␣m␣h␣w␣f␣ip␣port***]");
			// e.printStackTrace();
			return "NOTINGAME";
		}
	}

	// COMMANDE_READ: [POSIT␣id␣x␣y***] 25 bytes
	// id 8 bytes
	// x et y 3 bytes (char)
	public static void Command_posit(InputStream is) {
		try {
			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("POSIT")) {
				byte[] msg_byte = new byte[20];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[POSIT␣id␣x␣y***]_02");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[POSIT␣id␣x␣y***]_01");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[POSIT␣id␣x␣y***]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [UPMOV␣d***],[DOMOV␣d***],[LEMOV␣d***] et [RIMOV␣d***]
	public static void check_move_in_game(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);

			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("MOVE!")) { // [MOVE!␣x␣y***] 16 bytes
				byte[] msg_byte = new byte[11];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MOVE!]");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
			} else if (msg.equals("MOVEF")) { // [MOVEF␣x␣y␣p***] 21 bytes
				byte[] msg_byte = new byte[16];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MOVEF]");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
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
			} else if (msg.equals("GOBYE")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				if (msg.equals("GOBYE***")) {
					is_the_game_ended = 1;
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GOBYE]_[MOVE?]");
					System.out.println("MESSAGE RECU: " + msg);
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MOVE?]");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[MOVE]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [IQUIT***]
	public static void quit_in_game(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);

			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("GOBYE")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[IQUIT]_02");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
				if (msg.equals("GOBYE***")) {
					is_the_game_ended = 1;
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[IQUIT]_03");
					System.out.println("MESSAGE RECU: " + msg);
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
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[IQUIT]_01");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[IQUIT]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [GLIS?***]
	public static void glist_in_game(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);

			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("GLIS!")) {
				byte[] msg_byte = new byte[5];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					if (i == 1) {
						msg += (int) b;
					} else {
						msg += (char) b;
					}
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GLIS?]_02");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
				int nombre_de_joueur = Check_int_value(msg, 6);
				// [GPLYR␣id␣x␣y␣p***] 30 bytes
				if (nombre_de_joueur == 0) {
					System.out.println("Aucun joueurs dans la partie");
				} else {
					System.out.println("Nombre de joueurs: " + nombre_de_joueur);
				}
				for (int j = 0; j < nombre_de_joueur; j++) {
					byte[] msg_byte2 = new byte[30];
					if ((read = is.read(msg_byte2)) != -1) {
						receptacle = new String(msg_byte2, 0, read);
					}
					msg = "";
					for (int i = 0; i < msg_byte2.length; i++) {
						byte b = (byte) msg_byte2[i];
						msg += (char) b;
					}
					if (!Check_valide_message(msg)) {
						System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GPLYR␣id␣x␣y␣p***]");
						System.out.println("MESSAGE RECU: " + msg);
						return;
					}
					System.out.println("J'ai reçu en TCP: " + msg);
				}
			} else if (msg.equals("GOBYE")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				if (msg.equals("GOBYE***")) {
					is_the_game_ended = 1;
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GOBYE]_[GLIS?]");
					System.out.println("MESSAGE RECU: " + msg);
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
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GLIS?]_01");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[GLIS?]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [MALL?␣mess***]
	public static void msg_all_in_game(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);

			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("MALL!")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MALL!***]_00");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
			} else if (msg.equals("GOBYE")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MALL!***]_02");
					System.out.println("MESSAGE RECU: " + msg);
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				if (msg.equals("GOBYE***")) {
					is_the_game_ended = 1;
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MALL!***]_03");
					System.out.println("MESSAGE RECU: " + msg);
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
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MALL!***]_01");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[MALL!***]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [SEND?␣id␣mess***]
	public static void msg_send_in_game(String str, InputStream is, OutputStream os) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
			System.out.println("J'ai envoye en TCP: " + str);

			// Verifie que le message recu est correct
			String msg = "";
			String receptacle = null;
			int read;
			byte[] msg_byte_OK = new byte[5];
			if ((read = is.read(msg_byte_OK)) != -1) {
				receptacle = new String(msg_byte_OK, 0, read);
			}
			for (int i = 0; i < msg_byte_OK.length; i++) {
				byte b = (byte) msg_byte_OK[i];
				msg += (char) b;
			}
			if (msg.equals("SEND!")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[SEND!***]");
					System.out.println("MESSAGE RECU: " + msg);
				}
				// Si tout s'est bien passé
				System.out.println("J'ai reçu en TCP: " + msg);
			} else if (msg.equals("NSEND")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[NSEND!***]");
					System.out.println("MESSAGE RECU: " + msg);
				}
				System.out.println("J'ai reçu en TCP: " + msg);
			} else if (msg.equals("GOBYE")) {
				byte[] msg_byte = new byte[3];
				if ((read = is.read(msg_byte)) != -1) {
					receptacle = new String(msg_byte, 0, read);
				}
				for (int i = 0; i < msg_byte.length; i++) {
					byte b = (byte) msg_byte[i];
					msg += (char) b;
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[SEND!***]_02");
					System.out.println("MESSAGE RECU: " + msg);
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				if (msg.equals("GOBYE***")) {
					is_the_game_ended = 1;
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[SEND!***]_03");
					System.out.println("MESSAGE RECU: " + msg);
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
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[SEND!***]_01");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[SEND!***]");
			// e.printStackTrace();
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
