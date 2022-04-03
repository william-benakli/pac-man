import java.net.*;
import java.io.*;

// UDP CLIENT JAVA

public class Client_UDP {
	public static void main(String[] args) {
		try {
			int socket1 = Integer.valueOf(args[0]);
			int socket2 = Integer.valueOf(args[1]);
			DatagramSocket dso_reception = new DatagramSocket(socket1);
			while (true) {
				byte data[] = new byte[1024];
				// Reception
				DatagramPacket paquet_reception = new DatagramPacket(data, data.length);
				dso_reception.receive(paquet_reception);
				String st = new String(paquet_reception.getData(), 0, paquet_reception.getLength());
				System.out.print("RECEPTION: " + st);
				// Envoie
				DatagramSocket dso_envoie = new DatagramSocket();
				InetSocketAddress ia = new InetSocketAddress("127.0.0.1", socket2);
				DatagramPacket packet_envoie = new DatagramPacket(data, data.length, ia);
				dso_envoie.send(packet_envoie);
				String est = new String(packet_envoie.getData(), 0, packet_envoie.getLength());
				System.out.print("ENVOIE: " + est);
				System.out.println("STATUS: SUCCESS");
			}
		} catch (Exception e) {
			System.out.println("STATUS: FAILED");
			e.printStackTrace();
		}
	}
}
