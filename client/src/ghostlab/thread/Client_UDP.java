package src.ghostlab.thread;

import java.net.*;
public class Client_UDP implements Runnable {
	MulticastSocket mso;
	String ip_adress_mso;

	public Client_UDP(MulticastSocket multsock, String ip) {
		this.mso = multsock;
		this.ip_adress_mso = ip;
	}

	public void run() {
		// PARAMETRES POUR CLIENT UDP Multicast
		DatagramPacket paquet;
		String msg;
		String msg_recu;

		try {
			// Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
			// "225.1.2.4" IP_TEST
			mso.joinGroup(InetAddress.getByName(ip_adress_mso));
			byte[] data = new byte[1024];
			paquet = new DatagramPacket(data, data.length);

			msg_recu = "";

			while (true) {
				// Affiche msg du serveur - message en UDP Multicast
				mso.receive(paquet);
				msg = new String(paquet.getData(), 0, paquet.getLength());

				for (int i = 0; i < msg.length(); i++) {
					msg_recu += msg.charAt(i);
					if (msg_est_complet(msg_recu)) {
						System.out.println("J'ai reçu en UDP-Multicast: " + msg_recu);
						msg_recu = msg_recu.substring(i+1, msg_recu.length());
					}
				}

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

	public static boolean msg_est_complet(String s) {
		if (s.length() < 3)
			return false;
		if (s.charAt(s.length() - 1) != '+') {
			return false;
		}
		if (s.charAt(s.length() - 2) != '+') {
			return false;
		}
		if (s.charAt(s.length() - 3) != '+') {
			return false;
		}
		return true;
	}
}
