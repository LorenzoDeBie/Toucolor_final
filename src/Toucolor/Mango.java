package Toucolor;

import processing.core.PApplet;

/**
 * Created by loren on 07/05/2017.
 */
public class Mango extends Actor {

    private boolean isClaimed;

    Mango(int spawnX, int spawnY) {
        this.actorX = spawnX;
        this.actorY = spawnY;
        isClaimed = false;
    }

    boolean isColliding(int playerX, int playerY) {
        return (PApplet.abs(this.actorX - playerX) < Toucolor.BLOCKSIZE && PApplet.abs(this.actorY - playerY) < Toucolor.BLOCKSIZE);
    }

    void claim() {
        this.isClaimed = true;
    }

    boolean isClaimed() {
        return this.isClaimed;
    }

}
