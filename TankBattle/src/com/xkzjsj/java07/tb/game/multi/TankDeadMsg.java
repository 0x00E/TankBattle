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

public class TankDeadMsg
implements Msg {
    int msgType = 4;
    TankClient tc;
    int id;

    public TankDeadMsg(int id) {
        this.id = id;
    }

    public TankDeadMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (this.tc.myTank.id == id) {
                return;
            }
            int i = 0;
            while (i < this.tc.tanks.size()) {
                Tank t = this.tc.tanks.get(i);
                if (t.id == id) {
                    t.setLive(false);
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

