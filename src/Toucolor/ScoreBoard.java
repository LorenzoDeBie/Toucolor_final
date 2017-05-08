package Toucolor;

import processing.core.PApplet;
import processing.data.*;

/**
 * Created by Vince on 8/05/2017.
 */
public class ScoreBoard extends Startscreen{
    Table scoreb;
    String[] bestplayers = new String[10];
    PApplet applet;



    ScoreBoard(String file, PApplet applet){
        super();
        this.applet = applet;
        scoreb = applet.loadTable(file, "header, csv");
        for (int i = 0; i < 10; i++) {
            TableRow row = scoreb.getRow(i);
            int id = row.getInt("id");
            String naam = row.getString("naam");
            int score = row.getInt("score");
            bestplayers[i] = id+" "+naam+" "+score;
        }
        super.changeNames(bestplayers);





    }

}
