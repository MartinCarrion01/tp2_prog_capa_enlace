/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp2_prog_capa_enlace;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marti
 */
public class Frame {

    public static final char FLAG = '~';
    public static final char ESCAPE = '/';
    public String payload;
    public String checksum;

    public Frame(String message) {
        stuffMessage(message);
    }

    private void stuffMessage(String message) {
        System.out.println("Entra a stuff ");
        char[] characters = message.toCharArray();
        List<Character> stuffedMessage = new ArrayList<>();
        for (int i = 0; i < message.length(); i++) { 
            
            char character = characters[i];
           
            if (character == ESCAPE || character == FLAG) {
                stuffedMessage.add(ESCAPE);
                
            }
            stuffedMessage.add(character);
        }
        
        StringBuilder sb = new StringBuilder();
 
        for (Character ch : stuffedMessage) {
            sb.append(ch);
        }
 
        payload = sb.toString();
        

    }

    public byte[] toBytes() {
        
        String message = FLAG + payload + FLAG;
        System.out.println("entra a metodo" + message);
        return message.getBytes();
    }

}
