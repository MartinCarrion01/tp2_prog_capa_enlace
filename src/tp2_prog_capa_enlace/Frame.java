/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 *
 * @author marti
 */
//Clase que representa la trama que vamos a enviar o recibir
public class Frame {

    //Caracter que representa el delimitador
    public static final char FLAG = '~';
    //Caracter que representa el byte de escape (relleno de bytes)
    public static final char ESCAPE = '/';
    //Este byte representa el tipo de trama, 0 para datos, 1 para trama ACK
    public byte frameType;
    //Este byte representa la carga util, encapsula la información de la capa superior
    public byte[] payload;
    //Este byte se usa para la detección de errores con el método de bit de paridad
    public byte parityBit;

    public Frame() {
    }

    //Metodo para armar la trama
    public Frame(String message) {
        this.frameType = '0';
        this.payload = encodeMessage(message);
        this.parityBit = getParityBit();
    }

    //Este metodo procesa un flujo de bytes y lo convierte en una trama como tal
    //con todos los campos correspondientes
    public static Frame rawDataAsFrame(byte[] rawData) {
        Frame receivedFrame = new Frame();
        byte frameType = rawData[1];
        if (frameType == '0') {
            byte[] rawPayload = Arrays.copyOfRange(rawData, 2, rawData.length - 2);
            receivedFrame.payload = rawPayload;
            byte rawParityBit = rawData[rawData.length - 2];
            receivedFrame.parityBit = rawParityBit;
        }
        receivedFrame.frameType = frameType;
        return receivedFrame;
    }
    
    //Este metodo convierte la trama en un flujo de bytes, listo para ser 
    //transmitido sobre el enlace de comunicación
    public byte[] frameToBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(FLAG);
        output.write(frameType);
        if (frameType == '0') {
            output.write(payload);
            output.write(parityBit);
        }
        output.write(FLAG);
        return output.toByteArray();
    }

    //Este método sirve para imprimir por pantalla los campos de la trama
    public void printFrame() {
        System.out.println("Delimitador: " + FLAG);
        System.out.println("Tipo(0 para datos, 1 para ACK): " + (char) frameType);
        if (frameType == '0') {
            System.out.println("Carga útil: " + new String(payload));
            System.out.println("Bit de paridad: " + parityBit);
        }
        System.out.println("Delimitador: " + FLAG);
    }

    //Este método sirve para quitar el relleno de bytes del payload
    //y convertirlo en un mensaje interpretable por la capa superior
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

    //Este método determina el bit de paridad de la trama, se usa para detectar errores
    public byte getParityBit() {
        int cantUnos = 0;
        for (int i = 0; i < payload.length; i++) {
            String s = byteToString(payload[i]);
            cantUnos += s.length() - s.replace("1", "").length();
        }
        if (cantUnos % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    //Este método es el encargado de realizar el relleno de bytes con byte bandera
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

    //Este método sirve para convertir un byte en una cadena compuesta de 1s y 0s
    //Se usa para determinar el bit de paridad
    private String byteToString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }
}
