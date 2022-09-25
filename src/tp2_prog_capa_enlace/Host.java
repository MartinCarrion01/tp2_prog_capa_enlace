/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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
            Frame frameToSend = new Frame(message);
            try {
                final AtomicBoolean exitFlag = new AtomicBoolean(false);
                do {
                    exitFlag.set(false);
                    outputStream.write(frameToSend.frameToBytes());
                    blockTransmission.set(true);
                    System.out.println("Trama enviada: ");
                    frameToSend.printFrame();
                    System.out.println("Esperando ack...");
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("Expiró temporizador, volviendo a transmitir...");
                            exitFlag.set(true);
                        }
                    };
                    timer.schedule(task, 3000);
                    while (true) {
                        if (!blockTransmission.get()) {
                            exitFlag.set(false);
                            timer.cancel();
                            timer.purge();
                            System.out.println("ack recibido");
                            break;
                        }
                        if (exitFlag.get()) {
                            break;
                        }
                    }
                    if (new String(frameToSend.payload).equalsIgnoreCase("exit")) {
                        port.closePort();
                        break;
                    }
                } while (exitFlag.get());
            } catch (IOException e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    public void EnterReceptionMode() {
        port.addDataListener(new ReceiverEventListener(port));
    }
}
