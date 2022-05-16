package vue;

import vue.graphics.JButtonGraphiqueBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        final JButtonGraphiqueBuilder jButton = new JButtonGraphiqueBuilder(chemin);
        return jButton;
    }

    public static JTextField createJtextField(){
        final JTextField jTextField = new JTextField();
        jTextField.setBounds(1,1,1,1);
        return jTextField;
    }

    public static JComboBox createJComboBoxList(){
        final JComboBox jcombox = new JComboBox();
        return jcombox;
    }


    public static JTabbedPane createJTabbedPane(JPanel ... listComposant) {
        final JTabbedPane jTabbedPane = new JTabbedPane();
        for(JPanel panel : listComposant) jTabbedPane.addTab(panel.getName(), panel);
        return jTabbedPane;
    }

    public static JPanel createJpanel(String name, JComponent ... listComposant) {
        final JPanel jpanel = new JPanel();
        jpanel.setBorder(BorderFactory.createTitledBorder(name));
        for(JComponent panel : listComposant) jpanel.add(panel);
        return jpanel;
    }
    //Surgchage de la fonciton
    public static JPanel createJpanel(JComponent ... listComposant) {
        final JPanel jpanel = new JPanel();
        for(JComponent panel : listComposant) jpanel.add(panel);
        return jpanel;
    }

    public static JLabel createJLabel(String text) {
        final JLabel label = new JLabel();
        label.setText(text);
        label.setSize(120, 120);
        return label;
    }

    public static JPanel createJpanel(GridLayout grid, JComponent ... listComposant) {
        final JPanel jpanel = new JPanel();
        jpanel.setLayout(grid);
        for(JComponent panel : listComposant) {
            jpanel.add(panel);
        }
        return jpanel;
    }
}
