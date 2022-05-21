package src.ghostlab.vue.panel;

import src.ghostlab.controler.SendReq;
import src.ghostlab.modele.Game;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
public class PanelWaiting extends JPanelGraphiqueBuilder {

    private JPanel game_panel;
    private JLabel waiting, waiting_title;
    //system de retour serveur
    private SendReq controller;

    public PanelWaiting(SendReq controller){
        super("ressources/panel/background.jpg");
        this.controller = controller;
        this.game_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/waiting_panel.png");
        this.waiting = CreateGraphicsUtils.createLabelWithFont("La partie va bientot commencer", Color.RED);
        this.waiting_title = CreateGraphicsUtils.createLabelWithFont("En attente de joueurs...", Color.RED);

        GroupLayout mainLayout = new GroupLayout(this);
        setLayout(mainLayout);
        setPreferredSize(new Dimension(1280,720));

        /*X*/
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup().addGap(465).addComponent(waiting))
                        .addGroup(mainLayout.createSequentialGroup().addGap(465).addComponent(waiting_title))
                        .addGroup(mainLayout.createSequentialGroup().addGap(440).addComponent(game_panel, 400,400,400))
        );
        /* Y */
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainLayout.createSequentialGroup().addGap(300).addComponent(waiting))
                .addGroup(mainLayout.createSequentialGroup().addGap(250).addComponent(waiting_title))
                        .addGroup(mainLayout.createSequentialGroup().addGap(200).addComponent(game_panel, 150,150,150 ))
        );
        repaint();
        revalidate();
    }
}
