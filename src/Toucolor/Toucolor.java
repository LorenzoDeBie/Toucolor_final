/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toucolor;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.Key;

/**
 *
 * @author loren
 */

public class Toucolor extends PApplet {

    /**
     * CONSTRUCTOR
     * Zorgt ervoor dat het een volwaardige java-application wordt
     * dit zou de 'mooie manier zijn om een processing app in netbeans te maken
     * src: http://stackoverflow.com/questions/31845686/making-different-screens-using-processing-in-eclipse
     */
    public static void main(String args[]) {
        PApplet.main(new String[]{"Toucolor.Toucolor"});
    }

    /**
     * PRIVATE VARIABLES
     */
    //width & height of world
    static final int WORLDWIDTH = 1280;
    static final int WORLDHEIGHT = 1024;
    static int BLOCKSIZE = 80;
    //temp actorX --> goes into object later
    private int actorX;
    //array of the world which stores the Level

    //levelmanager functions
    private Level currentLevel;
    private Startscreen menu;
    private ScoreBoard scoreb;
    private String status;
    private int levelToLoad;


    private Animation playerWandelen, enemyWandelen;

    private Enemy poep = new Enemy(4,1,0.5f,100,500);
    private Enemy lel = new Enemy(1,2,0.001f,200,200);
    private Enemy swag = new Enemy(3,2,0.09f,300,400);

    private Player speler;
    private boolean isDead = false;

    private Enemy[] enemies;// =  {poep,lel,swag};

    //initializing variables
    private LoadScreen loadScreen;
    private String[] levelFiles;
    private int numberOfLevels;
    private String[] menuTexts = {"Play", "Score"};

    //endAnimation vars
    private boolean imageHasSwitched;
    private int lastOpacity;


    //score vars
    Score score;
    static final int LEVELTIME = 500;
    static final int VERMINDERING = 1;
    Time timer;

    //sound manager
    Sounds soundManager;

    //mango's
    private Mango[] mangos;
    private static final int MANGOSCORE = 1000;

    int iH = 0;
    String[] arr = {"A","A","A"};

    //score stuff
    private static final String SCOREFILE = "score.csv"
;
    /**
     * initializes the world
     * initializes a Level on startup (will be changed)
     */
    @Override
    public  void setup() {
        frameRate(144);
        status = "initializing";
        loadScreen = new LoadScreen("Initializing, Please wait.", this);
        thread("initWorld");
    }

    /**
     * define and initialize settings for the world
     */

    @Override
    public void settings() {
        size(WORLDWIDTH, WORLDHEIGHT);
    }

    /**
     * HERE HAPPENS THE RENDERING
     */
    @Override
    public void draw() {
        switch (status) {
            case "initializing":
                //show first loading screen
                loadScreen.renderLoadScreen();
                break;
            case "startscreen":
                menu.renderStartScreen();
                menu.renderSelecter();
                break;
            case "scoreboard":
                scoreb.renderScoreboard();
                break;
            case "naamkiezen":
                scoreb.renderNameSelection();
                scoreb.renderSelecter();
                break;
            case "levelSelectScreen":
                menu.renderStartScreen();
                menu.renderSelecter();
                break;
            case "loadScreen":
                loadScreen.renderLoadScreen();
                break;
            case "playing":
                //renders the level (blocks and stuff)
                currentLevel.renderLevel((int) speler.actorX);
                //refreshes all the values for the blocks around the player
                refreshAllValues();
                //dot this if player is dead
                //TODO: will be changed to fade to black and stuff
                if(speler.playerIsDead){
                    speler.playerIsDead = false;
                }
                if (currentLevel.isLevelEnding()) {
                    //if the level is ending, do stuff
                    doEndAnimation();
                }
                else{
                    //if not dead do keypress
                    doEnemies();
                    doPlayer();
                    timer.renderTime();
                    score.renderScore();
                }
                break;
        }
    }

    private void doMango() {
        for(Mango mango : mangos) {
            if (mango.isColliding((int) speler.actorX, (int) speler.actorY) && !mango.isClaimed()) {
                mango.claim();
                currentLevel.changeBlock((int) mango.actorX, (int) mango.actorY, 0, true);
                score.addToScore(MANGOSCORE);
            }
        }
    }

    //handles player movement + rendering
    private void doPlayer() {
        speler.keyUse();
        playerWandelen.display(speler.actorX, speler.actorY, speler.lastMove, speler.imgCounter);
        checkDood();
        doMango();
    }

    private void doEnemies(){
        //Update movement van alle enemies
        if(enemies != null) {
            for (Enemy swag : enemies) {
                swag.Move();
                enemyWandelen.display(swag.actorX, swag.actorY, 'r', 0);
            }
        }
    }

