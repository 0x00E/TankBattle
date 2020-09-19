/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.multi.ConnDialog;
import com.xkzjsj.java07.tb.game.multi.Dir;
import com.xkzjsj.java07.tb.game.multi.MissileDeadMsg;
import com.xkzjsj.java07.tb.game.multi.NetClient;

public class TankClient
extends Panel
implements ActionListener {
    private static final long serialVersionUID = 1;
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    public static boolean printable = true;
    Random r = new Random();
    public static int count = 0;
    public static int level = 0;
    public static int score = 0;
    public static int torpedo = 50;
    MenuBar jmb = null;
    Menu jm1 = null;
    Menu jm2 = null;
    Menu jm3 = null;
    Menu jm4 = null;
    MenuItem jmi1 = null;
    MenuItem jmi2 = null;
    MenuItem jmi3 = null;
    MenuItem jmi4 = null;
    MenuItem jmi5 = null;
    MenuItem jmi6 = null;
    MenuItem jmi7 = null;
    MenuItem jmi8 = null;
    MenuItem jmi9 = null;
    MenuItem jmi10 = null;
    MenuItem jmi11 = null;
    Wall w1;
    Wall w2;
    Wall w3;
    Wall w4;
    Wall w5;
    Wall w6;
    Wall w7;
    Wall w8;
    Wall w9;
    Wall w10;
    Wall w11;
    Wall w12;
    public Tank myTank;
    Home home;
    List<Wall> walls;
    List<River> rivers;
    Blood b;
    List<Blood> bloods;
    List<Grass> grasses;
    public List<Explode> explodes;
    public List<Missile> missiles;
    public List<Tank> tanks;
    Image offScreenImage;
    public NetClient nc;
    ConnDialog dialog;

    public TankClient() {
        this.home = new Home(379, 556, this);
        this.walls = new ArrayList<Wall>();
        this.rivers = new ArrayList<River>();
        this.b = null;
        this.bloods = new ArrayList<Blood>();
        this.grasses = new ArrayList<Grass>();
        this.explodes = new ArrayList<Explode>();
        this.missiles = new ArrayList<Missile>();
        this.tanks = new ArrayList<Tank>();
        this.offScreenImage = null;
        this.nc = new NetClient(this);
        this.dialog = new ConnDialog();
    }

    public Missile getMissile(int id) {
        for (Missile t : this.missiles) {
            if (t.id != id) continue;
            return t;
        }
        return null;
    }

    public Tank getTank(int tankid) {
        for (Tank t : this.tanks) {
            if (t.id != tankid) continue;
            return t;
        }
        return null;
    }

    public void init() {
        if (this.myTank == null) {
            if (Game.isMulti) {
                this.myTank = new Tank(this.r.nextInt(800), this.r.nextInt(600), true, Dir.STOP, this, 5, 5, 1000);
                if (this.myTank.CollidesWithWalls(this.walls)) {
                    while (this.myTank.CollidesWithWalls(this.walls)) {
                        this.myTank = new Tank(this.r.nextInt(800), this.r.nextInt(600), true, Dir.STOP, this, 5, 5, 1000);
                    }
                }
            } else {
                this.myTank = new Tank(220, 560, true, Dir.STOP, this, 5, 5, 9999);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        this.init();
        if (score >= 500 * level) {
            new TankClient().start();
        }
        if (!Game.isMulti) {
            this.home.draw(g);
        }
        if (this.myTank.getLife() <= 0 && count == 1) {
            this.myTank.setLive(false);
            this.home.gameOver(g);
        }
        if (!Game.isMulti && this.r.nextInt(50) > 48) {
            this.newBlood();
        }
        if (!Game.isMulti && this.tanks.size() <= 0) {
            this.goHome();
            this.newTank();
        }
        int i = 0;
        while (i < this.walls.size()) {
            this.walls.get(i).draw(g);
            ++i;
        }
        i = 0;
        while (i < this.missiles.size()) {
            Missile m = this.missiles.get(i);
            m.hitWalls(this.walls);
            if (!Game.isMulti) {
                m.hitTanks(this.tanks);
                m.hitHome(this.home);
                m.hitTank(this.myTank);
            } else if (m.hitTank(this.myTank)) {
                MissileDeadMsg mdmMsg = new MissileDeadMsg(this.myTank.id, m.id);
                this.nc.send(mdmMsg);
            }
            m.draw(g);
            ++i;
        }
        i = 0;
        while (i < this.explodes.size()) {
            Explode e = this.explodes.get(i);
            e.draw(g);
            ++i;
        }
        i = 0;
        while (i < this.tanks.size()) {
            Tank t = this.tanks.get(i);
            t.CollidesWithWalls(this.walls);
            if (!Game.isMulti) {
                t.CollidesWithHome(this.home);
                this.myTank.collidesWithTanks(this.tanks);
                t.collidesWithTanks(this.tanks);
            }
            t.CollidesWithRivers(this.rivers);
            t.draw(g);
            ++i;
        }
        this.myTank.CollidesWithWalls(this.walls);
        if (!Game.isMulti) {
            this.myTank.CollidesWithHome(this.home);
        }
        this.myTank.draw(g);
        i = 0;
        while (i < this.rivers.size()) {
            this.rivers.get(i).draw(g);
            ++i;
        }
        i = 0;
        while (i < this.grasses.size()) {
            this.grasses.get(i).draw(g);
            ++i;
        }
        i = 0;
        while (i < this.bloods.size()) {
            Blood b = this.bloods.get(i);
            b.draw(g);
            this.myTank.eat(b);
            ++i;
        }
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        if (Game.isMulti) {
            g.drawString("\u6211\u7684ID:" + this.myTank.id, 10, 30);
            if (this.myTank.getLife() <= 0) {
                g.setColor(Color.RED);
                g.drawString("\u4f60\u6b7b\u4e86\uff0c\u5f53\u524d\u4e3a\u89c2\u6218\u6a21\u5f0f\uff01", 10, 50);
            } else {
                g.drawString("\u751f\u547d\u503c:" + this.myTank.getLife(), 10, 50);
            }
            g.setColor(Color.WHITE);
            g.drawString("\u9c7c\u96f7\u4e2a\u6570:" + torpedo, 10, 70);
        } else {
            g.drawString("\u8840\u5757\u6570\u91cf:" + this.bloods.size(), 10, 30);
            g.drawString("\u5b50\u5f39\u6570\u91cf:" + this.missiles.size(), 10, 50);
            g.drawString("\u70b8\u5f39\u6570\u91cf:" + this.explodes.size(), 10, 70);
            g.drawString("\u5766\u514b\u6570\u91cf:" + this.tanks.size(), 10, 90);
            g.drawString("\u5173\u5361:" + level, 10, 110);
            g.drawString("\u5206\u6570:" + score, 10, 130);
            g.drawString("\u751f\u547d\u503c:" + this.myTank.getLife(), 10, 150);
            g.drawString("\u590d\u6d3b\u6b21\u6570:" + count, 10, 170);
            g.drawString("\u9c7c\u96f7\u4e2a\u6570:" + torpedo, 10, 190);
        }
        g.setColor(c);
    }

    public void goHome() {
        this.myTank.tp(220, 560);
        this.myTank.life = 9999;
        torpedo = 50;
    }

    @Override
    public void update(Graphics g) {
        if (this.offScreenImage == null) {
            this.offScreenImage = this.createImage(800, 600);
        }
        Graphics gOffScreen = this.offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, 800, 600);
        gOffScreen.setColor(c);
        this.paint(gOffScreen);
        g.drawImage(this.offScreenImage, 0, 0, null);
    }

    public MenuBar getJmb() {
        return this.jmb;
    }

    public void newMenu() {
        this.jmb = new MenuBar();
        this.jm1 = new Menu("\u6e38\u620f");
        this.jm2 = new Menu("\u6682\u505c/\u7ee7\u7eed");
        this.jm3 = new Menu("\u5e2e\u52a9");
        this.jm4 = new Menu("\u6e38\u620f\u96be\u5ea6");
        this.jm1.setFont(new Font("\u5b8b\u4f53", 1, 15));
        this.jm2.setFont(new Font("\u5b8b\u4f53", 1, 15));
        this.jm3.setFont(new Font("TimesRoman", 1, 15));
        this.jm4.setFont(new Font("TimesRoman", 1, 15));
        this.jmi1 = new MenuItem("\u91cd\u65b0\u5f00\u59cb");
        this.jmi2 = new MenuItem("\u9000\u51fa");
        this.jmi11 = new MenuItem("\u5f00\u542f\u80cc\u666f\u97f3\u4e50");
        this.jmi3 = new MenuItem("\u6682\u505c");
        this.jmi4 = new MenuItem("\u7ee7\u7eed");
        this.jmi5 = new MenuItem("\u6e38\u620f\u8bf4\u660e");
        this.jmi6 = new MenuItem("\u96be\u5ea61");
        this.jmi7 = new MenuItem("\u96be\u5ea62");
        this.jmi8 = new MenuItem("\u96be\u5ea63");
        this.jmi9 = new MenuItem("\u96be\u5ea64");
        this.jmi10 = new MenuItem("\u64cd\u4f5c\u8bf4\u660e");
        this.jmi1.setFont(new Font("TimesRoman", 1, 15));
        this.jmi2.setFont(new Font("TimesRoman", 1, 15));
        this.jmi3.setFont(new Font("TimesRoman", 1, 15));
        this.jmi4.setFont(new Font("TimesRoman", 1, 15));
        this.jmi5.setFont(new Font("TimesRoman", 1, 15));
        this.jm1.add(this.jmi1);
        this.jm1.add(this.jmi2);
        this.jm1.add(this.jmi11);
        this.jm2.add(this.jmi3);
        this.jm2.add(this.jmi4);
        this.jm3.add(this.jmi5);
        this.jm3.add(this.jmi10);
        this.jm4.add(this.jmi6);
        this.jm4.add(this.jmi7);
        this.jm4.add(this.jmi8);
        this.jm4.add(this.jmi9);
        this.jmb.add(this.jm1);
        this.jmb.add(this.jm2);
        this.jmb.add(this.jm4);
        this.jmb.add(this.jm3);
        this.jmi1.addActionListener(this);
        this.jmi1.setActionCommand("NewGame");
        this.jmi2.addActionListener(this);
        this.jmi2.setActionCommand("Exit");
        this.jmi11.addActionListener(this);
        this.jmi11.setActionCommand("startMain");
        this.jmi3.addActionListener(this);
        this.jmi3.setActionCommand("Stop");
        this.jmi4.addActionListener(this);
        this.jmi4.setActionCommand("Continue");
        this.jmi5.addActionListener(this);
        this.jmi5.setActionCommand("help1");
        this.jmi10.addActionListener(this);
        this.jmi10.setActionCommand("help2");
        this.jmi6.addActionListener(this);
        this.jmi6.setActionCommand("level1");
        this.jmi7.addActionListener(this);
        this.jmi7.setActionCommand("level2");
        this.jmi8.addActionListener(this);
        this.jmi8.setActionCommand("level3");
        this.jmi9.addActionListener(this);
        this.jmi9.setActionCommand("level4");
    }

    public void newWall() {
        if (level == 1) {
            this.w1 = new Wall(210, 200, 100, 30, this);
            this.w2 = new Wall(210, 200, 30, 190, this);
            this.w3 = new Wall(280, 200, 30, 190, this);
            this.w4 = new Wall(210, 370, 100, 30, this);
            this.w5 = new Wall(440, 200, 100, 30, this);
            this.w6 = new Wall(440, 200, 30, 95, this);
            this.w7 = new Wall(470, 265, 70, 30, this);
            this.w8 = new Wall(510, 200, 30, 190, this);
            this.w9 = new Wall(440, 370, 100, 30, this);
            this.w10 = new Wall(379, 530, 43, 20, this);
            this.w11 = new Wall(280, 530, 30, 70, this);
            this.w12 = new Wall(510, 530, 30, 70, this);
            this.walls.add(this.w1);
            this.walls.add(this.w2);
            this.walls.add(this.w3);
            this.walls.add(this.w4);
            this.walls.add(this.w5);
            this.walls.add(this.w6);
            this.walls.add(this.w7);
            this.walls.add(this.w8);
            this.walls.add(this.w9);
            this.walls.add(this.w10);
            this.walls.add(this.w11);
            this.walls.add(this.w12);
            if (Game.CHEAT) {
                this.walls.add(new Wall(280, 530, 260, 20, this));
            }
        } else if (level == 2) {
            this.w1 = new Wall(210, 200, 100, 30, this);
            this.w2 = new Wall(210, 200, 30, 190, this);
            this.w3 = new Wall(280, 200, 30, 190, this);
            this.w4 = new Wall(210, 370, 100, 30, this);
            this.w5 = new Wall(410, 200, 30, 70, this);
            this.w6 = new Wall(410, 200, 90, 30, this);
            this.w7 = new Wall(470, 200, 30, 190, this);
            this.w8 = new Wall(470, 370, 90, 30, this);
            this.w9 = new Wall(530, 320, 30, 65, this);
            this.w10 = new Wall(379, 530, 43, 20, this);
            this.w11 = new Wall(280, 530, 30, 70, this);
            this.w12 = new Wall(510, 530, 30, 70, this);
            this.walls.add(this.w1);
            this.walls.add(this.w2);
            this.walls.add(this.w3);
            this.walls.add(this.w4);
            this.walls.add(this.w5);
            this.walls.add(this.w6);
            this.walls.add(this.w7);
            this.walls.add(this.w8);
            this.walls.add(this.w9);
            this.walls.add(this.w10);
            this.walls.add(this.w11);
            this.walls.add(this.w12);
        } else if (level >= 3) {
            this.w1 = new Wall(210, 200, 100, 30, this);
            this.w2 = new Wall(210, 200, 30, 190, this);
            this.w3 = new Wall(280, 200, 30, 190, this);
            this.w4 = new Wall(210, 370, 100, 30, this);
            this.w5 = new Wall(430, 200, 90, 30, this);
            this.w6 = new Wall(430, 285, 90, 30, this);
            this.w7 = new Wall(430, 370, 90, 30, this);
            this.w8 = new Wall(500, 200, 30, 200, this);
            this.w10 = new Wall(379, 530, 43, 20, this);
            this.w11 = new Wall(280, 530, 30, 70, this);
            this.w12 = new Wall(510, 530, 30, 70, this);
            this.walls.add(this.w1);
            this.walls.add(this.w2);
            this.walls.add(this.w3);
            this.walls.add(this.w4);
            this.walls.add(this.w5);
            this.walls.add(this.w6);
            this.walls.add(this.w7);
            this.walls.add(this.w8);
            this.walls.add(this.w10);
            this.walls.add(this.w11);
            this.walls.add(this.w12);
        }
    }

    public void newBlood() {
        this.b = new Blood();
        if (this.bloods.size() == 0) {
            this.bloods.add(this.b);
        }
    }

    public void newRiver() {
        River river;
        int i = 0;
        while (i < 6) {
            river = new River(18 + i * 30, 340);
            this.rivers.add(river);
            river = new River(18 + i * 30, 370);
            this.rivers.add(river);
            ++i;
        }
        i = 0;
        while (i < 6) {
            river = new River(568 + i * 30, 340);
            this.rivers.add(river);
            river = new River(568 + i * 30, 370);
            this.rivers.add(river);
            ++i;
        }
    }

    public void newGrass() {
        Grass grass;
        int i = 0;
        while (i < 10) {
            grass = new Grass((i + 1) * 18, 200);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 218);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 236);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 254);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 272);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 290);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 308);
            this.grasses.add(grass);
            grass = new Grass((i + 1) * 18, 326);
            this.grasses.add(grass);
            ++i;
        }
        i = 1;
        while (i <= 11) {
            grass = new Grass(376, 182 + i * 18);
            this.grasses.add(grass);
            grass = new Grass(394, 182 + i * 18);
            this.grasses.add(grass);
            grass = new Grass(340, 182 + i * 18);
            this.grasses.add(grass);
            grass = new Grass(358, 182 + i * 18);
            this.grasses.add(grass);
            ++i;
        }
        i = 0;
        while (i < 10) {
            grass = new Grass(550 + (i + 1) * 18, 200);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 218);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 236);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 254);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 272);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 290);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 308);
            this.grasses.add(grass);
            grass = new Grass(550 + (i + 1) * 18, 326);
            this.grasses.add(grass);
            ++i;
        }
    }

    public void newTank() {
        int i = 0;
        while (i < 5) {
            Tank t = null;
            t = i < 2 ? new Tank(50 + 100 * (i + 1), 60, false, Dir.D, this) : new Tank(50 + 100 * (i + 1), 60, false, Dir.R, this);
            this.tanks.add(t);
            ++i;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("NewGame")) {
            printable = false;
            Object[] options = new Object[]{"\u786e\u5b9a", "\u53d6\u6d88"};
            int response = JOptionPane.showOptionDialog(this, "\u60a8\u786e\u8ba4\u8981\u91cd\u65b0\u5f00\u59cb\uff01", "", 0, 3, null, options, options[0]);
            if (response == 0) {
                score = 0;
                this.setLevel(1);
                printable = true;
                new TankClient().start();
            } else {
                printable = true;
                new Thread(new PaintThread(this, null)).start();
            }
        } else if (e.getActionCommand().endsWith("Stop")) {
            printable = false;
        } else if (e.getActionCommand().equals("Continue")) {
            if (!printable) {
                printable = true;
                new Thread(new PaintThread(this, null)).start();
            }
        } else if (e.getActionCommand().equals("Exit")) {
            printable = false;
            Object[] options = new Object[]{"\u786e\u5b9a", "\u53d6\u6d88"};
            int response = JOptionPane.showOptionDialog(this, "\u60a8\u786e\u8ba4\u8981\u9000\u51fa\u5417", "", 0, 3, null, options, options[0]);
            if (response == 0) {
                System.out.println("\u9000\u51fa");
                System.exit(0);
            } else {
                printable = true;
                new Thread(new PaintThread(this, null)).start();
            }
        } else if (e.getActionCommand().equals("help1")) {
            printable = false;
            JOptionPane.showMessageDialog(null, "\u968f\u7740\u4eba\u4eec\u7cbe\u795e\u6587\u5316\u751f\u6d3b\u7684\u65e5\u76ca\u4e30\u5bcc,\u6e38\u620f\u6210\u4e3a\u4e86\u4eba\u4eec\u751f\u6d3b\u4e2d\u5fc5\u4e0d\u53ef\u5c11\u7684\u4e00\u90e8\u5206\n90\u300a\u5766\u514b\u5927\u6218\u300b\u66f4\u662f90\u540e\u4e00\u4ee3\u4eba\u7ae5\u5e74\u7684\u56de\u5fc6,\u4e5f\u662f\u4e00\u6b3e\u7ecf\u5178\u6e38\u620f\u3002\n\u5f00\u53d1java\u7248\u5766\u514b\u5927\u6218\u6709\u5229\u7528\u66f4\u6df1\u5165\u7406\u89e3java\u9762\u5411\u5bf9\u8c61\u7f16\u7a0b\u3001swing\u754c\u9762\u7f16\u7a0b\u3001\u591a\u7ebf\u7a0b\u7f16\u7a0b\n\u53c2\u8003:\u9a6c\u58eb\u5175\u5766\u514b\u5927\u6218\u89c6\u9891\u6559\u7a0b\u3001\u4ee5\u53ca\u4e92\u8054\u7f51\u8d44\u6e90\n\u4f5c\u8005\u90ae\u7bb1:1275302036@qq.com\n\u82e5\u6709\u5173\u5185\u5bb9\u4fb5\u72af\u4e86\u60a8\u7684\u6743\u76ca,\u8bf7\u53ca\u65f6\u8054\u7cfb\u4f5c\u8005\u5220\u9664,\u8c22\u8c22!", "\u63d0\u793a\uff01", 1);
            this.setVisible(true);
            printable = true;
            new Thread(new PaintThread(this, null)).start();
        } else if (e.getActionCommand().equals("help2")) {
            printable = false;
            JOptionPane.showMessageDialog(null, "\u7528\u2192 \u2190 \u2191 \u2193\u63a7\u5236\u65b9\u5411,CTRL\u952e\u76d8\u53d1\u5c04,J\u8d85\u7ea7\u70ae\u5f39,K\u590d\u6d3b(\u53ea\u80fd\u590d\u6d3b\u4e00\u6b21),R\u91cd\u65b0\u5f00\u59cb\uff01\n\u4f5c\u8005\u90ae\u7bb1:1275302036@qq.com\n\u82e5\u6709\u5173\u5185\u5bb9\u4fb5\u72af\u4e86\u60a8\u7684\u6743\u76ca,\u8bf7\u53ca\u65f6\u8054\u7cfb\u4f5c\u8005\u5220\u9664,\u8c22\u8c22!", "\u63d0\u793a\uff01", 1);
            this.setVisible(true);
            printable = true;
            new Thread(new PaintThread(this, null)).start();
        } else if (e.getActionCommand().equals("level1")) {
            new TankClient().start();
        } else if (e.getActionCommand().equals("level2")) {
            new TankClient().start();
        } else if (e.getActionCommand().equals("level3")) {
            new TankClient().start();
        } else if (e.getActionCommand().equals("level4")) {
            new TankClient().start();
        } else if (e.getActionCommand().equals("startMain")) {
            new com.xkzjsj.java07.tb.game.Audio(0);
        }
    }

    public void setLevel(int level) {
        if (level > 10) {
            level = 10;
        }
        TankClient.level = level;
    }

    public void start() {
        this.setLevel(level + 1);
        if (Game.BACKGROUND_MUSIC) {
            new com.xkzjsj.java07.tb.game.Audio(0);
        }
        this.grasses.clear();
        this.rivers.clear();
        this.missiles.clear();
        this.tanks.clear();
        this.explodes.clear();
        this.walls.clear();
        this.newMenu();
        this.newWall();
        this.newRiver();
        this.newGrass();
        if (!Game.isMulti) {
            this.newTank();
        }
        this.setPreferredSize(new Dimension(800, 600));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width - this.getSize().width) / 2, (dim.height - this.getSize().height) / 2);
        this.addKeyListener(new KeyMonitor(this, null));
        this.setVisible(true);
        new Thread(new PaintThread(this, null)).start();
    }

    private class KeyMonitor
    extends KeyAdapter {
    	TankClient tankClient;

        private KeyMonitor(TankClient tankClient) {
            this.tankClient = tankClient;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            this.tankClient.myTank.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            this.tankClient.myTank.keyPressed(e);
        }

       	public KeyMonitor(TankClient tankClient, KeyMonitor keyMonitor) {
            this.tankClient=tankClient;
            new KeyMonitor(tankClient);
        }
    }

    private class PaintThread
    implements Runnable {
      TankClient tankClient;

        private PaintThread(TankClient tankClient) {
            this.tankClient = tankClient;
        }

        @Override
        public void run() {
            while (TankClient.printable) {
                this.tankClient.repaint();
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public PaintThread(TankClient tankClient, PaintThread paintThread) {
        	this.tankClient = tankClient;
            new PaintThread(tankClient);
        }
    }

}

