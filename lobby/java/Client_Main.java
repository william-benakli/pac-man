import java.net.*;
import java.io.*;

/*
 * CLIENT:
 * RECOIS: TCP / UDP Multicast
 * ENVOIE: TCP
 */

public class Client_Main {
// ANCIEN TEST: java Client_Main 10000 11111 localhost 225.1.2.4
// NOUVEAU TEST: java Client_Main 10000 localhost
	public static void main(String[] args) {
		try {
			// NUMEROS DES PORTS et ADRESSE IP
			int port_tcp = Integer.valueOf(args[0]);
			// int port_udp = Integer.valueOf(args[1]);
			String adress_ip_tcp = args[1];
			// String adress_ip_udp = args[3];

			// SOCKETS DES CLIENTS
			Socket client_tcp = new Socket(adress_ip_tcp, port_tcp);
			// MulticastSocket client_udp = new MulticastSocket(port_udp);

			try {

				// CREER LES CLIENTS UDP/TCP
				Client_TCP launcher_TCP = new Client_TCP(client_tcp);
				// Client_UDP launcher_UDP = new Client_UDP(client_udp, client_tcp, adress_ip_udp);

				// CREER LES THREADS
				Thread t_tcp = new Thread(launcher_TCP);
				// Thread t_udp = new Thread(launcher_UDP);

				// LANCER LES THREADS
				t_tcp.start();
				// t_udp.start();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// TODO: FERME LES CLIENTS?
			// client_tcp.close();
			// client_udp.close();
			return;

		} catch (Exception e) {
			System.out.println("java Client_Main [port_TCP] [ip_TCP]");
			return;
		}
	}
}
