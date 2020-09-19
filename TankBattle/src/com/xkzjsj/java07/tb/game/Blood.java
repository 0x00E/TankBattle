/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.multi.BloodNewMsg;

public class Blood {
    int x;
    int y;
    int w;
    int h;
    TankClient tc;
    int step = 0;
    public boolean live = true;
    Random r = new Random();

    public Blood() {
        this.x = (this.r.nextInt(20) + 3) * 25;
        this.y = (this.r.nextInt(20) + 3) * 25;
        this.h = 15;
        this.w = 15;
        BloodNewMsg msg = new BloodNewMsg();
        Game.getTc().nc.send(msg);
    }

    public void draw(Graphics g) {
        if (!this.live) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(this.x, this.y, this.w, this.h);
        g.setColor(c);
        this.move1();
    }

    private void move1() {
        if (this.step > 10) {
            this.step = 0;
        }
        ++this.step;
        if (this.step <= 5) {
            ++this.x;
            --this.y;
        } else {
            --this.x;
            ++this.y;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.w, this.h);
    }

    public boolean CollidesWithWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            return true;
        }
        return false;
    }

    public boolean CollidesWithWalls(List<Wall> walls) {
        int i = 0;
        if (i < walls.size()) {
            Wall w = walls.get(i);
            this.CollidesWithWall(w);
            return true;
        }
        return false;
    }

    public boolean isLive() {
        return this.live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}

