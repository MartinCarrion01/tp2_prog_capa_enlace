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
        port.addDataListener(new ReceiverEventListener(port, this));
    }
}
