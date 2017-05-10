/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toucolor;

import com.sun.scenario.effect.impl.prism.ps.PPSZeroSamplerPeer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.*;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author loren
 */
class Level {

    /**
     * changeable variables:
     * deze variablen hebben te maken met de algeme looks van het programma
     * kunnen achteraf veranderd worden om het spel mooier te maken
     */
    private static int BLOCKWIDTH = 80;
    private static String TILESFILENAME = "demoTiles.csv"; //file met alle namen van afbeelding

    /**
     * PRIVATE VARIABLES
     */
    //applet
    private Toucolor applet;
    //alles voor loadTiles();
    private Block[] tileBlocks; //array met alle afbeeldingen van de tiles
    private String levelFileName; //name of the file which describes the Level
    private int[][] levelMap; //an array which holds the map
    private PImage background;
    private PGraphics level;
    private int length;
    private int number;
    private int rows;
    private int columns;
    private boolean cameraLocked;

    //alles voor einde van level
    boolean levelEnding;
    private static int pixelsBeforeEnd = 400; //moet nog een echte waarde krijgen.

    //alles voor als player doodgaat
    private boolean playerDying; //misschien niet nodig

    //alles voor mango's
    private Mango[] mangos;

    //alles voor tempblocks
    List<TempBlock> tempBlocks;

    //returns an array of mango elements
    Mango[] getMangos() {
        return mangos;
    }

    //not sure if this is used somewhere
    Level(Toucolor applet, int number) {
        this.applet = applet;
        this.levelFileName = "level" + number + ".csv";
        loadTiles();
        loadlevel();
        background = applet.loadImage("Background.jpg");
        this.levelEnding = false;
        this.number = number;
        this.cameraLocked = false;
    }

    /**
     * Laad alle verschillende afbeeldingen en geeft ze een specifiek nummer
     * alle tiles worden uit een csv file gehaald
     * tiles worden opgeslaan in een array van Blocks: zie Block class
     */
    private void loadTiles() {
        //load the table
        Table myTable = applet.loadTable(TILESFILENAME, "header, csv");

        //get number of pictures
        int rowCount = myTable.getRowCount();

        //create the array for the pictures & names
        tileBlocks = new Block[rowCount];
        //get a new row and load the image and name of the tile
        //all print statements are for debugging
        for (int i = 0; i < rowCount; i++) {
            TableRow row = myTable.getRow(i); //get a new row

            int id = row.getInt("id"); //get id of img
            String name = row.getString("name");
            String imgFileName = row.getString("filename");
            boolean collision = PApplet.parseBoolean(row.getString("collision"));
            boolean brokkelt = PApplet.parseBoolean(row.getString("brokkelt"));
            boolean kills = PApplet.parseBoolean(row.getString("death"));

            if(!brokkelt) {
                tileBlocks[id] = new Block(id, name, imgFileName, collision, kills, level, applet);
            }
            else {
                tileBlocks[id] = new Block(id, name, "Background.jpg", collision, kills, level, applet); //load the img into the array
            }
        }
    }

    /**
     * Laad de csv file die de map van het Level beschrijft
     * slaat het Level op in een array
     */
    private void loadlevel() {
        //load the file which holds the Level
        Table myTable = applet.loadTable(levelFileName, "csv");

        //get number of rows & columns in the Level
        this.rows = myTable.getRowCount();
        this.columns = myTable.getColumnCount();

        //this var is used when determining if the level is ending
        this.length = columns * this.BLOCKWIDTH;

        //create the array for the Level
        levelMap = new int[columns][rows];

        //loop through the rows
        for (int i = 0; i < rows; i++) {
            TableRow row = myTable.getRow(i);//get next row
            //loop through the columns
            for(int c = 0; c < columns; c++) {
                levelMap[c][i] = row.getInt(c);//map the int to right position in array
            }
        }

        int levelWidth = columns * Toucolor.BLOCKSIZE;
        int levelHeight = rows * Toucolor.BLOCKSIZE;
        level = applet.createGraphics(levelWidth, levelHeight);
        level.beginDraw();
        //we draw the level here
        //render backgorund
        level.imageMode(PConstants.CORNER);
        level.background(48,169,226);

        //now draw all the blocks
        level.rectMode(PConstants.CORNER);

        //list to add mangos
        List<Mango> listMangos = new ArrayList<Mango>();
        tempBlocks = new ArrayList<TempBlock>();
        for (int i = 0; i < columns; i++) {
            for (int u = 0; u < rows; u++) {
                Block currentBlock = tileBlocks[levelMap[i][u]];
                if(currentBlock.getName().equals("Tijdelijke blok")) {
                    tempBlocks.add(new TempBlock(i * 80, u * 80, 5000, currentBlock.killsPlayer, applet));
                }
                if(currentBlock.drawBlock()) {
                    level.image(currentBlock.renderblock(),(i *80), u * 80, Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE);
                    if(currentBlock.getName().equals("Mango")) {
                        listMangos.add(new Mango(i * 80, u * 80));
                    }
                }
            }
        }
        level.endDraw();

        mangos = new Mango[listMangos.size()];
        listMangos.toArray(mangos);

        PApplet.print( this.levelFileName + " has been loaded.\n");
    }

