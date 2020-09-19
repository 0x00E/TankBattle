/*
 * Decompiled with CFR 0_123.
 */
package com.xkzjsj.java07.tb.game.multi;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.xkzjsj.java07.tb.Game;

public class ConnDialog
extends JDialog {
    private static final long serialVersionUID = 1;
    Button b = new Button("Go");
    TextField tfIP = new TextField("127.0.0.1", 12);
    TextField tfPort = new TextField("51235", 4);
    TextField tfMyUDPPort;
    static Random r = new Random();

    public ConnDialog() {
        super((Frame)Game.getJf(), true);
        this.tfMyUDPPort = new TextField(String.valueOf(this.getPort()), 4);
        this.setTitle("\u8fde\u63a5\u670d\u52a1\u5668");
        this.setLayout(new FlowLayout());
        this.add(new Label("IP:"));
        this.add(this.tfIP);
        this.add(new Label("Port:"));
        this.add(this.tfPort);
        this.add(new Label("My UDP Port:"));
        this.add(this.tfMyUDPPort);
        this.add(this.b);
        this.setLocation(300, 300);
        this.setResizable(false);
        this.pack();
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                ConnDialog.this.setVisible(false);
            }
        });
        this.b.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Game.start();
                String IP = ConnDialog.this.tfIP.getText().trim();
                int port = Integer.parseInt(ConnDialog.this.tfPort.getText().trim());
                int myUDPPort = Integer.parseInt(ConnDialog.this.tfMyUDPPort.getText().trim());
                Game.getTc().nc.setUdpPort(myUDPPort);
                try {
                    Game.getTc().nc.connect(IP, port);
                    ConnDialog.this.dispose();
                }
                catch (Throwable e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    System.exit(0);
                }
            }
        });
    }

    private int getPort() {
        return 50000 + r.nextInt(10000);
    }

}

