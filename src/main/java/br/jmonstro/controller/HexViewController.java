package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.util.Base64;

public class HexViewController {
    @FXML TextArea txtHex;

    void init(Boolean base64, String content){
        byte[] parsed = new byte[]{};

        if(base64) {
            try {
                parsed = Base64.getDecoder().decode(content.getBytes());
            } catch (Exception ignore) {
                Ui.alertError(Alert.AlertType.WARNING, MainController.MSG_ERRO_BASE64);
            }
        }else{
            parsed = content.getBytes();
        }

        final String printed = HexViewerService.print(parsed);

        Platform.runLater(() -> this.txtHex.setText(printed));
    }
}
