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
    protected int id;
    protected boolean collision;
    protected String name;
    protected PImage img;
    protected Toucolor applet;
    private PGraphics pg;
    protected boolean brokkelt;
    protected boolean drawBlock;
    protected boolean killsPlayer;

    //constructor
    Block(int id, String name, String imgFileName, boolean collision, boolean killsPlayer,PGraphics pg, Toucolor applet) {
        this.id = id;
        this.collision = collision;
        this.name = name;
        this.applet = applet;
        this.killsPlayer = killsPlayer;
        this.drawBlock = true;
        img = applet.loadImage(imgFileName); //load the image
        this.brokkelt = brokkelt;
        if(id == 0) {
            this.drawBlock = false;
        }
        this.pg = pg;
    }

    protected Block() {}

    //renders this block on given location
    PImage renderblock() {
        return img;
//        pg.imageMode(PConstants.CORNER);
//        pg.image(img, x, y, 80, 80);
        //voor brokkelende blocks
    }

    //getter for killplayer
    boolean killsPlayer() {
        return this.killsPlayer;
    }



    //destroys the this instance of the block --> block wordt niet meer getekent.
    void destroyblock() {
        this.collision = false;
    }

    //does the block have to be drawn
    boolean drawBlock() {
        return this.drawBlock;
    }

    //getters and setters
    int getId() {
        return id;
    }

    boolean isCollision() {
        return collision;
    }

    String getName() {
        return name;
    }

}
