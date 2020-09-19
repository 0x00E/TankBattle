/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game;

import java.awt.Color;
import java.awt.Graphics;

public class Chat {
    private static String chat = "";

    public static void send(String string) {
        chat = string;
    }

    public static void draw(Graphics g, int x, int y) {
        g.setColor(Color.ORANGE);
        g.drawString(chat, x, y - 30);
    }
}

