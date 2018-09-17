package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
import br.jmonstro.service.JMonstroService;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class MainController {
    private static final Ui ui = new Ui();

    public static final String MSG_ERRO_BASE64 = "Não foi possível carregar valor como Base64";

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
                        tree.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) ->
                            txtValor.setText(HexViewerService.parse(nv.getValue()))
                        );
                        progress.setProgress(0);
                    });
                }catch (Exception e){
                    log.error(MainController.class.getName(), e);
                    Platform.runLater(() -> progress.setProgress(0));
                    Ui.alertError(Alert.AlertType.ERROR, e.toString());
                }
            }).start();
        }
    }

    public void hexViewAction(Event event){
        if(!validarTxtValor()){return;}

        final Button button = (Button) event.getTarget();

        FXMLLoader loader = ui.fxmlLoad("hexview.fxml");
        Stage stage = new Stage();
        stage.setTitle("Hex View");
        stage.setScene(new Scene(loader.getRoot(), 600, 400));

        HexViewController hvc = loader.getController();
        boolean showController = hvc.init(button.getText().contains("Base64"), txtValor.getText());

        if(showController){
            stage.show();
        }else {
            stage.close();
        }
    }

    public void imageViewAction(){
        if(!validarTxtValor()){return;}

        FXMLLoader loader = ui.fxmlLoad("imageview.fxml");
        Stage stage = new Stage();
        stage.setTitle("Image View");
        stage.setScene(new Scene(loader.getRoot(), 640, 480));

        ImageViewController hvc = loader.getController();
        boolean showController = hvc.init(txtValor.getText());

        if(showController){
            stage.show();
        }else {
            stage.close();
        }
    }

    public void saveBinAction(Event event){
        if(!validarTxtValor()){return;}

        final Button button = (Button) event.getTarget();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save BIN");
        chooser.setInitialFileName("file.bin");

        File file = chooser.showSaveDialog(new Stage());

        if(file != null){
            try{
                byte[] content = txtValor.getText().getBytes();

                if(button.getText().contains("Base64")) {
                    content = Base64.getDecoder().decode(content);
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content);
                fos.close();
            } catch (IOException e) {
                log.error(MainController.class.getName(), e);
                Ui.alertError(Alert.AlertType.WARNING, e.getMessage());
            }
        }
    }

    public void rawToImageAction(){
        if(!validarTxtValor()){return;}

        FXMLLoader loader = ui.fxmlLoad("rawtoimage.fxml");
        Stage stage = new Stage();
        stage.setTitle("RawToImage View");
        stage.setScene(new Scene(loader.getRoot(), 640, 480));
        stage.show();

        RawToImageController hvc = loader.getController();
        boolean showController = hvc.init(txtValor.getText());

        if(showController){
            stage.show();
        }else {
            stage.close();
        }
    }

    private Boolean validarTxtValor(){
        String content = txtValor.getText();

        if(content == null || "".equals(content.trim())){
            Ui.alertError(Alert.AlertType.WARNING, "É necessário ter algum valor para a opção selecionada");
            return false;
        }else{
            return true;
        }
    }
}
