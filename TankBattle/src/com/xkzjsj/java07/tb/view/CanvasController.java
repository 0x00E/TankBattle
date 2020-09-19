/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.scene.layout.Pane
 */
package com.xkzjsj.java07.tb.view;

import com.xkzjsj.java07.tb.MainApp;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class CanvasController {
    private static Pane instance = (Pane)MainApp.getObject("view/Canvas.fxml");

    public CanvasController() {
        System.out.println((Object)instance);
    }

    @FXML
    public void keypress() {
    }
}

