package src.ghostlab.vue.panel;

import src.ghostlab.thread.Client_TCP_GRAPHIQUE;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.graphics.JButtonGraphiqueBuilder;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class PanelLobby extends JPanelGraphiqueBuilder {

    private JPanel game_panel; //image de photoshop
    private JPanel games_selection;
    private JButtonGraphiqueBuilder add_game, refresh_game;
    //system de retour serveur

    public PanelLobby(){
        super("ressources/panel/background.jpg");
        this.add_game = CreateGraphicsUtils.createJButtonImage("ressources/button/boutton_add.png");
        this.refresh_game = CreateGraphicsUtils.createJButtonImage("ressources/button/boutton_refresh.png");
        this.game_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/menu_game.png");
        this.game_panel.setPreferredSize(new Dimension(1280, 720));
        this.games_selection = new JPanel();

        GroupLayout mainLayout = new GroupLayout(this);
        setLayout(mainLayout);
        setPreferredSize(new Dimension(1280,720));

        games_selection.setBackground(new Color(9,9,9));
        games_selection.setLayout(new GridLayout(3,3));

        affichePartie();
        /*X*/
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup().addGap(100).addComponent(games_selection, 1050, 1050, 1050))
                        .addGroup(mainLayout.createSequentialGroup().addGap(20).addComponent(game_panel, 1200, 1200, 1200))
                        .addGroup(mainLayout.createSequentialGroup().addGap(1120).addComponent(refresh_game, 100, 100, 100))
                        .addGroup(mainLayout.createSequentialGroup().addGap(1010).addComponent(add_game, 100, 100, 100))
        );
        /* Y */
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup().addGap(85).addComponent(games_selection, 400, 400, 400))
                        .addGroup(mainLayout.createSequentialGroup().addGap(20).addComponent(game_panel, 530, 530, 530))
                        .addGroup(mainLayout.createSequentialGroup().addGap(565).addComponent(refresh_game, 50, 50, 50))
                        .addGroup(mainLayout.createSequentialGroup().addGap(565).addComponent(add_game, 50, 50, 50))
        );

        actionListerners();
        updateUI();
    }

    public void affichePartie(){
        games_selection.removeAll();

        ArrayList<Integer> list = Client_TCP_GRAPHIQUE.list_id_game;
        list.toString();
        if(list.size() > 1){
            list.forEach(e->{
                JPanel game_courant = new GameComposent(e);
                game_courant.setPreferredSize(new Dimension(20, 20));
                game_courant.setMaximumSize(new Dimension(20, 20));
                game_courant.setSize(new Dimension(20, 20));
                games_selection.add(game_courant);
            });
        }else{
            games_selection.add(CreateGraphicsUtils.createLabelWithFont("Aucune partie existante, essayez plus tard", Color.RED));
        }
 
        
    }
    public void actionListerners(){
        refresh_game.addActionListener(event->{
            Client_TCP_GRAPHIQUE.Command_Check("GAME?***", VueClient.is, VueClient.os, null);
            affichePartie();
        });

        add_game.addActionListener(event->{
            VueClient.setPanel(new PanelCreateGame());
        });
    }


}
