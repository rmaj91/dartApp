package com.rmaj91.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;


public class SoundPlayer {

    private boolean soundActive;
    // from 0.0 to 1.0 //
    private final double volumeScale = 0.75;
    private double volumeLevel;


    // Constructor //
    public SoundPlayer() {
        soundActive = true;
    }


    // GETTERS & SETTERS //
    public void setSoundActive(boolean soundActive) {
        this.soundActive = soundActive;
    }

    public void setVolumeLevel(double sliderValue) {
        this.volumeLevel = (Math.log10(sliderValue) / 2);
        if (this.volumeLevel < 0)
            this.volumeLevel = 0;
    }

    // METODS //
    public void playSound(String fieldKey) {
        if(!soundActive)
            return;
        try {
            String fileName = fieldKey.toLowerCase().replaceAll("\\s","");
            File file = new File(getClass().getClassLoader().getResource("sounds/" + fileName+ ".wav").getFile());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float range = (control.getMaximum() - control.getMinimum());
            double result = (range * volumeLevel * volumeScale + control.getMinimum());
            System.out.println(result);
            control.setValue((float) result);
            clip.start();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

}
