package br.jmonstro.controller;

import br.jmonstro.service.JMonstroService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MainController extends MainForm {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public void processarJsonAction(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar JSON");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
        chooser.getExtensionFilters().add(extFilter);

        File file = chooser.showOpenDialog(new Stage());

        if(file != null) {
            txtPathJson.setText(file.getAbsolutePath());

            try {
                JMonstroService jMonstroService = new JMonstroService();
                TreeItem<String> root = jMonstroService.getTree(file.getName(), file.getAbsolutePath());

                tree.setRoot(root);

                tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    txtValor.setText(newValue.getValue());
                });
            }catch (IOException|ParseException e){
                logger.warn(MainController.class.getName(), e);
                alertError(e.toString());
            }
        }
    }

    public static void alertError(String msg){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}
