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


/**
 *
 * @author loren
 */
public class Level {

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
    private PApplet applet;
    //alles voor loadTiles();
    private Block[] tileBlocks; //array met alle afbeeldingen van de tiles
    private String levelFileName; //name of the file which describes the Level
    private int[][] levelMap; //an array which holds the map
    private PImage background;
    private PGraphics level;

    //not sure if this is used somewhere
    Level(PApplet applet, String levelFileName) {
        this.applet = applet;
        this.levelFileName = levelFileName;
        loadTiles();
        loadlevel();
        background = applet.loadImage("Background.jpg");
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

//        //debug info
//        PApplet.print("rowcount= " + rowCount + "\n");

        //create the array for the pictures & names
        tileBlocks = new Block[rowCount];
        //get a new row and load the image and name of the tile
        //all print statements are for debugging
        for (int i = 0; i < rowCount; i++) {
            TableRow row = myTable.getRow(i); //get a new row

//            //debug info
//            PApplet.print("row loaded\n");
//            PApplet.print("id = " + row.getInt("id") + "\n");

            int id = row.getInt("id"); //get id of img
            String name = row.getString("name");
            String imgFileName = row.getString("filename");
            boolean collision = PApplet.parseBoolean(row.getString("collision"));
            boolean brokkelt = PApplet.parseBoolean(row.getString("brokkelt"));
            boolean kills = PApplet.parseBoolean(row.getString("death"));

            tileBlocks[id] = new Block(id, name, imgFileName, collision, brokkelt, kills, level, applet); //load the img into the array

//            //debug info
//            PApplet.print("img loaded:" + row.getString("filename") + "\n");
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
        int rowCount = myTable.getRowCount();
        int columnCount = myTable.getColumnCount();

        //create the array for the Level
        levelMap = new int[columnCount][rowCount];

        //loop through the rows
        for (int i = 0; i < rowCount; i++) {
            TableRow row = myTable.getRow(i);//get next row
            //loop through the columns
            for(int c = 0; c < columnCount; c++) {
                levelMap[c][i] = row.getInt(c);//map the int to right position in array
                //PApplet.print(i +" " + c + "\n");
            }
        }

        int levelWidth = columnCount * Toucolor.BLOCKSIZE;
        int levelHeight = rowCount * Toucolor.BLOCKSIZE;
        level = applet.createGraphics(levelWidth, levelHeight);
        level.beginDraw();
        //we draw the level here
            //render backgorund
            level.imageMode(PConstants.CORNER);
            level.background(255);

            //now draw all the blocks
            level.rectMode(PConstants.CORNER);
            for (int i = 0; i < columnCount; i++) {
                for (int u = 0; u < 9; u++) {
                    Block currentBlock = tileBlocks[levelMap[i][u]];
                    if(currentBlock.drawBlock()) {
                        level.image(currentBlock.renderblock(),(i *80), u * 80, Toucolor.BLOCKSIZE, Toucolor.BLOCKSIZE);
                    }
                }
            }

        level.endDraw();


        PApplet.print( this.levelFileName + " has been loaded.\n");
    }

    /**
     * renders the Level on screen based on the players location
     * todo: decide how to render Level --> see descriptoin
     *
     * @param playerX x-coordinate of player
     */
    void renderLevel(int playerX) {
        int drawX = -((((playerX - 600) < 0) ? 0 : (playerX - 600)));
        applet.imageMode(PConstants.CORNER);
        applet.image(level, drawX, 0);


        //kijker
//        applet.rectMode(PConstants.CORNER);
//        applet.stroke(0,0,0);
//        applet.fill(0,0,0);
//        applet.rect(0, 0, applet.width /13, applet.height);
//        applet.rect(applet.width - applet.width /13, 0, applet.width /13, applet.height);

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
    float[][] getCoords(int playerX, int playerY){
            float[][] coords = new float[8][2];

            int startblockX = (playerX / Toucolor.BLOCKSIZE) - 1;
            int startblockY = (playerY / Toucolor.BLOCKSIZE) - 1;

            for(int i = 0; i < 3; i++) {
                coords[i][0] = (startblockX + i) * Toucolor.BLOCKSIZE;
                coords[i][1] = startblockY * Toucolor.BLOCKSIZE;
            }

            coords[3][0] = startblockX * Toucolor.BLOCKSIZE;
            coords[3][1] = (startblockY + 1) * Toucolor.BLOCKSIZE;

            coords[4][0] = (startblockX + 2) * Toucolor.BLOCKSIZE;
            coords[4][1] = (startblockY + 1) * Toucolor.BLOCKSIZE;

            for(int i = 0; i < 3; i++) {
                coords[i + 5][0] = (startblockX + i) * Toucolor.BLOCKSIZE;
                coords[i + 5][1] = (startblockY + 2) * Toucolor.BLOCKSIZE;
            }

            return coords;
    }

    boolean[][] getColAndDeath(int playerX, int playerY) {
        boolean[][] bools = new boolean[8][2];

        int startblockX = (playerX / Toucolor.BLOCKSIZE) - 1;
        int startblockY = (playerY / Toucolor.BLOCKSIZE) - 1;

        for(int i = 0; i < 3; i++) {
            int blockX = (startblockX + i);
            int blockY = (startblockY) > 8 ? 8 : (startblockY);
            Block currentBlock = tileBlocks[levelMap[blockX][blockY]];

            bools[i][0] = currentBlock.isCollision();
            bools[i][1] = currentBlock.killsPlayer();
        }

        int rowY = (startblockY + 2) > 8 ? 8 : (startblockY + 2);
        bools[3][0] = tileBlocks[levelMap[startblockX][rowY]].isCollision();
        bools[3][1] = tileBlocks[levelMap[startblockX][rowY]].killsPlayer();

        rowY = (startblockY + 2) > 8 ? 8 : (startblockY + 2);
        bools[3][0] = tileBlocks[levelMap[startblockX + 2][rowY]].isCollision();
        bools[3][1] = tileBlocks[levelMap[startblockX + 2][rowY]].killsPlayer();

        for(int i = 0; i < 3; i++) {
            int blockX = (startblockX + i);
            int blockY = (startblockY) > 8 ? 8 : (startblockY);
            Block currentBlock = tileBlocks[levelMap[blockX][blockY]];

            bools[i + 5][0] = currentBlock.isCollision();
            bools[i + 5][1] = currentBlock.killsPlayer();
        }

        return  bools;
    }

    /**
     * test function
     * @param x qsdf
     * @param y qsdf
     * @param width qdsf
     * @param height qsdf
     * @param applet  Applet to draw on
     *
     */
    public void drawRect(int x, int y, int width, int height, PApplet applet) {
        //rect(x, y, width, height);
        applet.rect(10,10,10,10);
    }
}
