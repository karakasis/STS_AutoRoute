/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import com.sun.awt.AWTUtilities;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseEvent;
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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
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

        
        AWTUtilities.setWindowOpaque(this, false);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);

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
                            + "usually found in C:\\Program Files (x86)\\Steam\\steamapps\\common\\SlayTheSpire\\sendToDevs\\logs .",
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

        close = new javax.swing.JButton();
        minimize = new javax.swing.JButton();
        gif = new javax.swing.JLabel();
        text = new javax.swing.JLabel();
        titleName = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        back = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(400, 230));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/close.png"))); // NOI18N
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setPreferredSize(new java.awt.Dimension(25, 25));
        close.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/closeHighlight.png"))); // NOI18N
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        getContentPane().add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 7, -1, -1));

        minimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/min.png"))); // NOI18N
        minimize.setBorderPainted(false);
        minimize.setContentAreaFilled(false);
        minimize.setFocusPainted(false);
        minimize.setPreferredSize(new java.awt.Dimension(25, 25));
        minimize.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/minHighlight.png"))); // NOI18N
        minimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeActionPerformed(evt);
            }
        });
        getContentPane().add(minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 7, -1, -1));

        gif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/loader.gif"))); // NOI18N
        getContentPane().add(gif, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 400, 150));

        text.setFont(new java.awt.Font("Trebuchet MS", 2, 14)); // NOI18N
        text.setForeground(new java.awt.Color(234, 224, 208));
        text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text.setText("Waiting for Slay the Spire to produce Log file...");
        text.setToolTipText("");
        getContentPane().add(text, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 28, 390, 60));

        titleName.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        titleName.setForeground(new java.awt.Color(234, 223, 208));
        titleName.setText("AutoRoute Loader");
        getContentPane().add(titleName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/loadingBackTitle.png"))); // NOI18N
        title.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleMouseDragged(evt);
            }
        });
        title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                titleMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                titleMouseReleased(evt);
            }
        });
        getContentPane().add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/loadingBack.png"))); // NOI18N
        back.setMaximumSize(new java.awt.Dimension(400, 230));
        back.setMinimumSize(new java.awt.Dimension(400, 230));
        back.setPreferredSize(new java.awt.Dimension(400, 230));
        getContentPane().add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_closeActionPerformed

    private void minimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeActionPerformed
        // TODO add your handling code here:
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizeActionPerformed

    
    private boolean dontdragme = false;
    private boolean dragLock = false;
    private Point offset;
    private JComponent target;
    private Point start_drag;
    private Point start_loc;
    
    public static JFrame getFrame(Container target) {
        if (target instanceof JFrame) {
            return (JFrame) target;
        }
        return getFrame(target.getParent());
    }

    private Point getScreenLocation(java.awt.event.MouseEvent e) {
        Point cursor = e.getPoint();
        Point target_location = this.title.getLocationOnScreen();
        return new Point((int) (target_location.getX() + cursor.getX()),
                (int) (target_location.getY() + cursor.getY()));
    }
    
    private void titleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMousePressed
        // TODO add your handling code here:
        this.start_drag = this.getScreenLocation(evt);
        this.start_loc = this.getFrame(this.title).getLocation();
        dontdragme = false;
    }//GEN-LAST:event_titleMousePressed

    private void titleMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseReleased
        // TODO add your handling code here:
        if (dragLock) {
            dragLock = false;
        }
    }//GEN-LAST:event_titleMouseReleased

    private void titleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseDragged
        // TODO add your handling code here:
        if (!dontdragme) {
            Point current = this.getScreenLocation(evt);
            Point offset = new Point((int) current.getX() - (int) start_drag.getX(),
                    (int) current.getY() - (int) start_drag.getY());
            JFrame frame = this.getFrame(title);
            Point new_location = new Point(
                    (int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc
                    .getY() + offset.getY()));
            frame.setLocation(new_location);
        }
    }//GEN-LAST:event_titleMouseDragged

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
    private javax.swing.JLabel back;
    private javax.swing.JButton close;
    private javax.swing.JLabel gif;
    private javax.swing.JButton minimize;
    private javax.swing.JLabel text;
    private javax.swing.JLabel title;
    private javax.swing.JLabel titleName;
    // End of variables declaration//GEN-END:variables
}
