package Toucolor;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by loren on 08/05/2017.
 */
class Boss extends Actor {
    private PImage bossImage;
    private int jumpX, jumpY;
    private Level currentLevel;

    Boss(int spawX, int spawnY, Level currentlevel) {
        this.actorX = spawX;
        this.actorY = spawnY;
        this.currentLevel = currentLevel;
    }

    public void jumpToCoords(int jumpX, int jumpY) {
        this.jumpX = jumpX;
        this.jumpY = jumpY;
        this.isInAir = false;
    }

    private void jump() {
        if(actorX < jumpX) {
            actorX++;
        }
        if(actorY < jumpY) {
            actorY++;
        }
        if(actorX == jumpX && actorY == jumpY) {
            //TODO make temp block
        }
    }

    public void update() {
        //jump further if not jumped already
    }
}
