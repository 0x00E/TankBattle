/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.xkzjsj.java07.tb.game.Tank;
import com.xkzjsj.java07.tb.game.TankClient;

public class TankMoveMsg
implements Msg {
    int msgType = 2;
    int x;
    int y;
    int id;
    Dir ptDir;
    Dir dir;
    TankClient tc;

    public TankMoveMsg(int id, int x, int y, Dir dir, Dir ptDir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ptDir = ptDir;
    }

    public TankMoveMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (this.tc.myTank.id == id) {
                return;
            }
            int x = dis.readInt();
            int y = dis.readInt();
            Dir dir = Dir.values()[dis.readInt()];
            Dir ptDir = Dir.values()[dis.readInt()];
            int i = 0;
            while (i < this.tc.tanks.size()) {
                Tank t = this.tc.tanks.get(i);
                if (t.id == id) {
                    t.x = x;
                    t.y = y;
                    t.dir = dir;
                    t.ptDir = ptDir;
                    break;
                }
                ++i;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(this.msgType);
            dos.writeInt(this.id);
            dos.writeInt(this.x);
            dos.writeInt(this.y);
            dos.writeInt(this.dir.ordinal());
            dos.writeInt(this.ptDir.ordinal());
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
}

