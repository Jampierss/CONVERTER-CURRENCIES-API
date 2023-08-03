package com.alura.transformerconverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TransformerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TransformerApplication.class.getResource("transformer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TRANSFORMER");

        Image w_icon = new Image(Objects.requireNonNull(TransformerApplication.class.getResource("/images/autobots.png")).toString());
        stage.getIcons().add(w_icon);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}