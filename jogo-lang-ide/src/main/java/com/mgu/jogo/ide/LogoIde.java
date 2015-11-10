package com.mgu.jogo.ide;

import com.mgu.jogo.ide.editor.EditorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class LogoIde extends Application {

    private EditorController controller;

    @Override
    public void start(final Stage stage) throws Exception {
        final URL fxmlLocation = getClass().getResource("editor/editor.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxmlLocation);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        final Parent root = fxmlLoader.load(fxmlLocation.openStream());
        this.controller = fxmlLoader.getController();
        final Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(LogoIde.class.getResource("logo-keywords.css").toExternalForm());
        stage.setTitle("JOGO IDE");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        this.controller.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}