    private void checkDood(){
        if(speler.playerDie()){
            PApplet.println("TIS DEUD");
            isDead =true;
        }
        if(enemies != null) {
            for (Enemy swag : enemies) {
                if (PApplet.abs(swag.actorX - speler.actorX) < BLOCKSIZE && PApplet.abs(swag.actorY - speler.actorY) < BLOCKSIZE) {
                    PApplet.println("TIS DEUD");
                    isDead = true;
                }
            }
        }

        //Display de score aan de speler
        //laat speler naam kiezen en score opslaan
        //Laad oude csv score
        //Vergelijk en pas aan
        //Sla op
        if(isDead) {
            scoreb = new ScoreBoard(SCOREFILE, this);
            status = "naamkiezen";
        }

    }


    //does the animation on the end of a level
    private void doEndAnimation() {
        /**
         * the player keeps going right untill it collides with a block
         * the map will be made so that on the end of the level there will only be one block which collides
         * as soon as he collides we switch images
         * and then the screen slowly fades to black
         */
        //do movement
        speler.rightPressed = true;
        doPlayer();
        //check for collision
        if (speler.isHorizontaleCollision()) {
            //if collides check if images have changed
            if(!imageHasSwitched) {
                //switch images here
                imageHasSwitched = true;
            }
            //slowly fade to black
            if(lastOpacity > 255) {
                //set the loading screen and change status
                loadScreen.setText("Loading next level, please wait.");
                this.levelToLoad = currentLevel.numberOfcurrentLevel() + 1;
                thread("startLevel");
                this.status = "loadScreen";
                return;
            }
            fill(0, this.lastOpacity);
            rect(0, 0, Toucolor.WORLDWIDTH * 2 , Toucolor.WORLDHEIGHT *2);
            lastOpacity+=2;

        }

    }

    /**
     * creates menu screen
     * loads in all the files for the levels, blocks and other info
     * creates player
     *
     */
    public void initWorld() {
        //check if the files for the levels exist
        numberOfLevels = 0;
        boolean fileExists = true;
        //load necessary files
        for (int i = 0; fileExists; i++) {
            File f = new File(sketchPath() + "/data/level" + (i+1) + ".csv");
            if(f.exists() && !f.isDirectory()) {
                numberOfLevels++;
            } else { fileExists = false;}
        }

        levelFiles = new String[numberOfLevels];
        for(int i = 0; i < numberOfLevels; i++) {
            levelFiles[i] = "level" + (i+1) + ".csv";
        }

        //creating menu screen
        menu = new Startscreen( menuTexts, this);

        //create a sound manager
        soundManager = new Sounds(status, this);

        this.status = "startscreen";
    }

    class Animation {
        PImage[] images;
        int imageCount;
        int frame;

        Animation(String imagePrefix, int count) {
            imageCount = count;
            images = new PImage[imageCount];
            for (int i = 0; i < imageCount; i++) {
                String filename = imagePrefix + nf(i, 1) + ".png";
                images[i] = loadImage(filename);
            }
        }

        void display(float xpos, float ypos, char lastM, int frameR) {
            if(frameR < (144/4)) {
                frame = 0;
            }else if(frameR < (144/4)*2){
                frame = 1;
            }else if(frameR < (144/4)*3 ){
                frame = 2;
            }else if(frameR < (144/4) *4){
                frame = 3;
            }else{
                speler.imgCounter = 0;
            }
            xpos = (((xpos - 600) < 0) ? xpos : 600);
            //kijkt naar rechts
            if(lastM == 'r' || lastM == 'n') {
                //TODO: edit to make the

                image(images[frame], xpos, ypos, BLOCKSIZE, BLOCKSIZE);
            }
            //kijkt naar links
            else{
                pushMatrix();
                scale(-1,1);
                image(images[frame], - (xpos + images[frame].width), ypos, BLOCKSIZE, BLOCKSIZE);
                popMatrix();
            }
        }

    }

