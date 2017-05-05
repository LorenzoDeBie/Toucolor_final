package Toucolor;

import processing.core.*;

/**
 * Created by Vince on 4/3/2017.
 */
public class Enemy extends Actor {
    //Standaard vars voor een enemie
    private int bereik = 1;
    public float posX = 0;
    public float posY = 0;
    private float hoek = 0;
    private float moveSnelh = 0.01f;
    private int moveP;


    Enemy(int movePath, int range, float moveSpeed, float spawnPosX, float spawnPosY){
        moveP = movePath;
        bereik = range;
        moveSnelh = moveSpeed;

        //Uiteindelijke positie van de speler
        posX += spawnPosX;
        posY += spawnPosY;

    }


    private void Move(){
        //Bewegen van enemie volgens verschillende beweegmodussen
        if(moveP == 1){ //Links-rechts
            if(PApplet.cos(hoek) > 0){
                posX = posX + bereik;
                hoek += moveSnelh;
            }else{
                posX = posX - bereik;
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
        else if(moveP == 2){ //Op-neer
            if(PApplet.cos(hoek) > 0){
                posY = posY - bereik;
                hoek += moveSnelh;
            }else{
                posY = posY + bereik;
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
        else if(moveP == 3){ //Cirkel
            if(PApplet.cos(hoek) > 0){
                posX = posX + bereik*PApplet.cos(hoek);
                posY = posY - bereik*PApplet.sin(hoek);
                hoek += moveSnelh;
            }else{
                posX = posX + bereik*PApplet.cos(hoek);
                posY = posY - bereik*PApplet.sin(hoek);
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
    }

    private boolean checkPlayer(float X, float Y){
        if((PApplet.abs(X - posX)<80)&& (PApplet.abs(Y - posY)<80)){
            return true;
        }
        return false;
    }

    public boolean EnemyBehave(float spelerX, float spelerY){
        Move();
        return checkPlayer(spelerX,spelerY);
    }


}

