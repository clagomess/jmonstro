package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageViewController {
    @FXML ImageView img;

    void init(String base64Hex){
        byte[] parsed = HexViewerService.parse(base64Hex);

        try{
            parsed = Base64.getDecoder().decode(parsed);
            final ByteArrayInputStream logoByte = new ByteArrayInputStream(parsed);
            Platform.runLater(() -> img.setImage(new Image(logoByte)));
        } catch (Exception ignore){
            Ui.alertError(Alert.AlertType.INFORMATION, "Não possivel carregar como Base64");
        }
    }
}
