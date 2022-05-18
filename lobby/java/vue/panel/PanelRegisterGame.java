package vue.panel;

import modele.Game;
import vue.CreateGraphicsUtils;
import vue.VueClient;
import vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;

public class PanelRegisterGame extends JPanelGraphiqueBuilder {

    private Game game;
    private JPanel panel_register;
    private JLabel name_game, identifiantlabel, portlabel, reponse_commande, information, game_selectlabel;
    private JButton back, unreg,regis_game, size_game, list_game;
    private JTextField identifiant, port;
    private JTextArea reponse_list_size;

    public PanelRegisterGame(Game game){
        super("ressources/panel/background.jpg");
        this.game = game;
        this.name_game = CreateGraphicsUtils.createLabelWithFont("Game n-" + game.getIdGame(), Color.ORANGE);
        this.back = CreateGraphicsUtils.createJButtonImage("ressources/button/retour_button.png");
        this.unreg = CreateGraphicsUtils.createJButtonImage("ressources/button/unreg_button.png");
        this.panel_register = CreateGraphicsUtils.createPanelImage("ressources/panel/register_panel.png");
        this.regis_game = CreateGraphicsUtils.createJButtonImage("ressources/button/regis_button.png");
        this.list_game = CreateGraphicsUtils.createJButtonImage("ressources/button/list_button.png");
        this.size_game = CreateGraphicsUtils.createJButtonImage("ressources/button/size_button.png");
        this.identifiant = CreateGraphicsUtils.createJtextField();
        this.port = CreateGraphicsUtils.createJtextField();
        this.information = CreateGraphicsUtils.createLabelWithFont("Veuillez entrer des valeurs valides", Color.ORANGE);
        this.identifiantlabel = CreateGraphicsUtils.createLabelWithFont("Identifiant", Color.ORANGE);
        this.portlabel = CreateGraphicsUtils.createLabelWithFont("port", Color.ORANGE);
        this.reponse_list_size = CreateGraphicsUtils.createTextArea(70, 70);
        this.reponse_list_size.append("Aucun message pour le moment...");
        this.reponse_commande = CreateGraphicsUtils.createLabelWithFont("Reponse du serveur", Color.ORANGE);
        this.game_selectlabel = CreateGraphicsUtils.createLabelWithFont("Vous avez selectionne la partie Game n-"+game.getIdGame(), Color.WHITE);

        GroupLayout LayoutPrincpal = new GroupLayout(this);

        setLayout(LayoutPrincpal);
        setPreferredSize(new Dimension(1280,720));

        /*X*/
        LayoutPrincpal.setHorizontalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(0).addComponent(back))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(450).addComponent(game_selectlabel))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(800).addComponent(reponse_commande))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(800).addComponent(reponse_list_size, 350,350,350))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(1010).addComponent(list_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(850).addComponent(size_game))

                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(information))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(identifiantlabel))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(portlabel))

                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(180).addComponent(information))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(identifiant, 200,200,200))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(port, 200,200,200))

                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(480).addComponent(regis_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(220).addComponent(unreg))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(70).addComponent(panel_register))
                        );
        /* Y */
        LayoutPrincpal.setVerticalGroup(
                LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(0).addComponent(back))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(150).addComponent(game_selectlabel))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(180).addComponent(reponse_commande))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(200).addComponent(reponse_list_size, 300,300,300))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(list_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(size_game))

                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(200).addComponent(information))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(280).addComponent(identifiantlabel))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(380).addComponent(portlabel))

                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(250).addComponent(identifiant, 50,50,50))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(400).addComponent(port, 50,50,50))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(560).addComponent(regis_game))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(unreg))
                        .addGroup(LayoutPrincpal.createSequentialGroup().addGap(70).addComponent(panel_register))
        );
        actionListerner();
    }

    public void actionListerner(){
        this.back.addActionListener(event->{
            VueClient.setPanel(new PanelLobby());
        });

    }

}
