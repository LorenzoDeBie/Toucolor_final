package Toucolor;

import processing.core.*;

import static javafx.application.Platform.exit;

/**
 * Created by Vince on 4/1/2017.
 */
public class Player {

    //Wereld variabelen die gebruikt worden
    private int blockSize = 80;
    private int sizeY = 720;

    private float[][] fullCoords; //Hier moeten alle coords komen die gecheckt moeten worden
    private boolean[][] propterties;


    //Voor collision
    private float xblock;
    private float yblock;
    private boolean isDeadly;
    private boolean canCollide;
    //Startpos speler
    float playerX = 500;
    float playerY = 200;

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
    int cProfile = 1;

    //Voor collision
    private boolean collideX, collideY;
    private int colvar = 1;
    private int jumpvar = 1;
    private boolean jumping = false;
    int status;

    public int imgCounter = 0;
    public char lastMove = 'n';

    float PI = PApplet.PI;

    float lastUpdX = playerX;
    float lastUpdY = playerY;
    float updateL, updateR, updateU, updateD, fullUpdateX, fullUpdateY;
    boolean mR = false;
    boolean mL = false;
    boolean mD = false;
    boolean mU = false;


    public void refreshValues(float[][] coords, boolean[][] props) {
        this.fullCoords = coords;
        this.propterties = props;
    }


