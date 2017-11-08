package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final Ui ui = new Ui();

    @FXML TextField txtPathJson;
    @FXML TreeView<String> tree;
    @FXML TextArea txtValor;

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

    public void hexViewAction(){
        Scene scene = ui.newScene("hexview.fxml", 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Hex View");
        stage.setScene(scene);
        stage.show();
    }

    public static void alertError(String msg){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}
