package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * calculating score based on time and mango
 */

public class Score{
    //variables
    int points = 0;
    int multiplierS = 10;
    boolean touchingMango;
    int addScore = 1000;
    private boolean gameEnded;
    private boolean gameOver;
    private PApplet applet;


    void Score( Time tijd) {
        // after game points + getting time
        int sec = tijd.getSec();
        points = points + (sec * multiplierS);
    }
    void addScore(){
        // ingame score

        points = points + addScore;
    }



    //getters and setters
    public int getpoints() {
        return points;
    }

    public void renderScore(PApplet applet){
        applet.text("Score: "+points, 1160, 100);
    }
}