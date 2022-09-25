/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 *
 * @author marti
 */
public class Frame {

    public static final char FLAG = '~';
    public static final char ESCAPE = '/';
    public byte[] payload;
    public byte[] checksum;

    public Frame() {
    }

    public Frame(String message) {
        this.payload = encodeMessage(message);
    }

    public static Frame rawDataAsFrame(byte[] rawData) {
        if ((char) rawData[0] != FLAG || (char) rawData[rawData.length - 1] != FLAG) {
            System.out.println("Delimitación inválida de la trama");
            return null;
        }
        byte[] rawPayload = Arrays.copyOfRange(rawData, 1, rawData.length - 1);
        Frame receivedFrame = new Frame();
        receivedFrame.payload = rawPayload;
        return receivedFrame;
    }

    public byte[] frameToBytes() {
        String frameAsBytes = FLAG + new String(payload) + FLAG;
        return frameAsBytes.getBytes();
    }

    public void printFrame() {
        System.out.println("Delimitador: " + FLAG);
        System.out.println("Carga útil: " + new String(payload));
        System.out.println("Delimitador: " + FLAG);
    }

    private byte[] encodeMessage(String message) {
        byte[] messageAsBytes = message.getBytes();
        List<Byte> aux = new ArrayList<>();
        for (int i = 0; i < messageAsBytes.length; i++) {
            if (messageAsBytes[i] == ESCAPE || messageAsBytes[i] == FLAG) {
                aux.add((byte) ESCAPE);
            }
            aux.add(messageAsBytes[i]);
        }
        byte[] encodedMessage = new byte[aux.size()];
        for (int i = 0; i < encodedMessage.length; i++) {
            encodedMessage[i] = aux.get(i);
        }
        return encodedMessage;
    }

    public String decodeMessage() {
        String payloadAsString = new String(this.payload);
        char[] payloadCharacters = payloadAsString.toCharArray();
        List<Character> stuffedMessage = new ArrayList<>();
        for (int i = 0; i < payloadCharacters.length; i++) {
            char character = payloadCharacters[i];
            if (!(character == ESCAPE && (payloadCharacters[i + 1] == FLAG || payloadCharacters[i + 1] == ESCAPE))) {
                stuffedMessage.add(character);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Character ch : stuffedMessage) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
