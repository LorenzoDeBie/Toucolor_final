package Toucolor;

import processing.core.PApplet;

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

    float actorX = 156, actorY=300, xblock, yblock;
    boolean canCollide, isDeadly, horizontaleCollision, verticaleCollision, isInAir, customHorizontaleCollision, touchedDeadly, jumping, cancelJump;
    int blockSize = Toucolor.BLOCKSIZE;



    void refreshValues(float[][] coords, boolean[][] props) {
        this.fullCoords = coords;
        this.propterties = props;
    }

    protected void collision(float r, float l, float u, float d, boolean inDeLucht, boolean springt) {
        updateR = r;
        updateL = l;
        updateU = u;
        updateD = d;
        isInAir = inDeLucht;
        jumping = springt;

        horizontaleCollision = false;
        verticaleCollision = false;
        cancelJump = false;

        fullUpdateX = actorX + (updateL + updateR);
        fullUpdateY = actorY + (updateD + updateU);

        if(fullUpdateX < 0){
            fullUpdateX = 0;
        }

        for (int i = 0; i< fullCoords.length; i++) {
            //PApplet.println(i);
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];

            //if(canCollide){
            if(canCollide && PApplet.abs(fullUpdateX - xblock) < blockSize && PApplet.abs(actorY - yblock) < blockSize){
                //enkel de x-beweging zal colliden
                horizontaleCollision = true;
                fullUpdateX = actorX;
                if(isDeadly){
                    touchedDeadly = true;
                }
            }
            if(canCollide && PApplet.abs(fullUpdateY - yblock)< blockSize && PApplet.abs(actorX - xblock)< blockSize){
                //enkel y-beweging zal colliden
                if(fullUpdateY - yblock < 0){
                    fullUpdateY = yblock - blockSize;
                    isInAir = false;
                    if(isDeadly){
                        touchedDeadly = true;

                    }

                }else if(fullUpdateY - yblock > 0){
                    verticaleCollision = true;
                    if(jumping){
                        fullUpdateY = yblock + blockSize;
                        cancelJump = true;
                    }
                    if(isDeadly){
                        touchedDeadly = true;
                    }
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
