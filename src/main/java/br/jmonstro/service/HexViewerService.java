package br.jmonstro.service;

public class HexViewerService {
    public static String print(byte[] binaryContent) {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineHex = new StringBuilder();
        StringBuilder lineTex = new StringBuilder();
        int i = 0;

        for (byte readByte : binaryContent) {
            lineHex.append((readByte <= 0xF) ? "0" : "");
            lineHex.append(Integer.toHexString(readByte).toUpperCase());
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
}
