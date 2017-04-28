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
    private int moveSpeed = 5;
    private float iceSpeed = 0;
    public boolean isInAir = true;
    private float hoek = 0;
    public boolean upIsPressed = false;
    float jumpSpeed = 9;
    float valSpeed = 8;
    public boolean rightPressed;
    public boolean leftPressed;
    public boolean downPressed;
    int cProfile = 1;

    //Voor collision
    private boolean jumping = false;

    public int imgCounter = 0;
    public char lastMove = 'n';

    float PI = PApplet.PI;

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
                //isInAir = true;
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

        for (int i = 0; i< fullCoords.length; i++) {
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];

            if(canCollide){
                if(PApplet.abs(fullUpdateX - xblock) < blockSize && PApplet.abs(playerY - yblock) < blockSize){
                    //enkel de x-beweging zal colliden
                    fullUpdateX = playerX;
                }
                if(PApplet.abs(fullUpdateY - yblock)< blockSize && PApplet.abs(playerX - xblock)< blockSize){
                    //enkel y-beweging zal colliden
                    if(fullUpdateY - yblock < 0){
                        isInAir = false;
                        fullUpdateY = yblock - blockSize;
                    }else if(fullUpdateY - yblock > 0){
                        hoek = PApplet.PI / 2;
                        fullUpdateY = yblock + blockSize;
                    }
                }
            }

        }

        playerX = fullUpdateX;
        playerY = fullUpdateY;

        if(playerY > 730){
            playerY = 300;
            playerX = playerX - 3*blockSize;
        }

        updateR = 0;
        updateL = 0;
        updateU = 0;
        updateD = 0;

        if(hoek == 0){
            PApplet.println(hoek);
        }
    }

    public void duck(){

    }

    public void playerDie(){


    }


}

