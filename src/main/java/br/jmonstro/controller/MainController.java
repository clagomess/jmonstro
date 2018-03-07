package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final Ui ui = new Ui();

    @FXML TextField txtPathJson;
    @FXML TreeView<String> tree;
    @FXML TextArea txtValor;
    @FXML ProgressBar progress;

    public void processarJsonAction(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar JSON");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
        chooser.getExtensionFilters().add(extFilter);

        File file = chooser.showOpenDialog(new Stage());

        if(file != null) {
            Platform.runLater(() -> {
                try {
                    progress.setProgress(-1);
                    txtPathJson.setText(file.getAbsolutePath());

                    JMonstroService jMonstroService = new JMonstroService();
                    TreeItem<String> root = jMonstroService.getTree(file.getName(), file.getAbsolutePath());

                    tree.setRoot(root);
                    tree.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> txtValor.setText(nv.getValue()));
                }catch (Exception e){
                    logger.error(MainController.class.getName(), e);
                    Ui.alertError(Alert.AlertType.ERROR, e.toString());
                } finally {
                    progress.setProgress(0);
                }
            });
        }
    }

    public void hexViewAction(){
        FXMLLoader loader = ui.fxmlLoad("hexview.fxml");
        Stage stage = new Stage();
        stage.setTitle("Hex View");
        stage.setScene(new Scene(loader.getRoot(), 600, 400));
        stage.show();

        HexViewController hvc = loader.getController();
        hvc.init(txtValor.getText());
    }
}
