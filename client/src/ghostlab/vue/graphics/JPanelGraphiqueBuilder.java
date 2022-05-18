package src.ghostlab.vue.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPanelGraphiqueBuilder extends JPanel {

    /*#GraphiqueBuilder
     *
     * Redifinition d'un JPanel
     * afin d'y integre une image
     *
     */

    private BufferedImage image;
    private Image img;

    public JPanelGraphiqueBuilder(String chemin){
        try {
            image = ImageIO.read(new File(chemin));
        } catch (IOException e) {
            System.out.println("Ressources inconnue, contactez un adminstateur");
        }
        img = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight() ,null);
    }
}