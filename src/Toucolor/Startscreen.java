package Toucolor;

import javafx.scene.Parent;
import processing.core.*;

import java.awt.event.KeyEvent;
import java.awt.print.Paper;

/**
 * Created by loren on 07/04/2017.
 * class which draws startscreen bcuz u hav to start somewhere nub, git gud.
 */
class Startscreen {
    //these be static bcuz lulz
    private static int BUTTONWIDTH = 160;
    private static int BUTTONHEIGHT = 80;
    private static int STARTY = 200;
    private static int SPACEBETWOONBUTTONS = 40;

    //PROPERTIES
    protected PImage bImage; //background image
    menuButton[] menuItems; //all the items in the menu
    PImage logo;
    Toucolor applet;
    menuButton selectedButton;

    //in the level selection screen

    /*
    constructor has been overloaded for two usecases
    1) for a menu selection screen
        takes an array of strings as param
    2) for a level selection screen
        takes the number of levels as param
     */
    Startscreen(){}

    //used in scoreboard
    void changeNames(String[] names){
        int textLength = 0;
        menuItems = new menuButton[names.length];
        int heightPerBlock = (Toucolor.WORLDHEIGHT - STARTY) / names.length;
        int buttonHeight = ((int) (0.75 * heightPerBlock)) < BUTTONHEIGHT ? ((int) (0.75 * heightPerBlock)) : BUTTONHEIGHT;
        int spaceBetweenButtons = heightPerBlock - buttonHeight < SPACEBETWOONBUTTONS ? heightPerBlock - buttonHeight : SPACEBETWOONBUTTONS;

        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new menuButton(applet.width/2, STARTY + (i * (buttonHeight + spaceBetweenButtons)), BUTTONWIDTH, buttonHeight, names[i], applet, i);
            textLength = applet.textWidth(names[i]) > textLength ? (int) applet.textWidth(names[i]) : textLength;
        }
        selectedButton = menuItems[0];
        selectedButton.buildSelecter();

