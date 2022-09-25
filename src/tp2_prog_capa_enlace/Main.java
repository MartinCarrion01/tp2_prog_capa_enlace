/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tp2_prog_capa_enlace;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*
        Dependiendo si por consola mandamos "tx" o "rx",
        el programa empieza en modo trasmisor o receptor.
         */
        switch (args[0]) {
            case "tx":
                System.out.println("Entrando a modo emisión");
                //Creamos un host el cual tiene asignado el puerto COM1 y por ahí transmite
                Host emisor = new Host(SerialPortBuilder.initializePort("COM1", 19200));
                //Si el puerto esta abierto, transmitimos
                if (emisor.port.isOpen()) {
                    emisor.EnterTransmissionMode(emisor.port.getOutputStream(), new Scanner(System.in));
                } else {
                    System.out.println("El puerto no se pudo abrir, saliendo...");
                }
                break;
            case "rx":
                System.out.println("Entrando a modo recepción");
                //Creamos un host el cual tiene asignado el puerto COM2 y por ahí recibe
                Host receptor = new Host(SerialPortBuilder.initializePort("COM2", 19200));
                //Si el puerto esta abierto, nos abrimos a la recepción
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
