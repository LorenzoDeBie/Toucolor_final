package Toucolor;

import processing.core.PApplet;
import processing.data.*;

import java.awt.event.KeyEvent;
import java.io.PrintWriter;

/**
 * Created by Vince on 8/05/2017.
 */
class ScoreBoard extends Startscreen{
    private String[] bestplayers = new String[10];

    //name selection
    private int iH = 0;
    private char[] arr = {'A','A','A'};

    ScoreBoard(String file, Toucolor applet){
        this.applet = applet;
        logo = applet.loadImage("menu_logo.png");
        String fileName = file;
        loadScores(fileName);
    }

    //loads the highscores
    private void loadScores(String fileName) {
        Table scoreb = applet.loadTable(fileName, "header, csv");
        for (int i = 0; i < 10; i++) {
            TableRow row = scoreb.getRow(i);
            int id = row.getInt("id");
            String naam = row.getString("naam");
            int score = row.getInt("score");
            bestplayers[i] = id+"       "+naam+"        "+score;
        }
    }

    void saveScore(int score) {
        String name = new String(arr);
        int[] sbpoints = new int[10];
        String[] sbnames = new String[10];
        int[] sbid = new int[10];

        Table scores = applet.loadTable("score.csv", "header, csv");

        for (int i = 0; i < 10; i++) {
            TableRow row = scores.getRow(i);
            score = row.getInt("score");
            name = row.getString("naam");
            int id = row.getInt("id");
            sbpoints[i] = score;
            sbnames[i] = name;
            sbid[i] = id;
        }

        for (int i = 0; i < sbpoints.length; i++) {
            if (score >= sbpoints[i]) {
                for (int j = sbpoints.length - 1; j >= PApplet.max(i, 1); j--) {
                    sbpoints[j] = sbpoints[j - 1];
                    sbnames[j] = sbnames[j - 1];
                }
                sbpoints[i] = score;
                sbnames[i] = name;
                break;
            }
        }
        scores.clearRows();

        PrintWriter scorefile = applet.createWriter("\\data\\score.csv");

        scorefile.println("id,naam,score");

        for (int i = 0; i < sbid.length; i++) {
            scorefile.println((i+1)+","+sbnames[i]+","+sbpoints[i]);
        }

        scorefile.flush();
        scorefile.close();
    }

    //renders the name selection screen
    void renderNameSelection() {
        //sets the right letters for buttons
        String[] names = {Character.toString(arr[0]), Character.toString(arr[1]), Character.toString(arr[2])};
        changeNames(names);
        selectedButton = menuItems[iH];
        selectedButton.buildSelecter();
        //renders the startscreen
        renderStartScreen();
    }

    void renderScoreboard() {
        changeNames(bestplayers);
        renderStartScreen();
    }

    void keyPressed(int keyCode, String status) {
        if(status.equals("naamkiezen")) {
            //UP and DOWN is changing which letter is selected
            if(keyCode == Toucolor.UP){
                iH = iH-- > 0 ? iH-- : 0;
            }
            if(keyCode == Toucolor.DOWN){
                iH = iH++ < 2 ? iH++ : 2;
            }
            //RIGHT and LEFT chagne the letter
            if(keyCode == Toucolor. RIGHT){
                arr[iH] = arr[iH]++ < KeyEvent.VK_Z ? arr[iH]++ : KeyEvent.VK_Z;
            }
            if(keyCode == Toucolor.LEFT) {
                arr[iH] = arr[iH]-- > KeyEvent.VK_A ? arr[iH]-- :KeyEvent.VK_A;
            }
        }
        else if(status.equals("scoreboard")) {
            //do something with scoreboard
            applet.setStatus("startscreen");
        }
    }

}
