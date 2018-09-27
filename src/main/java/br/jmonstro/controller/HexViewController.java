package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class HexViewController {
    @FXML TextArea txtHex;

    boolean init(Boolean base64, String content){
        boolean sucesso = true;
        byte[] parsed = new byte[]{};

        if(base64) {
            try {
                parsed = Base64.getDecoder().decode(content.getBytes());
            } catch (Throwable e) {
                sucesso = false;
                log.warn(HexViewController.class.getName(), e);
                Ui.alert(Alert.AlertType.WARNING, MainController.MSG_ERRO_BASE64);
            }
        }else{
            parsed = content.getBytes();
        }

        final String printed = HexViewerService.print(parsed);

        Platform.runLater(() -> this.txtHex.setText(printed));

        return sucesso;
    }
}
