package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    
    private Clip clip;
    private File soundURL[] = new File[10];
    private int soundNum;

    public Sound() {
        addNewClip("app/res/clips/menuTheme.wav"); // 0
        addNewClip("app/res/clips/gameTheme.wav"); // 1
        addNewClip("app/res/clips/hit.wav");       // 2
        addNewClip("app/res/clips/miss.wav");      // 3
        addNewClip("app/res/clips/win.wav");       // 4
        addNewClip("app/res/clips/lose.wav");      // 5
        addNewClip("app/res/clips/placeShip.wav"); // 6
        addNewClip("app/res/clips/error.wav");     // 7
        addNewClip("app/res/clips/shipSunk.wav");  // 8
        addNewClip("app/res/clips/rotate.wav");    // 9
    }

    private void addNewClip(String filepath) {
        try {
            File soundFile = new File(filepath);
            soundURL[soundNum++] = soundFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return clip != null;
    }

    public void setFile(int i) {
        try {
            // Open audio file in java
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            System.out.println("Clip did not open");
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }
    
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
