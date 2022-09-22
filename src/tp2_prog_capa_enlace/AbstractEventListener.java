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
public abstract class AbstractEventListener {

    public SerialPort port;
    
    public Host host;

    public AbstractEventListener(SerialPort port, Host host) {
        this.port = port;
        this.host = host;
    }
}
