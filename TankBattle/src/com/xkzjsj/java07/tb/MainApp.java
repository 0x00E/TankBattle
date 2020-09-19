/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.layout.Pane
 *  javafx.stage.Stage
 */
package com.xkzjsj.java07.tb;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp
extends Application {
    private static Stage primaryStage;
    private static Pane rootLayout;
    private static FXMLLoader loader;

    public static void setLoader(FXMLLoader loader) {
        MainApp.loader = loader;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public void start(Stage primaryStage) {
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("TankBattle 0.0.1");
        primaryStage.setResizable(false);
        this.initRootLayout();
    }

    public void initRootLayout() {
        try {
            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameWindow.fxml"));
            rootLayout = (Pane)loader.load();
            Scene scene = new Scene((Parent)rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        MainApp.launch((String[])args);
    }

    public static Scene getScene(String viewPath) {
        MainApp.setLoader(new FXMLLoader());
        MainApp.getLoader().setLocation(MainApp.class.getResource(viewPath));
        try {
            return new Scene((Parent)MainApp.getLoader().load());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getObject(String viewPath) {
        MainApp.setLoader(new FXMLLoader());
        MainApp.getLoader().setLocation(MainApp.class.getResource(viewPath));
        try {
            return MainApp.getLoader().load();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

