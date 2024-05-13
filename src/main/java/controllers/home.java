package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class home extends Application {
    public static int userID;
    public static String email ;
    public static int id_role ;
    public static String imagee;
    public static String codeConfirmation;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/seconnecter.fxml"));
        try {
            Parent root=loader.load();
            Scene scene =new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
