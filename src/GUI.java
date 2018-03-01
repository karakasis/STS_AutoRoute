/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ProgressMonitorInputStream;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEADING;
import static javax.swing.SwingConstants.TRAILING;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 *
 * @author Xrhstos
 */
public class GUI extends javax.swing.JFrame {

    private ArrayList<JLabel> bitErrorsList;
    private ArrayList<JLabel> stringList;
    FCSCreator fcsClass;
    Transmitter msgGen;
    Receiver receiver;
    private boolean comboSwitch = false;
    private Font font;
    private int defaultFontSize;
    private boolean resultsMode = true;

    private boolean addDisMSG = false;
    private boolean addDisPOL = false;
    private int bitsForListPOL = 0;
    private int bitsForListMSG = 0;

    public static class Options {

        public static int CUSTOMIZER_INDEX = 2;
        public static int MSG = 10000;
        public static int BIT_1 = 18;
        public static int BIT_2 = 6;
        public static double BER_1 = 0;
        public static double BER_2 = 0.009;
        public static int DEC_1 = 8;
        public static int DEC_2 = 1;
    }

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        listeners();
        indexField.setEditable(false);
        transmittedField.setEditable(false);
        receivedField.setEditable(false);
        noiseField.setEditable(false);
        bitErrorsField.setEditable(false);
        crcField.setEditable(false);
        TotalNoiseField.setEditable(false);
        TotalCRCDetectedField.setEditable(false);
        TotalCRCNoDetectedField.setEditable(false);
        polynomialDisplayer.setEditable(false);
        this.setLocationRelativeTo(null);
        jRadioButtonMenuItem3.setSelected(true);
        jLabel16.setVisible(false);
        jScrollPane2.setVisible(false);

        retransmitButton.setVisible(false);
        jLabel20.setVisible(false);
        binField.setVisible(false);
        batchRetransMenu.setEnabled(false);

        DefaultListModel demoList = new DefaultListModel();
        jList1.setModel(demoList);
        msgList.setModel(demoList);

        InputStream is = getClass().getResourceAsStream("/Raleway-Regular.ttf");

