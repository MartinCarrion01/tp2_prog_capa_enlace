/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author marti
 */
public class Host {

    public SerialPort port;

    public final AtomicBoolean blockTransmission = new AtomicBoolean(false);

    public Host(SerialPort port) {
        this.port = port;
    }

    public void EnterTransmissionMode(OutputStream outputStream, Scanner scanner) {
        port.addDataListener(new TransmitterEventListener(port, this));
        while (true) {
            System.out.println("A continuación, tipee un mensaje a enviar");
            String message = scanner.nextLine();
            
            Frame frame = new Frame(message);//se realiza entramado
            
            try {
                outputStream.write(frame.toBytes());
                blockTransmission.set(true);
                System.out.println("Esperando ack...");
                while (true) {
                    if (!blockTransmission.get()) {
                        System.out.println("ack recibido");
                        break;
                    }
                }
                if (message.equalsIgnoreCase("exit")) {
                    port.closePort();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    public void EnterReceptionMode() {
        port.addDataListener(new ReceiverEventListener(port));
    }
}
