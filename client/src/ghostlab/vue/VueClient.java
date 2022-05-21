package src.ghostlab.vue;

import src.ghostlab.controler.SendReq;
import src.ghostlab.vue.graphics.FontGraphiqueBuilder;
import src.ghostlab.vue.panel.PanelLobby;

import javax.swing.*;
import java.awt.*;

public class VueClient extends JFrame {

    public static JPanel panel_courant = new JPanel();

    public static Font font;
    private SendReq sendReq;

    public VueClient(SendReq sendReq) throws Exception {
        this.sendReq = sendReq;

        this.setTitle("Projet Reseau - CatchGhost");
        this.setSize(1280, 720);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        final FontGraphiqueBuilder builderFont = new FontGraphiqueBuilder(10f);
        font = builderFont.getFont();
        this.setIconImage(new ImageIcon("ressources/icon/logo.png").getImage());
        VueClient.setPanel(new PanelLobby(sendReq));
        this.setContentPane(panel_courant);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
    }

    public static void setPanel(JPanel panel){
        panel_courant.removeAll();
        panel_courant.add(panel, BorderLayout.CENTER);
        panel_courant.updateUI();
    }

    public SendReq getController(){
        return sendReq;
    }

}
