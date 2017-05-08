package Toucolor;

import processing.core.*;

/**
 * Created by Vince on 4/3/2017.
 */
class Enemy extends Actor {
    //Standaard vars voor een enemie
    private int bereik = 1;
    private float hoek = 0;
    private float moveSnelh = 0.01f;
    private int moveP;
    private float PI = PApplet.PI;
    float valSpeed = 8;
    private int moveVar = 1;



    Enemy(int movePath, int range, float moveSpeed, float spawnPosX, float spawnPosY){
        moveP = movePath;
        bereik = range;
        moveSnelh = moveSpeed;

        //Uiteindelijke positie van de speler
        actorX = spawnPosX;
        actorY = spawnPosY;

    }


    public void Move(){
        updateR = 0;
        updateL = 0;
        updateD = 0;
        updateU = 0;
        //Bewegen van enemie volgens verschillende beweegmodussen
        if(moveP == 1){ //Links-rechts
            if(PApplet.cos(hoek) > 0){
                updateR = bereik;
                hoek += moveSnelh;
            }else{
                updateL = - bereik;
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
        else if(moveP == 2){ //Op-neer
            if(PApplet.cos(hoek) > 0){
                updateU = - bereik;
                hoek += moveSnelh;
            }else{
                updateD = bereik;
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
        else if(moveP == 3){ //Cirkel
            if(PApplet.cos(hoek) > 0){
                updateR = bereik*PApplet.cos(hoek);
                updateU = - bereik*PApplet.sin(hoek);
                hoek += moveSnelh;
            }else{
                updateL = bereik*PApplet.cos(hoek);
                updateD = - bereik*PApplet.sin(hoek);
                hoek += moveSnelh;
            }
            if(hoek >= 2* PApplet.PI){
                hoek = 0;
            }
        }
        else if(moveP == 4){
            if(getIsInAir()){
                if (hoek < PI / 2) {
                    updateD = PApplet.sin(hoek) * valSpeed;
                    hoek += 0.03;
                    //isInAir = true;
                } else {
                    updateD = valSpeed;
                    isInAir = true;
                }
            }
            if(getHorizontaleCollision()){
                bereik = bereik *-1;
            }
            updateR = bereik;
        }
        collision(updateR,updateL,updateU,updateD,true,false);

    }





}

