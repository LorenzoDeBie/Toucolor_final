package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * calculating score based on time and mango
 */

class Score{
    //variables
    int points = 0;
    int multiplierS = 10;
    boolean touchingMango;
    int addScore = 1000;
    private boolean gameEnded;
    private boolean gameOver;
    private PApplet applet;
    Time tijd;


    Score( Toucolor applet) {
        this.applet = applet;
    }

    void addToScore(int pointsToAdd) {
        points += pointsToAdd;
    }

    void timeToPoints(Time tijd){
        this.tijd = tijd;
        int seconden = tijd.secondsLeft();
        points += seconden*multiplierS;
    }

    //getters and setters
    public int getpoints() {
        return points;
    }

    void renderScore(){
        applet.textSize(16);
        applet.fill(0);
        applet.text("Score: "+points, 1160, 20);
    }
}