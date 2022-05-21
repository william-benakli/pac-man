package src.ghostlab.controler;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import src.ghostlab.modele.Game;
import src.ghostlab.modele.Player;
import src.ghostlab.thread.Client_UDP_GRAPHIQUE;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.panel.PanelInGame;
public class SendReq{

    private final ArrayList<Integer> list_id_game = new ArrayList<>();
    private final Socket socket;
    private final InputStream is;
    private final OutputStream os;
    private String port_du_joueur;

    public SendReq(Socket socket) throws IOException{
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Integer> getListId(){return list_id_game;}

    public void connectUdp(Game game, JTextArea zone_multi, JTextArea zone_send){
        String str_socket_UDP = Check_udp_port(game.getPortUdp());
        String adress_ip_udp = (game.getIpUdp());
        
        try { // CREER LES CLIENTS UDP
            MulticastSocket client_udp_jeu = new MulticastSocket(Integer.valueOf(str_socket_UDP));
            MulticastSocket client_udp_joueur = new MulticastSocket(Integer.valueOf(port_du_joueur));
            
            // Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
            Client_UDP_GRAPHIQUE launcher_UDP_jeu = new Client_UDP_GRAPHIQUE(client_udp_jeu, adress_ip_udp, zone_multi);
            Client_UDP_GRAPHIQUE launcher_UDP_joueur = new Client_UDP_GRAPHIQUE(client_udp_joueur, adress_ip_udp,zone_send);

            // CREER LES THREADS UDP
            Thread t_udp_jeu = new Thread(launcher_UDP_jeu);
            Thread t_udp_joueur = new Thread(launcher_UDP_joueur);

            // LANCE LES THREADS UDP
            t_udp_jeu.start();
            t_udp_joueur.start();
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String Check_udp_port(String s) {
		String res = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '/') {
				res += s.charAt(i);
			} else
				break;
		}
		return res;
	}

    public void commandStart(){
        try {
            // Envoie msg - message en TCP
            String start = "START***";
            os.write(start.getBytes());
            Game game = Command_welcome();
            VueClient.setPanel(new PanelInGame(game, this));
            return;
        } catch (Exception e) {
            System.out.println("START ERROR (COMMAND_CHECK)");
        }
    }

	public String Check_udp_ip(String s) {
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

	public boolean Check_valide_message(String s) {
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
	public int Check_int_value(String s, int pos) {
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
	public int Check_nb_game(String s) {
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
	
	public boolean Check_if_integer_zero_nine(byte b) {
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

	public void closeClient(Socket socket){
		try{
			socket.close();
		}catch(IOException e){
			System.out.println("Erreur fermeture du socket");
		}
	}

	// Pour verifier la commande 5 premiers chars (commande)
	public String Command_Read(String msg) {
		String res = "";
		for (int i = 0; i < 5; i++) {
			res += msg.charAt(i);
		}
		return res;
	}

	// COMMANDE_SEND: [LIST?␣m***] m sur un byte
	public void Command_list_player(String str, JTextArea reponse) {
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
			os.write(to_send);
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
						reponse.setText(msg);
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
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[LIST?␣m***]");
				System.out.println("MESSAGE RECU: " + msg);
			}
		} catch (Exception e) {
			System.out.println("ERREUR LIST");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [SIZE?␣m***] m sur un byte
	public void Command_size_player(String str, JTextArea reponse) {
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
			os.write(to_send);
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
				System.out.println(msg);
			}
			reponse.setText(msg);
		} catch (Exception e) {
			System.out.println("ERREUR SIZE");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [UNREG***]
	public void Command_unreg_player(String str, JTextArea reponse) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
			reponse.setText(msg);
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
		} catch (Exception e) {
			System.out.println("ERREUR UNREG");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [NEWPL␣id␣port***]
	public void Command_new_player(String str, JTextArea area) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
			area.setText(msg);
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
				area.setText("Erreur : " + msg);
			}
			area.setText(msg);
		} catch (Exception e) {
			System.out.println("ERREUR NEWPL");
			e.printStackTrace();
		}
	}

	// COMMANDE_SEND: [REGIS␣id␣port␣m***] m en byte
	public void Command_regis_player(String str, JTextArea reponse) {
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
			os.write(to_send);
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
			reponse.setText(msg);
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
				reponse.setText("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[REGIS␣id␣port␣m***]");
				reponse.setText(msg);
			}
			reponse.setText(msg);
		} catch (Exception e) {
			System.out.println("ERREUR REGIS");
			e.printStackTrace();
		}
	}

    public void Command_send_game() {
		try {
            String game = "GAME?***";
            os.write(game.getBytes());
        }catch(IOException e){

        }
    }
	// COMMANDE_READ: [GAMES␣n***]
	public void Command_games_number() {
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
			// retourne le nombre n dans [GAMES␣n***]
			int nombre_de_partie = Check_nb_game(msg);
			// [OGAME␣m␣s***] m indique le numéro de partie et s le nombre de joueur
			if (Check_int_value(msg, 6) == 0) {
				System.out.println("Aucune partie en cours..");
			} else {
				System.out.println("Nombre de parties disponible: " + Check_int_value(msg, 6));
			}
            list_id_game.clear();

			for (int j = 0; j < nombre_de_partie; j++) {
				byte[] msg_byte2 = new byte[12];
				if ((read = is.read(msg_byte2)) != -1) {
					receptacle = new String(msg_byte2, 0, read);
				}
				msg = "";
                boolean b_t = true;
				for (int i = 0; i < msg_byte2.length; i++) {
					byte b = (byte) msg_byte2[i];
					// Printing the content of byte array
					if (i != 6 && i != 8) {
						msg += (char) b;
					} else {
                        if(b_t){
                            list_id_game.add((int) b);
                            b_t = false;
                        }else{
                            b_t = true;
                        }
						msg += String.valueOf((int) b);
					}
				}
				if (!Check_valide_message(msg)) {
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[OGAME␣m␣s***]");
					System.out.println("MESSAGE RECU: " + msg);
					return;
				}
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
	public Game Command_welcome() {
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
					return null;
				}
				// Si tout s'est bien passé
                String args[] = msg.split(" ");
				String port = args[6].replace("***", "");
				String ip = args[5].replaceAll("#", "");
				int m = Integer.valueOf(args[1]);
				int h = Integer.valueOf(args[2]);
				int w = Integer.valueOf(args[3]);
				int f = Integer.valueOf(args[4]);

				Player p = Command_posit();
				//if(p == null)return null;

				return Game.createGame(m, h, w, f, ip, port, p);

			} else if (msg.equals("DUNNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				return null;
			} else
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[WELCO␣m␣h␣w␣f␣ip␣port***]_01");
			System.out.println("MESSAGE RECU: " + msg);
			return null;
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[WELCO␣m␣h␣w␣f␣ip␣port***]");
			// e.printStackTrace();
			return null;
		}
	}

	// COMMANDE_READ: [POSIT␣id␣x␣y***] 25 bytes
	// id 8 bytes
	// x et y 3 bytes (char)
	public Player Command_posit() {
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
					return null;
				}
				// Si tout s'est bien passé

				String identifant = msg.substring(6, msg.length()-1);
                String args[] = msg.split(" ");
            
				int x = Integer.valueOf(args[2]);
				int y = Integer.valueOf(args[3].replace("***", ""));
				return Player.createPlayer(identifant, x, y);
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[POSIT␣id␣x␣y***]_01");
				System.out.println("MESSAGE RECU: " + msg);
				return null;
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[POSIT␣id␣x␣y***]");
			// e.printStackTrace();
			return null;
		}
	}

	// COMMAND_SEND: [UPMOV␣d***],[DOMOV␣d***],[LEMOV␣d***] et [RIMOV␣d***]
	public void check_move_in_game(String str, Game game, int distance, int direction, JTextArea reponse) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
					reponse.setText(msg);
				}
				// Si tout s'est bien passé
                String args[] = msg.split(" ");
                int x = Integer.valueOf(args[1]);
                int y = Integer.valueOf(args[2].replace("***", ""));
                Player p = game.getPlayer();
                if(direction == 3){//right
                    for(int i = 0; i < x-p.getPosX(); i++){
                        if(i < game.getLargeur()){
                            game.setPosition(p.getPosX()+i, p.getPosY(), '1');
                         }
                        }
                    if((p.getPosX()+distance) != x && p.getPosX()+distance < game.getLargeur()){
                        game.setPosition(x+1, y, '#');
                    }
                }else if(direction == 4){//left
                    for(int i = 0; i < p.getPosX()-x; i++){
                        if(i > 0){
                            game.setPosition(p.getPosX()-i, p.getPosY(), '1');
                        }
                    }
                    if((p.getPosX()-distance) != x && p.getPosX()-distance > 0){
                        game.setPosition(x-1, y, '#');
                    }

                }else if(direction == 1){//up
                    for(int i = 0; i < p.getPosY()-y; i++){
                        if(i > 0){
                            game.setPosition(p.getPosX(), p.getPosY()-i, '1');
                        }
                    } 
                    if((p.getPosY()-distance) != y && p.getPosY()-distance > 0){
                        game.setPosition(x, y-1, '#');
                    }     
                } else if(direction == 2){//domov
                    for(int i = 0; i < y-p.getPosY(); i++){
                        if(i < game.getHauteur()){
                            game.setPosition(p.getPosX(), p.getPosY()+i, '1');
                        }
                    }
                    if((p.getPosY()+distance) != y && p.getPosY()+distance < game.getLargeur()){
                        game.setPosition(x, y+1, '#');
                    } 
                }
                game.movePlayer(x, y);
                reponse.setText(msg);
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
					reponse.setText(msg);
				}
				// Si tout s'est bien passé
				reponse.setText("Vous avez attrape un fantome ! +1 bravo");
                game.getPlayer().addPoint();
                String args[] = msg.split(" ");
                int x = Integer.valueOf(args[1]);
                int y = Integer.valueOf(args[2]);
                game.setPosition(x, y,'f');
                game.setPosition(game.getPlayer().getPosX(), game.getPlayer().getPosY(),'1');
                Player p = game.getPlayer();
                if(direction == 3){//right
                    for(int i = 0; i < x-p.getPosX(); i++){
                        if(i < game.getLargeur()){
                            game.setPosition(p.getPosX()+i, p.getPosY(), '1');
                         }
                        }
                }else if(direction == 4){//left
                    for(int i = 0; i < p.getPosX()-x; i++){
                        if(i > 0){
                            game.setPosition(p.getPosX()-i, p.getPosY(), '1');
                        }
                    }
                }else if(direction == 1){//up
                    for(int i = 0; i < p.getPosY()-y; i++){
                        if(i > 0){
                            game.setPosition(p.getPosX(), p.getPosY()-i, '1');
                        }
                    } 
                } else if(direction == 2){//domov
                    for(int i = 0; i < y-p.getPosY(); i++){
                        if(i < game.getHauteur()){
                            game.setPosition(p.getPosX(), p.getPosY()+i, '1');
                        }
                    }
                }
             
                game.getPlayer().setPoX(x);
                game.getPlayer().setPoY(y);
			} else if (msg.equals("DUNNO")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				reponse.setText(msg);
			} else if (msg.equals("GOBYE")) {
				byte[] msg_byte_NO = new byte[3];
				if ((read = is.read(msg_byte_NO)) != -1) {
					receptacle = new String(msg_byte_NO, 0, read);
				}
				for (int i = 0; i < msg_byte_NO.length; i++) {
					byte b = (byte) msg_byte_NO[i];
					msg += (char) b;
				}
				reponse.setText(msg);
				if (msg.equals("GOBYE***")) {
					closeSocket();
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
					System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[GOBYE]_[MOVE?]");
					System.out.println("MESSAGE RECU: " + msg);
					reponse.setText(msg);
				}
			} else {
				System.out.println("MESSAGE RECU NON VALIDE PROTOCOLE_TCP_[MOVE?]");
				System.out.println("MESSAGE RECU: " + msg);
				reponse.setText(msg);
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[MOVE]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [IQUIT***]
	public void quit_in_game(String str) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
				if (msg.equals("GOBYE***")) {
					
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
	public void glist_in_game(String str, JTextArea reponse) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
					reponse.setText("Une erreur s'est produite, ressayez !");
				}
				// Si tout s'est bien passé
				reponse.setText(msg);
				int nombre_de_joueur = Check_int_value(msg, 6);
				// [GPLYR␣id␣x␣y␣p***] 30 bytes
				if (nombre_de_joueur == 0) {
					reponse.setText("Aucun joueurs dans la partie");
				} else {
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
                        reponse.setText("Une erreur s'est produite, ressayez !");
						return;
					}
                    reponse.append("\nNombre de joueurs: " + nombre_de_joueur);
                    reponse.append("\n" + msg);
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
                reponse.setText(msg);
				if (msg.equals("GOBYE***")) {
				    closeSocket();
				} else { // Si ca ne s'est pas bien passé car la reponse est mauvaise
                    reponse.setText("Une erreur s'est produite, ressayez !");
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
                reponse.setText(msg);
			} else {
                reponse.setText("Une erreur s'est produite, ressayez !");
			}
		} catch (Exception e) {
			System.out.println("MESSAGE RECU ERREUR_TCP_[GLIS?]");
			// e.printStackTrace();
		}
	}

	// COMMAND_SEND: [MALL?␣mess***]
	public void msg_all_in_game(String str, JTextArea reponse) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
				reponse.setText(msg);
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
				if (msg.equals("GOBYE***")) {
					    closeSocket();
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
	public void msg_send_in_game(String str, JTextArea reponse) {
		try {
			// Envoie msg - message en TCP
			os.write(str.getBytes());
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
				reponse.setText(msg);
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
				if (msg.equals("GOBYE***")) {
                    closeSocket();
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
	public boolean hostAvailabilityCheck(String adress, int port) {
		try (Socket s = new Socket(adress, port)) {
			return true;
		} catch (IOException ex) {
			System.out.println("Main_Client_TCP");
		}
		return false;
	}

}