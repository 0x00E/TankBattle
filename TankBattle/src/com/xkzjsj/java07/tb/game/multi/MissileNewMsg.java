/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.game.Missile;
import com.xkzjsj.java07.tb.game.TankClient;

public class MissileNewMsg
implements Msg {
    int msgType = 3;
    TankClient tc;
    Missile m;

    public MissileNewMsg(Missile m) {
        this.m = m;
    }

    public MissileNewMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(this.msgType);
            dos.writeInt(this.m.tankId);
            dos.writeInt(this.m.id);
            dos.writeInt(this.m.x);
            dos.writeInt(this.m.y);
            dos.writeInt(this.m.dir.ordinal());
            dos.writeBoolean(this.m.good);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
            ds.send(dp);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int tankId = dis.readInt();
            if (tankId == this.tc.myTank.id) {
                return;
            }
            int id = dis.readInt();
            int x = dis.readInt();
            int y = dis.readInt();
            Dir dir = Dir.values()[dis.readInt()];
            boolean good = dis.readBoolean();
            Missile m = new Missile(tankId, x, y, dir, good, Game.getTc(), 10, 10, Color.YELLOW);
            m.id = id;
            this.tc.missiles.add(m);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

