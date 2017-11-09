package br.jmonstro.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Ui {
    private static final Logger logger = LoggerFactory.getLogger(Ui.class);

    public FXMLLoader fxmlLoad(String fxmlFile){
        FXMLLoader fxml = null;

        try {
            fxml = new FXMLLoader(getClass().getResource("../fxml/" + fxmlFile));
            fxml.setRoot(fxml.load());
        }catch (Exception ea){
            logger.warn(Main.class.getName(), ea);

            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource(Main.SYS_PATH + "fxml/" + fxmlFile);

                if(url != null) {
                    fxml = new FXMLLoader(url);
                    fxml.setRoot(fxml.load());
                }
            }catch (Exception eb) {
                logger.warn(Main.class.getName(), eb);
            }
        }

        return fxml;
    }

    public Scene newScene(String fxmlFile, Integer width, Integer height) {
        return new Scene(fxmlLoad(fxmlFile).getRoot(), width, height);
    }
}
