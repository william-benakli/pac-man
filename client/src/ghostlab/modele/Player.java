package src.ghostlab.modele;

public class Player {

    private String identifant;
    private int x,y, point;

    private Player(String identifant, int x, int y){
        this.identifant = identifant;
        this.x = x;
        this.y = y;
        this.point = 0;
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
    public int getPoint(){
        return point;
    }
    public void addPoint(){
        point++;
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
