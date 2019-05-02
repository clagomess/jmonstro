package br.jmonstro.controller;

import br.jmonstro.bean.MainForm;
import br.jmonstro.bean.RestForm;
import br.jmonstro.bean.RestResponseDto;
import br.jmonstro.bean.postman.Environment;
import br.jmonstro.bean.restform.KeyValueTable;
import br.jmonstro.bean.restparam.BodyType;
import br.jmonstro.bean.restparam.Method;
import br.jmonstro.bean.restparam.RestParam;
import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import br.jmonstro.service.PostmanService;
import br.jmonstro.service.RestService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class RestController extends RestForm implements Initializable {
    private MainForm mainForm = null;

    void init(MainForm mainForm){
        this.mainForm = mainForm;

        Platform.runLater(() -> {
            try{
                PostmanService ps = new PostmanService();

                // Collections
                this.postmanCollection.setShowRoot(false);
                this.postmanCollection.setRoot(ps.getTree());
                this.postmanCollection.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
                    if(nv != null && nv.getValue().getRequest() != null) {
                        this.setFormValue(nv.getValue().getRequest());
                    }
                });

                // Environments
                this.cbxEnviroment.setItems(FXCollections.observableArrayList(ps.readEnvironmentFolder("environment")));

                // Globals
                for (Environment envs : ps.readEnvironmentFolder("globals")){
                    for(Environment.Value value : envs.getValues()) {
                        tblGlobal.getItems().add(new KeyValueTable(value.getKey(), value.getValue()));
                    }
                }
            }catch (IOException e){
                log.error(RestController.class.getName(), e);
                Ui.alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rbBodyTypeNone.setToggleGroup(this.tipBodyType);
        this.rbBodyTypeFormData.setToggleGroup(this.tipBodyType);
        this.rbBodyTypeFormUrlencoded.setToggleGroup(this.tipBodyType);
        this.rbBodyTypeRaw.setToggleGroup(this.tipBodyType);
        this.rbBodyTypeBinary.setToggleGroup(this.tipBodyType);
    }

    public void executeAction(){
        Platform.runLater(() -> btnExecutar.setDisable(true));

        new Thread(() -> {
            try {
                final RestParam restParam = new RestParam(this);

                Platform.runLater(() -> {
                    Stage stage = (Stage) mainPane.getScene().getWindow();
                    stage.setTitle(String.format("%s: %s", restParam.getMethod(), restParam.getUrl()));
                });

                RestResponseDto dto = RestService.perform(restParam);

                // reponse data
                Platform.runLater(() -> {
                    txtResponseMethod.setText(dto.getMethod());
                    txtResponseUrl.setText(dto.getUrl());
                    txtResponseTime.setText(dto.getTime().toString() + " ms");
                    txtResponseStatus.setText(dto.getStatus().toString());
                    txtResponseSize.setText(FileUtils.byteCountToDisplaySize(dto.getSize()));

                    tblResponseHeader.getItems().remove(0, tblResponseHeader.getItems().size());

                    for (Map.Entry<String, List<String>> entry : dto.getHeaders().entrySet()){
                        KeyValueTable kvt = new KeyValueTable(entry.getKey(), String.join(", ",  entry.getValue()));
                        tblResponseHeader.getItems().add(kvt);
                    }
                });

                // mount view
                JMonstroService jMonstroService = new JMonstroService();
                jMonstroService.processar(dto.getFile(), mainForm);
            }catch (Throwable e){
                log.error(JMonstroService.class.getName(), e);
                Ui.alert(Alert.AlertType.ERROR, e.getMessage());
            }finally {
                Platform.runLater(() -> btnExecutar.setDisable(false));
            }
        }).start();
    }

    public void chxProxyAction(){
        boolean disable = !chxProxy.isSelected();

        txtProxyUrl.setDisable(disable);
        txtProxyUsername.setDisable(disable);
        txtProxyPassword.setDisable(disable);
    }

    public void cbxMetodoAction(){
        Method method = Method.valueOf(cbxMetodo.getValue());

        tabBody.setDisable(method == Method.GET || method == Method.DELETE);
    }

    public void tblFormDataAddAction(){
        tblFormData.getItems().add(new KeyValueTable(tblFormDataKey, tblFormDataValue));
        tblFormDataKey.setText("");
        tblFormDataValue.setText("");
    }

    public void tipBodyTypeChange(){
        BodyType bodyType = BodyType.valueOf((String) tipBodyType.getSelectedToggle().getUserData());

        // Desabilita tudo
        this.grpFormDataBtn.setVisible(false);
        this.tblFormData.setVisible(false);
        this.txtBody.setVisible(false);

        // Depois habilita conforme o tipo
        switch (bodyType){
            case FORM_URLENCODED: //@TODO: criar especifico para cada
            case FORM_DATA:
                this.grpFormDataBtn.setVisible(true);
                this.tblFormData.setVisible(true);
                break;
            case RAW:
                this.txtBody.setVisible(true);
                break;
            case BINARY:
                //@TODO: implements
                break;
        }
    }

    public void tblFormDataRemoveAction(){
        tblFormData.getItems().remove(0, tblFormData.getItems().size());
    }

    public void tblHeaderAddAction(){
        tblHeader.getItems().add(new KeyValueTable(tblHeaderKey, tblHeaderValue));
        tblHeaderKey.setText("");
        tblHeaderValue.setText("");
    }

    public void tblHeaderRemoveAction(){
        tblHeader.getItems().remove(0, tblHeader.getItems().size());
    }

    public void tblCookieAddAction(){
        tblCookie.getItems().add(new KeyValueTable(tblCookieKey, tblCookieValue));
        tblCookieKey.setText("");
        tblCookieValue.setText("");
    }

    public void tblCookieRemoveAction(){
        tblCookie.getItems().remove(0, tblCookie.getItems().size());
    }

    // POSTMAN ACTIONS
    public void cbxEnviromentAction(){
        Environment environment = cbxEnviroment.getValue();

        if(environment != null){
            tblEnviroment.getItems().remove(0, tblEnviroment.getItems().size());

            for (Environment.Value value : environment.getValues()){
                tblEnviroment.getItems().add(new KeyValueTable(value.getKey(), value.getValue()));
            }
        }
    }
}
