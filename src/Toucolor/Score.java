package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * calculating score based on time and mango
 */

public class Score{
    //variables
    int points = 0;
    int multiplierM = 2;
    int multiplierS = 1;
    boolean touchingMango;
    int addScore = 1000;
    private boolean gameEnded;
    private boolean gameOver;
    private PApplet applet;


    Score( Time tijd){
        // after game points + getting time
        if (gameEnded == true){
            int min = tijd.getMin();
            int sec = tijd.getSec();
            points = points + min * multiplierM;
            points = points + sec * multiplierS;

        }
        //ingame points
        else {
            if (touchingMango == true){
                points = points + addScore;
            }
        }
    }


    //getters and setters
    public int getpoints() {
        return points;
    }

    public void renderScore(){
        //ppaplet nodig ofzoiets

    }
}