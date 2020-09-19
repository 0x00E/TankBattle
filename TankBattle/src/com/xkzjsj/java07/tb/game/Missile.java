/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.multi.Dir;

public class Missile {
	public int XSPEED = 5;
	public int YSPEED = 5;
	public Color mcolor = Color.RED;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public int x;
	public int y;
	public Dir dir;
	public boolean live = true;
	public boolean good = true;
	private static int ID = 1;
	public TankClient tc;
	public int id;
	public int tankId;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	private static /* synthetic */ int[] $SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir;

	static {
		imgs.put("L", new ImageIcon("./img/missileL.gif").getImage());
		imgs.put("U", new ImageIcon("./img/missileU.gif").getImage());
		imgs.put("R", new ImageIcon("./img/missileR.gif").getImage());
		imgs.put("D", new ImageIcon("./img/missileD.gif").getImage());
	}

	public Missile(int tankId, int x, int y, Dir dir) {
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = ID++;
	}

	public Missile(int tankId, int x, int y, Dir dir, boolean good, TankClient tc) {
		this(tankId, x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	public Missile(int tankId, int x, int y, Dir dir, boolean good, TankClient tc, int xspeed, int yspeed,
			Color mcolor) {
		this(tankId, x, y, dir);
		this.tc = tc;
		this.good = good;
		this.XSPEED = xspeed;
		this.YSPEED = yspeed;
		this.mcolor = mcolor;
	}

	public void draw(Graphics g2) {
		if (!this.live) {
			this.tc.missiles.remove(this);
			return;
		}
		switch (Missile.$SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir()[this.dir.ordinal()]) {
		case 1: {
			g2.drawImage(imgs.get("L"), this.x, this.y, null);
			break;
		}
		case 2: {
			g2.drawImage(imgs.get("U"), this.x, this.y, null);
			break;
		}
		case 3: {
			g2.drawImage(imgs.get("R"), this.x, this.y, null);
			break;
		}
		case 4: {
			g2.drawImage(imgs.get("D"), this.x, this.y, null);
			break;
		}
		}
		this.move();
	}

	private void move() {
		switch (Missile.$SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir()[this.dir.ordinal()]) {
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
		if (this.x < 0 || this.y < 0 || this.x > 800 || this.y > 600) {
			this.live = false;
		}
	}

	public boolean isLive() {
		return this.live;
	}

	public Rectangle getRect() {
		return new Rectangle(this.x, this.y, 10, 10);
	}

	public boolean hitTank(Tank t) {
		if (this.live && 
				this.getRect().intersects(t.getRect()) && 
				t.isLive() &&
				 (this.good != t.isGood() || Game.isMulti) &&
				(this.tankId != t.id || !Game.isMulti)) 
		{
			if (Game.isMulti) {
				t.setLife(t.getLife() - 100);
				if (t.getLife() <= 0) {
					t.setLive(false);
				}
			} else if (t.isGood()) {
				t.setLife(t.getLife() - 100);
				if (t.getLife() <= 0) {
					t.setLive(false);
				}
			} else {
				t.setLife(t.getLife() - 100);
				if (t.getLife() <= 0) {
					t.setLive(false);
					TankClient.score += 100;
				}
			}
			this.live = false;
			Explode e = new Explode(this.x, this.y, this.tc);
			this.tc.explodes.add(e);
			new com.xkzjsj.java07.tb.game.Audio(3);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		int i = 0;
		while (i < tanks.size()) {
			if (this.hitTank(tanks.get(i))) {
				return true;
			}
			++i;
		}
		return false;
	}

	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}

	public boolean hitWalls(List<Wall> walls) {
		int i = 0;
		while (i < walls.size()) {
			Wall w = walls.get(i);
			if (this.live && this.getRect().intersects(w.getRect())) {
				this.live = false;
				return true;
			}
			++i;
		}
		return false;
	}

	public boolean hitHome(Home h) {
		if (this.live && h.isLive() && this.getRect().intersects(h.getRect())) {
			this.live = false;
			h.setLive(false);
			return true;
		}
		return false;
	}

	static /* synthetic */ int[] $SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir() {
		int[] arrn;
		int[] arrn2 = $SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir;
		if (arrn2 != null) {
			return arrn2;
		}
		arrn = new int[Dir.values().length];
		try {
			arrn[Dir.D.ordinal()] = 4;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Dir.L.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Dir.R.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Dir.STOP.ordinal()] = 5;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Dir.U.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		$SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir = arrn;
		return $SWITCH_TABLE$com$xkzjsj$java07$tb$game$multi$Dir;
	}
}
