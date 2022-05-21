package src.ghostlab.vue.panel;

import src.ghostlab.modele.Game;
import src.ghostlab.controler.SendReq;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;
import javax.swing.*;
import java.awt.*;

public class PanelInGame extends JPanelGraphiqueBuilder {

    private Game game;
    private JLabel game_over, point, multicastlabel, msg_privelabel, multicasttextlabel, msg_privetextlabel,pasdeplacementlabel, idplayerlabel;

    private JPanel game_panel, laby_panel, in_laby_panel, chat_panel, reponse_panel;
    private JButton up,down,right,left,quit,sendpv,sendmulti, glist;
    private JTextField pasdeplacementext, idplayerpvtext, msgmulticast, msgprive;
    private JTextArea  msgpvtextarea, multicasttextarea, reponserveurtextarea;
    private SendReq controller;

    public PanelInGame(Game game, SendReq controller) {
        super("ressources/panel/background.jpg");
        this.controller = controller;
        this.game_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/game_panel.png");
        this.chat_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/systemchat_panel.png");
        this.reponse_panel = CreateGraphicsUtils.createPanelImage("ressources/panel/reponse_panel.png");

        this.multicastlabel = CreateGraphicsUtils.createLabelWithFont("Reponse Multicast", Color.ORANGE);
        this.msg_privelabel = CreateGraphicsUtils.createLabelWithFont("Reponse Prive", Color.ORANGE);

        this.multicasttextlabel = CreateGraphicsUtils.createLabelWithFont("Msg", Color.ORANGE);
        this.msg_privetextlabel = CreateGraphicsUtils.createLabelWithFont("Msg", Color.ORANGE);
        this.pasdeplacementlabel = CreateGraphicsUtils.createLabelWithFont("Pas", Color.ORANGE);
        this.idplayerlabel = CreateGraphicsUtils.createLabelWithFont("Id", Color.ORANGE);
        this.point = CreateGraphicsUtils.createLabelWithFont("Vos points: 0", Color.ORANGE);
        this.game_over = CreateGraphicsUtils.createLabelWithFont("GAME OVER", Color.RED);
        this.game_over.setVisible(false);
        this.laby_panel = new JPanel();
        this.in_laby_panel = new JPanel();

        this.up = CreateGraphicsUtils.createJButtonImage("ressources/button/up_button.png");
        this.down = CreateGraphicsUtils.createJButtonImage("ressources/button/down_button.png");
        this.right = CreateGraphicsUtils.createJButtonImage("ressources/button/right_button.png");
        this.left = CreateGraphicsUtils.createJButtonImage("ressources/button/left_button.png");
        this.pasdeplacementext = new JTextField("001");
        
        this.quit = CreateGraphicsUtils.createJButtonImage("ressources/button/iquit_button.png");
        this.sendpv = CreateGraphicsUtils.createJButtonImage("ressources/button/send_button.png");
        this.sendmulti = CreateGraphicsUtils.createJButtonImage("ressources/button/send_button.png");
        this.glist = CreateGraphicsUtils.createJButtonImage("ressources/button/glist_button.png");
        this.idplayerpvtext = new JTextField();
        this.game = game;

        this.msgpvtextarea =  CreateGraphicsUtils.createTextArea(280, 200);
        this.multicasttextarea = CreateGraphicsUtils.createTextArea(280,200);
        this.reponserveurtextarea = CreateGraphicsUtils.createTextArea(900, 145);
        this.msgmulticast = new JTextField();
        this.msgprive = new JTextField();

      /*  JScrollPane scroll = new JScrollPane (reponserveurtextarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        reponserveurtextarea.append("afazfazfzafazfzafzafzazf");
        */
        GroupLayout LayoutMain = new GroupLayout(this);
        setLayout(LayoutMain);
        //this.laby_panel.setPreferredSize(new Dimension(400, 300));

        /* X */
        LayoutMain.setHorizontalGroup(
                LayoutMain.createParallelGroup(GroupLayout.Alignment.LEADING)
                        //panel game et laby
                        .addGroup(LayoutMain.createSequentialGroup().addGap(185).addComponent(game_over))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(40).addComponent(laby_panel, 400, 400, 400))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(60).addComponent(point))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(520).addComponent(up, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(520).addComponent(down, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(575).addComponent(right, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(465).addComponent(left, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(470).addComponent(pasdeplacementlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(470).addComponent(pasdeplacementext, 80, 80, 80))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(555).addComponent(glist, 80, 80, 80))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(70).addComponent(reponserveurtextarea, 900, 900, 900))
                        //Panel mutli-cast et msg private
                        .addGroup(LayoutMain.createSequentialGroup().addGap(680).addComponent(multicastlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(680).addComponent(multicasttextarea, 280, 280, 280))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(700).addComponent(multicasttextlabel, 120, 120, 120))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(750).addComponent(msgmulticast, 120, 120, 120))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(760).addComponent(sendmulti, 100, 100, 100))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(920).addComponent(msg_privetextlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(1075).addComponent(idplayerlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(970).addComponent(msg_privelabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(970).addComponent(msgpvtextarea, 280, 280, 280))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(970).addComponent(msgprive, 100, 100, 100))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(1120).addComponent(idplayerpvtext, 100, 100, 100))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(1040).addComponent(sendpv, 100, 100, 100))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(1050).addComponent(quit, 150, 150, 150))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(670).addComponent(chat_panel, 590, 590, 590))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(20).addComponent(game_panel, 650, 650, 650))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(20).addComponent(reponse_panel, 1000, 1000, 1000))

        );
        /* Y */
        LayoutMain.setVerticalGroup(
                LayoutMain.createParallelGroup(GroupLayout.Alignment.LEADING)
                        //panel game et laby
                        .addGroup(LayoutMain.createSequentialGroup().addGap(210).addComponent(game_over))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(60).addComponent(laby_panel, 300, 300, 300))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(370).addComponent(point))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(90).addComponent(up, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(210).addComponent(down, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(150).addComponent(right, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(150).addComponent(left, 50, 50, 50))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(282).addComponent(pasdeplacementlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(300).addComponent(pasdeplacementext, 40, 40, 40))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(300).addComponent(glist, 40, 40, 40))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(485).addComponent(reponserveurtextarea, 145, 145, 145))
                        //Panel mutli-cast et msg private

                        .addGroup(LayoutMain.createSequentialGroup().addGap(55).addComponent(multicastlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(80).addComponent(multicasttextarea, 200, 200, 200))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(260).addComponent(multicasttextlabel, 120, 120, 120))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(300).addComponent(msgmulticast, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(360).addComponent(sendmulti, 50, 50, 50))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(55).addComponent(msg_privelabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(320).addComponent(msg_privetextlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(80).addComponent(msgpvtextarea, 200, 200, 200))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(320).addComponent(idplayerlabel))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(300).addComponent(idplayerpvtext, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(300).addComponent(msgprive, 50, 50, 50))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(360).addComponent(sendpv, 50, 50, 50))

                        .addGroup(LayoutMain.createSequentialGroup().addGap(515).addComponent(quit, 70, 70, 70))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(20).addComponent(chat_panel, 400, 400, 400))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(20).addComponent(game_panel, 400, 400, 400))
                        .addGroup(LayoutMain.createSequentialGroup().addGap(450).addComponent(reponse_panel, 198, 198, 198))

        );
        laby_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        laby_panel.setLayout(new BorderLayout());
        laby_panel.add(in_laby_panel);
        laby_panel.setBackground(Color.black);
        this.in_laby_panel.setLayout(new GridLayout(game.getHauteur(),game.getLargeur()));
        controller.connectUdp(game, multicasttextarea,msgpvtextarea);
        updateUI();
        refresh_laby(game);
        actionListerner();
        
    }
    

    public void refresh_laby(Game game) {
        in_laby_panel.removeAll();
        final char[][] labyrinth_to_parcour = game.getLabyrinth_to_parcour();

        for (int i = 0; i < game.getLargeur(); i++){
            for (int j = 0; j < game.getHauteur(); j++){
                final JPanel mur = CreateGraphicsUtils.createPanelImage("ressources/textures/mur.png");
                final JPanel vide = CreateGraphicsUtils.createPanelImage("ressources/textures/vide.png");
                final JPanel sol = CreateGraphicsUtils.createPanelImage("ressources/textures/sol.png");
                final JPanel player = CreateGraphicsUtils.createPanelImage("ressources/textures/joueur.png");
                final JPanel fantome = CreateGraphicsUtils.createPanelImage("ressources/textures/fantome.png");

                mur.setSize(new Dimension(40,50));
                mur.setMaximumSize(new Dimension(40,50));
                mur.setPreferredSize(new Dimension(40,50));
                vide.setSize(new Dimension(40,40));
                vide.setMaximumSize(new Dimension(40,50));
                vide.setPreferredSize(new Dimension(40,50));

                player.setSize(new Dimension(40,50));
                player.setMaximumSize(new Dimension(40,50));
                player.setPreferredSize(new Dimension(40,50));

                sol.setSize(new Dimension(40,50));
                sol.setMaximumSize(new Dimension(40,50));
                sol.setPreferredSize(new Dimension(40,50));

                if(labyrinth_to_parcour[j][i] == '#'){
                    in_laby_panel.add(mur);
                }else if(labyrinth_to_parcour[j][i] == '1'){
                    in_laby_panel.add(sol);
                }else if(labyrinth_to_parcour[j][i] == 'p'){
                    in_laby_panel.add(player);
                }else if(labyrinth_to_parcour[j][i] == 'f'){
                    in_laby_panel.add(fantome);
                }else{
                    in_laby_panel.add(vide);
                }
            }
            if(reponserveurtextarea.getText().startsWith("GOBYE")){
                affiche_end();
            }
        }
        point.setText("Vos points :" + game.getPlayer().getPoint());
    }

    public void affiche_end(){
        in_laby_panel.removeAll();
        for(int i = 0; i < 10; i++){
            for(int y = 0; y < 10; y++){
                    JPanel noir = new JPanel();
                    noir.setBackground(Color.BLACK);
                    noir.setSize(new Dimension(40,50));
                    noir.setMaximumSize(new Dimension(40,50));
                    noir.setPreferredSize(new Dimension(40,50));
                    in_laby_panel.add(noir);
                }
        }
        updateUI();
        laby_panel.updateUI();
        this.game_over.setVisible(true);
    }
    public void actionListerner(){
        up.addActionListener(event->{
            controller.check_move_in_game("UPMOV "+ pasdeplacementext.getText() + "***", game,Integer.valueOf(pasdeplacementext.getText()),1, reponserveurtextarea);
            refresh_laby(game);
        });
        down.addActionListener(event->{
            controller.check_move_in_game("DOMOV "+ pasdeplacementext.getText() + "***", game,Integer.valueOf(pasdeplacementext.getText()), 2,reponserveurtextarea);
            refresh_laby(game);
        });
        right.addActionListener(event->{
            controller.check_move_in_game("RIMOV "+ pasdeplacementext.getText() + "***", game,Integer.valueOf(pasdeplacementext.getText()), 3,reponserveurtextarea);
            refresh_laby(game);
        });
        left.addActionListener(event->{
            controller.check_move_in_game("LEMOV "+ pasdeplacementext.getText() + "***",game, Integer.valueOf(pasdeplacementext.getText()), 4, reponserveurtextarea);
            refresh_laby(game);
        });

        sendmulti.addActionListener(event->{
            controller.msg_all_in_game("MALL? "+ msgmulticast.getText() + "***", reponserveurtextarea);
        });
        
        sendpv.addActionListener(event->{
            if(idplayerlabel.getText().length() != 8 ){
                controller.msg_send_in_game("SEND? "+ idplayerpvtext.getText() + " " + msgprive.getText() + "***",reponserveurtextarea);
            }else{
                msgpvtextarea.setText("Identifiant incorrect.");
            }

        });

        quit.addActionListener(event->{
            controller.quit_in_game("IQUIT***");
            controller.closeSocket();
            System.exit(0);
        });
        
        glist.addActionListener(event->{
            controller.glist_in_game("GLIS?***", reponserveurtextarea);
        });
    }
    }


