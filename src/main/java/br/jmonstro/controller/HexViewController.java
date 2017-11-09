package br.jmonstro.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HexViewController {
    @FXML Label txtHex;

    void init(String txtHex){
        this.txtHex.setText(txtHex);
    }
}
