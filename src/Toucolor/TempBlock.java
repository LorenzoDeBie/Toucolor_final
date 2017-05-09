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
    private int blockX;
    private int blockY;

    TempBlock(int blockX, int blockY,int timeTillGone, boolean killsPlayer, PApplet applet) {
        this.timeTillGone = timeTillGone;
        isStandingOn = false;
        this.killsPlayer = killsPlayer;
        this.blockX = blockX;
        this.blockY = blockY;
        this.img = applet.loadImage("TempBlock.png");
        this.id = 4;
        this.applet = applet;
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

    int getBlockX(){
        return this.blockX;
    }

    int getBlockY() {
        return this.blockY;
    }



}
