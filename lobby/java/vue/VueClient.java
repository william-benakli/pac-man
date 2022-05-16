package vue;

import vue.panel.PanelConnection;

import javax.swing.*;
import java.awt.*;

public class VueClient extends JFrame {

    public static JPanel panel_courant = new JPanel();

    public VueClient() throws Exception {
        this.setTitle("Projet Reseau - CatchGhost");
        this.setSize(1280, 720);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
      //  this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.jpg")).getImage());
        VueClient.setPanel(new PanelConnection());
        this.setContentPane(panel_courant);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
    }

    public static void setPanel(JPanel panel){
        panel_courant.removeAll();
        panel_courant.add(panel, BorderLayout.CENTER);
        panel_courant.updateUI();
    }

    public static void main(String []args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VueClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
