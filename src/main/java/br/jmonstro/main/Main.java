package br.jmonstro.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Main extends Application {
    private static final String SYS_TITLE = "jMonstro";
    private static final String SYS_PATH = "br/jmonstro/";
    private static final String SYS_ICON = SYS_PATH + "resources/icon.png";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(final Stage primaryStage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
        }catch (Exception ea){
            logger.warn(Main.class.getName(), ea);

            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource(SYS_PATH + "fxml/main.fxml");

                if(url != null) {
                    root = FXMLLoader.load(url);
                }
            }catch (Exception eb) {
                logger.warn(Main.class.getName(), eb);
            }
        }

        if(root != null) {
            primaryStage.setTitle(SYS_TITLE);
            primaryStage.setScene(new Scene(root, 600, 600));
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
