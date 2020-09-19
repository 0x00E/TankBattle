/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.scene.Scene
 */
package com.xkzjsj.java07.tb.view;

import java.io.IOException;

import com.xkzjsj.java07.tb.Game;
import com.xkzjsj.java07.tb.MainApp;

import javafx.fxml.FXML;

public class GameWindowController {
    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void multiplayer() throws IOException {
        Game.isMulti = true;
        MainApp.getStage().setScene(MainApp.getScene("view/MultiPlayer.fxml"));
    }

    @FXML
    public void singleplayer() {
        Game.isMulti = false;
        Game.start();
        MainApp.getStage().close();
    }

    @FXML
    public void option() {
        MainApp.getStage().setScene(MainApp.getScene("view/Option.fxml"));
    }
}

