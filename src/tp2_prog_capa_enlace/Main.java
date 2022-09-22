/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author marti
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        switch (args[0]) {
            case "tx":
                System.out.println("Entrando a modo emisión");
                Host emisor = new Host(SerialPortBuilder.initializePort("COM1", 19200));
                if (emisor.port.isOpen()) {
                    emisor.EnterTransmissionMode(emisor.port.getOutputStream(), new Scanner(System.in));
                } else {
                    System.out.println("El puerto no se pudo abrir, saliendo...");
                }
                break;
            case "rx":
                System.out.println("Entrando a modo recepción");
                Host receptor = new Host(SerialPortBuilder.initializePort("COM2", 19200));
                if (receptor.port.isOpen()) {
                    receptor.EnterReceptionMode();
                } else {
                    System.out.println("El puerto no se pudo abrir, saliendo...");
                }
                break;
            default:
                System.out.println("Opción inválida, saliendo...");
                break;
        }
    }

}
