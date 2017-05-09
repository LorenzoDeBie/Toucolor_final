package Toucolor;

import processing.core.*;

/**
 * Created by Vince on 4/1/2017.
 */
 class Player extends Actor {
    //Alle stuff voor keyuse, snelheid enz

    private float iceSpeed = 0;
    private boolean isInAir = true;
    private float hoek = 0;
    boolean upIsPressed = false;
    boolean rightPressed;
    boolean leftPressed;
    boolean downPressed;
    boolean playerIsDead = false;

    //Voor collision
    private boolean jumping = false;

    int imgCounter = 0;
    char lastMove = 'n';

    void keyUse() {
        if(getCancelJump()){
            isInAir = true;
            mU = false;
            jumping = false;
            upIsPressed = false;
            hoek = 0;
        }
        if (rightPressed) {
            imgCounter++;
            mR = true;
        } else {
            mR = false;
        }
        if (leftPressed) {
            imgCounter++;
            mL = true;
        } else {
            mL = false;
        }
        if (getIsInAir()) {
            upIsPressed = false;
            mD = true;
        } else {
            if (jumping) {
                upIsPressed = false;
                mU = true;
            } else {
                if (upIsPressed) {
                    jumping = true;
                    hoek = 0;
                    mU = true;
                }
            }
        }
        updateMove();
    }

    private void updateMove() {
        int moveSpeed = 6;
        int cProfile = 1;
        if (mR) {
            updateR = (moveSpeed * cProfile);
            lastMove = 'r';
        }
        if (mL) {
            updateL = -(moveSpeed * cProfile);
            lastMove = 'l';
        }
        float PI = PApplet.PI;
        if (mD) {
            float valSpeed = 8;
            if (hoek < PI / 2) {
                updateD = PApplet.sin(hoek) * valSpeed;
                hoek += 0.03;
                isInAir = true;
            } else {
                updateD = valSpeed;
                isInAir = true;
                jumping = false;
                upIsPressed = false;
            }
        }
        if (mU) {
            if (hoek < PI / 2) {
                float jumpSpeed = 10;
                updateU = -PApplet.cos(hoek) * jumpSpeed;
                hoek += 0.0005;
            } else {
                jumping = false;
                upIsPressed = false;
            }
        }
        collision(updateR, updateL, updateU, updateD, isInAir, jumping, upIsPressed);
    }

    boolean isHorizontaleCollision() {
        return this.customHorizontaleCollision;
    }

    public void setUp(){
        upIsPressed = false;
        jumping = false;
    }

    public void setRight(){
        rightPressed = false;
    }

    public void setLeft(){
        leftPressed = false;
    }



    boolean playerDie(){
        return playerIsDead || getTouchedDeadly();
    }




}

