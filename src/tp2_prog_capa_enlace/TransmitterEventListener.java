/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 *
 * @author marti
 */
//Esta clase es la encargada de escuchar eventos en el canal y procesarlos
public class TransmitterEventListener implements SerialPortDataListener {

    public SerialPort port;

    public Host host;

    public TransmitterEventListener(SerialPort port, Host host) {
        this.port = port;
        this.host = host;
    }

    //Hacemos que escuche el evento llegada de datos
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }
    
    //Este método realiza una serie de pasos una vez escucha la llegada de datos al buffer
    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        Frame receivedFrame = Frame.rawDataAsFrame(newData);
        System.out.println("Trama recibida: ");
        receivedFrame.printFrame();
        //Si la trama que recibimos, es una cuyo tipo es igual a 1, es un ack
        //por lo tanto, desbloqueamos la transmisión y podemos seguir enviando tramas al receptor
        if (receivedFrame.frameType == '1') {
            host.blockTransmission.set(false);
        }
    }
}
