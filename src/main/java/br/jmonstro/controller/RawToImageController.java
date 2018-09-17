package br.jmonstro.controller;

import br.jmonstro.main.Ui;
import br.jmonstro.service.HexViewerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

@Slf4j
public class RawToImageController {
    @FXML ImageView img;

    private static byte[] parsed;

    boolean init(String content){
        boolean sucesso = true;

        try{
            parsed = Base64.getDecoder().decode(content.getBytes());
            List<int[]> dimensions = HexViewerService.getDimension(parsed.length);

            if(parsed.length % 2 != 0 || dimensions.isEmpty()){
                throw new Exception("O conteúdo não parece ser uma imagem");
            }

            int[] ratio = dimensions.get(0);

            ByteArrayInputStream image = new ByteArrayInputStream(HexViewerService.rawToImage(parsed, ratio[0], ratio[1]));

            Platform.runLater(() -> img.setImage(new Image(image)));
        } catch (Throwable e){
            sucesso = false;
            log.warn(RawToImageController.class.getName(), e);
            Ui.alertError(Alert.AlertType.WARNING, e.getMessage());
        }

        return sucesso;
    }
}
