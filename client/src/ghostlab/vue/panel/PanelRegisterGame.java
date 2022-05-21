package src.ghostlab.vue.panel;

import src.ghostlab.controler.SendReq;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;
import src.ghostlab.vue.panel.*;

import javax.swing.*;
import java.awt.*;

public class PanelRegisterGame extends JPanelGraphiqueBuilder {

	private int game_id;
	private JPanel panel_register;
	private JLabel name_game, identifiantlabel, portlabel, reponse_commande, information, game_selectlabel;
	private JButton start_game, back, unreg, regis_game, size_game, list_game;
	private JTextField identifiant, port;
	private JTextArea reponse_list_size;

	private boolean partie_join = false;
	private SendReq controller;
	private JLabel waiting, waiting_title;

	public PanelRegisterGame(int game_id, SendReq controller) {
		super("ressources/panel/background.jpg");
		this.controller = controller;
		this.game_id = game_id;
		this.name_game = CreateGraphicsUtils.createLabelWithFont("Game n-" + game_id, Color.ORANGE);
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
		this.game_selectlabel = CreateGraphicsUtils
				.createLabelWithFont("Vous avez selectionne la partie Game n-" + game_id, Color.WHITE);
		panel_register.add(name_game);
		this.start_game = CreateGraphicsUtils.createJButtonImage("ressources/button/start_button.png");
		this.waiting = CreateGraphicsUtils.createLabelWithFont("La partie va bientot commencer", Color.RED);
		this.waiting_title = CreateGraphicsUtils.createLabelWithFont("En attente de joueurs...", Color.RED);
		this.waiting.setVisible(false);
		this.waiting_title.setVisible(false);

		GroupLayout LayoutPrincpal = new GroupLayout(this);

		setLayout(LayoutPrincpal);
		setPreferredSize(new Dimension(1280, 720));

		/* X */
		LayoutPrincpal.setHorizontalGroup(LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(465).addComponent(waiting))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(465).addComponent(waiting_title))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(0).addComponent(back))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(450).addComponent(game_selectlabel))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(800).addComponent(reponse_commande))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(800).addComponent(reponse_list_size, 350, 350,
						350))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(1010).addComponent(list_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(850).addComponent(size_game))

				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(information))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(identifiantlabel))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(120).addComponent(portlabel))

				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(180).addComponent(information))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(identifiant, 200, 200, 200))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(port, 200, 200, 200))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(590).addComponent(start_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(390).addComponent(regis_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(220).addComponent(unreg))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(70).addComponent(panel_register)));
		/* Y */
		LayoutPrincpal.setVerticalGroup(LayoutPrincpal.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(300).addComponent(waiting))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(250).addComponent(waiting_title))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(0).addComponent(back))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(150).addComponent(game_selectlabel))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(180).addComponent(reponse_commande))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(200).addComponent(reponse_list_size, 300, 300,
						300))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(list_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(size_game))

				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(200).addComponent(information))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(280).addComponent(identifiantlabel))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(380).addComponent(portlabel))

				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(250).addComponent(identifiant, 50, 50, 50))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(400).addComponent(port, 50, 50, 50))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(start_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(560).addComponent(regis_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(unreg))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(70).addComponent(panel_register)));
		actionListerner();
	}

	public void actionListerner() {
		this.start_game.addActionListener(event -> {
			if (partie_join) {
				this.waiting_title.setVisible(true);
				this.waiting.setVisible(true);
				this.updateUI();
				controller.commandStart();
			} else {
				reponse_list_size.setText("Aucune partie existante.");
			}
		});

		this.back.addActionListener(event -> {
			controller.Command_unreg_player("UNREG " + game_id + "***", reponse_list_size);
			VueClient.setPanel(new PanelLobby(controller));
			partie_join = false;
		});

		this.size_game.addActionListener(e -> {
			controller.Command_size_player("SIZE? " + game_id + "***", reponse_list_size);
		});

		this.list_game.addActionListener(e -> {
			controller.Command_list_player("LIST? " + game_id + "***", reponse_list_size);
		});

		this.unreg.addActionListener(e -> {
			if (partie_join) {
				controller.Command_unreg_player("UNREG " + game_id + "***", reponse_list_size);
				if (reponse_list_size.getText().startsWith("UNROK ")) {
					partie_join = false;
					this.regis_game = CreateGraphicsUtils.createJButtonImage("ressources/button/regis_button.png");
					updateUI();
				} else {
					reponse_list_size.setText("Erreur : vous etes toujours dans la partie");
				}
			} else {
				reponse_list_size.setText("Vous n'etes pas inscrit.");
			}
		});

		this.regis_game.addActionListener(e -> {
			if (identifiant.getText().length() != 8 || port.getText().length() != 4) {
				reponse_list_size.setText("Votre identifiant ou port n'est pas correct.");
			} else {
				controller.Command_regis_player(
						"REGIS " + identifiant.getText() + " " + port.getText() + " " + game_id + "***",
						reponse_list_size);
				if (reponse_list_size.getText().startsWith("REGOK")) {
					partie_join = true;
					this.regis_game = CreateGraphicsUtils.createJButtonImage("ressources/button/start_button.png");
					updateUI();
				}
			}
		});
	}

}
