package br.jmonstro.service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexViewerService {
    public static String print(byte[] binaryContent) {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineHex = new StringBuilder();
        StringBuilder lineTex = new StringBuilder();
        int i = 0;

        for (byte readByte : binaryContent) {
            readByte = (byte) (readByte & 0xFF);

            lineHex.append(DatatypeConverter.printHexBinary(new byte[]{readByte}));
            lineHex.append(" ");
            lineHex.append((i == 7 ? " " : ""));

            lineTex.append((readByte >= 33 && readByte <= 126) ? (char) readByte : '.');
            lineTex.append((i == 7 ? " " : ""));

            i++;

            if(i==16){
                sb.append(lineHex);
                sb.append(" ");
                sb.append(lineTex);
                sb.append("\n");
                lineHex = new StringBuilder();
                lineTex = new StringBuilder();
                i=0;
            }
        }

        sb.append(String.format("%-49s", lineHex.toString()));
        sb.append(" ");
        sb.append(lineTex);

        return sb.toString();
    }

    public static String parse(final String content){
        if(content == null){
            return " ";
        }

        String result = null;
        Pattern p = Pattern.compile("\\[[0-9]+\\] : (.*)");
        Matcher m = p.matcher(content);

        if(m.find()){
            result = m.group(1);
        }

        if(result == null){
            p = Pattern.compile("(.+) : (.+)");
            m = p.matcher(content);

            if(m.find()){
                result = m.group(2);
            }
        }

        if(result == null){
            result = content;
        }

        return result;
    }

    public static byte[] rawToImage(byte[] content, int width, int height) throws Exception {
        if(width * height != content.length){
            throw new Exception("[width] ou [height] não está com a proporção correta");
        }

        final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(content));

        int i = 0, x = 0, y = 0;
        while (i < content.length) {

            int pixel = bis.read();

            // Grava Pixel
            Color cor = new Color(pixel, pixel, pixel);
            bi.setRGB(x, y, cor.getRGB());


            if (x < (width - 1)) {
                x++;
            } else {
                x = 0;
                y++;
            }

            i++;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bi, "jpg", baos );
        baos.flush();
        byte[] toReturn = baos.toByteArray();
        baos.close();

        return toReturn;
    }

    public static List<int[]> getDimension(int size){
        List<int[]> sizes = new ArrayList<>();

        int max = (int) Math.ceil(Math.sqrt((double) 16/9 * (double) size));

        for(int w = 0; w <= max; w++){
            for (int h = 0;h <= max; h++){
                if(w*h == size){
                    sizes.add(new int[]{w, h});
                }
                h++;
            }
            w++;
        }

        return sizes;
    }
}
