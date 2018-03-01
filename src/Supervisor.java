/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Xrhstos
 */
public class Supervisor implements java.io.Serializable {

    public int indexOfMessage;
    public Integer[] messageTransmitted;
    public Integer[] messageReceived;
    public Integer[] crcReceived;
    public boolean errorFromNoise;
    public boolean crcErrorDetected;
    public boolean noiseNoCRC;
    public int bitError;
    public ArrayList<String> divToStringTrans;
    public ArrayList<String> divToStringRec;
    public Integer[] decoder;
    
    public static int errorFromNoiseCounter = 0;
    public static int crcErrorDetectedCounter = 0;
    public static int noiseNoCRCCounter = 0;
    public static int[] bitErrors;

    public Supervisor(int index, Integer[] msg) {
        indexOfMessage = index;
        messageTransmitted = Arrays.copyOf(msg, msg.length);
        
        errorFromNoise = false;
        crcErrorDetected = false;
        noiseNoCRC = false;
    }
    
    public Supervisor(int index, Integer[] msg, Integer[] decoder) {
        indexOfMessage = index;
        messageTransmitted = Arrays.copyOf(msg, msg.length);
        this.decoder = decoder;
        errorFromNoise = false;
        crcErrorDetected = false;
        noiseNoCRC = false;
    }

    public void sendDataToSupervisor(Integer[] msg, Integer[] crc) {
        messageReceived = msg;
        crcReceived = crc;
        checkForError();
    }

    private void checkForError() {
        for (int i = 0; i < crcReceived.length; i++) {
            if (crcReceived[i] == 1) {
                System.out.print("CRC Error detected.  T: ");
                for (int j = 0; j < messageTransmitted.length; j++) {
                    System.out.print(messageTransmitted[j]);
                }
                System.out.print(" R: ");
                for (int j = 0; j < messageReceived.length; j++) {
                    System.out.print(messageReceived[j]);
                }
                System.out.print(" FCS: ");
                for (int j = 0; j < crcReceived.length; j++) {
                    System.out.print(crcReceived[j]);
                }
                System.out.print(" Noise: " + errorFromNoise);
                System.out.println();

                crcErrorDetected = true;
                crcErrorDetectedCounter++;
                break;
            }
        }
        /*
        if (!crcErrorDetected) {
            System.out.print("    T: ");
            for (int j = 0; j < messageTransmitted.length; j++) {
                System.out.print(messageTransmitted[j]);
            }
            System.out.print(" R: ");
            for (int j = 0; j < messageReceived.length; j++) {
                System.out.print(messageReceived[j]);
            }

            System.out.print(" FCS: ");
            for (int j = 0; j < crcReceived.length; j++) {
                System.out.print(crcReceived[j]);
            }
            System.out.print(" Noise: " + errorFromNoise);
            System.out.println();
        }
        */
        if (errorFromNoise && !crcErrorDetected) {
            System.out.println("Error but CRC didnt detect it.");
            noiseNoCRC = true;
            noiseNoCRCCounter++;
        }
    }
    
    public static String[] calcStatPercentages(int msgAmount){
        String[] ret = new String[3];
        
        double val1 = ((double) (errorFromNoiseCounter * 100) )/ msgAmount;
        ret[0] = new DecimalFormat("##.#####").format(val1) + " %";
        
        double val2 = ((double) (crcErrorDetectedCounter * 100) )/ msgAmount;
        ret[1] = new DecimalFormat("##.#####").format(val2) + " %";
        
        double val3 = ((double) (noiseNoCRCCounter * 100) )/ msgAmount;
        ret[2] = new DecimalFormat("##.#####").format(val3) + " %";
        return ret;
    }
}
