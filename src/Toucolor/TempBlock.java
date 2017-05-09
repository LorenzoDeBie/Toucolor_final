package Toucolor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by loren on 08/05/2017.
 */
class TempBlock extends Block {

    private int millisTillGone;
    private int startMillis;
    private boolean isStandingOn;
    private int timeTillGone;

    TempBlock(int id, String name, String imgFileName, boolean collision, int timeTillGone, boolean killsPlayer, PGraphics pg, PApplet applet) {
        super(id, name, imgFileName, collision, killsPlayer, pg, applet);
        this.timeTillGone = timeTillGone;
        isStandingOn = false;
    }

    //this makes the block flikker
    private void flikker() {
        if((millisTillGone % (applet.frameRate / 2)) == 0) {
            this.drawBlock = !this.drawBlock; //changes true to false and opposite
        }
    }

    //called by player
    void standOn() {
        if(isStandingOn) {
            millisTillGone = applet.millis() - startMillis;
            flikker();
        }
        else {
            startMillis = applet.millis();
            millisTillGone = timeTillGone;
        }

        if(millisTillGone <= 0) {
            //destroy the block here
            destroyblock();

        }
    }

    private void destroyBlock() {
        super.destroyblock();

    }

}
