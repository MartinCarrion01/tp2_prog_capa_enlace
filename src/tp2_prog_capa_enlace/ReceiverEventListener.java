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

/**
 *
 * @author marti
 */

//Esta clase es la encargada de escuchar eventos en el canal y procesarlos
public class ReceiverEventListener implements SerialPortDataListener {

    public SerialPort port;

    public ReceiverEventListener(SerialPort port) {
        this.port = port;
    }

    //Hacemos que escuche el evento llegada de datos
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    //Este método realiza una serie de pasos una vez escucha la llegada de datos al buffer
    @Override
    public void serialEvent(SerialPortEvent event) {
        //Obtenemos los datos recibidos
        byte[] newData = event.getReceivedData();
        //Armamos una trama a partir de los datos que recibimos
        Frame receivedFrame = Frame.rawDataAsFrame(newData);
        System.out.println("Trama recibida: ");
        receivedFrame.printFrame();
        //Calculamos el bit de paridad en base a lo que recibimos
        byte parityBit = receivedFrame.getParityBit();
        //Comparamos el bit de paridad recibido con el calculado anteriormente
        if (parityBit == receivedFrame.parityBit) {
            //Mostramos el mensaje que se quizo transmitir
            String decodedMessage = receivedFrame.decodeMessage();
            System.out.println("Mensaje recibido: " + decodedMessage);
            //Enviamos acuse de recibo para desbloquear el transmisor
            sendAck();
            //Cerramos la recepción si mandamos un exit
            if (decodedMessage.equalsIgnoreCase("exit")) {
                System.out.println("El emisor ha terminado la comunicacion, cerrando...");
                port.closePort();
            }
        } else {
            //Si los bit de paridad son distintos, no mandamos acuse de recibo, provocando la retransmisión
            System.out.println("Ocurrió en un error en la transmisión del mensaje, solicitando retrasmisión...");
        }
    }

    //Enviamos acuse de recibo
    private void sendAck() {
        OutputStream outputStream = this.port.getOutputStream();
        try {
            Frame ackFrame = new Frame();
            ackFrame.frameType = '1';
            //Enviamos la trama ack mediante el enlace
            outputStream.write(ackFrame.frameToBytes());
        } catch (IOException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
