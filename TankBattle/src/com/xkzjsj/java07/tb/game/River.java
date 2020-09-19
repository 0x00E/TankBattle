/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class River {
    int x;
    int y;
    ImageIcon river = new ImageIcon("images/river.gif");

    public River(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.drawImage(this.river.getImage(), this.x, this.y, null);
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.river.getIconWidth(), this.river.getIconHeight());
    }
}

