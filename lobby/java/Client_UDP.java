import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Scanner;

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

		try {
			// Adresses de classe D comprises entre 224.0.0.0 à 239.255.255.255
			// "225.1.2.4" IP_TEST
			mso.joinGroup(InetAddress.getByName(ip_adress_mso));
			byte[] data = new byte[1024];
			paquet = new DatagramPacket(data, data.length);

			while (true) {
				// Affiche msg du serveur - message en UDP Multicast
				mso.receive(paquet);
				msg = new String(paquet.getData(), 0, paquet.getLength());
				System.out.println("J'ai reçu en UDP-Multicast: " + msg);
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
