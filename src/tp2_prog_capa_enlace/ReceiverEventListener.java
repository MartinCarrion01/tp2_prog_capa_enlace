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

/**
 *
 * @author marti
 */
public class ReceiverEventListener extends AbstractEventListener implements SerialPortDataListener {

    public ReceiverEventListener(SerialPort port, Host host) {
        super(port, host);
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        System.out.println("Se recibio un mensaje de: " + newData.length);
        String mensaje = new String(newData);
        System.out.println("Mensaje recibido: " + mensaje);
        this.sendAck();
        if (mensaje.equalsIgnoreCase("exit")) {
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
