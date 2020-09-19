/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.xkzjsj.java07.tb.game.Chat;
import com.xkzjsj.java07.tb.game.Explode;
import com.xkzjsj.java07.tb.game.TankClient;

public class Game {
	public static final String GAME_VERSION = "0.0.1";
	public static boolean isMulti = true;
	public static boolean BACKGROUND_MUSIC = false;
	public static final int MULTI_BLOOD = 1000;
	public static final int MULTI_XSPEED = 5;
	public static final int MULTI_YSPEED = 5;
	public static final int MULTI_POWER = 100;
	public static final int MYTANK_BLOOD = 9999;
	public static final int MYTANK_XSPEED = 5;
	public static final int MYTANK_YSPEED = 5;
	public static final int MYTANK_POWER = 100;
	public static final int MYTANK_X = 220;
	public static final int MYTANK_Y = 560;
	public static final int TANK_BLOOD = 1000;
	public static final int TANK_POWER = 100;
	public static final int TANK_XSPEED = 1;
	public static final int TANK_YSPEED = 1;
	public static final int TANK_OTHER_WIDTH = 100;
	public static final int TANK_ACOUNT = 5;
	public static final int TORPEDO = 50;
	public static boolean CHEAT = false;
	private static TankClient tc = new TankClient();
	public static int CHAT_TIME = 3;
	private static JFrame jf = new JFrame();
	private static JPanel jp = new JPanel();
	private static JTextField jtf = new JTextField(30);
	private static JButton jb = new JButton("\u53d1\u9001");

	static {

		jtf.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					jb.doClick();
				}
			}
		});
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tc.requestFocusInWindow();
				Chat.send(jtf.getText());
				jtf.setText("");
				jb.setEnabled(false);
				new Thread() {

					@Override
					public void run() {
						int i = 0;
						while (i != Game.CHAT_TIME) {
							jb.setText("\u8bf7\u7b49\u5f85" + (Game.CHAT_TIME - i) + "\u79d2");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							++i;
						}
						jb.setEnabled(true);
						jb.setText("\u53d1\u9001");
						Chat.send("");
					}
				}.start();
			}

		});
		jp.add(jtf);
		jp.add(jb);
		jf.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jf.setTitle("TankBattle 0.0.1");
		jf.setLayout(new BorderLayout());
		jf.add((Component) jp, "North");
		jf.setContentPane(tc);
		jf.setBackground(Color.BLACK);
	}

	public static JFrame getJf() {
		return jf;
	}

	public static TankClient getTc() {
		return tc;
	}

	public static JTextField getJtf() {
		return jtf;
	}

	public static void show() {

		/**
		 * 全屏，在Windows 10系统中有BUG，黑屏
		jf.setUndecorated(true);
		GraphicsDevice dev;

		try {
			dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			dev.setFullScreenWindow(jf);
			dev.setDisplayMode(new DisplayMode(800, 600, 32, 0));
		} catch (Throwable t) {
			throw new RuntimeException("Getting screen device failed");
		}
		*/

		jf.setResizable(false);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		tc.requestFocusInWindow();
		tc.init();
	}

	public static void start() {
		Explode.init();
		tc.start();
		Game.show();
	}

}
