package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

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
                progress.setProgress(-1);
                txtPathJson.setText(file.getAbsolutePath());
            });

            new Thread(() -> {
                try {
                    JMonstroService jMonstroService = new JMonstroService();
                    TreeItem<String> root = jMonstroService.getTree(file.getName(), file.getAbsolutePath());

                    Platform.runLater(() -> {
                        tree.setRoot(root);
                        tree.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> txtValor.setText(nv.getValue()));
                        progress.setProgress(0);
                    });
                }catch (Exception e){
                    logger.error(MainController.class.getName(), e);
                    Platform.runLater(() -> progress.setProgress(0));
                    Ui.alertError(Alert.AlertType.ERROR, e.toString());
                }
            }).start();
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

    public void imageViewAction(){
        FXMLLoader loader = ui.fxmlLoad("imageview.fxml");
        Stage stage = new Stage();
        stage.setTitle("Image View");
        stage.setScene(new Scene(loader.getRoot(), 640, 480));
        stage.show();

        ImageViewController hvc = loader.getController();
        hvc.init(txtValor.getText());
    }

    public void saveBinAction(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save BIN");
        chooser.setInitialFileName("file.bin");

        File file = chooser.showSaveDialog(new Stage());

        if(file != null){
            try{
                byte[] content = HexViewerService.parse(txtValor.getText());
                content = Base64.getDecoder().decode(content);

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content);
                fos.close();
            } catch (IOException e) {
                logger.error(MainController.class.getName(), e);
            }
        }
    }
}