        for(menuButton button : menuItems) {
            button.setDimensions(textLength + 20, buttonHeight);
        }
    }

    //menu selection screen
    Startscreen(String[] itemsText, Toucolor applet) {
        //load the logo to display on top of page
        logo = applet.loadImage("menu_logo.png");
        //set on which applet to draw
        this.applet = applet;

        //all the items in the menu
        menuItems = new menuButton[itemsText.length];

        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new menuButton(applet.width/2, STARTY + (i * (BUTTONHEIGHT + SPACEBETWOONBUTTONS)), BUTTONWIDTH, BUTTONHEIGHT, itemsText[i], applet, i);
        }
        selectedButton = menuItems[0];
        selectedButton.buildSelecter();
    }

    //level selectin screen
    Startscreen(int numberOfLevels, Toucolor applet) {
        //load the logo to display on top of page
        logo = applet.loadImage("menu_logo.png");
        //set on which applet to draw
        this.applet = applet;

        //all the items in the menu
        menuItems = new menuButton[numberOfLevels];


        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new menuButton(applet.width/2, STARTY + (i * (BUTTONHEIGHT + SPACEBETWOONBUTTONS)), BUTTONWIDTH, BUTTONHEIGHT, Integer.toString(i+1), applet, i);
        }

        selectedButton = menuItems[0];
        selectedButton.buildSelecter();
    }

    //this function is called by the sketches
    void renderStartScreen() {
        applet.background(0);
        renderLogo();
        renderMenu();
    }

    //renders selector of selected button
    void renderSelecter() {
        selectedButton.renderSelecter();
    }

    //draws the logo to screen
    private void renderLogo() {
        //render temp logo
        applet.imageMode(PConstants.CENTER); //now i can give the center point of the image
        applet.image(logo, applet.width/2, logo.height /2); //render image using center coordinates
    }

    //renders the menu
    private void renderMenu() {
        for(menuButton button : menuItems) {
            button.renderbutton();
        }
    }

    //handles key movement in the selection screen
    void keyPressed(int keyCode ) {
        //checks which key and if the key is possible
        //up
        if(keyCode == KeyEvent.VK_UP && (selectedButton.arrayID > 0)) {
            //up arrow
            selectedButton = menuItems[selectedButton.arrayID - 1];
            selectedButton.buildSelecter();
        }
        //down
        else if(keyCode == KeyEvent.VK_DOWN && (selectedButton.arrayID + 1  < menuItems.length)) {
            //down arrow
            selectedButton = menuItems[selectedButton.arrayID + 1];
            selectedButton.buildSelecter();
        }
    }

    //returns the index in the array of the selcted element
    int getIdOfSelected() {
        return this.selectedButton.arrayID;
    }

    //return the text in the currently selected button
    String getTextOfSelected() { return this.selectedButton.buttonText; }

    //a subclass for the menubuttons, will only be used here
    protected class menuButton {
        private int posX;
        private int posY;
        private int buttonWidth;
        private int buttonHeight;
        private String buttonText;
        private int fillColor;
        private int textColor;
        private int arrayID;

        int[][] selecterCoords;
        int selecterRectLengt;
        int spaceBetween;

        private PApplet applet;

        menuButton(int x, int y, int width, int height, String text, PApplet applet, int arrayID) {
            posX = x; posY = y; buttonWidth = width; buttonHeight = height; buttonText = text; //assignments
            fillColor = 255;
            this.applet = applet;
            spaceBetween = 10;
            selecterRectLengt = (int) (0.0625 * buttonWidth) ;
            this.arrayID = arrayID;
        }

        //renders the button
        void renderbutton() {
            //render the button
            applet.fill(fillColor);
            applet.rectMode(PConstants.CENTER);
            applet.rect(posX, posY, buttonWidth, buttonHeight);
            //render the text
            applet.fill(textColor);
            applet.textSize(32);
            applet.textAlign(PConstants.CENTER);
            applet.text(buttonText, posX, posY);
        }

        //setters for the fillcolor and textcolor
        void setFillColor(int fillColor) {
            this.fillColor = fillColor;
        }

        void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        void renderSelecter() {
            applet.fill(255);
            applet.rectMode(PConstants.CENTER);
            for (int[] selecterCoord : selecterCoords) {
                applet.rect(selecterCoord[0], selecterCoord[1], selecterCoord[2], selecterCoord[3]);
            }
        }

        void buildSelecter() {

             selecterCoords = new int[8][4];

            //all x-coords
            selecterCoords[0][0] = posX - (buttonWidth / 2);
            selecterCoords[1][0] = posX - (buttonWidth /2) - spaceBetween - (selecterRectLengt / 2);
            selecterCoords[2][0] = posX + (buttonWidth /2);
            selecterCoords[3][0] = posX + (buttonWidth /2) + spaceBetween + (selecterRectLengt / 2);
            selecterCoords[5][0] = posX - (buttonWidth /2) - spaceBetween - (selecterRectLengt / 2);
            selecterCoords[4][0] = posX - (buttonWidth /2);
            selecterCoords[6][0] = posX + (buttonWidth /2);
            selecterCoords[7][0] = posX + (buttonWidth /2) + spaceBetween + (selecterRectLengt / 2);

            //all y-coords
            selecterCoords[0][1] = posY - (buttonHeight / 2) - spaceBetween - (selecterRectLengt / 2);
            selecterCoords[1][1] = posY - (buttonHeight /2);
            selecterCoords[2][1] = posY - (buttonHeight / 2) - spaceBetween - (selecterRectLengt / 2);
            selecterCoords[3][1] = posY - (buttonHeight / 2);
            selecterCoords[5][1] = posY + (buttonHeight /2);
            selecterCoords[4][1] = posY + (buttonHeight / 2) + spaceBetween + (selecterRectLengt / 2);
            selecterCoords[6][1] = posY + (buttonHeight / 2) + spaceBetween + (selecterRectLengt / 2);
            selecterCoords[7][1] = posY + (buttonHeight /2);


            for (int i = 0; i < selecterCoords.length; i++) {
                if((i % 2) != 0) {
                    //oneven dus verticaal rechthoekje
                    selecterCoords[i][2] = (selecterRectLengt /2 ) - spaceBetween;
                    selecterCoords[i][3] = selecterRectLengt * 2;
                }
                else {
                    //even
                    selecterCoords[i][3] = (selecterRectLengt /2 ) - spaceBetween;
                    selecterCoords[i][2] = selecterRectLengt * 2;
                }
            }
        }

        void setDimensions(int width, int height) {
            this.buttonHeight = height > BUTTONHEIGHT ? height : BUTTONHEIGHT;
            this.buttonWidth = width > BUTTONWIDTH ? width : BUTTONWIDTH;
        }
    }
}
