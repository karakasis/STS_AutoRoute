/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 *
 * @author Xrhstos
 */
public class Loading extends javax.swing.JFrame {

    private File logFile;
    private boolean loading;
    private Process proc;
    private boolean firstClick = true;

    /**
     * Creates new form Loading
     */
    public Loading() {
        setupLAF();

        initComponents();

        this.setVisible(true);

        loading = true;
    }

    private void setupLAF() {
        UIManager.put("RootPane.setupButtonVisible", false);
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //IManager.put( "TabbedPane.tabAreaInsets", new javax.Swing.plaf.InsetsUIResource( 3 , 20 , 2 , 20 ));
        UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
    }

    public boolean parentCallback() {
        if (loading) {
            JOptionPane.showMessageDialog(this,
                    "Log file has been loaded.",
                    "Log Loaded",
                    JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false); //error?
            loading = false;
            return true;
        } else {
            int response = JOptionPane.showConfirmDialog(this,
                    "A new Map layout has been found. \nDo you want to reset layout? ",
                    "Map Found", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            return response == JOptionPane.YES_OPTION;
        }
    }

    private File chooseLog() {
        JOptionPane.showMessageDialog(this,
                    "Please select the SlayTheSpire.log file located in your installation folder, \n"
                            + "usually found in C:\\Program Files (x86)\\Steam\\steamapps\\common\\SlayTheSpire .",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        File logFile = null;
        CustomFileChooser fc = new CustomFileChooser("log");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            logFile = fc.getSelectedFile();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            System.exit(1);
        }

        BufferedWriter out = null;

        File inputFile = null;
        try {
            inputFile = new File("log.txt");
            out = new BufferedWriter(new FileWriter(inputFile, false));
            //code goes here
            out.append(logFile.getAbsolutePath());
            //
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return logFile;
    }

    private void checkLog() {

        Scanner fileScanner = null;
        String logFileAdress = "";
        File logFile = null;
        try {
            fileScanner = new Scanner(Paths.get("log.txt"));
            while (fileScanner.hasNext()) {

                logFileAdress = fileScanner.nextLine();

            }
            logFile = new File(logFileAdress);
        } catch (IOException ex) {
            System.err.println("Error opening file");
            logFile = chooseLog();
        } catch (Exception ex) {
            System.out.println(ex.getClass().getCanonicalName());
            logFile = chooseLog();
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }

        if (logFile != null) {
            this.logFile = logFile;
        }

    }

    public File getLogFile() {
        checkLog();
        return logFile;
    }

    public void startSlayTheSpireSTEAM() {
        /*
        proc = null;
        try {
            proc = Runtime.getRuntime().exec("C:\\Program Files (x86)\\Steam\\Steam.exe -applaunch 646570");
            System.out.println("SlayTheSpire.exe launched!");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        if (firstClick) {
            firstClick = false;
            //popup
            JOptionPane.showMessageDialog(this,
                    "This is a quick shortcut that opens Slay the Spire within Steam Client.\n"
                            + "In order for this to function properly make sure you have closed the game first.",
                    "Launch Shortcut",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (isSlayTheSpireRunning()) {
            JOptionPane.showMessageDialog(this,
                    "Please close the game before attempting to launch it from here.",
                    "Launch Shortcut",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            URI uri = null;
            try {
                uri = new URI("steam://run/646570");
            } catch (URISyntaxException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void killSlayTheSpireSTEAM() {

        /*
            String line;

            Process p = null;
            try {
                p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            try {
                while ((line = input.readLine()) != null) {
                    if (line.contains("SlayTheSpire.exe")) {
                        System.out.println("SlayTheSpire.exe found!");
                        Runtime.getRuntime().exec("taskkill /F /IM SlayTheSpire.exe");
                        System.out.println("SlayTheSpire.exe killed!");
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
         */
        startSlayTheSpireSTEAM();

    }

    public boolean isSlayTheSpireRunning() {

        String line;

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        try {
            while ((line = input.readLine()) != null) {
                if (line.contains("SlayTheSpire.exe")) {
                    System.out.println("SlayTheSpire.exe found!");
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/load.gif"))); // NOI18N

        jLabel2.setText("Waiting for Slay the Spire to produce Log file...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Loading.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Loading.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Loading.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loading.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loading().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
