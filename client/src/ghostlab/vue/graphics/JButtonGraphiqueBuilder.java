package src.ghostlab.vue.graphics;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.event.MouseInputListener;

public class JButtonGraphiqueBuilder extends JButton implements MouseInputListener{

    /*#GraphiqueBuilder*/
    private Image scaledImg;
    private boolean hover;

    public JButtonGraphiqueBuilder(String chemin){
        ImageIcon icon = new ImageIcon(chemin);
        scaledImg = icon.getImage();
        icon = new ImageIcon(scaledImg);
        setIcon(icon);
        setPreferredSize(new Dimension(icon.getIconWidth() -5, icon.getIconHeight() -5));
        setMaximumSize(new Dimension(icon.getIconWidth()-5, icon.getIconHeight() -5));
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(hover) {
            g.setColor(new Color(250,250,250,75));
            g.fillRoundRect(0, 0, scaledImg.getWidth(this)-5, scaledImg.getHeight(this)-5, 60,20);
        }else g.setColor(new Color(0,0,0,0));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        hover = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hover = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hover = false;
        repaint();
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
