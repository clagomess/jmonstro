package br.jmonstro.controller;

import br.jmonstro.bean.MainForm;
import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class MainController extends MainForm {
    private static final Ui ui = new Ui();
    public static final String MSG_ERRO_BASE64 = "Não foi possível carregar valor como Base64";

    public void processarJsonAction(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar JSON");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
        chooser.getExtensionFilters().add(extFilter);

        File file = chooser.showOpenDialog(new Stage());

        JMonstroService jMonstroService = new JMonstroService();
        jMonstroService.processar(file, this);
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
                Ui.alert(Alert.AlertType.WARNING, e.getMessage());
            }
        }
    }

    public void rawToImageAction(){
        if(!validarTxtValor()){return;}

        FXMLLoader loader = ui.fxmlLoad("rawtoimage.fxml");
        Stage stage = new Stage();
        stage.setTitle("RawToImage View");
        stage.setScene(new Scene(loader.getRoot(), 640, 480));

        RawToImageController hvc = loader.getController();
        boolean showController = hvc.init(txtValor.getText());

        if(showController){
            stage.show();
        }else {
            stage.close();
        }
    }

    public void processarJsonRestAction(){
        FXMLLoader loader = ui.fxmlLoad("rest.fxml");
        Stage stage = new Stage();
        stage.setTitle("REST");
        stage.setScene(new Scene(loader.getRoot(), 640, 600));
        stage.show();

        RestController hvc = loader.getController();
        hvc.init(this);
    }

    private Boolean validarTxtValor(){
        String content = txtValor.getText();

        if(content == null || "".equals(content.trim())){
            Ui.alert(Alert.AlertType.WARNING, "É necessário ter algum valor para a opção selecionada");
            return false;
        }else{
            return true;
        }
    }
}
