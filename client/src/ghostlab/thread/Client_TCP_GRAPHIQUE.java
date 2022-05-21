package src.ghostlab.thread;

import src.ghostlab.vue.VueClient;


import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import src.ghostlab.controler.SendReq;

public class Client_TCP_GRAPHIQUE implements Runnable {

	private Socket socket;
	private VueClient client_vue;
	private SendReq controller;

	public Client_TCP_GRAPHIQUE(Socket sock) throws IOException {
		this.socket = sock;
		this.controller = new SendReq(socket);
	}

	public void run() {
		try {
			// PARAMETRAGE POUR CLIENT TCP
			controller.Command_games_number();
			EventQueue.invokeLater(	new Runnable() {
				public void run() {
					try {
						client_vue = new VueClient(controller);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			if (!controller.hostAvailabilityCheck(socket.getInetAddress().toString(), socket.getPort())) {
				System.out.println("[TCP] Le serveur est deconnecte.");
			}
			e.printStackTrace();
			return;
		}
	}
}

