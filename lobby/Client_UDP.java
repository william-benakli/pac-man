import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class Client_UDP implements Runnable {
	MulticastSocket mso;
	String ip_adress_mso;
	Socket socket;
	

	public Client_UDP(MulticastSocket multsock, Socket sock, String ip) {
		this.mso = multsock;
		this.socket = sock;
		this.ip_adress_mso = ip;
	}

	public void run() {
		// PARAMETRES POUR CLIENT TCP
		PrintWriter pw;

		// PARAMETRES POUR CLIENT UDP Multicast
		DatagramPacket paquet;
		Scanner sc = new Scanner(System.in);
		String msg;

		try {
			// Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
			// "225.1.2.4" IP_TEST
			mso.joinGroup(InetAddress.getByName(ip_adress_mso));
			byte[] data = new byte[1024];
			paquet = new DatagramPacket(data, data.length);

			while (true) {
				// PARAMETRAGE POUR CLIENT TCP
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

				// Affiche msg du serveur - message en UDP Multicast
				mso.receive(paquet);
				msg = new String(paquet.getData(), 0, paquet.getLength());
				System.out.println("J'ai reçu en UDP-Multicast: " + msg);

				// Envoie msg - message en TCP
				System.out.print("Votre msg TCP: ");
				msg = sc.nextLine();
				pw.print(msg);
				pw.flush();
				System.out.println("J'ai envoye en TCP: " + msg);
			}
			// TODO: GERER LE SERVEUR RAGEQUIT?
			// mso.leaveGroup(InetAddress.getByName("225.1.2.4"));
			// mso.close();
			// return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
}
