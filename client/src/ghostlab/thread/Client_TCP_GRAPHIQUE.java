package src.ghostlab.thread;

import src.ghostlab.vue.VueClient;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import src.ghostlab.controler.SendReq;
import src.ghostlab.modele.Game;
import src.ghostlab.modele.Player;

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

