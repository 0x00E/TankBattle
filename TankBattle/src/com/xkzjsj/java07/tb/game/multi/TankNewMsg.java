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

public class TankNewMsg
implements Msg {
    int msgType = 1;
    Tank tank;
    TankClient tc;

    public TankNewMsg(Tank tank) {
        this.tank = tank;
    }

    public TankNewMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(this.msgType);
            dos.writeInt(this.tank.id);
            dos.writeInt(this.tank.x);
            dos.writeInt(this.tank.y);
            dos.writeInt(this.tank.dir.ordinal());
            dos.writeBoolean(this.tank.good);
            dos.writeInt(this.tank.life);
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
            Tank t;
            int id = dis.readInt();
            if (this.tc.myTank.id == id) {
                return;
            }
            int x = dis.readInt();
            int y = dis.readInt();
            Dir dir = Dir.values()[dis.readInt()];
            boolean good = dis.readBoolean();
            int life = dis.readInt();
            if (life <= 0) {
                return;
            }
            boolean exist = false;
            int i = 0;
            while (i < this.tc.tanks.size()) {
                t = this.tc.tanks.get(i);
                if (t.id == id) {
                    exist = true;
                    break;
                }
                ++i;
            }
            if (!exist) {
                TankNewMsg tnMsg = new TankNewMsg(this.tc.myTank);
                this.tc.nc.send(tnMsg);
                t = new Tank(x, y, good, dir, this.tc, 5, 5, life);
                t.id = id;
                this.tc.tanks.add(t);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

