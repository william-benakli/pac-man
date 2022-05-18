package vue.panel;

import modele.Game;
import vue.CreateGraphicsUtils;
import vue.VueClient;
import vue.graphics.JButtonGraphiqueBuilder;
import vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;

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

       for(int i = 0; i < 15; i++){
           JPanel game_courant = new GameComposent(Game.createGame(i,8,7,5, "12.151.02151.0212"));
           game_courant.setPreferredSize(new Dimension(20, 20));
           game_courant.setMaximumSize(new Dimension(20, 20));
           game_courant.setSize(new Dimension(20, 20));
           games_selection.add(game_courant);
       }

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

    public void actionListerners(){
        refresh_game.addActionListener(event->{
            //controller.send(Games?);
            VueClient.setPanel(new PanelInGame(Game.createGame(0,8,7,0, "12.151.02151.0212")));
        });

        add_game.addActionListener(event->{
            VueClient.setPanel(new PanelCreateGame());
        });
    }


}