    public void keyUse() {
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
        if (downPressed) {
            duck();
        }
        if (isInAir) {
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
                isInAir = true;
            } else {
                updateD = valSpeed;
                isInAir = true;
            }
        }
        if (mU == true) {
            if (hoek < PI / 2) {
                updateU = -PApplet.cos(hoek) * jumpSpeed;
                hoek += 0.01;
            } else {
                jumping = false;
                upIsPressed = false;
            }
        }
        collision(updateR, updateL, updateU, updateD);
    }


    public void collision(float r, float l, float u, float d) {
        updateR = r;
        updateL = l;
        updateU = u;
        updateD = d;


        fullUpdateX = playerX + (updateL + updateR);
        fullUpdateY = playerY + (updateD + updateU);

        if ((fullUpdateX - lastUpdX) < 0) {
            colvar = -1;
        } else {
            colvar = 1;
        }
        if ((fullUpdateY - lastUpdY) < 0) {
            jumpvar = -1;
        } else {
            jumpvar = 1;
        }

        collideX = false;
        collideY = false;

        if (updateD + updateU == 0) {
            if (updateL + updateR == 0) {
                status = 0;
            } else {
                status = 1;
            }
        } else {
            if(updateL + updateR == 0 && updateD + updateU > 0){
                status = 4;
            } else if(updateL + updateR == 0 && updateD + updateU < 0){
                status = 3;
            }else{
                status = 5;
            }
        }

        for (int i = 0; i< fullCoords.length; i++) {
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];

            switch (status) {
                case 0:
                    break;
                case 1: //links en rechts
                    if ((PApplet.abs(fullUpdateX - xblock) < blockSize) && (PApplet.abs(playerY - yblock) < blockSize) && canCollide) {
                        fullUpdateX = xblock - blockSize * colvar;
                        if(!upIsPressed){
                            isInAir = true;
                        }
                        if (isDeadly) {
                            PApplet.print("DEUD");
                        }
                    }
                    break;

                case 3: //jump
                    if ((PApplet.abs(playerX - xblock) < blockSize) && (PApplet.abs(fullUpdateY - yblock) < blockSize) && canCollide) {
                        hoek = 0;
                        upIsPressed = false;
                        //isInAir = true;
                        if (isDeadly) {
                            PApplet.print("DEUD");
                        } else {

                        }
                    }
                    break;

                case 4: //val
                    if ((PApplet.abs(playerX - xblock) < blockSize) && (PApplet.abs(fullUpdateY - yblock) < blockSize) && canCollide && yblock-fullUpdateY > 0) {
                        fullUpdateY = yblock - blockSize;
                        isInAir = false;
                        if (isDeadly) {
                            PApplet.print("DEUD");
                        }
                    }
                    break;
                case 5:

                    break;


                default: //niets doen
                    break;
            }
        }

        PApplet.println(status);


        playerX = fullUpdateX;
        playerY = fullUpdateY;

        lastUpdY = playerY;

        updateR = 0;
        updateL = 0;
        updateU = 0;
        updateD = 0;
        status = 0;
    }













            /*if(updateL + updateR == 0){
            if(updateD + updateU == 0){
                fullUpdateY = playerY;
                fullUpdateX = playerX;
            }else {
                PApplet.println(fullCoords[6][1]+" "+propterties[6][0]);
                PApplet.println(PApplet.abs(fullUpdateY-fullCoords[6][1]));
                if((updateD + updateU) > 0){
                    if(propterties[6][0] && (PApplet.abs(fullUpdateY-fullCoords[6][1])<blockSize)){
                        isInAir = false;
                        fullUpdateY = ((playerY /blockSize)-1)*blockSize;
                    }
                }else{
                    if(propterties[1][0] && (PApplet.abs(fullUpdateY-fullCoords[1][1])<blockSize)){
                        jumping = false;
                        fullUpdateY = fullCoords[6][1] - blockSize;
                    }
                }
            }
        } else {
            if(updateL + updateR > 0){
                if(propterties[4][0] && (PApplet.abs(fullUpdateX-fullCoords[4][0])<blockSize)){
                    fullUpdateX = playerX - 1;
                }
                else if(propterties[6][0] && (PApplet.abs(fullUpdateY - fullCoords[6][1])< blockSize) ){

                }
            }
        }*/

   /* for (int i = 0; i < fullCoords.length; i++) {
        canCollide = propterties[i][0];
        xblock = fullCoords[i][0];
        yblock = fullCoords[i][1];
        isDeadly = propterties[i][1];

        PApplet.println("Speler x: " + playerX + "\tSpeler y: " + playerY+" "+canCollide);
        PApplet.println("[" + i + "] Block x: " + xblock + "\tBlock y: " + yblock);
        PApplet.println(" ");}

        */

            /*if(canCollide && (PApplet.abs(playerX - xblock)<blockSize) && (PApplet.abs(playerY-yblock)<blockSize)){
                PApplet.println("Houston, we have a problem");
            }
            else{
                if(canCollide && (PApplet.abs(fullUpdateX-xblock)<blockSize) && (PApplet.abs(playerY-yblock)< blockSize)){
                    //collision horizontaal met nieuwe update
                    if(canCollide && (PApplet.abs(fullUpdateY-yblock)<blockSize) && (PApplet.abs(playerX-xblock)<blockSize)){
                        //collision horizontaal en verticaal met nieuwe coords
                        fullUpdateX = playerX;
                        fullUpdateY = playerY;
                        break;
                    } else {
                        fullUpdateX = playerX;
                        break;
                    }
                }
                else if(canCollide && (PApplet.abs(fullUpdateY-yblock)<blockSize) && (PApplet.abs(playerX-xblock)< blockSize)){
                    //collision verticaal met nieuwe update
                    if((PApplet.abs(fullUpdateX-xblock)<blockSize) && (PApplet.abs(playerY-yblock)<blockSize)){
                        //collision horizontaal en verticaal met nieuwe coords
                        fullUpdateX = playerX;
                        fullUpdateY = playerY;
                        break;
                    }
                    else{
                        fullUpdateY = playerY;
                        break;
                    }
                }
            }*/







    /*public void val() {
        if (hoek < PI / 2) {
            updateD = playerY + PApplet.sin(hoek) * valSpeed;
            hoek += 0.03;
        } else {
            updateD = playerY + valSpeed;
        }
        isInAir = false;
        hoek = 0;
    }*/

   /* public void moveLeft(){
        updateL = playerX - (moveSpeed * cProfile);
        imgCounter++;
        lastMove = 'l';

    }

    public void moveRight(){
        updateR = playerX + (moveSpeed * cProfile);
        imgCounter++;
        lastMove = 'r';
    }*/

    public void duck(){}

   /* public void jump(){
        if (hoek < PI / 2) {
            updateU = playerY - PApplet.cos(hoek) * jumpSpeed;
            hoek += 0.07;
        } else {
            isInAir = true;
            hoek = 0;
            upIsPressed = false;
        }
    }*/

   /* public boolean collision(int status){
        collide = false;
        for (int i = 0; i< fullCoords.length; i++) {
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];

            switch (status) {
                case 0: break;
                case 1: //links en rechts
                    float cst = playerX + (moveSpeed * cProfile) * colvar;
                    if ((PApplet.abs(cst - xblock) < blockSize) && (PApplet.abs(playerY - yblock) < blockSize) && canCollide) {
                        playerX = xblock - blockSize * colvar;
                        if (isDeadly) {
                            PApplet.print("DEUD");
                        } else {
                            collide = true;
                        }
                    } else if (!upIsPressed) {
                        isInAir = true;
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
                    if ((playerY + PApplet.sin(hoek) * valSpeed) > sizeY - 40) {
                    } else
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
    } */


    public void playerDie(){


    }


}

