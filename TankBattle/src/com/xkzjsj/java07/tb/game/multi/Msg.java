/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
    public static final int TANK_NEW_MSG = 1;
    public static final int TANK_MOVE_MSG = 2;
    public static final int MISSILE_NEW_MSG = 3;
    public static final int TANK_DEAD_MSG = 4;
    public static final int MISSILE_DEAD_MSG = 5;

    public void send(DatagramSocket var1, String var2, int var3);

    public void parse(DataInputStream var1);
}

