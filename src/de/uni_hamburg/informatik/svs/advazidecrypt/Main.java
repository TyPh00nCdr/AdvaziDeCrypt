package de.uni_hamburg.informatik.svs.advazidecrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static int PADDING;

    public static void main(String[] args) {
        try {
            StringBuilder sb = new StringBuilder();
            Files.lines(Paths.get(args[0])).forEach(sb::append);
            String messageString = sb.toString();

            byte[] messageBytes = Files.readAllBytes(Paths.get(args[0]));
            String decodedString = decodeAdvazi(messageBytes, getMessageKey(messageString));

            System.out.println(decodedString.substring(0, decodedString.length() - PADDING));
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.err.println("Datei konnte nicht gelesen werden: " + e.getMessage());
        }
    }

    private static byte[] getMessageKey(String message) {
        String pattern = message.substring(message.length() - 10, message.length());
        int pad = 0;
        while (message.endsWith(pattern)) {
            pad += 10;
            message = message.substring(0, message.length() - 10);
        }

        int pad2 = 9;
        for (int i = 1; i < 10 && !message.endsWith(pattern.substring(i, pattern.length())); ++i, --pad2) ;
        PADDING = pad + pad2;

        byte[] result = new byte[10];
        for (int i = 0; i < 10; ++i) {
            result[i] = (byte) (pattern.getBytes()[i] ^ (byte) PADDING);
        }

        return result;
    }

    private static String decodeAdvazi(byte[] message, byte[] key) {
        byte[] result = new byte[message.length];

        for (int i = 0; i < message.length; ++i) {
            result[i] = (byte) (message[i] ^ key[i % 10]);
        }

        return new String(result);
    }
}
