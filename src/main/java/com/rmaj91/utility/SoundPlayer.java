package com.rmaj91.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * This class provides audio player
 */
public class SoundPlayer {

    /*Variables*/
    private boolean soundsActive;
    // This is global control of application volume, scope from 0.0 to 1.0
    private final double volumeScale = 0.75;
    private double volumeLevel;
    private Clip previousClip = null;

    /*Constructor*/
    public SoundPlayer() {
        soundsActive = true;
    }

    /*Getters & Setters*/
    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public void setVolumeLevel(double sliderValue) {
        this.volumeLevel = (Math.log10(sliderValue) / 2);
        if (this.volumeLevel < 0)
            this.volumeLevel = 0;
    }

    /*Public Methods*/

    /**
     * This method plays the ".wav" audio from /resources/sounds/ directory, it determines the name of audio
     * by passed parameter which is the dart boards field name eg. "DOUBLE 25" it tranforms it to "double25.wav"
     * @param fieldName the name of thrown field
     */
    public void playSound(String fieldName) {
        if(!soundsActive)
            return;
        if(previousClip != null)
            previousClip.stop();
        try {
            String fileName = fieldName.toLowerCase().replaceAll("\\s","");
            File file = new File(getClass().getClassLoader().getResource("sounds/" + fileName+ ".wav").getFile());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Setting volume control
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // determining the allowed range of volume control
            float range = (control.getMaximum() - control.getMinimum());

            // calculating resultVolume
            double resultVolume = (range * volumeLevel * volumeScale + control.getMinimum());
            control.setValue((float) resultVolume);
            clip.start();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

}
