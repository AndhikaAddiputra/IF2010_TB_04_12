package utility;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;
    private boolean isPlaying = false;

    public void play(String musicPath) {
        try {
            URL url = getClass().getResource(musicPath);
            if (url == null) {
                System.out.println("Cannot find music file: " + musicPath);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(0.5f); // 50% volume by default
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            isPlaying = true;
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            isPlaying = false;
        }
    }

    public void setVolume(float volume) {
        // volume should be between 0.0 and 1.0
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            volumeControl.setValue((max - min) * volume + min);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}