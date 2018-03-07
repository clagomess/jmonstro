package br.jmonstro.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static final String SYS_TITLE = "jMonstro";
    public static final String SYS_PATH = "br/jmonstro/";
    private static final String SYS_ICON = SYS_PATH + "resources/icon.png";

    @Override
    public void start(final Stage primaryStage) {
        Ui ui = new Ui();
        Scene scene = ui.newScene("main.fxml", 600, 600);

        if(scene != null) {
            primaryStage.setTitle(SYS_TITLE);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(SYS_ICON));
            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
