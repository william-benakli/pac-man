package modele;

public class Game {

    private int id_game; //m
    private int hauteur, largeur;//h w
    private int fantome; // f
    private String ip_partie;//ip de la partie


    private Game(int id_game, int hauteur, int largeur, int fantome, String ip_partie){
        this.id_game = id_game;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.fantome = fantome;
        this.ip_partie = ip_partie;
    }

    public int getIdGame(){
        return this.id_game;
    }

    /* Fabrique static vu en L3 */
    public static Game createGame(int id_game, int hauteur, int largeur, int fantome, String ip_partie){
        return new Game(id_game, hauteur, largeur, fantome, ip_partie);
    }
}