    /**
     * renders the Level on screen based on the players location
     *
     * @param playerX x-coordinate of player
     */
    void renderLevel(int playerX, int playerY) {

        //render map
        int drawX = 0;
        if(!cameraLocked) {
            if(tileBlocks[levelMap[(playerX / BLOCKWIDTH) + 1][2]].getName().equals("Test")) {
                this.levelEnding = true;
            }
            drawX = -((((playerX - 600) < 0) ? 0 : (playerX - 600)));
        }

        applet.imageMode(PConstants.CORNER);
        applet.image(level, drawX, 0);

        //render each tempblock that in on the screen
        for(TempBlock block : tempBlocks) {
            if(drawX < block.getBlockX() && block.getBlockX() < -drawX + Toucolor.WORLDWIDTH && block.drawBlock) {
                //calc where to draw  the block
                int drawBlockX = block.getBlockX() + drawX;
                //draw the block
                applet.image(block.renderblock(),drawBlockX, block.getBlockY(), Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE);
            }
            //and to everything that needs to happen when standing on it
            if(PApplet.abs(block.getBlockX() - playerX) < Toucolor.BLOCKSIZE && block.getBlockY() - playerY == Toucolor.BLOCKSIZE) {
                block.standOn();
            }
            //update the block (this does the counting down + changes the drawBlock field
            block.update();
        }
    }

    /**
     * renders a block on a given location
     *
     * @param x location on x-axis to render image
     * @param y location on y-axis to render image
     * @param img image to render
     */
    private void renderBlock(int x, int y, PImage img) {
        applet.image(img, x, y, 80, 80);
    }

