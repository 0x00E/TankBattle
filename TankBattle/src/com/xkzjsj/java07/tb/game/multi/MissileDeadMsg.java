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

import com.xkzjsj.java07.tb.game.Missile;
import com.xkzjsj.java07.tb.game.Tank;
import com.xkzjsj.java07.tb.game.TankClient;

public class MissileDeadMsg
implements Msg {
    int msgType = 5;
    TankClient tc;
    int tankId;
    int id;

    public MissileDeadMsg(int tankId, int id) {
        this.tankId = tankId;
        this.id = id;
    }

    public MissileDeadMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int tankId = dis.readInt();
            int id = dis.readInt();
            int i = 0;
            while (i < this.tc.missiles.size()) {
                Missile m = this.tc.missiles.get(i);
                if (m.id == id) {
                    Tank t = this.tc.getTank(tankId);
                    m.hitTank(t);
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
            dos.writeInt(this.tankId);
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

