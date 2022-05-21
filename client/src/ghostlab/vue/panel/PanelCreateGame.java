package src.ghostlab.vue.panel;

import src.ghostlab.controler.SendReq;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;
import src.ghostlab.vue.graphics.JButtonGraphiqueBuilder;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;

public class PanelCreateGame extends JPanelGraphiqueBuilder {

	private JPanel panel_register;
	private JLabel identifiantlabel, portlabel, reponse_commande, information, game_selectlabel;
	private JButton back, start_game, unreg, regis_game, size_game, list_game;
	private JTextField identifiant, port;
	private JTextArea reponse_list_size;

	private SendReq controller;

	private int id_game;
	private boolean partie_lance = false;
	private JLabel waiting, waiting_title;

	public PanelCreateGame(SendReq controller) {
		super("ressources/panel/background.jpg");
		this.controller = controller;
		this.back = CreateGraphicsUtils.createJButtonImage("ressources/button/retour_button.png");
		this.unreg = CreateGraphicsUtils.createJButtonImage("ressources/button/unreg_button.png");
		this.panel_register = CreateGraphicsUtils.createPanelImage("ressources/panel/newpl_panel.png");
		this.regis_game = CreateGraphicsUtils.createJButtonImage("ressources/button/newpl_button.png");
		this.start_game = CreateGraphicsUtils.createJButtonImage("ressources/button/start_button.png");

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
		this.game_selectlabel = CreateGraphicsUtils.createLabelWithFont("Creeation d'une nouvelle game", Color.WHITE);
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
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(410).addComponent(regis_game))
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
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(regis_game))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(550).addComponent(unreg))
				.addGroup(LayoutPrincpal.createSequentialGroup().addGap(70).addComponent(panel_register)));
		actionListerner();
	}

	public void actionListerner() {
		this.back.addActionListener(event -> {
			if (partie_lance) {
				controller.Command_unreg_player("UNREG " + id_game + "***", reponse_list_size);
				partie_lance = false;
			}
			VueClient.setPanel(new PanelLobby(controller));
		});
		this.start_game.addActionListener(event -> {
			if (partie_lance) {
				this.waiting_title.setVisible(true);
				this.waiting.setVisible(true);
				this.updateUI();
				controller.commandStart();
			} else {
				reponse_list_size.setText("Aucune partie existante.");
			}
		});
		this.size_game.addActionListener(e -> {
			if (partie_lance) {
				controller.Command_size_player("SIZE? " + id_game + "***", reponse_list_size);
			} else {
				reponse_list_size.setText("Aucune partie existante.");
			}
		});

		this.list_game.addActionListener(e -> {
			if (partie_lance) {
				controller.Command_list_player("LIST? " + id_game + "***", reponse_list_size);
			} else {
				reponse_list_size.setText("Aucune partie existante.");
			}
		});

		this.unreg.addActionListener(e -> {
			if (partie_lance) {
				controller.Command_unreg_player("UNREG " + id_game + "***", reponse_list_size);
				if (reponse_list_size.getText().startsWith("UNROK")) {
					partie_lance = false;
					this.back.setVisible(true);
					regis_game = CreateGraphicsUtils.createJButtonImage("ressources/button/boutton_add.png");
					list_game = CreateGraphicsUtils.createJButtonImage("ressources/button/list_off_button.png");
					size_game = CreateGraphicsUtils.createJButtonImage("ressources/button/size_off_button.png");
					this.updateUI();
				}
			} else {
				reponse_list_size.setText("Aucune partie existante.");
			}
		});

		this.regis_game.addActionListener(e -> {
			if (identifiant.getText().length() != 8 || port.getText().length() != 4) {
				reponse_list_size.setText("Votre identifiant ou port n'est pas correct.");
			} else {
				controller.Command_new_player("NEWPL " + identifiant.getText() + " " + port.getText() + "***",
						reponse_list_size);
				if (reponse_list_size.getText().startsWith("REGOK")) {
					partie_lance = true;
					id_game = Integer.valueOf(reponse_list_size.getText().replace("REGOK ", "").replace("***", ""));
					size_game = new JButtonGraphiqueBuilder("ressources/button/size_button.png");
					list_game = new JButtonGraphiqueBuilder("ressources/button/list_button.png");
					this.updateUI();
				}
			}
		});
	}
}
