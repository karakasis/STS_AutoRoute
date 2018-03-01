/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JProgressBar;

/**
 *
 * @author Xrhstos
 */
public class Transmitter implements java.io.Serializable {

    private ArrayList<Integer[]> messages;
    public int msgAmount;
    public int kBits;
    public Integer[] decoder;
    public double BER;
    private int transmissionCounter;
    private double base;
    private int multi;
    private double BEP;
    private HashMap<Integer, Supervisor> svMap;
    public static ArrayList<String> divToStringTrans;
    private HashSet<Integer> pool;

    public int errorFromNoiseCounter = 0;
    public int crcErrorDetectedCounter = 0;
    public int noiseNoCRCCounter = 0;
    public int[] bitErrors;
    
    public String mode = "";

    public Transmitter(int msgAmount, int kBits, Integer[] decoder, double BER) {

        mode = "auto";
        messages = new ArrayList<>();
        this.msgAmount = msgAmount;
        this.kBits = kBits;
        this.decoder = decoder;
        this.BER = BER;
        svMap = new HashMap<>();
        Supervisor.bitErrors = new int[kBits + decoder.length - 1];
        Supervisor.errorFromNoiseCounter = 0;
        Supervisor.crcErrorDetectedCounter = 0;
        Supervisor.noiseNoCRCCounter = 0;
        Arrays.fill(Supervisor.bitErrors, 0);
        for (int i = 0; i < 100; i++) {
            if (BER < 1 / (Math.pow(10, i))) {
                continue;
            } else {
                base = (float) (BER * (Math.pow(10, i)));
                multi = (int) Math.pow(10, i);
                break;
            }
        }
        base = Math.round(base);
        //BEP = Math.round(multi/base);

        pool = new HashSet<>();
        for (int i = 0; i < base; i++) {
            pool.add(i);
        }

        for (int i = 0; i < msgAmount; i++) {
            messages.add(generateMessage(kBits));
        }

        transmissionCounter = 0;

    }
    
    public Transmitter() {

        mode = "manual";
        messages = new ArrayList<>();
        svMap = new HashMap<>();
        transmissionCounter = 1;

    }
    
    public void addMessage(Integer[] msg, Integer[] decoder, boolean noise, int[] bitErrors) {

        messages = new ArrayList<>();
        messages.add(msg);
        Integer[] message = CRCAlgorithm.executeCRC(FCSCreator.addFCS(msg,decoder), decoder, "Encode");
        svMap.put(transmissionCounter, new Supervisor(transmissionCounter, message, decoder));
        svMap.get(transmissionCounter).divToStringTrans = Transmitter.divToStringTrans;
        
        int bitErrorsInData = 0;
        if(noise){
            for (int i = 0; i < bitErrors.length; i++) {
                bitErrorsInData++;
                if (message[bitErrors[i]] == 0) {
                    message[bitErrors[i]] = 1;
                } else {
                    message[bitErrors[i]] = 0;
                }
            }
            svMap.get(transmissionCounter).bitError = bitErrorsInData;
        }
        svMap.get(transmissionCounter).errorFromNoise = noise;
        
        Receiver.channel(message, decoder,svMap.get(transmissionCounter));
        transmissionCounter++;

    }

    private Integer[] generateMessage(int kBits) {

        Random rnd = new Random();
        Integer[] message = new Integer[kBits];
        for (int i = 0; i < kBits; i++) {
            message[i] = rnd.nextInt(2);
        }
        return message;
    }

    private Integer[] requestMessage() {
        if (transmissionCounter == msgAmount) {
            System.out.println("All messages have been transmitted.");
            return null;
        }
        //System.err.println("Sending Message: "+transmissionCounter);
        return FCSCreator.addFCS(messages.get(transmissionCounter++));
    }

