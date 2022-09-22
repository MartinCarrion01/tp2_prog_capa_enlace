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
import java.util.Scanner;

/**
 *
 * @author marti
 */
public class Host {

    public SerialPort port;

    public Host(SerialPort port) {
        this.port = port;
    }

    public void EnterTransmissionMode(OutputStream outputStream, Scanner scanner) {
        while (true) {
            System.out.println("A continuación, tipee un mensaje a enviar");
            String payload = scanner.nextLine();
            try {
                outputStream.write(payload.getBytes());
                if (payload.equalsIgnoreCase("exit")) {
                    port.closePort();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    public void EnterReceptionMode() {
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] newData = event.getReceivedData();
                System.out.println("Se recibio un mensaje de: " + newData.length);
                //for (int i = 0; i < newData.length; ++i) {
                //    System.out.print((char) newData[i]);
                //}
                String mensaje = new String(newData);
                System.out.println("Mensaje recibido: " + mensaje);
                if (mensaje.equalsIgnoreCase("exit")) {
                    System.out.println("cerrando...");
                    port.closePort();
                }
            }
        });
    }
}
