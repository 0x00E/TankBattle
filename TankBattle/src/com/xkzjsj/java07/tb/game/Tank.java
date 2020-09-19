/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.multi.Dir;
import com.xkzjsj.java07.tb.game.multi.MissileNewMsg;
import com.xkzjsj.java07.tb.game.multi.TankMoveMsg;

public class Tank {
    public boolean isDie = false;
    public int XSPEED;
    public int YSPEED;
    public boolean live = true;
    public int life = 1000;
    TankClient tc = Game.getTc();
    public boolean good = true;
    public int oldX;
    public int oldY;
    public int x;
    public int y;
    public static Random r = new Random();
    public int step = r.nextInt(30) + 10;
    public boolean bL = false;
    public boolean bU = false;
    public boolean bR = false;
    public boolean bD = false;
    public Dir dir = Dir.STOP;
    public Dir ptDir = Dir.D;
    public BloodBar bar;
    public boolean isPlayer;
    public int id;
    public String imageDir;
    public ImageIcon tank_down;
    public ImageIcon tank_up;
    public ImageIcon tank_left;
    public ImageIcon tank_right;
    public ImageIcon background;
    public final int WIDTH;
    public final int HEIGHT;
    private static int[] dirs;

    public void init() {
        if (Game.isMulti) {
            this.XSPEED = 5;
            this.YSPEED = 5;
        } else {
            this.XSPEED = 1;
            this.YSPEED = 1;
        }
    }

    public Tank(int x, int y, boolean good) {
        this.bar = new BloodBar();
        this.imageDir = "img";
        this.tank_down = new ImageIcon(String.valueOf(this.imageDir) + File.separator + "tank_down.png");
        this.tank_up = new ImageIcon(String.valueOf(this.imageDir) + File.separator + "tank_up.png");
        this.tank_left = new ImageIcon(String.valueOf(this.imageDir) + File.separator + "tank_left.png");
        this.tank_right = new ImageIcon(String.valueOf(this.imageDir) + File.separator + "tank_right.png");
        this.background = this.tank_up;
        this.WIDTH = this.background.getIconWidth();
        this.HEIGHT = this.background.getIconHeight();
        this.init();
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Dir dir, TankClient tc) {
        this(x, y, good);
        this.tc = tc;
        this.oldX = x;
        this.oldY = y;
        this.dir = dir;
        this.tc = tc;
    }

    public Tank(int x, int y, boolean good, Dir dir, TankClient tc, int xspeed, int yspeed, int life) {
        this(x, y, good);
        this.tc = tc;
        this.oldX = x;
        this.oldY = y;
        this.dir = dir;
        this.XSPEED = xspeed;
        this.YSPEED = yspeed;
        this.isPlayer = true;
        this.life = life;
    }

    public void draw(Graphics g) {
        if (!this.live) {
            if (!this.good) {
                this.tc.tanks.remove(this);
            }
            return;
        }
        g.drawImage(this.background.getImage(), this.x, this.y, null);
        if (Game.isMulti || this.good) {
            Chat.draw(g, this.x, this.y);
        }
        if (Game.isMulti) {
            g.setColor(Color.YELLOW);
            g.drawString("ID\uff1a" + this.id, this.x, this.y - 30);
        }
        this.bar.draw(g, this.good);
        g.setColor(Color.WHITE);
        switch (Tank.Dir()[this.ptDir.ordinal()]) {
            case 1: {
                this.background = this.tank_left;
                break;
            }
            case 2: {
                this.background = this.tank_up;
                break;
            }
            case 3: {
                this.background = this.tank_right;
                break;
            }
            case 4: {
                this.background = this.tank_down;
                break;
            }
        }
        this.move();
    }

