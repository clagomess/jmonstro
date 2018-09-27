package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
public class ImageViewController {
    @FXML ImageView img;

    boolean init(String content){
        boolean sucesso = true;

        try{
            final byte[] parsed = Base64.getDecoder().decode(content.getBytes());
            final ByteArrayInputStream logoByte = new ByteArrayInputStream(parsed);
            Platform.runLater(() -> img.setImage(new Image(logoByte)));
        } catch (Throwable e){
            sucesso = false;
            log.warn(ImageViewController.class.getName(), e);
            Ui.alert(Alert.AlertType.WARNING, MainController.MSG_ERRO_BASE64);
        }

        return sucesso;
    }
}
