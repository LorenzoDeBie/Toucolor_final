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
    private boolean isBroken;

    TempBlock(int blockX, int blockY,int timeTillGone, boolean killsPlayer, Toucolor applet) {
        this.timeTillGone = timeTillGone;
        isStandingOn = false;
        this.killsPlayer = killsPlayer;
        this.blockX = blockX;
        this.blockY = blockY;
        this.img = applet.loadImage("TempBlock.png");
        this.id = 4;
        this.applet = applet;
        this.drawBlock = true;
        this.isBroken = false;
        this.collision = true;
    }
    void update() {
        //only update if block is not broken
        if(!isBroken) {
            //if the block has been stand on --> calc how long since first stand on
            if(isStandingOn) {
                millisTillGone = applet.millis() - startMillis;
            }
            //make the block flikker
            flikker();
            //enough time has passed
            if(millisTillGone > timeTillGone) {
                //destroy the block here
                destroyBlock();
            }
        }
    }

    //this makes the block flikker
    private void flikker() {
        //first 3 secs, only change every sec
        if(millisTillGone < 3000) {
            this.drawBlock = (millisTillGone / 1000) % 2 == 0;
        }
        else {
            //after three secs, chagne very 1/10th of a sec
            this.drawBlock = (millisTillGone / 100) % 2 == 0;
        }
    }

    //called by player
    //starts timer and sets the fields right
    void standOn() {
        if(!isBroken && !isStandingOn) {
            startMillis = applet.millis();
            isStandingOn = true;
        }
    }

    //sets the fields right and changes the block in the level
    private void destroyBlock() {
        this.drawBlock = false;
        this.isStandingOn = false;
        this.isBroken = true;
        super.destroyblock();
        applet.getCurrentLevel().changeBlock(blockX, blockY, 0, true);
    }

    boolean getIsBroken() {
        return this.isBroken;
    }

    int getBlockX(){
        return this.blockX;
    }

    int getBlockY() {
        return this.blockY;
    }



}
