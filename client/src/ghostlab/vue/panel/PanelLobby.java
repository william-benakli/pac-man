package src.ghostlab.vue.panel;

import src.ghostlab.controler.SendReq;
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
    private SendReq controller;

    public PanelLobby(SendReq controller){
        super("ressources/panel/background.jpg");
        this.controller = controller;

        this.add_game = CreateGraphicsUtils.createJButtonImage("ressources/button/newpl_button.png");
        this.refresh_game = CreateGraphicsUtils.createJButtonImage("ressources/button/game_button.png");
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
                        .addGroup(mainLayout.createSequentialGroup().addGap(1120).addComponent(refresh_game))
                        .addGroup(mainLayout.createSequentialGroup().addGap(950).addComponent(add_game))
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

        ArrayList<Integer> list = controller.getListId();
        System.out.println(list.toString() + " ----");
        
        if(list.size() > 0){
            list.forEach(e->{
                JPanel game_courant = new GameComposent(e, controller);
                game_courant.setPreferredSize(new Dimension(20, 20));
                game_courant.setMaximumSize(new Dimension(20, 20));
                game_courant.setSize(new Dimension(20, 20));
                games_selection.add(game_courant);
            });
        }else{
            games_selection.add(CreateGraphicsUtils.createLabelWithFont("Aucune partie existante, essayez plus tard", Color.RED));
        }
        games_selection.updateUI();
        updateUI();
    }
    public void actionListerners(){
        refresh_game.addActionListener(event->{
            controller.Command_send_game();
            controller.Command_games_number();
            affichePartie();
        });

        add_game.addActionListener(event->{
            VueClient.setPanel(new PanelCreateGame(controller));
        });
    }


}
