package Toucolor;

import processing.core.PApplet;
/**
 * Created by robbe on 31/03/2017.
 * Tijdsdruk kills
 */

public class Time{
    //variables
    float Tijd;
    float vermindering;
    int Min;
    float kommaSec;
    int Sec;
    private PApplet applet;
    private boolean gameEnded;
    private boolean gameOver;



    //Set time
    Time(float Tijd,  PApplet applet, float verm) {
        this.Tijd = Tijd;
        this.applet = applet;
        this.vermindering = 1/verm;
    }


    //update time
    void Update(){
        if (gameEnded == false){
            Tijd = Tijd - vermindering;
            Min = (int)Tijd;
            kommaSec = (Tijd-Min)*60;
            Sec = (int)kommaSec;
        }
        //times up scrub
        if(Min == 0 && Sec == 0){
            gameOver = true;
        }

    }


    //getters and setters
    public int getMin() {
        return Min;
    }
    public int getSec() {
        return Sec;
    }
}