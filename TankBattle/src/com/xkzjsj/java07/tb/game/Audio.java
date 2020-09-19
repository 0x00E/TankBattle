/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class Audio
implements Runnable {
    private String url = "audio/";
    private int type = 0;
    private static HashSet<Integer> hs = new HashSet<Integer>();
    AudioFilePlayer player = new AudioFilePlayer();

    public HashSet<Integer> getHs() {
        return hs;
    }

    public Audio(int type) {
        if (type == 0) {
            if (!hs.add(type)) {
                return;
            }
            hs.add(type);
        }
        this.type = type;
        switch (type) {
            case 0: {
                this.url = String.valueOf(this.url) + "main.wav";
                break;
            }
            case 1: {
                this.url = String.valueOf(this.url) + "move.wav";
                break;
            }
            case 2: {
                this.url = String.valueOf(this.url) + "shoot.wav";
                break;
            }
            case 3: {
                this.url = String.valueOf(this.url) + "explode.wav";
                break;
            }
            case 4: {
                this.url = String.valueOf(this.url) + "eatblood.wav";
                break;
            }
            case 5: {
                this.url = String.valueOf(this.url) + "levelup.wav";
                break;
            }
            case 6: {
                this.url = String.valueOf(this.url) + "gameover.wav";
            }
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        FilterInputStream ais = null;
        InputStream is = null;
        try {
            try {
                if (this.type == 0) {
                    do {
                    	
                        player.play(this.url);
                        Thread.sleep(60000);
                    } while (true);
                }
                player.play(this.url);
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (ais != null) {
                    ais.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

