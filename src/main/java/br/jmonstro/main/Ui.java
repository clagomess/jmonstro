package br.jmonstro.main;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Ui {
    private static final Logger logger = LoggerFactory.getLogger(Ui.class);

    public FXMLLoader fxmlLoad(String fxmlFile){
        FXMLLoader fxml = null;

        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("fxml/" + fxmlFile);

            if(url == null){
                throw new Exception("Falha ao carregar o " + fxmlFile);
            }

            fxml = new FXMLLoader(url);
            fxml.setRoot(fxml.load());
        }catch (Throwable e){
            logger.error(Ui.class.getName(), e);
        }

        return fxml;
    }

    public Scene newScene(String fxmlFile, Integer width, Integer height) {
        return new Scene(fxmlLoad(fxmlFile).getRoot(), width, height);
    }

    public static void alertError(Alert.AlertType alertType, String msg){
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}
