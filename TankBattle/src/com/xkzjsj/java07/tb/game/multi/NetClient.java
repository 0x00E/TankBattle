/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import com.xkzjsj.java07.tb.game.TankClient;

public class NetClient {
    TankClient tc;
    private int udpPort;
    String IP;
    DatagramSocket ds = null;

    public NetClient(TankClient tc) {
        this.tc = tc;
    }

    public void connect(String IP, int port) throws Throwable {
        this.IP = IP;
        try {
            this.ds = new DatagramSocket(this.udpPort);
        }
        catch (SocketException e) {
            throw new Throwable(e.getMessage());
        }
        Socket s = null;
        try {
            try {
                int id;
                s = new Socket(IP, port);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(this.udpPort);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                this.tc.myTank.id = id = dis.readInt();
                this.tc.myTank.good = id % 2 != 0;
                System.out.println("Connected to server! and server give me a ID:" + id);
            }
            catch (Throwable e) {
                throw new Throwable(e.getMessage());
            }
        }
        finally {
            if (s != null) {
                try {
                    s.close();
                    s = null;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        TankNewMsg msg = new TankNewMsg(this.tc.myTank);
        this.send(msg);
        new Thread(new UDPRecvThread(this)).start();
    }

    public void send(Msg msg) {
        msg.send(this.ds, this.IP, 51234);
    }

    public int getUdpPort() {
        return this.udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    private class UDPRecvThread
    implements Runnable {
        byte[] buf;
        NetClient netClient;

        private UDPRecvThread(NetClient netClient) {
            this.netClient = netClient;
            this.buf = new byte[1024];
        }

        @Override
        public void run() {
            while (this.netClient.ds != null) {
                DatagramPacket dp = new DatagramPacket(this.buf, this.buf.length);
                try {
                    this.netClient.ds.receive(dp);
                    this.parse(dp);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void parse(DatagramPacket dp) {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.buf, 0, dp.getLength());
            DataInputStream dis = new DataInputStream(bais);
            int msgType = 0;
            try {
                msgType = dis.readInt();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Msg msg = null;
            switch (msgType) {
                case 1: {
                    msg = new TankNewMsg(this.netClient.tc);
                    msg.parse(dis);
                    break;
                }
                case 2: {
                    msg = new TankMoveMsg(this.netClient.tc);
                    msg.parse(dis);
                    break;
                }
                case 3: {
                    msg = new MissileNewMsg(this.netClient.tc);
                    msg.parse(dis);
                    break;
                }
                case 4: {
                    msg = new TankDeadMsg(this.netClient.tc);
                    msg.parse(dis);
                    break;
                }
                case 5: {
                    msg = new MissileDeadMsg(this.netClient.tc);
                    msg.parse(dis);
                }
            }
        }
    }

}

