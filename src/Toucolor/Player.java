package Toucolor;

import processing.core.*;

/**
 * Created by Vince on 4/1/2017.
 */
public class Player {

    //Wereld variabelen die gebruikt worden
    private int blockSize = 80;
    private int sizeY = 720;

    private float[][] fullCoords; //Hier moeten alle coords komen die gecheckt moeten worden
    private boolean [][] propterties;


    //Voor collision
    private float xblock;
    private float yblock;
    private boolean isDeadly;
    private boolean canCollide;
    //Startpos speler
    float playerX = 900;
    float playerY = 100;

    //Alle stuff voor keyuse, snelheid enz
    int moveSpeed = 5;
    float iceSpeed = 0;
    boolean isInAir = true;
    float hoek = 0;
    boolean upIsPressed = false;
    float jumpSpeed = 10;
    float valSpeed = 8;
    boolean rightPressed;
    boolean leftPressed;
    boolean downPressed;
    int moveState = 0;
    int cProfile = 1;

    //Voor collision
    private boolean collide;
    private int colvar;


    public int imgCounter = 0;
    public char lastMove = 'n';

    float PI = PApplet.PI;

    public void refreshValues(float[][] coords, boolean[][] props) {
        this.fullCoords = coords;
        this.propterties = props;
    }



    public void keyUse() {
        if(rightPressed) {
            moveState = 1;
            imgCounter++;
            colvar = 1;
            moveRight();
        }
        if(leftPressed) {
            moveState = 1;
            imgCounter++;
            colvar = -1;
            moveLeft();
        }
        if(downPressed){
            moveState = 2;
            duck();
        }
        if (upIsPressed) {
            moveState = 3;
            jump();
        }
        if (isInAir) {
            moveState = 4;
            val();
        }
    }

    public void val() {
        if(!collision(moveState)) {
            if (hoek < PI / 2) {
                playerY = playerY + PApplet.sin(hoek) * valSpeed;
                hoek += 0.03;
            } else {
                playerY = playerY + valSpeed;
            }
        }
        else{
            isInAir = false;
            hoek = 0;
        }
    }

    public void moveLeft(){
        if(!collision(moveState)){
            playerX = playerX - (moveSpeed * cProfile);
            imgCounter++;
            lastMove = 'l';
        }
    }

    public void moveRight(){
        if(!collision(moveState)){
            playerX = playerX + (moveSpeed * cProfile);
            imgCounter++;
            lastMove = 'r';
        }
    }

    public void duck(){}

    public void jump(){
        if(!collision(moveState)) {
            if (hoek < PI / 2) {
                playerY = playerY - PApplet.cos(hoek) * jumpSpeed;
                hoek += 0.07;
            } else {
                isInAir = true;
                hoek = 0;
                upIsPressed = false;
                val();
            }
        }
    }

    public boolean collision(int status){
        collide = false;
        for (int i = 0; i< fullCoords.length; i++) {
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];
            switch (status) {
                case 1: //links en rechts
                    if(canCollide) {
                        float cst = playerX + (moveSpeed * cProfile) * colvar;
                        if ((PApplet.abs(cst - xblock) < blockSize) && (PApplet.abs(playerY - yblock) < blockSize)) {
                            playerX = xblock - blockSize * colvar;
                            if (isDeadly) {
                                PApplet.print("DEUD");
                            } else {
                                collide = true;
                            }
                        } else if (!upIsPressed) {
                            isInAir = true;
                        }
                    }
                    break;

                case 2://duck
                    break;

                case 3: //jump
                    float YJ = playerY - PApplet.cos(hoek) * jumpSpeed;
                    if ((playerY - PApplet.cos(hoek) * jumpSpeed) < 1) {
                        isInAir = true;
                        upIsPressed = false;
                        hoek = 0;
                        playerY = 1;
                    } else if ((PApplet.abs(playerX - xblock) < blockSize) && (PApplet.abs(YJ - yblock) < blockSize) && canCollide) {
                        playerY = yblock + blockSize;
                        hoek = 0;
                        upIsPressed = false;
                        isInAir = true;
                        if(isDeadly){
                            PApplet.print("DEUD");
                        }else {
                            collide = true;
                        }
                    }
                    break;

                case 4: //val
                    float YV = playerY + PApplet.sin(hoek) * valSpeed;
                    //if ((playerY + PApplet.sin(hoek) * valSpeed) > sizeY - blockSize) {
                       // isInAir = false;
                       // hoek = 0;
                     //   playerY = sizeY - blockSize;
                   // } else
                        if ((PApplet.abs(playerX - xblock) < blockSize) && (PApplet.abs(YV - yblock) < blockSize) && canCollide) {
                        playerY = yblock - blockSize;
                        if(isDeadly){
                            PApplet.print("DEUD");
                        }else {
                            collide = true;
                        }
                    }
                    break;

                default: //niets doen
                    break;
            }
        }

        return collide;
    }

    public void playerDie(){


    }


}

