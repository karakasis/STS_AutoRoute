/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import java.util.ArrayList;

/**
 *
 * @author Xrhstos
 */
public class Receiver {
    
    private static ArrayList<Integer[]> receivedMessages;
    private static Integer[] decoder;
    public static ArrayList<String> divToStringRec;
    
    public Receiver(Integer[] decoder){
        receivedMessages = new ArrayList<>();
        Receiver.decoder = decoder;
    }
    public Receiver(){
        receivedMessages = new ArrayList<>();
    }
    
    public static void channel(Integer[] msg, Supervisor sv){
        receivedMessages.add(msg);
        Integer[] crcReceived = CRCAlgorithm.executeCRC(msg, decoder, "Decode");
        sv.divToStringRec = Receiver.divToStringRec;
        sv.sendDataToSupervisor(msg, crcReceived);
    }
    
    public static void channel(Integer[] msg,Integer[] decoder, Supervisor sv){
        receivedMessages.add(msg);
        Integer[] crcReceived = CRCAlgorithm.executeCRC(msg, decoder, "Decode");
        sv.divToStringRec = Receiver.divToStringRec;
        sv.sendDataToSupervisor(msg, crcReceived);
    }
    
}
