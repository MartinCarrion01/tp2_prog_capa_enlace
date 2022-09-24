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
public class TransmitterEventListener implements SerialPortDataListener {

    public SerialPort port;

    public Host host;

    public TransmitterEventListener(SerialPort port, Host host) {
        this.port = port;
        this.host = host;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        String mensaje = new String(newData);
        if (mensaje.equalsIgnoreCase("ack")) {
            host.blockTransmission.set(false);
        }
    }
}
