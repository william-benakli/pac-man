package src.ghostlab.vue.panel;

import src.ghostlab.controler.SendReq;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameComposent extends JPanel implements MouseInputListener {

    private int game_select;
    private JLabel name_game;
    private SendReq sendReq;

    public GameComposent(int game_select, SendReq sendReq){
        this.sendReq = sendReq;
        this.game_select = game_select;
        this.name_game = CreateGraphicsUtils.createLabelWithFont(" Game n-" + game_select, Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(name_game, BorderLayout.CENTER);
        this.setBackground(new Color(20,20,20));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int getGame_select(){
        return game_select;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        VueClient.setPanel(new PanelRegisterGame(game_select, sendReq));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setBackground(new Color(9,9,9));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setBackground(new Color(20,20,20));

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
