package Toucolor;

import processing.core.*;
/**
 * Created by robbe on 31/03/2017.
 * Tijdsdruk kills
 */

class Time{

    private int timeStart;
    private int tijdForLevel;
    //variables
    private int timeLeftMSeconds;
    private int timeLeftSeconds;
    private PApplet applet;
    private boolean gameOver;



    //Set time
    Time(int tijd,  PApplet applet) {
        this.tijdForLevel = tijd;
        timeStart = applet.millis();
        this.timeLeftSeconds = tijd;
        this.timeLeftMSeconds = timeLeftSeconds * 1000;
        this.applet = applet;
    }


    //update time
    private void update() {
        timeLeftMSeconds = ((Toucolor.LEVELTIME * 1000) - (applet.millis() - timeStart));
        timeLeftSeconds = timeLeftMSeconds/1000;
    }

    void renderTime(){
        update();
        applet.textSize(16);
        applet.fill(0);
        applet.text("Time: " + timeLeftSeconds, 1060, 20);
    }

    //todo negatieve punted? git good

    int getTimeForLevel(){
        return tijdForLevel;
    }

    int millisLeft() {
        return timeLeftMSeconds;
    }

    int secondsLeft() {
        return timeLeftSeconds;
    }

}