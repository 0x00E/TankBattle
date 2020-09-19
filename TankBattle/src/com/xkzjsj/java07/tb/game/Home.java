/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Home {
    int x;
    int y;
    private TankClient tc;
    private boolean live = true;
    ImageIcon home = new ImageIcon("images/home.jpg");

    public boolean isLive() {
        return this.live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Home(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (this.live) {
            g.drawImage(this.home.getImage(), this.x, this.y, null);
        } else {
            this.gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        this.tc.bloods.clear();
        this.tc.grasses.clear();
        this.tc.rivers.clear();
        this.tc.walls.clear();
        this.tc.missiles.clear();
        this.tc.tanks.clear();
        this.tc.explodes.clear();
        this.tc.home.setLive(false);
        this.tc.myTank.setLive(false);
        TankClient.count = 0;
        Color c = g.getColor();
        g.setColor(Color.RED);
        Font f = g.getFont();
        g.setFont(new Font(" ", 0, 40));
        g.drawString("\u5f88\u9057\u61be,\u60a8\u8f93\u4e86\uff01", 245, 250);
        g.drawString("\u8bf7\u6309R\u952e\u91cd\u65b0\u5f00\u59cb\uff01 ", 230, 300);
        g.setFont(f);
        g.setColor(c);
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.home.getIconWidth(), this.home.getIconHeight());
    }
}

