package br.jmonstro.controller;

import br.jmonstro.bean.MainForm;
import br.jmonstro.bean.RestForm;
import br.jmonstro.bean.RestParam;
import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import br.jmonstro.service.PostmanService;
import br.jmonstro.service.RestService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class RestController extends RestForm {
    private MainForm mainForm = null;

    void init(MainForm mainForm){
        this.mainForm = mainForm;

        try{
            PostmanService ps = new PostmanService();
            this.postmanCollection.setRoot(ps.getTree());
        }catch (IOException e){
            // @TODO: botar alert panel aqui
            log.error(RestController.class.getName(), e);
        }
    }

    public void executeAction(){
        Platform.runLater(() -> btnExecutar.setDisable(true));

        new Thread(() -> {
            try {
                File file = RestService.get(new RestParam(this));

                Ui.alert(Alert.AlertType.INFORMATION, "Executado com sucesso!");

                JMonstroService jMonstroService = new JMonstroService();
                jMonstroService.processar(file, mainForm);
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
        boolean disable = RestParam.Metodo.valueOf(cbxMetodo.getValue()) == RestParam.Metodo.GET;

        tblFormData.setDisable(disable);
        txtBodyJson.setDisable(disable);
        tblFormDataBtnAdd.setDisable(disable);
        tblFormDataBtnRemove.setDisable(disable);
    }

    public void tblFormDataAddAction(){
        tblFormData.getItems().add(new KeyValueTable(tblFormDataKey, tblFormDataValue));
        tblFormDataKey.setText("");
        tblFormDataValue.setText("");
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
}
