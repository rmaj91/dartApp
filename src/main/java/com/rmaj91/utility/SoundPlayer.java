package com.rmaj91.utility;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides audio player
 */
public class SoundPlayer {

    //==================================================================================================
    // Properties
    //==================================================================================================
    private boolean soundsActive;
    /**
     * This is global control parameter for application volume, scope from 0.0 to 1.0
     */
    private final double volumeScale = 0.75;
    private double volumeLevel;
    private Clip previousClip = null;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public SoundPlayer() {
        soundsActive = true;
    }


    //==================================================================================================
    // Assesors
    //==================================================================================================
    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public void setVolumeLevel(double sliderValue) {
        this.volumeLevel = (Math.log10(sliderValue) / 2);
        if (this.volumeLevel < 0)
            this.volumeLevel = 0;
    }


    //==================================================================================================
    // Public Methods
    //==================================================================================================

    /**
     * This method plays the ".wav" audio from /resources/sounds/ directory, it determines the name of audio
     * by passed parameter which is the dart boards field name eg. "DOUBLE 25" it tranforms it to "double25.wav"
     * if doesnt find plays default "throw.wav"
     *
     * @param fieldName the name of thrown field
     */
    public void playSound(String fieldName) {
        if (!soundsActive)
            return;
        if (previousClip != null)
            previousClip.stop();
        try {
            String fileName = fieldName.toLowerCase().replaceAll("\\s", "");
            File file = null;
            file = getFile(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

            Clip clip = AudioSystem.getClip();
            previousClip = clip;
            clip.open(audioInputStream);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumeControlRange = (volumeControl.getMaximum() - volumeControl.getMinimum());

            double resultVolume = (volumeControlRange * volumeLevel * volumeScale + volumeControl.getMinimum());
            volumeControl.setValue((float) resultVolume);
            clip.start();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private File getFile(String fileName) {
        File file;
        try {
            file = new File(getClass().getClassLoader().getResource("sounds/" + fileName + ".wav").getFile());
        } catch (NullPointerException e) {
            file = new File(getClass().getClassLoader().getResource("sounds/throw.wav").getFile());
        }
        return file;
    }

}
