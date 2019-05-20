package com.bkpp;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    private static Stage primaryStage;

    public static void main( String[] args )
    {
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.parameters"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("CPS Pawel Pomaranski & Bartosz Kacperski");
        stage.show();
        primaryStage = stage;
    }
}
