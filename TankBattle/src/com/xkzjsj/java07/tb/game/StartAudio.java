/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class StartAudio
extends Thread {
    private String filename;

    public StartAudio(String wavfile) {
        this.filename = wavfile;
    }

    @Override
    public void run() {
        File soundFile = new File(this.filename);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        }
        catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            auline = (SourceDataLine)AudioSystem.getLine(info);
            auline.open(format);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[512];
        try {
            try {
                while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    if (nBytesRead < 0) continue;
                    auline.write(abData, 0, nBytesRead);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                auline.drain();
                auline.close();
                return;
            }
        }
        finally {
            auline.drain();
            auline.close();
        }
    }
}

