package br.jmonstro.controller;

import br.jmonstro.bean.MainForm;
import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class MainController extends MainForm {
    private static final Ui ui = new Ui();
    public static final String MSG_ERRO_BASE64 = "Não foi possível carregar valor como Base64";

    public void processarJsonAction(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar JSON/XML");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON/XML", "*.json", "*.xml");
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
        stage.getIcons().add(new Image("icon.png"));
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
        stage.getIcons().add(new Image("icon.png"));
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
        stage.getIcons().add(new Image("icon.png"));
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
        stage.getIcons().add(new Image("icon.png"));
        stage.setScene(new Scene(loader.getRoot(), 800, 600));
        stage.show();

        RestController hvc = loader.getController();
        hvc.init(this);
    }

    private Boolean validarTxtValor(){
        if(StringUtils.isEmpty(txtValor.getText())){
            Ui.alert(Alert.AlertType.WARNING, "É necessário ter algum valor para a opção selecionada");
            return false;
        }else{
            return true;
        }
    }

    // BUSCA
    private List<TreeItem<String>> busca = new ArrayList<>();
    private int posicaoBusca = 0;

    private void buscaButtons(){
        if(busca.isEmpty()){
            btnBuscarPrev.setDisable(true);
            btnBuscarNext.setDisable(true);
            lblItensEncontrado.setText("0 de 0");
        }else{
            btnBuscarPrev.setDisable(posicaoBusca == 0);
            btnBuscarNext.setDisable(busca.size() == posicaoBusca + 1);
            lblItensEncontrado.setText(String.format("%s de %s", posicaoBusca + 1, busca.size()));
        }
    }

    private void treeSelect(){
        tree.getSelectionModel().select(busca.get(posicaoBusca));
        tree.scrollTo(tree.getSelectionModel().getSelectedIndex());
    }

    public void btnBuscarAction(){
        busca.clear();
        posicaoBusca = 0;
        buscaButtons();

        if(StringUtils.isEmpty(txtBusca.getText())){
            Ui.alert(Alert.AlertType.WARNING, "É necessário preencher algum valor de busca");
            return;
        }

        if(chkBuscaRegex.isSelected()){
            try {
                Pattern.compile(txtBusca.getText(), Pattern.DOTALL);
            }catch (Throwable e){
                Ui.alert(Alert.AlertType.WARNING, "RegEx é Inválido: " + e.getMessage());
                return;
            }
        }

        JMonstroService jms = new JMonstroService();
        jms.treeExpanded(tree.getRoot(), false);
        busca.addAll(jms.buscar(this, tree.getRoot()));

        if(!busca.isEmpty()){
            treeSelect();
            buscaButtons();
        }
    }

    public void btnCollapseAction(){
        JMonstroService jms = new JMonstroService();
        jms.treeExpanded(tree.getRoot(), false);
    }

    public void btnBuscarPrevAction(){
        posicaoBusca--;
        treeSelect();
        buscaButtons();
    }

    public void btnBuscarNextAction(){
        posicaoBusca++;
        treeSelect();
        buscaButtons();
    }
}