    @Override
    public void keyPressed() {
        //TODO: een defitge logica schrijven voor dit
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                //enter wordt ingedrukt
                //sound of selection
                soundManager.play("select2");
                switch (status) {
                    case "scoreboard":
                        this.status = "startscreen";
                        break;
                    case "startscreen":
                        //startscherm is geladen
                        if(menu.getTextOfSelected().equals(menuTexts[0])) {
                            //PLAY HAS BEEN SELECTED
                            menu = new Startscreen(this.numberOfLevels, this);
                            this.status = "levelSelectScreen";
                        } else {
                            //SCORE HAS BEEN SELECTED
                            scoreb = new ScoreBoard(SCOREFILE, this);
                            this.status = "scoreboard";
                        }
                        break;
                    case "naamkiezen":
                        scoreb.saveScore(score.getpoints());
                        scoreb.loadScores();
                        this.status = "scoreboard";
                        break;
                    case "levelSelectScreen":
                        //levle selectiescherm is geladen
                        this.levelToLoad = menu.getIdOfSelected() + 1; //set the number of level to load
                        thread("startLevel"); //init the level in seperate thread
                        //create new loading screen
                        this.loadScreen = new LoadScreen("Loading, Please wait.", this);
                        this.status = "loadScreen"; //change status
                        break;
                }
                break;
            default:
                //andere toets
                switch (status) {
                    case "startscreen":
                        //sound of selection
                        soundManager.play("select1");
                        menu.keyPressed(keyCode);
                        break;
                    case "scoreboard":
                        //sound of selection
                        soundManager.play("select1");
                        scoreb.keyPressed(keyCode, status);
                        break;
                    case "levelSelectScreen":
                        //sound of selection
                        soundManager.play("select1");
                        menu.keyPressed(keyCode);
                        break;
                    case "naamkiezen":
                        soundManager.play("select1");
                        scoreb.keyPressed(keyCode, status);
//                        if(keyCode == UP){
//                            iH--;
//                            if(iH < 0){
//                                iH = 0;
//                            }
//                            kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                        }
//                        if(keyCode == DOWN){
//                            iH++;
//                            if(iH >2){
//                                iH = 2;
//                            }
//                            kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                        }
//                        menu.keyPressed(iH,1);
//                        if(keyCode == RIGHT){
//                            if(iH == 0){
//                                iA++;
//                                if(iA > A.length-1 ){
//                                    iA = A.length-1;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            } else if(iH == 1){
//                                iB++;
//                                if(iB > B.length-1 ){
//                                    iB = B.length-1;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            }else if (iH == 2){
//                                iC++;
//                                if(iC > C.length-1 ){
//                                    iC = C.length-1;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            }
//                        }
//                        if(keyCode == LEFT){
//                            if(iH == 0){
//                                iA--;
//                                if(iA < 0){
//                                    iA = 0;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            } else if(iH == 1){
//                                iB --;
//                                if(iB < 0){
//                                    iB = 0;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            }else if (iH == 2){
//                                iC--;
//                                if(iC < 0){
//                                    iC = 0;
//                                }
//                                kiesnaam = new Startscreen(new String[]{A[iA],B[iB],C[iC]},this,iH);
//                            }
//                        }
                        break;
                    case "playing":
                        //only check for input when level is not ending
                        if(!currentLevel.isLevelEnding()) {
                            if (keyCode == RIGHT) {
                                speler.rightPressed = true;
                            }
                            if (keyCode == LEFT) {
                                speler.leftPressed = true;
                            }
                            if (keyCode == UP) {
                                if(!speler.getIsInAir()){
                                    soundManager.play("jump");
                                }
                                speler.upIsPressed = true;
                            }
                            if(keyCode == DOWN){
                                speler.downPressed = true;
                            }
                            //this is to respawn --> ONLY FOR TESTING
                            if(keyCode == KeyEvent.VK_R)
                            {
                                speler.actorX = 300;
                                speler.actorY = 500;
                            }
                            if(keyCode == KeyEvent.VK_E) {
                                //levle selectiescherm is geladen
//                                this.levelToLoad = menu.getIdOfSelected() + 1; //set the number of level to load
                                thread("startLevel"); //init the level in seperate thread
                                //create new loading screen
                                this.loadScreen = new LoadScreen("Loading, Please wait.", this);
                                this.status = "loadScreen"; //change status
                            }
                        }
                        break;
                }
        }

    }

    @Override
    public void keyReleased() {
        switch (status) {
            case "playing":
                if (keyCode == RIGHT) {
                    speler.rightPressed = false;
                }
                if (keyCode == LEFT) {
                    speler.leftPressed = false;
                }
                if (keyCode == DOWN) {
                    speler.downPressed = false;
                }
                break;
        }
    }
    /**
     * creates new level object and initializes it.
     * makes it ready to render the level
     * this function is used in a seperate thread
     */
    public void startLevel() {
        //this.currentLevel = new Level(this, levelFiles[this.levelToLoad -1 ]);
        //this.currentLevel = new Level(this, "DemoLevel_NoEnemies.csv");
        this.currentLevel = new Level(this, this.levelToLoad);
        playerWandelen = new Animation("Toucolooor", 4);
        enemyWandelen = new Animation("Timberman",1);
        //enemyWandelen = new Animation("soccer_player_fro", 1); //testenemy
        speler = new Player();
        this.imageHasSwitched = false;
        this.lastOpacity = 0;
        timer = new Time(LEVELTIME, this);
        score = new Score(this);
        mangos = currentLevel.getMangos();

        this.status = "playing";

    }

    void refreshAllValues(){
        speler.refreshValues(currentLevel.getCoords((int) speler.actorX, (int) speler.actorY),
                currentLevel.getColAndDeath((int) speler.actorX, (int) speler.actorY));
        //forach isspawn
        if(enemies != null) {
            for (Enemy swag : enemies) {
                swag.refreshValues(currentLevel.getCoords((int) swag.actorX, (int) swag.actorY),
                        currentLevel.getColAndDeath((int) swag.actorX, (int) swag.actorY));
            }
        }

    }

    void setStatus(String status) {
        this.status = status;
    }


}

