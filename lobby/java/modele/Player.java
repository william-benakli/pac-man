package modele;

public class Player {

    private String identifant;
    private int x,y;

    private Player(String identifant, int x, int y){
        this.identifant = identifant;
        this.x = x;
        this.y = y;
    }

    public String getIdentifant(){
        return this.identifant;
    }
    public int getPosX(){
        return x;
    }
    public int getPosY(){
        return y;
    }

    /* Fabrique static vu en L3 */
    public static Player createPlayer(String identifant, int x, int y){
        return new Player(identifant, x, y);
    }

    public void setPoX(int x) {
        this.x = x;
    }
    public void setPoY(int y) {
        this.y = y;
    }
}
