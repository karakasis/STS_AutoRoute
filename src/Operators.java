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
public class Operators {
    
    public static int carry = 0;
    
    public static int xor(int x , int y){
        if(x == y){
            return 0;
        }else{
            return 1;
        }
    }
    
    public static int add(int x , int y){
        if(carry + x + y > 1){
            carry = 1;
            return 0;
        }else{
           return (carry + x + y);
        }
    }
    
}
