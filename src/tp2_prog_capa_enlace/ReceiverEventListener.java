/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import static tp2_prog_capa_enlace.Frame.ESCAPE;
import static tp2_prog_capa_enlace.Frame.FLAG;

/**
 *
 * @author marti
 */
public class ReceiverEventListener implements SerialPortDataListener {

    public SerialPort port;

    public ReceiverEventListener(SerialPort port) {
        this.port = port;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        Frame receivedFrame = Frame.rawDataAsFrame(newData);
        System.out.println("Trama recibida: ");
        receivedFrame.printFrame();
        String decodedMessage = receivedFrame.decodeMessage();
        System.out.println("Mensaje recibido: " + decodedMessage);
        this.sendAck();
        if (decodedMessage.equalsIgnoreCase("exit")) {
            System.out.println("El emisor ha terminado la comunicacion, cerrando...");
            port.closePort();
        }
    }

    private void sendAck() {
        OutputStream outputStream = this.port.getOutputStream();
        try {
            outputStream.write("ack".getBytes());
        } catch (IOException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
