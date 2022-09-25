/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;

/**
 *
 * @author marti
 */
public class SerialPortBuilder {
    
    //Metodo usado para inicializar un puerto serie y abrirlo
    public static SerialPort initializePort(String portName, int baudRate){
       SerialPort port = SerialPort.getCommPort(portName);
       port.setBaudRate(baudRate);
       port.openPort();
       return port;
    }
}
