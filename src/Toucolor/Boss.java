package Toucolor;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

/**
 * Created by loren on 08/05/2017.
 */
class Boss extends Actor {
    private PImage bossImage;
    private int jumpX, jumpY;
    private Level currentLevel;
    private int row;
    private int timesLeft;
    //waiting, jumping
    private String status;
    private int millisTillNextJump;
    private Toucolor applet;

    //three rows where boss jumps to
    private static final int[] ROWSTOJUMP = {3, 5, 9};
    private static final int RANGE = 3;

    Boss(int spawX, int spawnY, Toucolor applet) {
        this.actorX = spawX;
        this.actorY = spawnY;
        this.currentLevel = applet.getCurrentLevel();
        this.row = 0;
        this.timesLeft = 2;
        this.status = "waiting";
        this.applet = applet;
        this.bossImage = applet.loadImage("Leprechaun0.png");
        millisTillNextJump = 500;
    }

    private void jumpToNextBlock() {
        int currentXBlock = (int) actorX / Toucolor.BLOCKSIZE;
        int nextBlockX;
        do {
            Random rnd = new Random();
            int randomNum = rnd.nextInt(RANGE + 1);
            int teken = rnd.nextInt(2) < 1 ? -1 : 1;
            int verandering = teken * randomNum;
            nextBlockX = currentXBlock + verandering;
        } while (!(0 < nextBlockX && nextBlockX < 16) || nextBlockX == currentXBlock);
        int currentY = (int) actorY / Toucolor.BLOCKSIZE;
        int nextY = timesLeft > 0 ? currentY : ROWSTOJUMP[row++];
        timesLeft = timesLeft > 0 ? timesLeft : row;
        jumpX = nextBlockX * Toucolor.BLOCKSIZE; jumpY = nextY * Toucolor.BLOCKSIZE;
        PApplet.println("Now jumping to: " + nextBlockX + ", " + nextY);
    }

    private void jump() {
        if(actorX < jumpX) {
            actorX++;
        }
        else if(actorX > jumpX){
            actorX--;
        }

        if(actorY < jumpY) {
            actorY++;
        }
        else if(actorY > jumpY) {
            actorY--;
        }
        if(actorX == jumpX && actorY == jumpY) {
            currentLevel.changeBlock((int) actorX, (int) actorY + 80, 4, true);
            timesLeft--;
            millisTillNextJump = 400;
            this.status = "waiting";
        }
    }

    private void waitForJump() {
         if(millisTillNextJump > 0) {
             millisTillNextJump--;
         }
         else {
            jumpToNextBlock();
            this.status = "jumping";
         }
    }

    private void update() {
        if(row == 3 && timesLeft == 0) {
            PApplet.println("YOU BEAT THIS STUPID ASS GAME, WHAT ARE YOU DOING WITH YOUR LIFE?");
            currentLevel.levelEnding = true;
        }
        else {
            //jump further if not jumped already
            if(status.equals("jumping")) {
                jump();
            }
            else {
                waitForJump();
            }
        }
    }

    void renderBoss() {
        update();
        applet.image(bossImage, (int) actorX, (int) actorY);
    }
}
