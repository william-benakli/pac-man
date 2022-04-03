import java.net.*;
import java.io.*;

// SERVEUR TCP

public class Serveur_TCP {
	public static void main(String[] args) {
		try {
			int port = Integer.valueOf(args[0]);
			ServerSocket server = new ServerSocket(port);
			while (true) {
				Socket socket = server.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				String msg;
				// SALUT LE CLIENT
				pw.println("BONJOUR NOUVEAU CLIENT!");
				pw.flush();
				while (true) {
					// LIS LE MESSAGE DU CLIENT
					msg = br.readLine();
					// Verification client quit
					if (msg == null) {
						System.out.println("Un client est mort.");
						break;
					}
					// Affiche test valeur recu
					System.out.println("Message recu par le serveur: " + msg);
					// CONFIRME LA RECEPTION DU MESSAGE CLIENT
					pw.println("BIEN RECU!");
					pw.flush();
				}
				// Fin normal
				br.close();
				pw.close();
				socket.close();
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}