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
//Clase que representa un Host
public class Host {

    //Puerto que tiene asignado el host
    public SerialPort port;

    //Atributo usado para bloquear las transmisiones hasta que se tengamos el acuse de recibo
    //Usamos atomicBoolean para poder manipular este atributo entre distintos threads
    public final AtomicBoolean blockTransmission = new AtomicBoolean(false);

    public Host(SerialPort port) {
        this.port = port;
    }

    //Este método sirve para empezar a transmitir
    public void EnterTransmissionMode(OutputStream outputStream, Scanner scanner) {
        //Preparamos el puerto para escuchar las señales de acuse de recibo cuando lleguen
        port.addDataListener(new TransmitterEventListener(port, this));
        while (true) {
            System.out.println("A continuación, tipee un mensaje a enviar");
            //"Obtenemos" el mensaje de una capa superior
            String message = scanner.nextLine();
            //Armamos la trama a partir del mensaje recibido
            Frame frameToSend = new Frame(message);
            try {
                final AtomicBoolean exitFlag = new AtomicBoolean(false);
                do {
                    exitFlag.set(false);
                    //Escribimos cada byte de la trama sobre el enlace de comunicación
                    outputStream.write(frameToSend.frameToBytes());
                    //Bloqueamos la transmision hasta que el receptor confirme
                    //Esto para no empezar a inundarlo de tramas (Control de flujo)
                    blockTransmission.set(true);
                    System.out.println("Trama enviada: ");
                    frameToSend.printFrame();
                    System.out.println("Esperando ack...");
                    //Inicializamos un temporizador, que a los 3 segundos de no recibir
                    //acuse de recibo, reenvía la trama.
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
                        //Cuando recibimos el ack, reanudamos la transmisión de datos y 
                        //cancelamos el temporizador
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
                    //Si el emisor manda exit, cerramos la transmisión y tambien cerramos la recepción
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

    //Este metodo sirve para empezar a recibir datos
    public void EnterReceptionMode() {
        //Preparamos el puerto para escuchar la llegada de datos del trasmisor
        port.addDataListener(new ReceiverEventListener(port));
    }
}
