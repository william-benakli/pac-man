package src.ghostlab.vue;

import src.ghostlab.vue.graphics.JButtonGraphiqueBuilder;
import src.ghostlab.vue.graphics.JPanelGraphiqueBuilder;

import javax.swing.*;
import java.awt.*;

public class CreateGraphicsUtils {

    /* Cette class est utile pour créer des objets
    * simplement et proprement en fabrique statique comme vu en L3
    * */

    public static JButton createJButton(String text){
        final JButton jButton = new JButton(text);
        return jButton;
    }

    public static JButtonGraphiqueBuilder createJButtonImage(String chemin){
        JButtonGraphiqueBuilder jButton = new JButtonGraphiqueBuilder(chemin);
        return jButton;
    }

    public static JPanelGraphiqueBuilder createPanelImage(String chemin){
        final JPanelGraphiqueBuilder jPanel = new JPanelGraphiqueBuilder(chemin);
        return jPanel;
    }

    public static JLabel createLabelWithFont(String nom){
        final JLabel jLabel = new JLabel(nom);
        jLabel.setFont(VueClient.font);
        return jLabel;
    }

    public static JLabel createLabelWithFont(String nom, Color color){
        final JLabel jLabel = new JLabel(nom);
        jLabel.setFont(VueClient.font);
        jLabel.setForeground(color);
        return jLabel;
    }

    public static JTextArea createTextArea(int w, int h){
        final JTextArea jTextArea = new JTextArea();
        jTextArea.setEnabled(false);
        jTextArea.setFont(VueClient.font);
        jTextArea.setForeground(Color.white);
        jTextArea.setBackground(Color.GRAY);
        jTextArea.setBounds(0,0, w,h);
        return jTextArea;
    }

    public static JTextField createJtextField(){
        final JTextField jTextField = new JTextField();
        jTextField.setFont(VueClient.font);
        jTextField.setBounds(1,1,1,1);
        return jTextField;
    }

    public static JComboBox createJComboBoxList(){
        final JComboBox jcombox = new JComboBox();
        return jcombox;
    }


}
