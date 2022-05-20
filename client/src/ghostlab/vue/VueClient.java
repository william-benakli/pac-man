package src.ghostlab.vue;

import src.ghostlab.vue.graphics.FontGraphiqueBuilder;
import src.ghostlab.vue.panel.PanelLobby;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class VueClient extends JFrame {

    public static JPanel panel_courant = new JPanel();

    public static Font font;
    public static Socket socket;
    public static InputStream is;
    public static OutputStream os;

    public VueClient(Socket socket) throws Exception {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();

        this.setTitle("Projet Reseau - CatchGhost");
        this.setSize(1280, 720);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        final FontGraphiqueBuilder builderFont = new FontGraphiqueBuilder(10f);
        font = builderFont.getFont();
        this.setIconImage(new ImageIcon("ressources/icon/logo.png").getImage());
        VueClient.setPanel(new PanelLobby());
        this.setContentPane(panel_courant);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
    }

    public static void setPanel(JPanel panel){
        panel_courant.removeAll();
        panel_courant.add(panel, BorderLayout.CENTER);
        panel_courant.updateUI();
    }

   
}
