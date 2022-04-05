import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
 * CLIENT:
 * RECOIS: TCP / UDP Multicast
 * ENVOIE: TCP
 */

public class Client {
	static int port;
	static String msg;

	// PARAMETRES POUR CLIENT TCP
	static Socket socket;
	static BufferedReader br;
	static PrintWriter pw;
	// FIN PARAMETRE POUR CLIENT TCP

	// PARAMETRES POUR CLIENT UDP Multicast
	static MulticastSocket mso;
	static DatagramPacket paquet;
	// FIN PARAMETRE POUR CLIENT UDP Multicast

	public static void main(String[] args) {
		try {
			// NUM de PORT ARG[0]
			port = Integer.valueOf(args[0]);

			// PARAMETRAGE POUR CLIENT TCP
			socket = new Socket("localhost", port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			// FIN PARAMETRAGE POUR CLIENT TCP

			// PARAMETRAGE POUR CLIENT UDP Multicast
			mso = new MulticastSocket(9999);
			// Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
			mso.joinGroup(InetAddress.getByName("225.1.2.4"));
			byte[] data = new byte[100];
			paquet = new DatagramPacket(data, data.length);
			// FIN PARAMETRAGE POUR CLIENT UDP Multicast

			while (true) { // CLIENT RECOIS TCP ou UDP Multicast ENVOIE TCP UNIQUEMENT
				try {
					socket.setSoTimeout(2000);
					// Affiche msg du serveur - message en TCP
					msg = br.readLine();
				} catch (SocketTimeoutException e) {
					// Si le serveur est mort on quitte
					if (!hostAvailabilityCheck()) {
						System.out.println("Le serveur est deconnecte.");
						break;
					}
					// Affiche msg du serveur - message en UDP Multicast
					mso.receive(paquet);
					msg = new String(paquet.getData(), 0, paquet.getLength());
					System.out.println("J'ai reçu en UDP-Multicast: " + msg + "\n");
					// FIN - affiche message du serveur en UDP Multicast
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("ERREUR TRYCATCH MAIN!");
				}
				// Si le serveur est mort on quitte
				if (!hostAvailabilityCheck()) {
					System.out.println("Le serveur est deconnecte.");
					break;
				}
				System.out.println("J'ai reçu en TCP: " + msg);
				// FIN - affiche message du serveur en TCP

				// Envoie un string - message en TCP
				Scanner sc = new Scanner(System.in);
				System.out.print("Votre msg TCP: ");
				msg = sc.nextLine();
				pw.println(msg);
				pw.flush();
				// FIN - envoie message au serveur en TCP

				// Affiche le message envoye en TCP
				System.out.println("J'ai envoye en TCP: " + msg);
				msg = null;
			}

			// Affiche le dernier message recu en TCP et ferme la connexion
			if (msg != null) {
				System.out.println("Dernier msg recu par client: " + br.readLine());
			}
			pw.close();
			br.close();
			socket.close();
			mso.leaveGroup(InetAddress.getByName("225.1.2.4"));
			mso.close();
			// FIN - connexion TCP et UDP Multicast fermée

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur Client try_catch.");
			return;
		}
	}

	public static boolean hostAvailabilityCheck() {
		try (Socket s = new Socket("localhost", port)) {
			return true;
		} catch (IOException ex) {
			/* ignore */
		}
		return false;
	}

}
