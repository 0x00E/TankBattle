/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import com.xkzjsj.java07.tb.game.TankClient;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
    int x;
    int y;
    int w;
    int h;
    TankClient tc;

    public Wall(int x, int y, int w, int h, TankClient tc) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(13879753));
        g.fillRect(this.x, this.y, this.w, this.h);
        g.setColor(c);
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.w, this.h);
    }
}

