package Toucolor;

import processing.core.PApplet;

import java.awt.*;

/**
 * Created by Vince on 5/5/2017.
 */
class Actor {
    private float[][] fullCoords;
    private boolean[][] propterties;

    float updateL, updateR, updateU, updateD, fullUpdateX, fullUpdateY;
    boolean mR = false;
    boolean mL = false;
    boolean mD = false;
    boolean mU = false;

    float actorX = 150, actorY=300, xblock, yblock;
    boolean canCollide, isDeadly, horizontaleCollision, verticaleCollision, isInAir, customHorizontaleCollision, touchedDeadly, jumping, cancelJump, upispressed;
    int blockSize = Toucolor.BLOCKSIZE;
    Actor type;
    int collblocks;



    void refreshValues(float[][] coords, boolean[][] props) {
        this.fullCoords = coords;
        this.propterties = props;
    }

    protected void collision(float r, float l, float u, float d, boolean inDeLucht, boolean springt, boolean upispressed) {
        collblocks = 0;
        this.updateR = r;
        this.updateL = l;
        this.updateU = u;
        this.updateD = d;
        isInAir = inDeLucht;
        jumping = springt;
        this.type = type;

        horizontaleCollision = false;
        verticaleCollision = false;
        cancelJump = false;

        fullUpdateX = actorX + (updateL + updateR);
        fullUpdateY = actorY + (updateD + updateU);

        if(fullUpdateX < 0){
            fullUpdateX = 0;
        }

        if((propterties[1][0] || propterties[2][0]) && propterties[6][0] && !propterties[4][0]){
            fullUpdateY = actorY;
        }

        for (int i = 0; i< fullCoords.length; i++) {
            //PApplet.println(i);
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];


            Rectangle fullplayer = new Rectangle((int)fullUpdateX, (int)fullUpdateY, blockSize, blockSize);
            Rectangle lastplayer = new Rectangle((int)actorX, (int)actorY, blockSize, blockSize);
            Rectangle playerX = new Rectangle((int)fullUpdateX, (int)actorY, blockSize, blockSize);
            Rectangle playerY = new Rectangle((int)actorX, (int)fullUpdateY, blockSize, blockSize);
            Rectangle currblock = new Rectangle((int)xblock, (int)yblock, blockSize, blockSize);


            if(canCollide && fullplayer.intersects(currblock)) {
                if (!isDeadly) {
                    if (canCollide && playerY.intersects(currblock) && !upispressed) {
                        if (!lastplayer.intersects(currblock)) {
                            if(jumping){
                                fullUpdateY = fullCoords[1][1] + blockSize;
                                cancelJump = true;
                            } else {
                                fullUpdateY = actorY;
                                isInAir = false;
                            }

                        } else {
                            if (jumping) {
                                fullUpdateY = fullCoords[1][1] + blockSize;
                                cancelJump = true;
                            } else {
                                fullUpdateY = fullCoords[6][1] - blockSize;
                                isInAir = false;
                            }
                        }
                    } else {
                        PApplet.println("collide niet met Y");
                    }
                    if (canCollide && playerX.intersects(currblock)) {
                        if (!lastplayer.intersects(currblock)) {
                            fullUpdateX = actorX;
                        } else {
                            if (updateL + updateR > 0 && (fullUpdateY == actorY || fullCoords[6][1] - fullUpdateY == blockSize)) {
                                fullUpdateX = fullCoords[4][0] - blockSize;
                            } else if (updateL + updateR < 0 && (fullUpdateY == actorY || fullCoords[6][1] - fullUpdateY == blockSize)) {
                                fullUpdateX = fullCoords[3][0] + blockSize;
                            }
                        }
                    } else {
                        PApplet.println("Collide niet met x");
                    }

                } else {
                    touchedDeadly = true;
                }
            }

        }

        customHorizontaleCollision = horizontaleCollision;

        actorX = fullUpdateX;
        actorY = fullUpdateY;

        if(actorY > 645){
            touchedDeadly = true;
        }

        updateR = 0;
        updateL = 0;
        updateU = 0;
        updateD = 0;
    }

    protected boolean getTouchedDeadly(){
        if(touchedDeadly){
            touchedDeadly = false;
            return true;
        }else {
            return false;
        }
    }

    protected boolean getIsInAir(){
        return isInAir;
    }

    protected boolean getHorizontaleCollision(){
        return  horizontaleCollision;
    }


    protected boolean getCancelJump(){
        return cancelJump;
    }
}
