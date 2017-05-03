package Toucolor;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Created by loren on 10/04/2017.
 * small class which creates a basic loading screen
 * this class is lightweight because this cant take long to build and stuff
 *
 */
class LoadScreen {
    private String loadText;
    private PApplet applet;

    LoadScreen(String loadText, PApplet applet) {
        this.loadText = loadText;
        this.applet = applet;
    }

    void renderLoadScreen() {
        applet.background(0);
        applet.textAlign(PConstants.CENTER, PConstants.CENTER);
        applet.textSize(32);
        applet.fill(255);
        applet.text(loadText, applet.width/2, applet.height/2);
    }

    void setText(String text) {
        this.loadText = text;
    }
}
