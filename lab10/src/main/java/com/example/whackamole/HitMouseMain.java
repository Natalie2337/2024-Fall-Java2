package com.example.whackamole;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//The program entry for the game of Whack-a-mole
public class HitMouseMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Whack-a-mole");
        //The initial UI of the program is loaded from the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("hit_mouse.fxml"));
        Scene scene = new Scene(root, 560, 700); //create a scene
        stage.setScene(scene); //set the scene as the stage's scene
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // start the JavaFX application, which is then moved to the start method
    }

}