        try {
            //create the font to use. Specify the size!
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(font);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        /*
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        defaultFontSize = 0;
        changeFont(this, font, 0);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font);
        UIManager.put("InternalFrame.titleFont", font);

        Customize.setVisible(false);

        this.setVisible(true);
    }

    public void changeFont(Component component, Font font, int fontSize) {
        Font f = component.getFont();
        component.setFont(new Font(font.getName(), f.getStyle(), f.getSize() + fontSize));
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, font, fontSize);
            }
        }
    }

    private void listeners() {

        Action startPress = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton.isEnabled()) {
                    startButtonActionPerformed(null);
                }
            }
        };

        Action addPress = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton1.isEnabled()) {
                    startButton1ActionPerformed(null);
                }
            }
        };

        msgsAmountField.addActionListener(startPress);
        bitsField.addActionListener(startPress);
        berField.addActionListener(startPress);
        polynomialField.addActionListener(startPress);

        msgsAmountField1.addActionListener(addPress);
        polynomialField1.addActionListener(addPress);

        polynomialField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                updatePolynomial();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                updatePolynomial();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            }
        });
        polynomialField1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                updatePolynomial1();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                updatePolynomial1();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            }
        });
        msgsAmountField1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                updateMessageManual();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                updateMessageManual();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            }
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                jMenuItem6ActionPerformed(null);
            }
        });

    }

    private void updatePolynomial() {
        String decodeMsg = polynomialField.getText();
        Integer[] decodeMsgToInt = new Integer[decodeMsg.length()];
        boolean wrongInput = false;
        for (int i = 0; i < decodeMsg.length(); i++) {
            if (decodeMsg.charAt(i) != '1' && decodeMsg.charAt(i) != '0') {
                decodeMsgToInt = null;
                wrongInput = true;
                break;
            }
            decodeMsgToInt[i] = decodeMsg.charAt(i) - '0';
        }
        if (wrongInput) {
            polynomialField.setBackground(new Color(255, 51, 51, 255));
            startButton.setEnabled(false);
        } else {
            polynomialField.setBackground(new Color(255, 255, 255, 255));
            startButton.setEnabled(true);
        }
        int counter = 1;
        String polynomial = "";
        if (decodeMsgToInt != null && decodeMsgToInt.length > 0) {
            if (decodeMsgToInt[decodeMsgToInt.length - 1] == 1) {
                polynomial = "1";
            }
            for (int i = decodeMsgToInt.length - 2; i >= 0; i--) {

                if (decodeMsgToInt[i] == 1) {
                    if (counter == 1) {
                        polynomial = "x + " + polynomial;
                    } else {
                        polynomial = "x<sup>" + counter + "</sup> + " + polynomial;
                    }
                }
                //<html>x<sup>2</sup>+ x + 1</html>
                counter++;
            }
            if (decodeMsgToInt[decodeMsgToInt.length - 1] == 0) {
                polynomial = polynomial.substring(0, polynomial.length() - 2);
            }
            polynomial = "<html>" + polynomial + "</html>";
            //polynomialField.setToolTipText(polynomial);
            polynomialDisplayer.setContentType("text/html");
            polynomialDisplayer.setText(polynomial);
            polynomialDisplayer.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            polynomialDisplayer.setFont(new Font(font.getName(), Font.PLAIN, 12));
        }
    }

    public void activateInstance(int msgAm, int bitsAm, double berAm, Integer[] decodeMsgToInt, boolean mode) {
        comboSwitch = true;
        msgList.clearSelection();
        textArea1.setText("");
        textArea2.setText("");

        indexField.setText("");
        transmittedField.setText("");
        receivedField.setText("");
        noiseField.setText("");
        bitErrorsField.setText("");
        crcField.setText("");

        if (mode) {
            for (int i = 0; i < msgAm; i++) {
                msgGen.transmitMessage();
            }
        } else {
            msgsAmountField.setText(msgAm + "");
            bitsField.setText(bitsAm + "");
            berField.setText(berAm + "");
            String decodeMsg = "";
            for (int i = 0; i < decodeMsgToInt.length; i++) {
                decodeMsg = decodeMsg + decodeMsgToInt[i];
            }
            polynomialField.setText(decodeMsg);
            updatePolynomial();
        }

        calculateResults();

        HashMap<Integer, Supervisor> svMap = msgGen.getSupervisors();
        showChoicesActionPerformed(null);
    }

    public void calculateResults() {
        if (resultsMode) {
            String[] stats = Supervisor.calcStatPercentages(msgGen.msgAmount);
            TotalNoiseField.setText(stats[0]);
            TotalCRCDetectedField.setText(stats[1]);
            TotalCRCNoDetectedField.setText(stats[2]);
        } else {
            TotalNoiseField.setText(Supervisor.errorFromNoiseCounter + "");
            TotalCRCDetectedField.setText(Supervisor.crcErrorDetectedCounter + "");
            TotalCRCNoDetectedField.setText(Supervisor.noiseNoCRCCounter + "");
        }
        bitErrorsPanel1.removeAll();
        bitErrorsPanel2.removeAll();
        GridLayout lm1 = new GridLayout(0, 2);
        GridLayout lm2 = new GridLayout(0, 2);
        bitErrorsList = new ArrayList<>();
        bitErrorsPanel1.setLayout(lm1);
        bitErrorsPanel2.setLayout(lm2);
        //lm1.setRows(Supervisor.bitErrors.length/2);
        //lm1.setColumns(2);
        //lm2.setRows(Supervisor.bitErrors.length/2);
        //lm2.setColumns(2);
        int counter = 0;
        //System.out.println("Bit-Errors: ");
        Font fontGrid = new Font(font.getName(), Font.PLAIN, 10);
        boolean hide = false;
        if (msgGen.kBits + msgGen.decoder.length - 1 > 17) {
            hide = true;
        }
        for (int i = 0; i < Supervisor.bitErrors.length - 1; i++) {
            if (hide && Supervisor.bitErrors[counter] == 0) {
                bitErrorsList.add(new JLabel("BER1Grid" + i));
                bitErrorsList.add(new JLabel("BER1GridValue" + i));
                i++;
                counter++;
            } else {
                bitErrorsList.add(new JLabel("BER1Grid" + i));
                bitErrorsList.get(i).setText((counter + 1) + ". ");
                bitErrorsList.get(i).setFont(fontGrid);
                bitErrorsList.get(i).setHorizontalAlignment(CENTER);

                bitErrorsPanel1.add(bitErrorsList.get(i));

                bitErrorsList.add(new JLabel("BER1GridValue" + i));
                bitErrorsList.get(i + 1).setText(Supervisor.bitErrors[counter++] + "");
                bitErrorsList.get(i + 1).setFont(fontGrid);
                bitErrorsList.get(i + 1).setHorizontalAlignment(LEADING);

                bitErrorsPanel1.add(bitErrorsList.get(i + 1));

                i++;
            }
            bitErrorsPanel1.revalidate();
            bitErrorsPanel1.repaint();
        }
        int end = bitErrorsList.size() + Supervisor.bitErrors.length;
        for (int i = bitErrorsList.size(); i < end; i++) {
            if (hide && Supervisor.bitErrors[counter] == 0) {
                bitErrorsList.add(new JLabel("BER1Grid" + i));
                bitErrorsList.add(new JLabel("BER1GridValue" + i));
                i++;
                counter++;

            } else {
                bitErrorsList.add(new JLabel("BER2Grid" + i));
                bitErrorsList.get(i).setText((counter + 1) + ". ");
                bitErrorsList.get(i).setFont(fontGrid);
                bitErrorsList.get(i).setHorizontalAlignment(CENTER);

                bitErrorsPanel2.add(bitErrorsList.get(i));

                bitErrorsList.add(new JLabel("BER2GridValue" + i));
                bitErrorsList.get(i + 1).setText(Supervisor.bitErrors[counter++] + "");
                bitErrorsList.get(i + 1).setFont(fontGrid);
                bitErrorsList.get(i + 1).setHorizontalAlignment(LEADING);

                bitErrorsPanel2.add(bitErrorsList.get(i + 1));

                i++;
            }
            bitErrorsPanel2.revalidate();
            bitErrorsPanel2.repaint();
        }
        bitErrorsPanel1.setPreferredSize(bitErrorsPanel1.getPreferredSize());
        bitErrorsPanel2.setPreferredSize(bitErrorsPanel2.getPreferredSize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        messageList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgList = new javax.swing.JList<>();
        showChoices = new javax.swing.JComboBox<>();
        showLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        indexField = new javax.swing.JTextField();
        transmittedField = new javax.swing.JTextField();
        receivedField = new javax.swing.JTextField();
        noiseField = new javax.swing.JTextField();
        bitErrorsField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        crcField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        binField = new javax.swing.JTextField();
        retransmitButton = new javax.swing.JButton();
        CardsPanel = new javax.swing.JPanel();
        SettingsAndStart = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        msgsAmountField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bitsField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        polynomialDisplayer = new javax.swing.JEditorPane();
        berField = new javax.swing.JTextField();
        polynomialField = new javax.swing.JTextField();
        Customize = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        startButton1 = new javax.swing.JButton();
        polynomialDisplayer1 = new javax.swing.JEditorPane();
        polynomialField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        msgsAmountField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        resultsPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        TotalNoiseField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        bitErrorsPanel = new javax.swing.JPanel();
        bitErrorsPanel1 = new javax.swing.JPanel();
        bitErrorsPanel2 = new javax.swing.JPanel();
        TotalCRCDetectedField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        TotalCRCNoDetectedField = new javax.swing.JTextField();
        CRCPanel = new javax.swing.JTabbedPane();
        transCRC = new javax.swing.JScrollPane();
        textArea1 = new javax.swing.JTextArea();
        recCRC = new javax.swing.JScrollPane();
        textArea2 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        automaticButton = new javax.swing.JRadioButtonMenuItem();
        manualButton = new javax.swing.JRadioButtonMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        batchRetransMenu = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem4 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem5 = new javax.swing.JRadioButtonMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CRC ");
        setBackground(new java.awt.Color(255, 204, 153));
        setMinimumSize(new java.awt.Dimension(987, 740));
        setResizable(false);

        messageList.setBackground(new java.awt.Color(255, 255, 255));
        messageList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        messageList.setMinimumSize(new java.awt.Dimension(167, 444));

        msgList.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        msgList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                msgListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(msgList);

        showChoices.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        showChoices.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Noise", "CRC", "No CRC" }));
        showChoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showChoicesActionPerformed(evt);
            }
        });

        showLabel.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        showLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showLabel.setText("Show");

        javax.swing.GroupLayout messageListLayout = new javax.swing.GroupLayout(messageList);
        messageList.setLayout(messageListLayout);
        messageListLayout.setHorizontalGroup(
            messageListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(messageListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(messageListLayout.createSequentialGroup()
                        .addComponent(showLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showChoices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        messageListLayout.setVerticalGroup(
            messageListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messageListLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(messageListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showChoices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setMaximumSize(new java.awt.Dimension(462, 239));
        jPanel4.setMinimumSize(new java.awt.Dimension(462, 239));
        jPanel4.setPreferredSize(new java.awt.Dimension(462, 239));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel6.setText("Transmitted :");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 47, 76, 28));

        jLabel7.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel7.setText("Index : ");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 76, 28));

        jLabel8.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel8.setText("Noise Altered :");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 118, 76, 28));

        jLabel9.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel9.setText("Received :");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 81, 76, 28));

        jLabel10.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel10.setText("Bit-Errors :");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 187, 76, 28));

        indexField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(indexField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 17, 112, -1));

        transmittedField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(transmittedField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 51, 358, -1));

        receivedField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(receivedField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 85, 358, -1));

        noiseField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(noiseField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 122, 112, -1));

        bitErrorsField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(bitErrorsField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 191, 112, -1));

        jLabel11.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel11.setText("CRC Detected :");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 152, 76, 28));

        crcField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(crcField, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 156, 112, -1));

        jLabel20.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel20.setText("Binary Polynomial:");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 13, -1, 28));

        binField.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N
        jPanel4.add(binField, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 17, 112, -1));

        retransmitButton.setText("Re-Transmit");
        retransmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retransmitButtonActionPerformed(evt);
            }
        });
        jPanel4.add(retransmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 190, 105, 31));

        CardsPanel.setLayout(new java.awt.CardLayout());

        SettingsAndStart.setBackground(new java.awt.Color(255, 204, 153));
        SettingsAndStart.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SettingsAndStart.setMaximumSize(new java.awt.Dimension(306, 448));
        SettingsAndStart.setMinimumSize(new java.awt.Dimension(306, 448));
        SettingsAndStart.setPreferredSize(new java.awt.Dimension(306, 448));

        jLabel1.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel1.setText("Binary Messages");
        jLabel1.setToolTipText("Insert an integer number to define how many messages the transmitter will send.");

        msgsAmountField.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        msgsAmountField.setText("Insert Number of Messages...");
        msgsAmountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                msgsAmountFieldFocusGained(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel2.setText("Bits Per Message");
        jLabel2.setToolTipText("Insert an integer number to define how many bits will a message send.");

        bitsField.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        bitsField.setText("Insert Number of Bits...");
        bitsField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bitsFieldFocusGained(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel3.setText("Bit Error Rate");
        jLabel3.setToolTipText("<html> The bit error rate (BER) is the number of bit errors per unit time.<br /> The bit error ratio (also BER) is the number of bit errors divided by the total number <br /> of transferred bits during a studied time interval. <br />Insert a float number less than 1.<br /> E.g. 0.003 BER will be translated as 3 per thousand bits.");

        jLabel4.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel4.setText("Binary Polynomial");
        jLabel4.setToolTipText("<html> The General CRC Generator block generates cyclic redundancy code (CRC) bits for each<br /> input data frame and appends them to the frame.<br /> This block accepts a binary string input signal. <br /> You specify the generator polynomial for the CRC algorithm <br />using the Generator polynomial parameter. <br />This block is general in the sense that the degree of the polynomial <br />does not need to be a power of two. You represent the polynomial as: <br /> As a binary string containing the coefficients in descending order of powers.<br /> For example 1101  represents the polynomial x^3 + x^2 + 1. <br /> Your input must always start with 1.");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        polynomialDisplayer.setBorder(null);
        polynomialDisplayer.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N

        berField.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        berField.setText("Insert Bit Error Rate...");
        berField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                berFieldFocusGained(evt);
            }
        });

        polynomialField.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        polynomialField.setText("Insert a binary sequence...");
        polynomialField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                polynomialFieldFocusGained(evt);
            }
        });

        javax.swing.GroupLayout SettingsAndStartLayout = new javax.swing.GroupLayout(SettingsAndStart);
        SettingsAndStart.setLayout(SettingsAndStartLayout);
        SettingsAndStartLayout.setHorizontalGroup(
            SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsAndStartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SettingsAndStartLayout.createSequentialGroup()
                        .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(polynomialDisplayer)
                            .addComponent(msgsAmountField)
                            .addComponent(bitsField)
                            .addComponent(berField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(polynomialField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SettingsAndStartLayout.setVerticalGroup(
            SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsAndStartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(msgsAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bitsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(berField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SettingsAndStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(polynomialField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(polynomialDisplayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        CardsPanel.add(SettingsAndStart, "card2");

        Customize.setBackground(new java.awt.Color(157, 184, 236));
        Customize.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Customize.setMaximumSize(new java.awt.Dimension(306, 448));
        Customize.setMinimumSize(new java.awt.Dimension(306, 448));
        Customize.setPreferredSize(new java.awt.Dimension(306, 448));

        jLabel5.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel5.setText("Binary Message");
        jLabel5.setToolTipText("<html>\n This block accepts a binary string input signal. <br /> You specify the data to be sent from the transmitter to the receiver. <br /> Represent the message as: <br /> As a binary string.<br /> For example 110101011.");

        jLabel16.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel16.setText("Bit Errors");
        jLabel16.setToolTipText("<html> Select the bit to receive a noise alteration. <br/>\nIf you choose the Noise Altered option, at least <br/>\none bit must be selected. <br/>\nSelect multiple bits by ctrl + clicking the items on the list. <br/>\nThe bit indexing is calculated from left to right order.");

        jLabel18.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel18.setText("Noise Altered");
        jLabel18.setToolTipText("<html> Select whether the data sent, will get altered by noise or not.");

        jLabel19.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel19.setText("Binary Polynomial");
        jLabel19.setToolTipText("<html> The General CRC Generator block generates cyclic redundancy code (CRC) bits for each<br /> input data frame and appends them to the frame.<br /> This block accepts a binary string input signal. <br /> You specify the generator polynomial for the CRC algorithm <br />using the Generator polynomial parameter. <br />This block is general in the sense that the degree of the polynomial <br />does not need to be a power of two. You represent the polynomial as: <br /> As a binary string containing the coefficients in descending order of powers.<br /> For example 1101  represents the polynomial x^3 + x^2 + 1. <br /> Your input must always start with 1.");

        startButton1.setText("Add");
        startButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButton1ActionPerformed(evt);
            }
        });

        polynomialDisplayer1.setBorder(null);
        polynomialDisplayer1.setFont(new java.awt.Font("Raleway", 0, 11)); // NOI18N

        polynomialField1.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        polynomialField1.setText("Insert a binary sequence...");
        polynomialField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                polynomialField1FocusGained(evt);
            }
        });

        jCheckBox1.setBorder(null);
        jCheckBox1.setContentAreaFilled(false);
        jCheckBox1.setHideActionText(true);
        jCheckBox1.setIconTextGap(0);
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.setOpaque(true);
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jCheckBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCheckBox1PropertyChange(evt);
            }
        });

        msgsAmountField1.setFont(new java.awt.Font("Raleway", 0, 9)); // NOI18N
        msgsAmountField1.setText("Insert message...");
        msgsAmountField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                msgsAmountField1FocusGained(evt);
            }
        });

        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout CustomizeLayout = new javax.swing.GroupLayout(Customize);
        Customize.setLayout(CustomizeLayout);
        CustomizeLayout.setHorizontalGroup(
            CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomizeLayout.createSequentialGroup()
                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CustomizeLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CustomizeLayout.createSequentialGroup()
                        .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CustomizeLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CustomizeLayout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(polynomialDisplayer1)
                                            .addComponent(polynomialField1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(CustomizeLayout.createSequentialGroup()
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox1))
                                    .addGroup(CustomizeLayout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(CustomizeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(msgsAmountField1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        CustomizeLayout.setVerticalGroup(
            CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomizeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(msgsAmountField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CustomizeLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(polynomialField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(polynomialDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CustomizeLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jCheckBox1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                .addComponent(startButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        CardsPanel.add(Customize, "card1");

        resultsPanel.setBackground(new java.awt.Color(255, 255, 255));
        resultsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N
        jLabel12.setText("Results");

        jLabel15.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel15.setText("Bit-Errors :");

        jLabel13.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel13.setText("Noise Altered Data :");

        jLabel14.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel14.setText("CRC Detected :");

        bitErrorsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bitErrorsPanel.setMaximumSize(new java.awt.Dimension(330, 99));
        bitErrorsPanel.setMinimumSize(new java.awt.Dimension(330, 99));
        bitErrorsPanel.setLayout(new java.awt.GridLayout(1, 0));

        bitErrorsPanel1.setBackground(new java.awt.Color(255, 255, 255));
        bitErrorsPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bitErrorsPanel1.setMaximumSize(new java.awt.Dimension(247, 97));

        javax.swing.GroupLayout bitErrorsPanel1Layout = new javax.swing.GroupLayout(bitErrorsPanel1);
        bitErrorsPanel1.setLayout(bitErrorsPanel1Layout);
        bitErrorsPanel1Layout.setHorizontalGroup(
            bitErrorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );
        bitErrorsPanel1Layout.setVerticalGroup(
            bitErrorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );

        bitErrorsPanel.add(bitErrorsPanel1);

        bitErrorsPanel2.setBackground(new java.awt.Color(255, 255, 255));
        bitErrorsPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bitErrorsPanel2.setMaximumSize(new java.awt.Dimension(247, 97));

        javax.swing.GroupLayout bitErrorsPanel2Layout = new javax.swing.GroupLayout(bitErrorsPanel2);
        bitErrorsPanel2.setLayout(bitErrorsPanel2Layout);
        bitErrorsPanel2Layout.setHorizontalGroup(
            bitErrorsPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 163, Short.MAX_VALUE)
        );
        bitErrorsPanel2Layout.setVerticalGroup(
            bitErrorsPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );

        bitErrorsPanel.add(bitErrorsPanel2);

        TotalCRCDetectedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalCRCDetectedFieldActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Raleway", 0, 10)); // NOI18N
        jLabel17.setText("CRC Not detected :");

        TotalCRCNoDetectedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalCRCNoDetectedFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout resultsPanelLayout = new javax.swing.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(resultsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(bitErrorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(resultsPanelLayout.createSequentialGroup()
                                .addComponent(TotalCRCDetectedField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TotalCRCNoDetectedField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(resultsPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(resultsPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TotalNoiseField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalNoiseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TotalCRCDetectedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TotalCRCNoDetectedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(bitErrorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        CRCPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        CRCPanel.setMaximumSize(new java.awt.Dimension(100, 100));
        CRCPanel.setMinimumSize(new java.awt.Dimension(100, 100));

        textArea1.setColumns(1);
        textArea1.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        textArea1.setRows(1);
        textArea1.setMaximumSize(new java.awt.Dimension(104, 64));
        textArea1.setMinimumSize(new java.awt.Dimension(104, 64));
        transCRC.setViewportView(textArea1);

        CRCPanel.addTab("Transmitter CRC", transCRC);

        textArea2.setColumns(1);
        textArea2.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        textArea2.setRows(1);
        textArea2.setMaximumSize(new java.awt.Dimension(104, 64));
        textArea2.setMinimumSize(new java.awt.Dimension(104, 64));
        recCRC.setViewportView(textArea2);

        CRCPanel.addTab("Receiver CRC", recCRC);

        jMenu2.setText("File");

        jMenu5.setText("Mode");

        buttonGroup2.add(automaticButton);
        automaticButton.setSelected(true);
        automaticButton.setText("Automatic");
        automaticButton.setToolTipText("Sets mode to Automatic. Generate random messages from fixed input.");
        automaticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticButtonActionPerformed(evt);
            }
        });
        jMenu5.add(automaticButton);

        buttonGroup2.add(manualButton);
        manualButton.setText("Manual");
        manualButton.setToolTipText("<html> Sets mode to Manual. Generate messages from direct input, and create a custom Transmitter.<br/> <i>Statistic results are disabled since input may differ</i>.");
        manualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualButtonActionPerformed(evt);
            }
        });
        jMenu5.add(manualButton);

        jMenu2.add(jMenu5);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Screenshot");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);
        jMenu2.add(jSeparator5);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Import CRC");
        jMenuItem1.setToolTipText("Selects a .crc file and opens the saved instance.");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Export as CRC");
        jMenuItem3.setToolTipText("Saves the current instance in a .crc file.");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Export and Exit");
        jMenuItem5.setToolTipText("Saves the current instance in a .crc file, then exits.");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);
        jMenu2.add(jSeparator2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Clear Simulation");
        jMenuItem4.setToolTipText("Deletes current instance.");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);
        jMenu2.add(jSeparator3);

        jMenuItem6.setText("Exit");
        jMenuItem6.setToolTipText("");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Edit");

        jMenuItem7.setText("Run Target Simulation");
        jMenuItem7.setToolTipText("<html> Simulates CRC with the predefined inputs <br/ > Bits Per Message: 10 <br/ > Bit Error Rate: 0.001  <br/ > Binary Polynomial: 110101");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setText("Run Random Simulation");
        jMenuItem8.setToolTipText("Simulates a CRC instance from randomly generated input.");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);
        jMenu3.add(jSeparator6);

        batchRetransMenu.setText("Batch Re-Transmission");
        batchRetransMenu.setToolTipText("Resends all faulty messages to receiver.");
        batchRetransMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchRetransMenuActionPerformed(evt);
            }
        });
        jMenu3.add(batchRetransMenu);
        jMenu3.add(jSeparator4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Change Results Format");
        jMenuItem2.setToolTipText("Swaps format between constant values and percentages.");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem10.setText("Customize Random Simulation");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);
        jMenu3.add(jSeparator1);

        jMenu1.setText("Change Font Size");
        jMenu1.setToolTipText("Changes the font size.");

        buttonGroup1.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("-2");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem1);

        buttonGroup1.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText("-1");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem2);

        buttonGroup1.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setText("Default");
        jRadioButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem3);

        buttonGroup1.add(jRadioButtonMenuItem4);
        jRadioButtonMenuItem4.setText("+1");
        jRadioButtonMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem4);

        buttonGroup1.add(jRadioButtonMenuItem5);
        jRadioButtonMenuItem5.setText("+2");
        jRadioButtonMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem5);

        jMenu3.add(jMenu1);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("About");

        jMenuItem9.setText("Author");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CRCPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CardsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CardsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(messageList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CRCPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        boolean execute = true;
        String msgs = msgsAmountField.getText();
        int msgAm = 0;
        try {
            msgAm = Integer.parseInt(msgs);
        } catch (NumberFormatException e) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Binary Messages. Try entering an integer.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (msgAm <= 0) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Binary Messages. Enter a possitive value.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String bits = bitsField.getText();
        int bitsAm = 0;
        try {
            bitsAm = Integer.parseInt(bits);
        } catch (NumberFormatException e) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Bits per Message. Try entering an integer.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (bitsAm <= 0) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Binary Messages. Enter a possitive value.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String ber = berField.getText();
        ber = ber.replace(",", ".").replace(" ", "");
        double berAm = 0;
        try {
            berAm = Double.parseDouble(ber);
        } catch (NumberFormatException e) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Bit Error Rate. Try entering a float number less than 1.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (berAm <= 0.0 || berAm > 1.0) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Binary Messages. Try entering a float number [0-1].",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String decodeMsg = polynomialField.getText();
        Integer[] decodeMsgToInt = new Integer[decodeMsg.length()];
        if (!decodeMsg.equals("Insert a binary sequence...") && decodeMsg.charAt(0) - '0' != 0) {
            for (int i = 0; i < decodeMsg.length(); i++) {
                decodeMsgToInt[i] = decodeMsg.charAt(i) - '0';
            }
        } else {
            execute = false;
            polynomialField.setBackground(new Color(255, 51, 51, 255));
            startButton.setEnabled(false);
        }

        if (execute) {
            fcsClass = new FCSCreator(decodeMsgToInt);
            msgGen = new Transmitter(msgAm, bitsAm, decodeMsgToInt, berAm);
            receiver = new Receiver(decodeMsgToInt);
            activateInstance(msgAm, bitsAm, berAm, decodeMsgToInt, true);
            /*
            DefaultListModel demoList = new DefaultListModel();
            for (Integer sv : svMap.keySet()) {
                demoList.addElement("Message " + svMap.get(sv).indexOfMessage);
            }
            msgList.setModel(demoList);
             */
        }
        /*
        Insert Number of Messages...
        Insert Number of Bits...
        Insert BER...
        Insert a binary sequence...
         */
    }//GEN-LAST:event_startButtonActionPerformed

    private void TotalCRCDetectedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalCRCDetectedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalCRCDetectedFieldActionPerformed

    private void TotalCRCNoDetectedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalCRCNoDetectedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalCRCNoDetectedFieldActionPerformed

    private void msgListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_msgListValueChanged
        // TODO add your handling code here:
        if (!comboSwitch) {
            String msg = msgList.getSelectedValue();
            String num = "";
            for (int i = 8; i < msg.length(); i++) {
                num = num + msg.charAt(i);
            }
            int msgIndex = 0;
            try {
                msgIndex = Integer.parseInt(num);
            } catch (NumberFormatException e) {
            }
            HashMap<Integer, Supervisor> svMap = msgGen.getSupervisors();
            showInfo(svMap.get(msgIndex));
        }
        comboSwitch = false;
    }//GEN-LAST:event_msgListValueChanged

    private void showChoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showChoicesActionPerformed
        // TODO add your handling code here:
        int selection = showChoices.getSelectedIndex();
        if (msgGen != null) {
            HashMap<Integer, Supervisor> svMap = msgGen.getSupervisors();
            DefaultListModel demoList = new DefaultListModel();
            switch (selection) {
                case 0:
                    for (Integer sv : svMap.keySet()) {
                        demoList.addElement("Message " + svMap.get(sv).indexOfMessage);
                    }
                    break;
                case 1:
                    for (Integer sv : svMap.keySet()) {
                        if (svMap.get(sv).errorFromNoise) {
                            demoList.addElement("Message " + svMap.get(sv).indexOfMessage);
                        }
                    }
                    break;
                case 2:
                    for (Integer sv : svMap.keySet()) {
                        if (svMap.get(sv).crcErrorDetected) {
                            demoList.addElement("Message " + svMap.get(sv).indexOfMessage);
                        }
                    }
                    break;
                case 3:
                    for (Integer sv : svMap.keySet()) {
                        if (svMap.get(sv).noiseNoCRC) {
                            demoList.addElement("Message " + svMap.get(sv).indexOfMessage);
                        }
                    }
                    break;
                default:
                    break;
            }
            comboSwitch = true;
            msgList.setModel(demoList);
        }

    }//GEN-LAST:event_showChoicesActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        if (resultsMode) {
            resultsMode = false;
            TotalNoiseField.setText(Supervisor.errorFromNoiseCounter + "");
            TotalCRCDetectedField.setText(Supervisor.crcErrorDetectedCounter + "");
            TotalCRCNoDetectedField.setText(Supervisor.noiseNoCRCCounter + "");
        } else {
            resultsMode = true;
            String[] stats = Supervisor.calcStatPercentages(msgGen.msgAmount);
            TotalNoiseField.setText(stats[0]);
            TotalCRCDetectedField.setText(stats[1]);
            TotalCRCNoDetectedField.setText(stats[2]);

        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        // TODO add your handling code here:
        changeFont(this, font, -2 - defaultFontSize);
        defaultFontSize = -2;
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
        // TODO add your handling code here:
        changeFont(this, font, -1 - defaultFontSize);
        defaultFontSize = -1;
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void jRadioButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem3ActionPerformed
        // TODO add your handling code here:
        changeFont(this, font, 0 - defaultFontSize);
        defaultFontSize = 0;
    }//GEN-LAST:event_jRadioButtonMenuItem3ActionPerformed

    private void jRadioButtonMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem4ActionPerformed
        // TODO add your handling code here:
        changeFont(this, font, 1 - defaultFontSize);
        defaultFontSize = 1;
    }//GEN-LAST:event_jRadioButtonMenuItem4ActionPerformed

    private void jRadioButtonMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem5ActionPerformed
        // TODO add your handling code here:
        changeFont(this, font, 2 - defaultFontSize);
        defaultFontSize = 2;
    }//GEN-LAST:event_jRadioButtonMenuItem5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CustomFileChooser fc = new CustomFileChooser("crc");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            FileInputStream fileIn = null;
            ObjectInputStream in = null;
            try {
                fileIn = new FileInputStream(file.getAbsolutePath());
                in = new ObjectInputStream(fileIn);
                msgGen = (Transmitter) in.readObject();
                in.close();
                fileIn.close();
                if (msgGen.mode.equals(("auto"))) {
                    msgGen.loadInstanceStats();
                    activateInstance(msgGen.msgAmount, msgGen.kBits, msgGen.BER, msgGen.decoder, false);
                    batchRetransMenu.setEnabled(true);

                    resultsPanel.setVisible(true);
                    automaticButton.setSelected(true);
                    manualButton.setSelected(false);
                } else if (msgGen.mode.equals(("manual"))) {
                    DefaultListModel listModel = (DefaultListModel) msgList.getModel();
                    listModel.removeAllElements();
                    textArea1.setText("");
                    textArea2.setText("");

                    indexField.setText("");
                    transmittedField.setText("");
                    receivedField.setText("");
                    noiseField.setText("");
                    bitErrorsField.setText("");
                    crcField.setText("");
                    bitErrorsPanel1.removeAll();
                    bitErrorsPanel2.removeAll();
                    bitErrorsPanel1.repaint();
                    bitErrorsPanel2.repaint();

                    TotalNoiseField.setText("");
                    TotalCRCDetectedField.setText("");
                    TotalCRCNoDetectedField.setText("");
                    HashMap<Integer, Supervisor> svMap = msgGen.getSupervisors();
                    showChoicesActionPerformed(null);
                    manualButtonActionPerformed(null);

                    automaticButton.setSelected(false);
                    manualButton.setSelected(true);

                    batchRetransMenu.setEnabled(false);

                    resultsPanel.setVisible(false);
                }
            } catch (IOException i) {
                i.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "The file is not compatible with this version.", "Operation Aborted", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return;
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    fileIn.close();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        if (msgGen != null) {
            CustomFileChooser fc = new CustomFileChooser("crc");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileOutputStream fileOut = null;
                    if (file.getAbsolutePath().endsWith(".crc")) {
                        fileOut = new FileOutputStream(file.getAbsolutePath());
                    } else {
                        fileOut = new FileOutputStream(file.getAbsolutePath() + ".crc");
                    }
                    if (msgGen.mode.equals(("auto"))) {
                        msgGen.saveInstanceStats();
                    }
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(msgGen);
                    out.close();
                    fileOut.close();
                    JOptionPane.showMessageDialog(this,
                            "Instance was succesfully saved.", "File Saved", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException i) {
                    i.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "We were unable to save the instance.", "Save Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "There is no active instance.", "Operation Aborted", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void bitsFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bitsFieldFocusGained
        // TODO add your handling code here:
        bitsField.selectAll();
    }//GEN-LAST:event_bitsFieldFocusGained

    private void msgsAmountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_msgsAmountFieldFocusGained
        // TODO add your handling code here:
        msgsAmountField.selectAll();
    }//GEN-LAST:event_msgsAmountFieldFocusGained

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit? ",
                "Exit", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        String msgs = (String) JOptionPane.showInputDialog(
                this,
                "CRC with the predefined inputs \n"
                + ">Bits Per Message: 10 \n"
                + ">Bit Error Rate: 0.001 \n"
                + ">Binary Polynomial: 110101 \n"
                + " Insert Binary Messages Amount :",
                "Run Target Simulation",
                JOptionPane.PLAIN_MESSAGE);
        if (!(msgs == null || (msgs != null && ("".equals(msgs))))) {
            int msgAm = 0;
            boolean execute = true;
            try {
                msgAm = Integer.parseInt(msgs);
            } catch (NumberFormatException e) {
                execute = false;
                JOptionPane.showMessageDialog(this,
                        "Incorrect value in Binary Messages. Try entering an integer.",
                        "Input Format Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            if (msgAm <= 0) {
                execute = false;
                JOptionPane.showMessageDialog(this,
                        "Incorrect value in Binary Messages. Enter a possitive value.",
                        "Input Format Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            if (execute) {
                Integer[] decodeMsgToInt = new Integer[]{1, 1, 0, 1, 0, 1};
                fcsClass = new FCSCreator(decodeMsgToInt);
                msgGen = new Transmitter(msgAm, 10, decodeMsgToInt, 0.001);
                receiver = new Receiver(decodeMsgToInt);
                activateInstance(msgAm, 10, 0.001, decodeMsgToInt, true);

                msgsAmountField.setText(msgAm + "");
                bitsField.setText(10 + "");
                berField.setText(0.001 + "");
                String decodeMsg = "";
                for (int i = 0; i < decodeMsgToInt.length; i++) {
                    decodeMsg = decodeMsg + decodeMsgToInt[i];
                }
                polynomialField.setText(decodeMsg);
                updatePolynomial();

                batchRetransMenu.setEnabled(true);

                resultsPanel.setVisible(true);

                automaticButton.setSelected(true);
                manualButton.setSelected(false);
            }
        }

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void berFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_berFieldFocusGained
        // TODO add your handling code here:
        berField.selectAll();
    }//GEN-LAST:event_berFieldFocusGained

    private void polynomialFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_polynomialFieldFocusGained
        // TODO add your handling code here:
        polynomialField.selectAll();
    }//GEN-LAST:event_polynomialFieldFocusGained

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        if (msgGen != null) {
            CustomFileChooser fc = new CustomFileChooser("crc");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileOutputStream fileOut = null;
                    if (file.getAbsolutePath().endsWith(".crc")) {
                        fileOut = new FileOutputStream(file.getAbsolutePath());
                    } else {
                        fileOut = new FileOutputStream(file.getAbsolutePath() + ".crc");
                    }
                    msgGen.saveInstanceStats();
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(msgGen);
                    out.close();
                    fileOut.close();
                    JOptionPane.showMessageDialog(this,
                            "Instance was succesfully saved.", "File Saved", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } catch (IOException i) {
                    i.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "We were unable to save the instance.", "Save Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "There is no active instance.", "Operation Aborted", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        msgGen = null;
        fcsClass = null;
        receiver = null;
        comboSwitch = true;
        DefaultListModel listModel = (DefaultListModel) msgList.getModel();
        listModel.removeAllElements();
        textArea1.setText("");
        textArea2.setText("");

        indexField.setText("");
        transmittedField.setText("");
        receivedField.setText("");
        noiseField.setText("");
        bitErrorsField.setText("");
        crcField.setText("");
        bitErrorsPanel1.removeAll();
        bitErrorsPanel2.removeAll();
        bitErrorsPanel1.repaint();
        bitErrorsPanel2.repaint();

        TotalNoiseField.setText("");
        TotalCRCDetectedField.setText("");
        TotalCRCNoDetectedField.setText("");

        binField.setVisible(false);
        jLabel20.setVisible(false);
        retransmitButton.setVisible(false);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) CardsPanel.getLayout();
        cl.show(CardsPanel, "card2");
        Random rnd = new Random();
        Integer[] decodeMsgToInt = new Integer[rnd.nextInt(GUI.Options.DEC_1) + GUI.Options.DEC_2];
        decodeMsgToInt[0] = 1;
        for (int i = 1; i < decodeMsgToInt.length; i++) {
            decodeMsgToInt[i] = rnd.nextInt(2);
        }
        fcsClass = new FCSCreator(decodeMsgToInt);
        msgGen = new Transmitter(rnd.nextInt(GUI.Options.MSG) + 1,
                rnd.nextInt(GUI.Options.BIT_1) + GUI.Options.BIT_2, decodeMsgToInt,
                ThreadLocalRandom.current().nextDouble(GUI.Options.BER_1, GUI.Options.BER_2));
        receiver = new Receiver(decodeMsgToInt);
        activateInstance(msgGen.msgAmount, msgGen.kBits, msgGen.BER, msgGen.decoder, true);

        msgsAmountField.setText(msgGen.msgAmount + "");
        bitsField.setText(msgGen.kBits + "");
        berField.setText(msgGen.BER + "");
        String decodeMsg = "";
        for (int i = 0; i < msgGen.decoder.length; i++) {
            decodeMsg = decodeMsg + msgGen.decoder[i];
        }
        polynomialField.setText(decodeMsg);
        updatePolynomial();
        batchRetransMenu.setEnabled(true);

        resultsPanel.setVisible(true);
        binField.setVisible(false);
        jLabel20.setVisible(false);
        automaticButton.setSelected(true);
        manualButton.setSelected(false);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "A program developed by Christos Karakasis, as \n "
                + "a project for Digital Communications class.", "About Author", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void msgsAmountField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_msgsAmountField1FocusGained
        // TODO add your handling code here:
        msgsAmountField1.selectAll();
    }//GEN-LAST:event_msgsAmountField1FocusGained

    private void startButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButton1ActionPerformed
        // TODO add your handling code here:
        boolean execute = true;
        String msg = msgsAmountField1.getText();
        Integer[] msgInt = new Integer[msg.length()];
        if (!msg.equals("Insert message...")) {
            for (int i = 0; i < msg.length(); i++) {
                msgInt[i] = msg.charAt(i) - '0';
            }
        } else {
            execute = false;
            msgsAmountField1.setBackground(new Color(255, 51, 51, 255));
            startButton1.setEnabled(false);
            addDisMSG = true;
        }
        if (msgInt.length <= 0) {
            execute = false;
            JOptionPane.showMessageDialog(this,
                    "Incorrect value in Binary Message. Enter a possitive value.",
                    "Input Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String decodeMsg = polynomialField1.getText();
        Integer[] decodeMsgToInt = new Integer[decodeMsg.length()];
        if (!decodeMsg.equals("Insert a binary sequence...") && decodeMsg.charAt(0) - '0' != 0) {
            for (int i = 0; i < decodeMsg.length(); i++) {
                decodeMsgToInt[i] = decodeMsg.charAt(i) - '0';
            }
        } else {
            execute = false;
            polynomialField1.setBackground(new Color(255, 51, 51, 255));
            startButton1.setEnabled(false);
            addDisPOL = true;
        }

        int[] selectedIndices = null;
        if (jCheckBox1.isSelected()) {
            selectedIndices = jList1.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Incorrect value in Bit Error List. Select at least one value.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                execute = false;
            }

        }

        if (execute) {
            if (msgGen == null) {
                msgGen = new Transmitter();
                receiver = new Receiver();
                msgGen.addMessage(msgInt, decodeMsgToInt, jCheckBox1.isSelected(), selectedIndices);
                comboSwitch = true;
                msgList.clearSelection();
                textArea1.setText("");
                textArea2.setText("");

                indexField.setText("");
                transmittedField.setText("");
                receivedField.setText("");
                noiseField.setText("");
                bitErrorsField.setText("");
                crcField.setText("");
            } else {
                msgGen.addMessage(msgInt, decodeMsgToInt, jCheckBox1.isSelected(), selectedIndices);
            }
            HashMap<Integer, Supervisor> svMap = msgGen.getSupervisors();
            showChoicesActionPerformed(null);
        }
    }//GEN-LAST:event_startButton1ActionPerformed

    private void polynomialField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_polynomialField1FocusGained
        // TODO add your handling code here:
        polynomialField1.selectAll();
    }//GEN-LAST:event_polynomialField1FocusGained

    private void jCheckBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCheckBox1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1PropertyChange

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jLabel16.setVisible(true);
            jScrollPane2.setVisible(true);
            updateMessageManual();
            updatePolynomial1();
        } else {
            jLabel16.setVisible(false);
            jScrollPane2.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void retransmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retransmitButtonActionPerformed
        // TODO add your handling code here:
        msgGen.retransmitMessage(Integer.parseInt(indexField.getText()));
        showInfo(msgGen.getSupervisors().get(Integer.parseInt(indexField.getText())));

        if (msgGen.getSupervisors().get(Integer.parseInt(indexField.getText())).errorFromNoise) {
            JOptionPane.showMessageDialog(this,
                    "Message was delivered with noise.", "Re-Transmission Failure", JOptionPane.ERROR_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Message was delivered successfully.", "Re-Transmission Success", JOptionPane.INFORMATION_MESSAGE);
            showChoicesActionPerformed(null);
            calculateResults();
        }
    }//GEN-LAST:event_retransmitButtonActionPerformed

    private void batchRetransMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batchRetransMenuActionPerformed
        // TODO add your handling code here:
        if (Supervisor.crcErrorDetectedCounter > 0) {
            int[] results = msgGen.batchRetransmission();
            JOptionPane.showMessageDialog(this,
                    results[0] + " messages out of " + results[2] + " were delivered successfully.\n"
                    + results[1] + " messages out of " + results[2] + " were delivered with noise.\n",
                    "CRC : Batch Re-Transmission Report", JOptionPane.INFORMATION_MESSAGE);
            showChoicesActionPerformed(null);
            calculateResults();

            if (results[1] == 0) {
                batchRetransMenu.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "CRC : There is no noise in data.", "CRC : Re-Transmission Report", JOptionPane.ERROR_MESSAGE);
            batchRetransMenu.setEnabled(false);
        }

    }//GEN-LAST:event_batchRetransMenuActionPerformed

    private void manualButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualButtonActionPerformed
        // TODO add your handling code here:
        jMenuItem4ActionPerformed(null);
        manualButton.setSelected(true);
        CardLayout cl = (CardLayout) CardsPanel.getLayout();
        cl.show(CardsPanel, "card1");
        binField.setVisible(true);
        jLabel20.setVisible(true);
        
        binField.setText("");
        batchRetransMenu.setEnabled(false);
        resultsPanel.setVisible(false);
    }//GEN-LAST:event_manualButtonActionPerformed

    private void automaticButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticButtonActionPerformed
        // TODO add your handling code here:
        jMenuItem4ActionPerformed(null);
        automaticButton.setSelected(true);
        CardLayout cl = (CardLayout) CardsPanel.getLayout();
        cl.show(CardsPanel, "card2");
        binField.setVisible(false);
        jLabel20.setVisible(false);
        batchRetransMenu.setEnabled(false);
        resultsPanel.setVisible(true);
    }//GEN-LAST:event_automaticButtonActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        CustomizerRndSimulator d = new CustomizerRndSimulator(this, true);
        d.setVisible(true);
        if (GUI.Options.CUSTOMIZER_INDEX == 1) {
            jMenuItem8ActionPerformed(null);
        }

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            // TODO add your handling code here:
            screen2image.screenCapture(this.getBounds());
            ScreenshotDialog sd = new ScreenshotDialog(this,true);
            Timer timer = new Timer(2000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //dialog.dispose();
                    sd.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
            
            sd.setVisible(true);
            this.requestFocus();
            this.requestFocus();
            //*/

        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void updatePolynomial1() {
        String decodeMsg = polynomialField1.getText();
        Integer[] decodeMsgToInt = new Integer[decodeMsg.length()];
        boolean wrongInput = false;
        for (int i = 0; i < decodeMsg.length(); i++) {
            if (decodeMsg.charAt(i) != '1' && decodeMsg.charAt(i) != '0') {
                decodeMsgToInt = null;
                wrongInput = true;
                break;
            }
            decodeMsgToInt[i] = decodeMsg.charAt(i) - '0';
        }
        if (wrongInput) {
            polynomialField1.setBackground(new Color(255, 51, 51, 255));
            startButton1.setEnabled(false);
            DefaultListModel listModel = (DefaultListModel) jList1.getModel();
            listModel.removeAllElements();
            addDisPOL = true;
            bitsForListPOL = 0;
        } else {
            bitsForListPOL = decodeMsgToInt.length - 1;
            polynomialField1.setBackground(new Color(255, 255, 255, 255));
            if (!addDisMSG) {
                startButton1.setEnabled(true);
                if (jCheckBox1.isSelected()) {
                    showBitErrorListings();
                }
            }
            addDisPOL = false;

        }
        int counter = 1;
        String polynomial = "";
        if (decodeMsgToInt != null && decodeMsgToInt.length > 0) {
            if (decodeMsgToInt[decodeMsgToInt.length - 1] == 1) {
                polynomial = "1";
            }
            for (int i = decodeMsgToInt.length - 2; i >= 0; i--) {

                if (decodeMsgToInt[i] == 1) {
                    if (counter == 1) {
                        polynomial = "x + " + polynomial;
                    } else {
                        polynomial = "x<sup>" + counter + "</sup> + " + polynomial;
                    }
                }
                //<html>x<sup>2</sup>+ x + 1</html>
                counter++;
            }
            if (decodeMsgToInt[decodeMsgToInt.length - 1] == 0) {
                polynomial = polynomial.substring(0, polynomial.length() - 2);
            }
            polynomial = "<html>" + polynomial + "</html>";
            //polynomialField.setToolTipText(polynomial);
            polynomialDisplayer1.setContentType("text/html");
            polynomialDisplayer1.setText(polynomial);
            polynomialDisplayer1.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            polynomialDisplayer1.setFont(new Font(font.getName(), Font.PLAIN, 12));

        }
    }

    private void updateMessageManual() {
        String decodeMsg = msgsAmountField1.getText();
        Integer[] decodeMsgToInt = new Integer[decodeMsg.length()];
        boolean wrongInput = false;
        for (int i = 0; i < decodeMsg.length(); i++) {
            if (decodeMsg.charAt(i) != '1' && decodeMsg.charAt(i) != '0') {
                decodeMsgToInt = null;
                wrongInput = true;
                break;
            }
            decodeMsgToInt[i] = decodeMsg.charAt(i) - '0';
        }
        if (wrongInput) {
            bitsForListMSG = 0;
            msgsAmountField1.setBackground(new Color(255, 51, 51, 255));
            startButton1.setEnabled(false);
            addDisMSG = true;
            DefaultListModel listModel = (DefaultListModel) jList1.getModel();
            listModel.removeAllElements();
        } else {
            bitsForListMSG = decodeMsgToInt.length;
            msgsAmountField1.setBackground(new Color(255, 255, 255, 255));
            if (!addDisPOL) {
                startButton1.setEnabled(true);
                if (jCheckBox1.isSelected()) {
                    showBitErrorListings();
                }
            }
            addDisMSG = false;
        }
    }

    private void showBitErrorListings() {

        if (!addDisPOL && !addDisMSG) {
            DefaultListModel demoList = new DefaultListModel();
            for (int i = 0; i < bitsForListMSG + bitsForListPOL; i++) {
                demoList.addElement("Bit " + (i + 1));
            }
            jList1.setModel(demoList);
            startButton1.setEnabled(true);
        }
    }

    private void showInfo(Supervisor sv) {
        indexField.setText(sv.indexOfMessage + "");
        transmittedField.setText(Arrays.toString(sv.messageTransmitted));
        receivedField.setText(Arrays.toString(sv.messageReceived));
        noiseField.setText(sv.errorFromNoise + "");
        crcField.setText(sv.crcErrorDetected + "");
        bitErrorsField.setText(sv.bitError + "");
        drawCRC(sv.divToStringTrans, sv.divToStringRec);
        if (msgGen.mode.equals("auto") && sv.crcErrorDetected) {
            retransmitButton.setVisible(true);
        } else {
            retransmitButton.setVisible(false);
            binField.setText(Arrays.toString(sv.decoder));
        }
    }

    private void drawCRC(ArrayList<String> list, ArrayList<String> list2) {
        String finalS = "";
        for (String str : list) {
            finalS += str;
        }

        textArea1.setText(finalS);
        textArea1.setEditable(false);

        String finalS2 = "";
        for (String str : list2) {
            finalS2 += str;
        }

        textArea2.setText(finalS2);
        textArea2.setEditable(false);
    }

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
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            //IManager.put( "TabbedPane.tabAreaInsets", new javax.Swing.plaf.InsetsUIResource( 3 , 20 , 2 , 20 ));
            UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);

            /*
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
                }
                }
             */
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane CRCPanel;
    private javax.swing.JPanel CardsPanel;
    private javax.swing.JPanel Customize;
    private javax.swing.JPanel SettingsAndStart;
    private javax.swing.JTextField TotalCRCDetectedField;
    private javax.swing.JTextField TotalCRCNoDetectedField;
    private javax.swing.JTextField TotalNoiseField;
    private javax.swing.JRadioButtonMenuItem automaticButton;
    private javax.swing.JMenuItem batchRetransMenu;
    private javax.swing.JTextField berField;
    private javax.swing.JTextField binField;
    private javax.swing.JTextField bitErrorsField;
    private javax.swing.JPanel bitErrorsPanel;
    private javax.swing.JPanel bitErrorsPanel1;
    private javax.swing.JPanel bitErrorsPanel2;
    private javax.swing.JTextField bitsField;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextField crcField;
    private javax.swing.JTextField indexField;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JRadioButtonMenuItem manualButton;
    private javax.swing.JPanel messageList;
    private javax.swing.JList<String> msgList;
    private javax.swing.JTextField msgsAmountField;
    private javax.swing.JTextField msgsAmountField1;
    private javax.swing.JTextField noiseField;
    private javax.swing.JEditorPane polynomialDisplayer;
    private javax.swing.JEditorPane polynomialDisplayer1;
    private javax.swing.JTextField polynomialField;
    private javax.swing.JTextField polynomialField1;
    private javax.swing.JScrollPane recCRC;
    private javax.swing.JTextField receivedField;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JButton retransmitButton;
    private javax.swing.JComboBox<String> showChoices;
    private javax.swing.JLabel showLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton startButton1;
    private javax.swing.JTextArea textArea1;
    private javax.swing.JTextArea textArea2;
    private javax.swing.JScrollPane transCRC;
    private javax.swing.JTextField transmittedField;
    // End of variables declaration//GEN-END:variables
}