    public void transmitMessage() {
        Integer[] message = CRCAlgorithm.executeCRC(requestMessage(), decoder, "Encode");
        //System.err.println("Sent Message: "+(transmissionCounter-1));

        svMap.put(transmissionCounter, new Supervisor(transmissionCounter, message));
        svMap.get(transmissionCounter).divToStringTrans = Transmitter.divToStringTrans;
        boolean crcError = false;
        int bitErrorsInData = 0;
        for (int i = 0; i < message.length; i++) {
            if (applyBitError()) {
                bitErrorsInData++;
                if (message[i] == 0) {
                    message[i] = 1;
                } else {
                    message[i] = 0;
                }
                crcError = true;
            }
        }
        svMap.get(transmissionCounter).errorFromNoise = crcError;
        if (crcError) {
            Supervisor.bitErrors[bitErrorsInData - 1]++;
            Supervisor.errorFromNoiseCounter++;
            svMap.get(transmissionCounter).bitError = bitErrorsInData;
        }
        Receiver.channel(message, svMap.get(transmissionCounter));
    }

    private boolean applyBitError() {
        Random rnd = new Random();
        return pool.contains(rnd.nextInt(multi));
    }

    public HashMap<Integer, Supervisor> getSupervisors() {
        return svMap;
    }

    public void saveInstanceStats() {
        errorFromNoiseCounter = Supervisor.errorFromNoiseCounter;
        crcErrorDetectedCounter = Supervisor.crcErrorDetectedCounter;
        noiseNoCRCCounter = Supervisor.noiseNoCRCCounter;
        bitErrors = Supervisor.bitErrors;
    }
    
    public void loadInstanceStats(){
        FCSCreator fcs = new FCSCreator(decoder);
        Receiver rcv = new Receiver(decoder);
        Supervisor.errorFromNoiseCounter = errorFromNoiseCounter;
        Supervisor.crcErrorDetectedCounter = crcErrorDetectedCounter;
        Supervisor.noiseNoCRCCounter = noiseNoCRCCounter;
        Supervisor.bitErrors = bitErrors ;
    }
    
    public void retransmitMessage(int index){
        Supervisor sv = svMap.get(index);
        Supervisor.bitErrors[sv.bitError - 1]--;
        Supervisor.errorFromNoiseCounter--;
        if(sv.crcErrorDetected){
            Supervisor.crcErrorDetectedCounter--;
        }
        if(sv.noiseNoCRC){
            Supervisor.noiseNoCRCCounter--;
        }
        svMap.remove(index);
        
        Integer[] message = CRCAlgorithm.executeCRC(FCSCreator.addFCS(messages.get(index-1)), decoder, "Encode");
        //System.err.println("Sent Message: "+(transmissionCounter-1));

        svMap.put(index , new Supervisor(index, message));
        svMap.get(index ).divToStringTrans = Transmitter.divToStringTrans;
        boolean crcError = false;
        int bitErrorsInData = 0;
        for (int i = 0; i < message.length; i++) {
            if (applyBitError()) {
                bitErrorsInData++;
                if (message[i] == 0) {
                    message[i] = 1;
                } else {
                    message[i] = 0;
                }
                crcError = true;
            }
        }
        svMap.get(index).errorFromNoise = crcError;
        if (crcError) {
            Supervisor.bitErrors[bitErrorsInData - 1]++;
            Supervisor.errorFromNoiseCounter++;
            svMap.get(index ).bitError = bitErrorsInData;
        }
        Receiver.channel(message, svMap.get(index ));
    }
    
    public int[] batchRetransmission(){
        int[] results = new int[3];
        results[0] = 0;
        results[1] = 0;
        results[2] = 0;
        
        HashMap<Integer,Supervisor> cloneMap;
        cloneMap = (HashMap<Integer, Supervisor>) svMap.clone();
        
        for(Integer key : cloneMap.keySet()){
            if(svMap.get(key).crcErrorDetected){
                retransmitMessage(key);
                if(svMap.get(key).errorFromNoise){
                    results[1]++;
                }else{
                    results[0]++;
                }
                results[2]++;
            }
        }
        
        return results;
    }
    
    public String exportTXT(){
        String ret = "";
        for(Integer key : svMap.keySet()){
            ret += "Message " + svMap.get(key).indexOfMessage + "\n";
            ret += "Transmitter CRC: \n";
            ret += svMap.get(key).divToStringTrans.toString() + "\n";
            ret += "Receiver CRC: \n";
            ret += svMap.get(key).divToStringRec.toString()+ "\n";
        }
        return ret;
    }
}
