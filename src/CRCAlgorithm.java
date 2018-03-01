/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Xrhstos
 */
public class CRCAlgorithm {

    public static Integer[] executeCRC(Integer[] msg, Integer[] decoder, String mode) {
        /*
        if(msg!=null){
                for(int j=0; j<msg.length;j++){
                    System.out.print(msg[j]);
                }
                System.out.println();
        }
         */
        ArrayList<String> divToStringList = new ArrayList<>();
        boolean firstTime = true;
        String msgString = "";
        for (int j = 0; j < decoder.length; j++) {
            msgString = msgString + " ";//2spaces
            msgString = msgString + "  ";//2spaces
        }

        //msgString = msgString + ".";
        msgString = msgString + "| ";//|space
        for (int j = 0; j < msg.length; j++) {
            msgString = msgString + msg[j] + " "; //      | 1 1 0 1 1 0 0 1 0
        }
        msgString = msgString + "\n";
        divToStringList.add(msgString);

        String decString = " ";
        for (int j = 0; j < decoder.length; j++) {
            decString = decString + decoder[j] + " ";
        }
        String finalDecToString = decString; //< - save it
        decString = decString + "| ";// 1 1 0 1 | 
        divToStringList.add(decString);
        int leftPadding = decoder.length;
        int index = 0;
        int valueableBits = 0;
        Integer[] result = new Integer[decoder.length];
        Integer[] division;
        Integer[] fcs;
        fcs = new Integer[decoder.length - 1];
        division = new Integer[decoder.length];

        outerloop:
        while (index < msg.length) {
            //Init array for result of XOR
            result = new Integer[decoder.length];
            //Find the 2 binary arrays
            int innerIndex = 0;
            String lineSpaces = "";
            String line = "";

            lineSpaces = printer(index - valueableBits + leftPadding);
            line = divToStringList.get(divToStringList.size() - 1);
            divToStringList.remove(divToStringList.size() - 1);
            if (firstTime) {
                //lineSpaces = fixedLengthString(lineSpaces, index);
                //lineSpaces = printer(index);
                firstTime = false;

            } else {
                //lineSpaces = fixedLengthString(lineSpaces, index - valueableBits + leftPadding);
                line = line.substring(0, line.length() - 1);
            }
            for (int bit = index; bit < index + decoder.length - valueableBits; bit++) {
                division[innerIndex + valueableBits] = msg[bit];
                innerIndex++;

                line = line + msg[bit] + " "; // 1 1 0 1

                if (bit == msg.length - 1) {
                    for (int i = 0; i < decoder.length; i++) {
                        if (division[i] == null) {
                            for (int j = i; j > 0; j--) {
                                division[j] = division[j - 1];
                            }
                            division[0] = 0;
                        }
                    }
                    fcs = Arrays.copyOfRange(division, 1, division.length);
                    String fcsString = printer(index + leftPadding);
                    //String fcsString = lineSpaces;
                    if (fcsString.length() > 6 + decoder.length * 2) {
                        fcsString = fcsString.substring(0, fcsString.length() - 6 - decoder.length * 2);
                    }
                    fcsString = fcsString + ".FCS: "; // 1 1 0 1
                    for (int k = 0; k < fcs.length; k++) {
                        fcsString = fcsString + fcs[k] + " "; // 1 1 0 1
                    }
                    line = line + "\n";
                    divToStringList.add(line);
                    divToStringList.add(fcsString);
                    break outerloop;
                }
            }

            line = line + "\n";
            divToStringList.add(line);

            String line2 = lineSpaces;
            String line3 = lineSpaces;
            String lineUnder = lineSpaces;
            //XOR the 2 binary arrays
            for (int bit = 0; bit < decoder.length; bit++) {
                result[bit] = Operators.xor(division[bit], decoder[bit]);

                line2 = line2 + decoder[bit] + " ";
                line3 = line3 + result[bit] + " ";
                lineUnder = lineUnder + "--";
            }
            line2 = line2 + "\n";
            lineUnder = lineUnder + "\n";
            line3 = line3 + "\n";
            divToStringList.add(line2);
            divToStringList.add(lineUnder);
            divToStringList.add(line3);

            index = index + innerIndex;
            //Find valueableBits = from left to right of result[] stop at first 1.
            valueableBits = result.length;
            for (int vBit = 0; vBit < result.length; vBit++) {
                if (result[vBit] == 0) {
                    valueableBits--;
                } else {
                    break;
                }
            }
            //If .. repeat XOR with decoder
            if (valueableBits == result.length) {

                String line4 = lineSpaces;
                String line5 = lineSpaces;

                for (int bit = 0; bit < decoder.length; bit++) {
                    result[bit] = Operators.xor(result[bit], decoder[bit]);

                    line4 = line4 + decoder[bit] + " ";
                    line5 = line5 + result[bit] + " ";
                }
                line4 = line4 + "\n";
                line5 = line5 + "\n";
                divToStringList.add(line4);
                divToStringList.add(lineUnder);
                divToStringList.add(line5);

                for (int vBit = 0; vBit < result.length; vBit++) {
                    if (result[vBit] == 0) {
                        valueableBits--;
                    } else {
                        break;
                    }
                }
            }
            //Make the new division array from result
            division = new Integer[decoder.length];
            for (int bit = 0; bit < valueableBits; bit++) {
                if (bit < valueableBits) {
                    division[bit] = result[result.length - valueableBits + bit];
                } else {
                    division[bit] = 0;
                }
            }
        }

        if (mode.equals("Encode")) {
            int i = 0;
            for (int bitAdd = msg.length - decoder.length + 1; bitAdd < msg.length; bitAdd++) {
                msg[bitAdd] = fcs[i++];
            }
            Transmitter.divToStringTrans = divToStringList;
        } else if (mode.equals("Decode")) {
            Receiver.divToStringRec = divToStringList;
            return fcs;
        }

        return msg;
    }

    private static String printer(int index) {
        String ret = "";
        for (int i = 0; i < index; i++) {
            ret = ret + " ";
            ret = ret + "  ";
        }

        ret = ret + "  ";
        return ret;
    }

    public static String fixedLengthString(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    public static String leftpad(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }
}
