package src.ghostlab.modele;

public class Game {

    private Player player;
    private int id_game; //m
    private int hauteur, largeur;//h w
    private int fantome; // f
    private String ip_partie;//ip de la partie
    private String port;
    private char[][] labyrinth_to_parcour;


    private Game(int id_game, int hauteur, int largeur, int fantome, String ip_partie, String port, Player player){
        this.id_game = id_game;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.fantome = fantome;
        this.ip_partie = ip_partie;
        this.player = player;
        this.port = port;
        this.labyrinth_to_parcour = new char[hauteur][largeur];
        for (int i = 0; i < largeur ; i++){
            for (int j = 0; j < hauteur; j++){
                if(i == 0 || j == 0 || i == (hauteur-1) || j == (largeur-1)){
                    labyrinth_to_parcour[i][j]= '#';
                }else{
                    labyrinth_to_parcour[i][j]= '0';
                }
            }
        }
        movePlayer(player.getPosX(), player.getPosY());
    }

    public void setPosition(int x, int y, char c){
        labyrinth_to_parcour[x][y] = c;
    }

    public void movePlayer(int x, int y){
        labyrinth_to_parcour[player.getPosX()][player.getPosY()] = '1';
        labyrinth_to_parcour[x][y] = 'p';
        player.setPoX(x);
        player.setPoY(y);
    }

    public char[][] getLabyrinth_to_parcour() {
        return labyrinth_to_parcour;
    }

    public int getIdGame(){
        return this.id_game;
    }

    public String getIpUdp(){ 
        return ip_partie;}
    public String getPortUdp(){ return port;}
    public Player getPlayer(){ return player;}

    /* Fabrique static vu en L3 */
    public static Game createGame(int id_game, int hauteur, int largeur, int fantome, String ip_partie, String port, Player player){
        return new Game(id_game, hauteur, largeur, fantome, ip_partie, port, player);
    }
/*
    public void parcoursX(int depart_x, int arrive_x, int y){
        for(int i = 0; i < arrive_x; i++){
            movePlayer(depart_x+i, y);
        }
    }

    public void parcoursY(int depart_y, int arrive_y, int x){
        for(int i = 0; i < arrive_y; i++){
            movePlayer(x, depart_y+i);
        }
    }*/

    public int getLargeur() { return largeur; }
    public int getHauteur() { return hauteur; }

}
