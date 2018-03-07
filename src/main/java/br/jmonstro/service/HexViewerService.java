package br.jmonstro.service;

import javax.xml.bind.DatatypeConverter;
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

    public static byte[] parse(final String content){
        if(content == null){
            return new byte[]{(byte) 0x20};
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

        return result.getBytes();
    }
}
