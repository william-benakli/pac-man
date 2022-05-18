package src.ghostlab.vue.panel;

import src.ghostlab.modele.Game;
import src.ghostlab.vue.CreateGraphicsUtils;
import src.ghostlab.vue.VueClient;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameComposent extends JPanel implements MouseInputListener {

    private Game game_select;
    private JLabel name_game;

    private boolean hover;
    private int w,h;

    public GameComposent(Game game_select){
        this.game_select = game_select;
        this.name_game = CreateGraphicsUtils.createLabelWithFont(" Game n-" + game_select.getIdGame(), Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(name_game, BorderLayout.CENTER);
        this.w = this.getWidth();
        this.h = this.getHeight();
        this.setBackground(new Color(20,20,20));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Game getGame_select(){
        return game_select;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        VueClient.setPanel(new PanelRegisterGame(game_select));
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
