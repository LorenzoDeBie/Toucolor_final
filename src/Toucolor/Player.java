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

    private boolean horizontaleCollision = false, verticaleCollision = false;

    //Voor collision
    private boolean jumping = false;

    public int imgCounter = 0;
    public char lastMove = 'n';

    public boolean dying = false;

    float PI = PApplet.PI;

    float updateL, updateR, updateU, updateD, fullUpdateX, fullUpdateY;
    boolean mR = false;
    boolean mL = false;
    boolean mD = false;
    boolean mU = false;

    //end animation vars
    private Toucolor applet;
    private boolean customHorizontaleCollision;

    Player(Toucolor applet) {
        this.applet = applet;
    }

    void refreshValues(float[][] coords, boolean[][] props) {
        this.fullCoords = coords;
        this.propterties = props;
    }


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
                hoek += 0.0005;
            } else {
                jumping = false;
                upIsPressed = false;
            }
        }
        collision(updateR, updateL, updateU, updateD);
    }

    public boolean isHorizontaleCollision() {
        return this.customHorizontaleCollision;
    }

    private void collision(float r, float l, float u, float d) {
        updateR = r;
        updateL = l;
        updateU = u;
        updateD = d;

        fullUpdateX = playerX + (updateL + updateR);
        fullUpdateY = playerY + (updateD + updateU);

        for (int i = 0; i< fullCoords.length; i++) {
            //PApplet.println(i);
            xblock = fullCoords[i][0];
            yblock = fullCoords[i][1];
            canCollide = propterties[i][0];
            isDeadly = propterties[i][1];

            //if(canCollide){
            if(canCollide && PApplet.abs(fullUpdateX - xblock) < blockSize && PApplet.abs(playerY - yblock) < blockSize){
                //enkel de x-beweging zal colliden
                horizontaleCollision = true;
                fullUpdateX = playerX;
                if(isDeadly){
                    playerIsDead = true;
                    playerDie();
                }
            }
            if(canCollide && PApplet.abs(fullUpdateY - yblock)< blockSize && PApplet.abs(playerX - xblock)< blockSize){
                //enkel y-beweging zal colliden
                if(fullUpdateY - yblock < 0){
                    verticaleCollision = true;
                    fullUpdateY = yblock - blockSize;
                    isInAir = false;
                    if(isDeadly){
                        playerIsDead = true;
                        playerDie();
                    }

                }else if(fullUpdateY - yblock > 0){
                    verticaleCollision = true;
                    isInAir = false;
                    if(jumping){
                        fullUpdateY = yblock + blockSize;
                    }
                    if(isDeadly){
                        playerIsDead = true;
                        playerDie();
                    }
                }
            }

        }
        //PApplet.println(playerX, playerY);

        customHorizontaleCollision = horizontaleCollision;

        verticaleCollision = false;
        horizontaleCollision = false;


        playerX = fullUpdateX;
        playerY = fullUpdateY;

        //is dit test code?
        if(playerY > 730){
            playerIsDead = true;
            playerDie();
        }

        updateR = 0;
        updateL = 0;
        updateU = 0;
        updateD = 0;

        if(hoek == 0){
            PApplet.println(hoek);
        }
    }

    private void duck(){

    }

    private boolean playerDie(){
        if(playerIsDead) {
            return true;
        } else {
            return false;
        }
    }




}

