package vue.panel;

import vue.CreateGraphicsUtils;
import vue.VueClient;

import javax.swing.*;
import java.awt.*;

public class PanelConnection extends JPanel {

    private JButton jouer;
    private JTextField ip_serveur, port_serveur;
    private Image logo;

    public PanelConnection(){
        this.jouer = CreateGraphicsUtils.createJButton("Jouer");
        this.ip_serveur = CreateGraphicsUtils.createJtextField();
        this.port_serveur = CreateGraphicsUtils.createJtextField();

        GroupLayout LayoutPrincpal = new GroupLayout(this);

        setLayout(LayoutPrincpal);
        setPreferredSize(new Dimension(1280,720));

        /*X*/
        LayoutPrincpal.setHorizontalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(540).addComponent(ip_serveur, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(540).addComponent(port_serveur, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(560).addComponent(jouer))
        );
        /* Y */
        LayoutPrincpal.setVerticalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(ip_serveur, 50, 50, 50))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(360).addComponent(port_serveur, 50, 50, 50))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(420).addComponent(jouer))
        );
        jouer.addActionListener(event-> {
            VueClient.setPanel(new PanelLobby());
        });
    }

}
