package br.jmonstro.controller;

import br.jmonstro.service.HexViewerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Base64;

public class HexViewController {
    @FXML TextArea txtHex;

    void init(String base64Hex){
        byte[] parsed = HexViewerService.parse(base64Hex);

        try{
            parsed = Base64.getDecoder().decode(parsed);
        } catch (Exception ignore){
            //@TODO: Colocar alert aqui;
        }

        final String printed = HexViewerService.print(parsed);

        Platform.runLater(() -> this.txtHex.setText(printed));
    }
}
