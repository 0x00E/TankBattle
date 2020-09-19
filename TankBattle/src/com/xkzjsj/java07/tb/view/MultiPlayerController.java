/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.scene.Scene
 */
package com.xkzjsj.java07.tb.view;

import javax.swing.SwingUtilities;

import com.xkzjsj.java07.tb.MainApp;
import com.xkzjsj.java07.tb.game.multi.ConnDialog;

import javafx.fxml.FXML;

public class MultiPlayerController {
    @FXML
    public void home() {
        MainApp.getStage().setScene(MainApp.getScene("view/GameWindow.fxml"));
    }

    @FXML
    public void directConnect() {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                new ConnDialog().setVisible(true);
            }
        });
        MainApp.getStage().close();
    }

}

