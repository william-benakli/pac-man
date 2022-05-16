package vue.panel;

import modele.Game;
import vue.CreateGraphicsUtils;
import vue.graphics.JButtonGraphiqueBuilder;
import vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;

public class PanelLobby extends JPanel {

    private JPanel game_panel; //image de photoshop
    private JButtonGraphiqueBuilder add_game, refresh_game;
    //system de retour serveur

    public PanelLobby(){
        this.add_game = CreateGraphicsUtils.createJButtonImage("ressources/boutton_add.png");
        this.refresh_game = CreateGraphicsUtils.createJButtonImage("ressources/boutton_refresh.png");
        this.game_panel = new JPanelGraphiqueBuilder("ressources/menu_game.png");
        this.game_panel.setPreferredSize(new Dimension(1280, 720));

        GroupLayout LayoutPrincpal = new GroupLayout(this);

        setLayout(LayoutPrincpal);
        setPreferredSize(new Dimension(1280,720));

        for(int i = 0; i < 4; i++){
            game_panel.add(new GameComposent(Game.createGame(0,0,0,0, "12.151.02151.0212")));
        }

        /*X*/
        LayoutPrincpal.setHorizontalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(20).addComponent(game_panel, 1200, 1200, 1200))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(1120).addComponent(refresh_game, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(1010).addComponent(add_game, 100, 100, 100))
        );
        /* Y */
        LayoutPrincpal.setVerticalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(20).addComponent(game_panel, 530, 530, 530))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(565).addComponent(refresh_game, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(565).addComponent(add_game, 100, 100, 100))
        );

        updateUI();
    }


}
