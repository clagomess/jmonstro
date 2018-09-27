package br.jmonstro.controller;

import br.jmonstro.bean.MainForm;
import br.jmonstro.bean.RestForm;
import br.jmonstro.bean.RestParam;
import br.jmonstro.main.Ui;
import br.jmonstro.service.JMonstroService;
import br.jmonstro.service.RestService;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class RestController extends RestForm {
    private MainForm mainForm = null;

    void init(MainForm mainForm){
        this.mainForm = mainForm;

        this.tblFormData.getItems().add(new KeyValueTable("foo", "barr")); //@TODO: só pra ver de qault
    }

    public void executeAction(){
        try {
            File file = RestService.get(new RestParam(this));

            Ui.alert(Alert.AlertType.INFORMATION, "Executado com sucesso!");

            JMonstroService jMonstroService = new JMonstroService();
            jMonstroService.processar(file, mainForm);
        }catch (Throwable e){
            Ui.alert(Alert.AlertType.WARNING, e.getMessage());
            log.warn(RestController.class.getName(), e);
        }
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
