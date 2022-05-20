package src.ghostlab.vue.panel;

import src.ghostlab.modele.Game;
import src.ghostlab.thread.Client_TCP_GRAPHIQUE;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.graphics.JButtonGraphiqueBuilder;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class PanelWaiting extends JPanelGraphiqueBuilder {

    private JPanel game_panel;
    private JLabel waiting;
    //system de retour serveur

    public PanelWaiting(){
        super("ressources/panel/background.jpg");
        this.game_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/waiting_panel.png");
        this.waiting = CreateGraphicsUtils.createLabelWithFont("La partie va bientot commencer veuillez patientez...");
        
        GroupLayout mainLayout = new GroupLayout(this);
        setLayout(mainLayout);
        setPreferredSize(new Dimension(1280,720));

        game_panel.setLayout(new BorderLayout());
        game_panel.add(waiting, BorderLayout.CENTER);

        /*X*/
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup().addGap(200).addComponent(game_panel))
        );
        /* Y */
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup().addGap(100).addComponent(game_panel))
        );

        Game reponse = Client_TCP_GRAPHIQUE.Command_welcome(VueClient.is);
        if(reponse != null){
            VueClient.setPanel(new PanelInGame(reponse));
        }else{
            //gerer l'erreur
        }
        
    }


}
