package vue.panel;

import modele.Game;
import vue.CreateGraphicsUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class GameComposent extends JPanel {

    private Game game_select;
    private JLabel name_game;
    private JLabel information_button;
    private JButton size_game, list_game;


    public GameComposent(Game game_select){
        this.game_select = game_select;
        this.name_game = new JLabel(" Game numero : " + game_select.getIdGame());
        this.information_button = new JLabel("Aucune information pour le moment");
        this.size_game = CreateGraphicsUtils.createJButton("size");
        this.list_game = CreateGraphicsUtils.createJButton("list");
        this.setBackground(new Color(250,250,250,90));

        GroupLayout LayoutPrincpal = new GroupLayout(this);

        setLayout(LayoutPrincpal);
        setPreferredSize(new Dimension(1050,60));

        /*X*/
        LayoutPrincpal.setHorizontalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(5).addComponent(name_game, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(15).addComponent(information_button))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(775).addComponent(size_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(850).addComponent(list_game))
        );
        /* Y */
        LayoutPrincpal.setVerticalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(5).addComponent(name_game, 100, 100, 100))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(15).addComponent(information_button))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(10).addComponent(size_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(10).addComponent(list_game))
        );
    }
}
