package modele;

public class Game {

    private Player player;
    private int id_game; //m
    private int hauteur, largeur;//h w
    private String ip_partie;//ip de la partie
    private char[][] labyrinth_to_parcour;


    private Game(int id_game, int hauteur, int largeur, int fantome, String ip_partie){
        this.id_game = id_game;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.fantome = fantome;
        this.ip_partie = ip_partie;
        this.labyrinth_to_parcour = new char[hauteur][largeur];
        for (int i = 0; i < hauteur; i++){
            for (int j = 0; j < largeur; j++){
                if(i == 0 || j == 0 || i == (hauteur-1) || j == (largeur-1)){
                    labyrinth_to_parcour[i][j]= '#';
                }else{
                    labyrinth_to_parcour[i][j]= '0';
                }
            }
        }
        labyrinth_to_parcour[4][4]= 'p';
        labyrinth_to_parcour[4][5]= 'f';
        labyrinth_to_parcour[3][1]= 'f';

    }

    public void setPosition(int x, int y, char c){
        labyrinth_to_parcour[x][y] = c;
    }

    public void movePlayer(int x, int y){
        labyrinth_to_parcour[player.getPosX()][player.getPosY()] = '1';
        labyrinth_to_parcour[x][y] = 'p';
        player.setPoX(x);
        player.setPoY(x);
    }

    public char[][] getLabyrinth_to_parcour() {
        return labyrinth_to_parcour;
    }

    public int getIdGame(){
        return this.id_game;
    }

    /* Fabrique static vu en L3 */
    public static Game createGame(int id_game, int hauteur, int largeur, int fantome, String ip_partie){
        return new Game(id_game, hauteur, largeur, fantome, ip_partie);
    }

    public int getLargeur() { return largeur; }
    public int getHauteur() { return hauteur; }

}