    //returns array of the 9 blocks around the player, used in collision
    float[][] getCoords(float playerX, float playerY){
        float[][] coords = new float[8][2];

        if(playerY < Toucolor.BLOCKSIZE) {
            playerY = Toucolor.BLOCKSIZE;
        }


        int startblockX = (PApplet.floor(playerX / Toucolor.BLOCKSIZE)) - 1;
        int startblockY = (PApplet.floor(playerY / Toucolor.BLOCKSIZE)) - 1;

        coords[0][0] = startblockX * BLOCKWIDTH;
        coords[1][0] = (startblockX+1) * BLOCKWIDTH;
        coords[2][0] = (startblockX+2) * BLOCKWIDTH;

        coords[3][0] = startblockX * BLOCKWIDTH;
        coords[4][0] = (startblockX+2) * BLOCKWIDTH;

        coords[5][0] = startblockX * BLOCKWIDTH;
        coords[6][0] = (startblockX+1) * BLOCKWIDTH;
        coords[7][0] = (startblockX+2) * BLOCKWIDTH;

        coords[0][1] = startblockY * BLOCKWIDTH;
        coords[1][1] = startblockY * BLOCKWIDTH;
        coords[2][1] = startblockY * BLOCKWIDTH;
        coords[3][1] = (startblockY+1) * BLOCKWIDTH;
        coords[4][1] = (startblockY+1) * BLOCKWIDTH;
        coords[5][1] = (startblockY+2) * BLOCKWIDTH;
        coords[6][1] = (startblockY+2) * BLOCKWIDTH;
        coords[7][1] = (startblockY+2) * BLOCKWIDTH;

        return coords;
    }
    //is used with getCoords, gives the properties of the blocks from coords
    boolean[][] getColAndDeath(int playerX, int playerY) {
        boolean[][] bools = new boolean[8][2];

        if(playerX < BLOCKWIDTH){
            playerX = BLOCKWIDTH;
        }
        if(playerY < Toucolor.BLOCKSIZE) {
            playerY = Toucolor.BLOCKSIZE;
        }

        int startblockX = (playerX / Toucolor.BLOCKSIZE) - 1;
        int startblockY = (playerY / Toucolor.BLOCKSIZE) - 1;

        if(startblockY+3 > this.rows){
            startblockY = rows - 3;
        }
        if(startblockX < 0){
            startblockX = 0;
        }



        bools[0][0] = tileBlocks[levelMap[startblockX][startblockY]].isCollision();
        bools[1][0] = tileBlocks[levelMap[startblockX+1][startblockY]].isCollision();
        bools[2][0] = tileBlocks[levelMap[startblockX+2][startblockY]].isCollision();
        bools[3][0] = tileBlocks[levelMap[startblockX][startblockY+1]].isCollision();
        bools[4][0] = tileBlocks[levelMap[startblockX+2][startblockY+1]].isCollision();
        bools[5][0] = tileBlocks[levelMap[startblockX][startblockY+2]].isCollision();
        bools[6][0] = tileBlocks[levelMap[startblockX+1][startblockY+2]].isCollision();
        bools[7][0] = tileBlocks[levelMap[startblockX+2][startblockY+2]].isCollision();

        bools[0][1] = tileBlocks[levelMap[startblockX][startblockY]].killsPlayer();
        bools[1][1] = tileBlocks[levelMap[startblockX+1][startblockY]].killsPlayer();
        bools[2][1] = tileBlocks[levelMap[startblockX+2][startblockY]].killsPlayer();
        bools[3][1] = tileBlocks[levelMap[startblockX][startblockY+1]].killsPlayer();
        bools[4][1] = tileBlocks[levelMap[startblockX+2][startblockY+1]].killsPlayer();
        bools[5][1] = tileBlocks[levelMap[startblockX][startblockY+2]].killsPlayer();
        bools[6][1] = tileBlocks[levelMap[startblockX+1][startblockY+2]].killsPlayer();
        bools[7][1] = tileBlocks[levelMap[startblockX+2][startblockY+2]].killsPlayer();

        return  bools;
    }

    //getter for levelEnding
    boolean isLevelEnding() {
        return this.levelEnding;
    }

    //getter for the number of the level that is being played
    int numberOfcurrentLevel() {
        return this.number;
    }

    //used to lock the camera (is used in boss level)
    void setCameraLocked(boolean locked) {
        this.cameraLocked = locked;
    }

    boolean isCameraLocked() {
        return this.cameraLocked;
    }

    //changes one block in the map
    void changeBlock(int x, int y, int newBlock, boolean playerCoords) {
        int drawX, drawY;
        level.beginDraw();
        if(playerCoords) {
            drawX = x / Toucolor.BLOCKSIZE;
            drawY = y / Toucolor.BLOCKSIZE;
        }
        else {
            drawX = x;
            drawY = y;
        }
        level.image(tileBlocks[newBlock].renderblock(), drawX * Toucolor.BLOCKSIZE, drawY * Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE);
        level.endDraw();
        levelMap[drawX][drawY] = newBlock;
        if(newBlock == 4) {
            tempBlocks.add(new TempBlock(drawX * Toucolor.BLOCKSIZE, drawY * Toucolor.BLOCKSIZE, 5000, false, applet));
        }
    }

    void addTempBlock(int x, int y, int newBlock, boolean playerCoords, int timeTillGone) {
        int drawX, drawY;
        level.beginDraw();
        if(playerCoords) {
            drawX = x / Toucolor.BLOCKSIZE;
            drawY = y / Toucolor.BLOCKSIZE;
        }
        else {
            drawX = x;
            drawY = y;
        }
        level.image(tileBlocks[newBlock].renderblock(), drawX * Toucolor.BLOCKSIZE, drawY * Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE);
        level.endDraw();
        levelMap[drawX][drawY] = newBlock;
        if(newBlock == 4) {
            tempBlocks.add(new TempBlock(drawX * Toucolor.BLOCKSIZE, drawY * Toucolor.BLOCKSIZE, timeTillGone, false, applet));
        }
    }
}
