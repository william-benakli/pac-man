import java.net.*;
import java.io.*;
import java.util.Scanner;

// CLIENT TCP

public class Client_TCP {
	public static void main(String[] args) {
		try {
			int port = Integer.valueOf(args[0]);
			Socket socket = new Socket("127.0.0.1", port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String msg;
			while (true) {
				// Affiche msg du serveur
				msg = br.readLine();
				// Si le serveur est mort on sort
				if (msg == null) {
					System.out.println("Le serveur est mort.");
					break;
				}
				System.out.println("Msg recu par client TCP: " + msg);
				// Envoie un string
				Scanner sc = new Scanner(System.in);
				System.out.print("Votre msg: ");
				msg = sc.nextLine();
				pw.println(msg);
				pw.flush();
				// Affiche test valeur recu
				System.out.println("Msg envoye par client TCP: " + msg);
			}
			System.out.println("Dernier msg recu par client: " + br.readLine());
			pw.close();
			br.close();
			socket.close();
		} catch (Exception e) {
			return;
		}
	}
}
