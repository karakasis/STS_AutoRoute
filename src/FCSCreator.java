/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

/**
 *
 * @author Xrhstos
 */
public class FCSCreator {
    
    static Integer[] fcs;
    
    public FCSCreator(Integer[] decoder){
        fcs = new Integer[decoder.length - 1];
        for(int i=0; i<fcs.length;i++){
            fcs[i] = 0;
        }
    }
    
    public static Integer[] addFCS(Integer[] msg){
        Integer[] newMsg = new Integer[fcs.length + msg.length];
        
        for(int i=0; i<newMsg.length;i++){
            if(i<msg.length){
                newMsg[i] = msg[i];
            }else{
                newMsg[i] = fcs[i-msg.length];
            }
        }
        
        return newMsg;
    }
    
    public static Integer[] addFCS(Integer[] msg, Integer[] decoder){
        fcs = new Integer[decoder.length - 1];
        for(int i=0; i<fcs.length;i++){
            fcs[i] = 0;
        }
        Integer[] newMsg = new Integer[fcs.length + msg.length];
        
        for(int i=0; i<newMsg.length;i++){
            if(i<msg.length){
                newMsg[i] = msg[i];
            }else{
                newMsg[i] = fcs[i-msg.length];
            }
        }
        
        return newMsg;
    }
}
