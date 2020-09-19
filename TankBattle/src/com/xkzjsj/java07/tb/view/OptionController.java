/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.scene.Scene
 */
package com.xkzjsj.java07.tb.view;

import com.xkzjsj.java07.tb.MainApp;

import javafx.fxml.FXML;

public class OptionController {
    @FXML
    public void exit() {
        MainApp.getStage().setScene(MainApp.getScene("view/GameWindow.fxml"));
    }

    @FXML
    public void confirm() {
    }
}