    public void tp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void move() {
        this.oldX = this.x;
        this.oldY = this.y;
        switch (Tank.Dir()[this.dir.ordinal()]) {
            case 1: {
                this.x -= this.XSPEED;
                break;
            }
            case 2: {
                this.y -= this.YSPEED;
                break;
            }
            case 3: {
                this.x += this.XSPEED;
                break;
            }
            case 4: {
                this.y += this.YSPEED;
                break;
            }
        }
        if (this.dir != Dir.STOP) {
            this.ptDir = this.dir;
        }
        if (!Game.isMulti && !this.good) {
            Dir[] dirs = Dir.values();
            if (this.step == 0) {
                this.step = r.nextInt(30) + 10;
                int rn = r.nextInt(dirs.length);
                this.dir = dirs[rn];
            }
            --this.step;
            if (r.nextInt(40) > 36) {
                this.fire();
            }
        }
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.y < 0) {
            this.y = 0;
        }
        if (this.x + this.WIDTH > 800) {
            this.x = 800 - this.WIDTH;
        }
        if (this.y + this.HEIGHT > 600) {
            this.y = 600 - this.HEIGHT;
        }
    }

    public void stay() {
        this.x = this.oldX;
        this.y = this.oldY;
    }

    public void keyPressed(KeyEvent e) {
        if (Game.isMulti && this.isDie) {
            return;
        }
        int key = e.getKeyCode();
        switch (key) {
            case 82: {
                if (Game.isMulti) break;
                this.tc.bloods.clear();
                this.tc.grasses.clear();
                this.tc.rivers.clear();
                this.tc.walls.clear();
                this.tc.missiles.clear();
                this.tc.tanks.clear();
                this.tc.explodes.clear();
                TankClient.score = 0;
                TankClient.level = 1;
                this.tc.newGrass();
                this.tc.newRiver();
                this.tc.newWall();
                if (this.tc.tanks.size() == 0) {
                    this.tc.newTank();
                }
                this.tc.myTank = new Tank(220, 560, true, Dir.STOP, this.tc, 5, 5, 9999);
                if (!this.tc.home.isLive()) {
                    this.tc.home.setLive(true);
                }
                new TankClient().start();
                break;
            }
            case 37: {
                this.bL = true;
                break;
            }
            case 38: {
                this.bU = true;
                break;
            }
            case 39: {
                this.bR = true;
                break;
            }
            case 40: {
                this.bD = true;
            }
        }
        this.locateDir();
    }

    void locateDir() {
        Dir oldDir = this.dir;
        if (this.bL && !this.bU && !this.bR && !this.bD) {
            this.dir = Dir.L;
        } else if (!this.bL && this.bU && !this.bR && !this.bD) {
            this.dir = Dir.U;
        } else if (!this.bL && !this.bU && this.bR && !this.bD) {
            this.dir = Dir.R;
        } else if (!this.bL && !this.bU && !this.bR && this.bD) {
            this.dir = Dir.D;
        } else if (!(this.bL || this.bU || this.bR || this.bD)) {
            this.dir = Dir.STOP;
        }
        if (Game.isMulti && this.dir != oldDir) {
            TankMoveMsg msg = new TankMoveMsg(this.id, this.x, this.y, this.dir, this.ptDir);
            this.tc.nc.send(msg);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (Game.isMulti && this.isDie) {
            return;
        }
        int key = e.getKeyCode();
        switch (key) {
            case 75: {
                if (Game.isMulti || !this.good || this.live) break;
                this.live = true;
                this.life = 9999;
                ++TankClient.count;
                break;
            }
            case 88: {
                if (TankClient.torpedo == 0) break;
                this.superFire();
                new com.xkzjsj.java07.tb.game.Audio(3);
                break;
            }
            case 32: {
                this.fire();
                new com.xkzjsj.java07.tb.game.Audio(2);
                break;
            }
            case 37: {
                this.bL = false;
                new com.xkzjsj.java07.tb.game.Audio(1);
                break;
            }
            case 38: {
                this.bU = false;
                new com.xkzjsj.java07.tb.game.Audio(1);
                break;
            }
            case 39: {
                this.bR = false;
                new com.xkzjsj.java07.tb.game.Audio(1);
                break;
            }
            case 40: {
                this.bD = false;
                new com.xkzjsj.java07.tb.game.Audio(1);
                break;
            }
            case 10: {
                Game.getJtf().grabFocus();
                break;
            }
            case 27: {
                System.exit(0);
            }
        }
        this.locateDir();
    }

    public Missile fire() {
        if (!this.live) {
            return null;
        }
        int x = this.x + this.WIDTH / 2 - 5;
        int y = this.y + this.HEIGHT / 2 - 5;
        Missile m = null;
        m = this.isPlayer ? new Missile(this.id, x, y, this.ptDir, this.good, this.tc, 10, 10, Color.YELLOW) : new Missile(this.id, x, y, this.ptDir, this.good, this.tc);
        this.tc.missiles.add(m);
        if (Game.isMulti) {
            MissileNewMsg msg = new MissileNewMsg(m);
            this.tc.nc.send(msg);
        }
        return m;
    }

    public Missile fire(Dir dir) {
        if (!this.live) {
            return null;
        }
        int x = this.x + this.WIDTH / 2 - 5;
        int y = this.y + this.HEIGHT / 2 - 5;
        Missile m = null;
        m = this.isPlayer ? new Missile(this.id, x, y, this.ptDir, this.good, this.tc, 10, 10, Color.YELLOW) : new Missile(this.id, x, y, this.ptDir, this.good, this.tc);
        this.tc.missiles.add(m);
        if (Game.isMulti) {
            MissileNewMsg msg = new MissileNewMsg(m);
            this.tc.nc.send(msg);
        }
        return m;
    }

    public void superFire() {
        Dir[] dirs = Dir.values();
        int i = 0;
        while (i < dirs.length) {
            Missile m = this.fire(dirs[i]);
            if (Game.isMulti) {
                MissileNewMsg msg = new MissileNewMsg(m);
                this.tc.nc.send(msg);
            }
            ++i;
        }
        --TankClient.torpedo;
    }

    public boolean CollidesWithWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean CollidesWithWalls(List<Wall> walls) {
        int i = 0;
        while (i < walls.size()) {
            Wall w = walls.get(i);
            if (this.live && this.getRect().intersects(w.getRect())) {
                this.stay();
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        int i = 0;
        while (i < tanks.size()) {
            Tank t = tanks.get(i);
            if (this != t && this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                this.stay();
                t.stay();
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean CollidesWithHome(Home h) {
        if (this.live && h.isLive() && this.getRect().intersects(h.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean CollidesWithRiver(River r) {
        if (this.live && this.getRect().intersects(r.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean CollidesWithRivers(List<River> rivers) {
        int i = 0;
        while (i < rivers.size()) {
            River t = rivers.get(i);
            if (this.live && this.getRect().intersects(t.getRect())) {
                this.stay();
                return true;
            }
            ++i;
        }
        return false;
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.WIDTH, this.HEIGHT);
    }

    public boolean eat(Blood b) {
        if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            this.life = Game.isMulti ? 1000 : 9999;
            b.setLive(false);
            this.tc.bloods.remove(b);
            new com.xkzjsj.java07.tb.game.Audio(4);
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

    public int getLife() {
        return this.life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isGood() {
        return this.good;
    }

    public boolean collidesWithTank(Tank myTank) {
        if (myTank == null) {
            return false;
        }
        if (this.live && myTank.isLive() && this.getRect().intersects(myTank.getRect())) {
            this.stay();
            myTank.stay();
            return true;
        }
        return false;
    }

    static int[] Dir() {
        int[] arrn;
        int[] arrn2 = dirs;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Dir.values().length];
        try {
            arrn[Dir.D.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Dir.L.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Dir.R.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Dir.STOP.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Dir.U.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        dirs = arrn;
        return dirs;
    }

    public class BloodBar {
        public void draw(Graphics g, boolean b) {
            Color c = g.getColor();
            if (Game.isMulti) {
                g.setColor(Color.RED);
                g.drawString("\u2764" + Tank.this.life, Tank.this.x, Tank.this.y - 10);
            } else if (b) {
                g.setColor(Color.GREEN);
                g.drawString("\u2764" + Tank.this.life, Tank.this.x, Tank.this.y - 10);
            } else {
                g.setColor(Color.RED);
                g.drawString("\u2764" + Tank.this.life, Tank.this.x, Tank.this.y - 10);
            }
            g.setColor(c);
        }
    }

}

