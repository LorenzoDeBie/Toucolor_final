package Toucolor;

import processing.core.*;

import static javafx.application.Platform.exit;

/**
 * Created by Vince on 4/1/2017.
 */
 class Player extends Actor {
    //Alle stuff voor keyuse, snelheid enz
    private int moveSpeed = 6;
    private float iceSpeed = 0;
    public boolean isInAir = true;
    private float hoek = 0;
    public boolean upIsPressed = false;
    float jumpSpeed = 10;
    float valSpeed = 8;
    public boolean rightPressed;
    public boolean leftPressed;
    public boolean downPressed;
    int cProfile = 1;
    public boolean playerIsDead = false;

    //Voor collision
    private boolean jumping = false;

    public int imgCounter = 0;
    public char lastMove = 'n';

    float PI = PApplet.PI;

    void keyUse() {
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
            mD = true;
        } else {
            if (jumping) {
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
        if (mR == true) {
            updateR = (moveSpeed * cProfile);
            lastMove = 'r';
        }
        if (mL == true) {
            updateL = -(moveSpeed * cProfile);
            lastMove = 'l';
        }
        if (mD == true) {
            if (hoek < PI / 2) {
                updateD = PApplet.sin(hoek) * valSpeed;
                hoek += 0.03;
                //isInAir = true;
            } else {
                updateD = valSpeed;
                isInAir = true;
            }
        }
        if (mU == true) {

            if (hoek < PI / 2) {
                updateU = -PApplet.cos(hoek) * jumpSpeed;
                hoek += 0.0005;
            } else {
                jumping = false;
                upIsPressed = false;
            }
        }
        collision(updateR, updateL, updateU, updateD, isInAir, jumping);
    }

    public boolean isHorizontaleCollision() {
        return this.customHorizontaleCollision;
    }


    private boolean playerDie(){
        if(playerIsDead) {
            return true;
        } else {
            return false;
        }
    }




}

