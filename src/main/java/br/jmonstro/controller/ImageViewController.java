package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageViewController {
    @FXML ImageView img;

    void init(String content){
        try{
            final byte[] parsed = Base64.getDecoder().decode(content.getBytes());
            final ByteArrayInputStream logoByte = new ByteArrayInputStream(parsed);
            Platform.runLater(() -> img.setImage(new Image(logoByte)));
        } catch (Exception ignore){
            Ui.alertError(Alert.AlertType.WARNING, MainController.MSG_ERRO_BASE64);
        }
    }
}
