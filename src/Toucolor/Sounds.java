package Toucolor;

import ddf.minim.*;

import java.util.Arrays;
import java.util.List;

class Sounds {
    private String status;
    private Toucolor applet;

    private static final List<String> events = Arrays.asList("select1", "select2", "jump", "die", "coin", "end");
    private static final List<String> files = Arrays.asList("Select1.wav", "Select2.mp3", "Jump.wav", "Hurt.wav", "Coin.wav", "End.mp3");
    //private SoundFile[] soundFiles;

    //minim
    private Minim minim;
    private AudioPlayer[] soundFiles;

    Sounds(String status, Toucolor applet) {
        this.applet = applet;
        this.status = status;
        loadFiles();

    }

    private void loadFiles() {
        minim = new Minim(applet);
        soundFiles = new AudioPlayer[events.size()];
        for(int i = 0; i < soundFiles.length; i++) {
            soundFiles[i] = minim.loadFile(files.get(i));
        }
        AudioPlayer chiptune = minim.loadFile("Chiptune.mp3");
        chiptune.loop();
    }

    public void play(String event) {
        //mogelijke events:
        /*
         * menuscreen:
         * select1: op neer
         * select2: enter
         *
         * level:
         * jump: springen
         * die: doodgaan
         * coin: muntje
         * end: einde level
         */

         soundFiles[events.indexOf(event)].rewind();
         soundFiles[events.indexOf(event)].play();
    }
}
