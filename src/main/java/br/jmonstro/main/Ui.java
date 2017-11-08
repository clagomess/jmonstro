package br.jmonstro.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Ui {
    private static final Logger logger = LoggerFactory.getLogger(Ui.class);

    public Scene newScene(String fxmlFile, Integer width, Integer height) {
        Parent fxml = null;

        try {
            fxml = FXMLLoader.load(getClass().getResource("../fxml/" + fxmlFile));
        }catch (Exception ea){
            logger.warn(Main.class.getName(), ea);

            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource(Main.SYS_PATH + "fxml/" + fxmlFile);

                if(url != null) {
                    fxml = FXMLLoader.load(url);
                }
            }catch (Exception eb) {
                logger.warn(Main.class.getName(), eb);
            }
        }

        if(fxml != null){
            return new Scene(fxml, width, height);
        }else{
            logger.warn(String.format("Não foi possível abrir: %s", fxmlFile));
            return null;
        }
    }
}
