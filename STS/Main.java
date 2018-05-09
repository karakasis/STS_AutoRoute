/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.JFileChooser;

/**
 *
 * @author Xrhstos
 */
public class Main {

    public static ArrayList<String> log;
    
    public static void main(String[] args) {
        //GUI sts = new GUI();
        GUI sts = null;
        Tail tailer = null;
        log = new ArrayList<>();
        Controller controller = new Controller(tailer,sts);
        
    }
    
}
