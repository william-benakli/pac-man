package src.ghostlab.vue.graphics;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class FontGraphiqueBuilder{

    /*#GraphiqueBuilder
     * Class permettant l'importation d'une font
     *
     */
    private Font font;
    public FontGraphiqueBuilder(float value) throws FontFormatException, IOException {

        font = Font.createFont(Font.TRUETYPE_FONT, new File("ressources/font/8bithud.ttf")).deriveFont(value);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
    }

    public Font getFont() {
        return font;
    }


}
