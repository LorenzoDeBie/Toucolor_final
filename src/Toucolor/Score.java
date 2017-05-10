package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * calculating score based on time and mango
 */

class Score{
    Time tijd;
    //variables
    int points = 0;
    int multiplierF = 5;
    int multiplierD = 1;
    boolean touchingMango;
    int addScore = 1000;
    private boolean gameEnded;
    private boolean gameOver;
    private PApplet applet;
    int pos = 300;


    Score( Toucolor applet) {
        this.applet = applet;
    }

    void addToScore(int pointsToAdd) {
        points += pointsToAdd;
    }

    void timeToPointsF(Time tijd, int xpos){
        this.pos = xpos;
        int startTijd = tijd.getTimeForLevel();
        this.tijd = tijd;
        int seconden = startTijd - tijd.secondsLeft();
        points += (pos/seconden)*multiplierF;
    }

    void timeToPointsD(Time tijd, int xpos){
        this.pos = xpos;
        int startTijd = tijd.getTimeForLevel();
        this.tijd = tijd;
        int seconden = startTijd - tijd.secondsLeft();
        points += (pos/seconden)*multiplierD;
        PApplet.println(points);
    }

    //getters and setters
    int getpoints() {
        return points;
    }

    void renderScore(){
        applet.textSize(16);
        applet.fill(0);
        applet.text("Score: "+points, 1160, 20);
    }
}