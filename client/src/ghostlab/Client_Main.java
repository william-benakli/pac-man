package src.ghostlab;

import src.ghostlab.thread.Client_TCP;
import src.ghostlab.thread.Client_TCP_GRAPHIQUE;

import java.net.*;
import java.util.Scanner;

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
			if(args.length  != 2){
				System.out.println("Erreur : Arguments manquants, essayez 'java Client localhost 4545'");
			}
			String adress_ip_tcp = args[0];
			int port_tcp = Integer.valueOf(args[1]);
			// SOCKETS DES CLIENTS
			Socket client_tcp = new Socket(adress_ip_tcp, port_tcp);

			// MulticastSocket client_udp = new MulticastSocket(port_udp);
			Scanner sc = new Scanner(System.in);

			System.out.println("Selectionnez une option : ");
			System.out.println("1 | Vue graphique ");
			System.out.println("2 | Vue terminal ");
			try {
				int value = sc.nextInt();

				if(value == 1){
					Client_TCP_GRAPHIQUE launcher_TCP =  new Client_TCP_GRAPHIQUE(client_tcp);
					Thread t_tcp = new Thread(launcher_TCP);
					t_tcp.start();
				}else{
					Client_TCP launcher_TCP = new Client_TCP(client_tcp);
					Thread t_tcp = new Thread(launcher_TCP);
					t_tcp.start();
				}

				// Client_UDP launcher_UDP = new Client_UDP(client_udp, client_tcp, adress_ip_udp);
				// t_udp.start();
			} catch (Exception e) {
				System.out.println("Aucune connexion etablie... ressayez !");
			}

			// TODO: FERME LES CLIENTS?
			//client_tcp.close();
			//client_udp.close();
			return;

		} catch (Exception e) {
			System.out.println("[Erreur] Argument non valide : java Client_Main [ip] [port]");
			return;
		}
	}
}
