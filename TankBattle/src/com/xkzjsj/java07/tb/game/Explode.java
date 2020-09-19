/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.multi.MissileDeadMsg;
import com.xkzjsj.java07.tb.game.multi.TankDeadMsg;

public class Explode {
    int x;
    int y;
    private boolean live = true;
    private TankClient tc;
    int[] diameter = new int[]{4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
    int step = 0;
    private boolean init;
    private static Image[] images = new Image[]{new ImageIcon("./img/0.gif").getImage(), new ImageIcon("./img/1.gif").getImage(), new ImageIcon("./img/2.gif").getImage(), new ImageIcon("./img/3.gif").getImage(), new ImageIcon("./img/4.gif").getImage(), new ImageIcon("./img/5.gif").getImage(), new ImageIcon("./img/6.gif").getImage(), new ImageIcon("./img/7.gif").getImage(), new ImageIcon("./img/8.gif").getImage(), new ImageIcon("./img/9.gif").getImage(), new ImageIcon("./img/10.gif").getImage()};

    public static void init() {
    }

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!this.init) {
            int i = 0;
            while (i < images.length) {
                g.drawImage(images[i], -100, -100, null);
                ++i;
            }
            this.init = true;
        }
        if (!this.live) {
            this.tc.explodes.remove(this);
            return;
        }
        if (this.step == images.length) {
            this.live = false;
            this.step = 0;
            return;
        }
        g.drawImage(images[this.step], this.x - this.step * 2, this.y - this.step * 2, null);
        if (this.tc.myTank.getLife() <= 0) {
            if (!Game.getTc().myTank.isDie && Game.isMulti) {
                Game.getTc().myTank.isDie = true;
                TankDeadMsg msg = new TankDeadMsg(Game.getTc().myTank.id);
                Game.getTc().nc.send(msg);
                MissileDeadMsg mdmMsg = new MissileDeadMsg(Game.getTc().myTank.id, Game.getTc().myTank.id);
                Game.getTc().nc.send(mdmMsg);
            }
            return;
        }
        ++this.step;
    }
}

