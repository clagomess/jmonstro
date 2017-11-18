package br.jmonstro.controller;

import br.jmonstro.service.HexViewerService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class HexViewController {
    @FXML
    TextArea txtHex;

    void init(String base64Hex){
        String printed = HexViewerService.print(base64Hex.getBytes());

        this.txtHex.setText(printed);
    }
}
