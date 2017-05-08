package Toucolor;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by loren on 08/05/2017.
 */
public class TempBlock extends Block {

    private int millisTillGone;

    TempBlock(int id, String name, String imgFileName, boolean collision, int millisTillGone, boolean killsPlayer, PGraphics pg, PApplet applet) {
        super(id, name, imgFileName, collision, killsPlayer, pg, applet);
        this.millisTillGone = millisTillGone;
    }

    //this makes the block flikker
    private void flikker() {
        if((millisTillGone % (applet.frameRate / 2)) == 0) {
            this.drawBlock = !this.drawBlock; //changes true to false and opposite
        }
    }

    //called by player
    void standOn() {
        if(brokkelt) {
            millisTillGone--;
            if(this.millisTillGone == 0) {
                destroyblock();
            }
            flikker();
        }
    }
}
