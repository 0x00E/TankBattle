/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Grass {
    int x;
    int y;
    ImageIcon grass = new ImageIcon("images/grass.gif");

    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(this.grass.getImage(), this.x, this.y, null);
    }
}

