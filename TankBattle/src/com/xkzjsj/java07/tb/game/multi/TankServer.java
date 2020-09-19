/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	private static int ID = 10000;
	public static final int TCP_PORT = 51235;
	public static final int UDP_PORT = 51234;
	public static List<Client> clients = new ArrayList<Client>();

	@SuppressWarnings("resource")
	public void start() throws Throwable {
		System.out.println("TankServer Start Done!");
		new Thread(new UDPThread(this)).start();
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(51235);
		} catch (IOException e) {
			e.printStackTrace();
		}
		do {
			Socket s = null;
			try {
				try {
					s = ss.accept();
					DataInputStream dis = new DataInputStream(s.getInputStream());
					String IP = s.getInetAddress().getHostAddress();
					int udpPort = dis.readInt();
					Client c = new Client(IP, udpPort);
					clients.add(c);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					dos.writeInt(ID++);
					System.out.println("A Client Connect! Addr- " + s.getInetAddress() + ":" + s.getPort()
							+ "----UDP Port:" + udpPort);
					continue;
				} catch (Throwable e) {
					throw new Throwable(e.getMessage());
				}
			} finally {
				if (s != null) {
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} while (true);
	}

	public static void main(String[] args) {
		try {
			new TankServer().start();
		} catch (Throwable e) {
			System.err.println("[ERROR]" + e.getMessage());
		}
	}

	private class Client {
		String IP;
		int udpPort;

		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}

	private class UDPThread implements Runnable {
		byte[] buf;

		private UDPThread(TankServer tankServer) {
			this.buf = new byte[1024];
		}

		@Override
		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(51234);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("UDP thread started at port :" + UDP_PORT);
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(this.buf, this.buf.length);
				try {
					ds.receive(dp);
					int i = 0;
					while (i < TankServer.clients.size()) {
						Client c = TankServer.clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP, c.udpPort));
						ds.send(dp);
						++i;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
