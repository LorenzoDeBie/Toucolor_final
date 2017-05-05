package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * Tijdsdruk kills
 */

public class Time{
    //variables
    float tijd;
    float vermindering;
    int sec;
    private PApplet applet;
    private boolean gameOver;



    //Set time
    Time(float tijd,  PApplet applet, float verm) {
        this.tijd = tijd;
        this.applet = applet;
        this.vermindering = 1/verm;
    }


    //update time
    void Update() {
        tijd = tijd - vermindering;
        sec = (int) tijd;
        //times up scrub
        if (sec == 0) {
            gameOver = true;
        }
    }

    public void renderScore(PApplet applet){
        applet.text("Time: "+tijd, 960, 100);
    }

    //getters and setters
    public int getSec() {
        return sec;
    }
}