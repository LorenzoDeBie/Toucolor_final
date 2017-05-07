package Toucolor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by loren on 17/03/2017.
 *
 * this class holds all the properties of a certain block and is able to render the block on the Papplet.
 */
class Block {

    /**
     * PRIVATE VARIABLES
     */
    private int id;
    private boolean collision;
    private String name;
    private PImage img;
    private PApplet applet;
    private PGraphics pg;
    private boolean brokkelt;
    private int framesUntillGone;
    private boolean drawBlock;
    private boolean killsPlayer;

    //constructor
    Block(int id, String name, String imgFileName, boolean collision, boolean brokkelt, boolean killsPlayer,PGraphics pg, PApplet applet) {
        this.id = id;
        this.collision = collision;
        this.name = name;
        this.applet = applet;
        this.killsPlayer = killsPlayer;
        this.drawBlock = true;
        img = applet.loadImage(imgFileName); //load the image
        this.brokkelt = brokkelt;
        if(brokkelt) {
            this.framesUntillGone = 432; //only set framesleft indien het brokkelt
        }
        if(id == 0) {
            this.drawBlock = false;
        }
        this.pg = pg;
    }

    //renders this block on given location
    PImage renderblock() {
        return img;
//        pg.imageMode(PConstants.CORNER);
//        pg.image(img, x, y, 80, 80);
        //voor brokkelende blocks
    }

    //called by player
    void standOn() {
        if(brokkelt) {
            framesUntillGone--;
            if(this.framesUntillGone == 0) {
                destroyblock();
            }
            flikker();
        }
    }

    //getter for killplayer
    boolean killsPlayer() {
        return this.killsPlayer;
    }

    //this makes the block flikker
    private void flikker() {
        if((framesUntillGone % (applet.frameRate / 2)) == 0) {
            this.drawBlock = !this.drawBlock; //changes true to false and opposite
        }
    }

    //destroys the this instance of the block --> block wordt niet meer getekent.
    private void destroyblock() {
        this.collision = false;
    }

    //does the block have to be drawn
    boolean drawBlock() {
        return this.drawBlock;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public boolean isCollision() {
        return collision;
    }

    public String getName() {
        return name;
    }

}
