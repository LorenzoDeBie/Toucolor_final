package Toucolor;

import processing.core.PApplet;
import processing.data.*;

/**
 * Created by Vince on 8/05/2017.
 */
public class ScoreBoard extends Startscreen{
    Table scoreb;
    String[] bestplayers = new String[10];

    ScoreBoard(String file, PApplet applet){
        this.applet = applet;
        scoreb = applet.loadTable(file, "header, csv");
        for (int i = 0; i < 10; i++) {
            TableRow row = scoreb.getRow(i);
            int id = row.getInt("id");
            String naam = row.getString("naam");
            int score = row.getInt("score");
            bestplayers[i] = id+" "+naam+" "+score;
        }
        logo = applet.loadImage("menu_logo.png");
        super.changeNames(bestplayers);
    }

}
