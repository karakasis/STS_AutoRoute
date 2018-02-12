/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import com.sun.awt.AWTUtilities;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 *
 * @author Xrhstos
 */
public class GUI extends javax.swing.JFrame {

    private Map map;
    private ArrayList<String> mapList;
    private ArrayList<JLabel> icons;
    private JLabel[][] iconArray;
    private JLabel[][] stepArray;

    private ImageIcon imgTreasure;
    private ImageIcon imgEnemy;
    private ImageIcon imgUnknown;
    private ImageIcon imgElite;
    private ImageIcon imgMerchant;
    private ImageIcon imgRest;

    private ImageIcon imgTreasureM;
    private ImageIcon imgEnemyM;
    private ImageIcon imgUnknownM;
    private ImageIcon imgEliteM;
    private ImageIcon imgMerchantM;
    private ImageIcon imgRestM;

    private ImageIcon imgTreasureC;
    private ImageIcon imgEnemyC;
    private ImageIcon imgUnknownC;
    private ImageIcon imgEliteC;
    private ImageIcon imgMerchantC;
    private ImageIcon imgRestC;

    private ImageIcon imgStepD0;
    private ImageIcon imgStepD1;
    private ImageIcon imgStepD2;
    private ImageIcon imgStepL0;
    private ImageIcon imgStepL1;
    private ImageIcon imgStepR0;
    private ImageIcon imgStepR1;

    private ImageIcon imgStepD0M;
    private ImageIcon imgStepD1M;
    private ImageIcon imgStepD2M;
    private ImageIcon imgStepL0M;
    private ImageIcon imgStepL1M;
    private ImageIcon imgStepR0M;
    private ImageIcon imgStepR1M;

    private ImageIcon brute;
    private ImageIcon weight;

    private Graphics2D cursor;
    private BufferedImage cursorInput;
    private ImageIcon curL;
    private JLabel curLabel;

    private Loading load;
    private Tail tailer;
    private ArrayList<String> log;
    private Point origin;

    private boolean actionEventActive = false;
    private int activeRoute = -1;
    private int availableRoutes = -1;

    //Legend
    private JComboBox[] comboBoxInfo = new JComboBox[6];
    private HashMap<Integer, String> comboKeyMap = new HashMap<>();
    private boolean skipEliteBool = false;
    private String prevCard = "";
    private String currentCard = "";

    private int algorithm = 0;

    private JButton[] routesChangeArray;

    private void setupLAF() {
        UIManager.put("RootPane.setupButtonVisible", false);
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

        } catch (Exception ex) {
            Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //IManager.put( "TabbedPane.tabAreaInsets", new javax.Swing.plaf.InsetsUIResource( 3 , 20 , 2 , 20 ));
        UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
        //uncomment to transparent cursor and glass pane
        /*
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                image, new Point(0, 0), "invisibleCursor");
        getContentPane().setCursor(transparentCursor);
        getGlassPane().setCursor(transparentCursor);
         */

    }

    public void setupLayout() {
        boolean mappedInput = false;
        boolean mapFound = false;
        mapList = new ArrayList<>();
        for (String str : log) {
            if (str.contains("Game Seed:")) {
                break;
            }
            if (mapFound && !mappedInput) {
                mapList.add(str);
            }

            if (str.contains("Generated the following dungeon map:")) {
                mapFound = true;
            }
        }
        mapList.remove(0);
        this.map = new Map(mapList);
        initComponents();
        comp();

        jScrollPane1.setVisible(true);
        //jPanel1.requestFocusInWindow();
        jPanel1.scrollRectToVisible(
                new Rectangle(0, jPanel1.getHeight() - 101, 1, 1));
        comboBoxInfo[0] = restCombo;
        comboBoxInfo[1] = eliteCombo;
        comboBoxInfo[2] = merchantCombo;
        comboBoxInfo[3] = unknownCombo;
        comboBoxInfo[4] = enemyCombo;
        comboBoxInfo[5] = null;

        comboKeyMap.put(0, "rest");
        comboKeyMap.put(1, "elite");
        comboKeyMap.put(2, "merchant");
        comboKeyMap.put(3, "unknown");
        comboKeyMap.put(4, "enemy");
        comboKeyMap.put(5, "treasure");

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c2");
        currentCard = "c2";
        prevCard = "";
        optionButton.setVisible(false);
        backButton.setVisible(false);
        jScrollPane1.getViewport().setOpaque(false);
        availableRoutesIconLabel.setVisible(false);
        leftLabel.setVisible(false);
        rightLabel.setVisible(false);
        trackLabel.setVisible(false);
        AWTUtilities.setWindowOpaque(this, false);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        //uncomment for glass pane work
        /*
        JPanel gp = (JPanel) this.getGlassPane();
        this.getGlassPane().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                customCursorMove(evt);
            }
        });
        curLabel = new JLabel();
        curLabel.setIcon(curL);
        gp.add(curLabel);
         */
        cameraButton.setVisible(false);
        logo.setVisible(false);
        routeIDLabel.setVisible(false);
        this.setVisible(true);
        //gp.setVisible(true); <- same here

    }

    public ArrayList<String> getCurrentLoadedMap() {
        return mapList;
    }

    private void customCursorMove(java.awt.event.MouseEvent evt) {
        //System.out.println(evt.getX() + " " + evt.getY());
        cursor.drawImage(cursorInput, evt.getX(), evt.getY(), null);
        cursor.dispose();
        curLabel.setLocation(evt.getPoint());
        //redispatchMouseEvent(evt,false);
    }

    /**
     * Creates new form GUI
     *
     * @param firstRun
     * @param log
     * @param tailer
     */
    public GUI(ArrayList<String> log) {
        setupLAF();
        this.log = log;

        imgTreasure = null;
        imgEnemy = null;
        imgUnknown = null;
        imgElite = null;
        imgMerchant = null;
        imgRest = null;
        icons = new ArrayList<>();
        stepArray = new JLabel[19][57];
        iconArray = new JLabel[7][57];

        imgTreasureM = new javax.swing.ImageIcon(this.getClass().getResource("/map/chestM.png"));
        imgEnemyM = new javax.swing.ImageIcon(this.getClass().getResource("/map/enemyM.png"));
        imgUnknownM = new javax.swing.ImageIcon(this.getClass().getResource("/map/unknownM.png"));
        imgEliteM = new javax.swing.ImageIcon(this.getClass().getResource("/map/eliteM.png"));
        imgMerchantM = new javax.swing.ImageIcon(this.getClass().getResource("/map/merchantM.png"));
        imgRestM = new javax.swing.ImageIcon(this.getClass().getResource("/map/restM.png"));

        imgTreasureC = new javax.swing.ImageIcon(this.getClass().getResource("/map/chestC.png"));
        imgEnemyC = new javax.swing.ImageIcon(this.getClass().getResource("/map/enemyC.png"));
        imgUnknownC = new javax.swing.ImageIcon(this.getClass().getResource("/map/unknownC.png"));
        imgEliteC = new javax.swing.ImageIcon(this.getClass().getResource("/map/eliteC.png"));
        imgMerchantC = new javax.swing.ImageIcon(this.getClass().getResource("/map/merchantC.png"));
        imgRestC = new javax.swing.ImageIcon(this.getClass().getResource("/map/restC.png"));

        imgTreasure = new javax.swing.ImageIcon(this.getClass().getResource("/map/chest.png"));
        imgEnemy = new javax.swing.ImageIcon(this.getClass().getResource("/map/enemy.png"));
        imgUnknown = new javax.swing.ImageIcon(this.getClass().getResource("/map/unknown.png"));
        imgElite = new javax.swing.ImageIcon(this.getClass().getResource("/map/elite.png"));
        imgMerchant = new javax.swing.ImageIcon(this.getClass().getResource("/map/merchant.png"));
        imgRest = new javax.swing.ImageIcon(this.getClass().getResource("/map/rest.png"));

        imgStepD0 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step0.png"));
        imgStepD1 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step1.png"));
        imgStepD2 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step2.png"));
        imgStepL0 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepLeft/step0.png"));
        imgStepL1 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepLeft/step1.png"));
        imgStepR0 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepRight/step0.png"));
        imgStepR1 = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepRight/step1.png"));

        imgStepD0M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step0M.png"));
        imgStepD1M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step1M.png"));
        imgStepD2M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepDown/step2M.png"));
        imgStepL0M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepLeft/step0M.png"));
        imgStepL1M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepLeft/step1M.png"));
        imgStepR0M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepRight/step0M.png"));
        imgStepR1M = new javax.swing.ImageIcon(this.getClass().getResource("/map/stepRight/step1M.png"));
        brute = new javax.swing.ImageIcon(this.getClass().getResource("/map/bruteforce.png"));
        weight = new javax.swing.ImageIcon(this.getClass().getResource("/map/weight.png"));
        /*
        cursorInput = null;
        try {
            cursorInput = ImageIO.read(GUI.class.getResourceAsStream("/map/darkRed.png"));
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        curL = new javax.swing.ImageIcon(cursorInput);
        BufferedImage newImage = new BufferedImage(
                cursorInput.getWidth(), cursorInput.getHeight(), BufferedImage.TYPE_INT_ARGB);

        cursor = newImage.createGraphics();


// elsewhere
        //setupLayout();
        */
    }

    private void clickLabel(int i, int j) {
        if (isInRoute(i, j)) {
            if (isNotSelected(i, j)) {
                //check if previous is selected
                if (isSelected(roomForMapSelection(i, j), "child")) {
                    String type = roomForMapSelection(i, j).getType();
                    if (type != null) {
                        iconArray[i][j]
                                .setIcon(iconC(type)); // change icon to heavy black
                    }
                    finallyChangeIt(i, j);
                }
            } else {
                //check if next is selected
                if (isSelected(roomForMapSelection(i, j), "parent")) {
                    String type = roomForMapSelection(i, j).getType();
                    if (type != null) {
                        iconArray[i][j]
                                .setIcon(iconM(type)); // change icon to heavy black
                    }
                    finallyChangeIt(i, j);
                }
            }
        }

    }

    private ArrayList<Integer[]> currentRouteRoomsInXYMap;
    private ArrayList<Integer[]> roomSelectionInXYMap;

    private boolean isInRoute(int x, int y) {
        if (activeRoute != -1) {
            for (Integer[] i : currentRouteRoomsInXYMap) {
                if (i[0] == x && i[1] == y) {
                    return true;
                }
            }
        }
        //System.out.println("NOT IN ROUTE");
        return false;
    }

    private boolean isSelected(Room room, String family) {
        if (family.equals("child")) {
            //System.out.println("Checking if : SELECTION");
            if (room.isBottom()) {
                //System.out.println("IS BOTTOM FLOOR");
                return true;
            } else {
                for (Room children : room.getNodes()) {
                    for (Integer[] i : roomSelectionInXYMap) {
                        if (i[0] == children.getXYInMap()[0] && i[1] == children.getXYInMap()[1]) {
                            // check if any of children are selected if yes return  true
                            //System.out.println("IS CONNECTED TO 1 CHILD");
                            return true;
                        }
                    }

                }
            }
        } else if (family.equals("parent")) {
            //System.out.println("Checking if : REMOVAL");
            if (room.getFloor() == 14) { // always top
                //System.out.println("IS TOP FLOOR");
                return true;
            } else {
                for (Room parent : room.getParents()) {
                    for (Integer[] i : roomSelectionInXYMap) {
                        if (i[0] == parent.getXYInMap()[0] && i[1] == parent.getXYInMap()[1]) {
                            // check if any of parents are selected if yes return false cause its locked there
                            //System.out.println("IS CONNECTED TO 1 PARENT : CANT REMOVE");
                            return false;
                        }
                    }

                }

                //System.out.println("HAS NO CONNECTION TO PARENT : REMOVE");
                //if not return true
                return true;
            }
        }
        //if anything goes wrong return false
        return false;
    }

    private boolean isNotSelected(int x, int y) {
        for (Integer[] i : roomSelectionInXYMap) {
            if (i[0] == x && i[1] == y) {
                return false;
            }
        }
        return true;
    }

    private void finallyChangeIt(int x, int y) {
        for (Integer[] i : roomSelectionInXYMap) {
            if (i[0] == x && i[1] == y) {
                roomSelectionInXYMap.remove(i);
                if (roomSelectionInXYMap.isEmpty()) {
                    routeFromCurButton.setEnabled(false);
                    routeFromCurButton.setToolTipText("Select your path in the map.");
                }
                return;
            }
        }
        roomSelectionInXYMap.add(new Integer[]{x, y});
        routeFromCurButton.setEnabled(true);
        routeFromCurButton.setToolTipText(null);
    }

    private Room roomForMapSelection(int x, int y) {
        if (activeRoute != -1) {

            for (Room room : map.getBestRoutes().get(activeRoute).getRoute()) {
                if (room.getXYInMap()[0] == x && room.getXYInMap()[1] == y) {
                    return room;
                }
            }
        }
        return null;
    }

    private void comp() {

        for (int j = 0; j < 57; j++) {
            for (int i = 0; i < 7; i++) {
                iconArray[i][j] = new JLabel();
                iconPanel.add(iconArray[i][j]);
                if (!skip(j)) {
                    String type = map.drawMapNodesGUI(i, j);
                    if (type != null) {
                        iconArray[i][j].setIcon((icon(type)));
                        iconArray[i][j].setHorizontalAlignment(randomiseHPos());
                        iconArray[i][j].setVerticalAlignment(randomiseVPos());

                    }
                }
            }
        }

        for (int k = 0; k < 57; k++) {
            for (int l = 0; l < 19; l++) {
                stepArray[l][k] = new JLabel();
                stepPanel.add(stepArray[l][k]);
            }
        }

        for (Integer[] steps : map.requestSteps()) {
            if (steps[0] == 0) {
                stepArray[steps[1]][steps[2]].setIcon((imgStepL0));
                stepArray[steps[3]][steps[4]].setIcon((imgStepL1));
                stepArray[steps[5]][steps[6]].setIcon((imgStepL0));
            } else if (steps[0] == 1) {
                stepArray[steps[1]][steps[2]].setIcon((imgStepD0));
                stepArray[steps[3]][steps[4]].setIcon((imgStepD1));
                stepArray[steps[5]][steps[6]].setIcon((imgStepD2));
            } else if (steps[0] == 2) {
                stepArray[steps[1]][steps[2]].setIcon((imgStepR0));
                stepArray[steps[3]][steps[4]].setIcon((imgStepR1));
                stepArray[steps[5]][steps[6]].setIcon((imgStepR0));
            }
        }
        pack();
    }

    private void showRoute(int routeID) {
        //The routes have been reversed for ?command line reasons? so i ll just re-reverse twice
        //Collections.reverse(map.getBestRoutes().get(routeID).getRoute()); fixed
        ArrayList<Character> orders = map.getBestRoutes().get(routeID).getOrders();
        int routeInternalID = map.getBestRoutes().get(routeID).getID();
        int i = 0;
        //routeIDLabel.setText(routeInternalID + "");
        showInformation();

        currentRouteRoomsInXYMap = new ArrayList<>();
        for (Room room : map.getBestRoutes().get(routeID).getRoute()) {
            iconArray[(room.getX() - 7) / 3][room.getY() * 2].setIcon(iconM(room.getType())); // change icon to heavy black
            currentRouteRoomsInXYMap.add(new Integer[]{(room.getX() - 7) / 3, room.getY() * 2});
            room.setXYInMap(new int[]{(room.getX() - 7) / 3, room.getY() * 2});
            if (i != orders.size()) // last node = leaf has no order to follow
            {
                if (orders.get(i) == '|') {
                    Integer[] steps = map.drawMapStepsGUI('|', room.getX(), room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepD0M));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepD1M));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepD2M));
                } else if (orders.get(i) == '/') {
                    Integer[] steps = map.drawMapStepsGUI('/', room.getX() - 2, room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepL0M));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepL1M));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepL0M));
                } else if (orders.get(i) == '\\') {
                    Integer[] steps = map.drawMapStepsGUI('\\', room.getX() + 2, room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepR0M));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepR1M));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepR0M));
                }
            }
            i++;
        }
        //revert back for ?whatever reasons?
        //Collections.reverse(map.getBestRoutes().get(routeID).getRoute());
        if (trackFromCurrentRoom) {
            for (Integer[] rooms : roomSelectionInXYMap) {
                String type = roomForMapSelection(rooms[0], rooms[1]).getType();
                if (type != null) {
                    iconArray[rooms[0]][rooms[1]]
                            .setIcon(iconC(type)); // change icon to heavy black
                }
            }
        } else {
            roomSelectionInXYMap = new ArrayList<>();
        }

        pack();
    }

    private void removeRoute(int routeID) {
        //The routes have been reversed for ?command line reasons? so i ll just re-reverse twice
        //Collections.reverse(map.getBestRoutes().get(routeID).getRoute()); fixed
        ArrayList<Character> orders = map.getBestRoutes().get(routeID).getOrders();

        int i = 0;
        for (Room room : map.getBestRoutes().get(routeID).getRoute()) {
            iconArray[(room.getX() - 7) / 3][room.getY() * 2].setIcon(icon(room.getType())); // change icon to heavy black
            if (i != orders.size()) // last node = leaf has no order to follow
            {
                if (orders.get(i) == '|') {
                    Integer[] steps = map.drawMapStepsGUI('|', room.getX(), room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepD0));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepD1));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepD2));
                } else if (orders.get(i) == '/') {
                    Integer[] steps = map.drawMapStepsGUI('/', room.getX() - 2, room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepL0));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepL1));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepL0));
                } else if (orders.get(i) == '\\') {
                    Integer[] steps = map.drawMapStepsGUI('\\', room.getX() + 2, room.getY() + 1);
                    stepArray[steps[1]][steps[2]].setIcon((imgStepR0));
                    stepArray[steps[3]][steps[4]].setIcon((imgStepR1));
                    stepArray[steps[5]][steps[6]].setIcon((imgStepR0));
                }
            }
            i++;
        }
        //revert back for ?whatever reasons?
        //Collections.reverse(map.getBestRoutes().get(routeID).getRoute());
        pack();
    }

    private boolean skip(int x) {
        for (int i = 0; i < skip.length; i++) {
            if (x == skip[i]) {
                return true;
            }
        }
        return false;
    }

    private int randomiseHPos() {
        Random random = new Random();
        switch (random.nextInt(5)) {
            case 0:
                return SwingConstants.RIGHT;
            case 1:
                return SwingConstants.LEFT;
            case 2:
                return SwingConstants.LEADING;
            case 3:
                return SwingConstants.TRAILING;
            case 4:
                return SwingConstants.CENTER;
            default:
                break;
        }
        return SwingConstants.CENTER;
    }

    private int randomiseVPos() {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                return SwingConstants.BOTTOM;
            case 1:
                return SwingConstants.TOP;
            case 2:
                return SwingConstants.CENTER;
        }
        return SwingConstants.CENTER;
    }

    private ImageIcon icon(String type) {
        switch (type) {
            case "Enemy":
                return imgEnemy;
            case "Unknown":
                return imgUnknown;
            case "Rest":
                return imgRest;
            case "Elite":
                return imgElite;
            case "Merchant":
                return imgMerchant;
            case "Treasure":
                return imgTreasure;
            default:
                break;
        }
        return null;
    }

    private ImageIcon iconM(String type) {
        switch (type) {
            case "Enemy":
                return imgEnemyM;
            case "Unknown":
                return imgUnknownM;
            case "Rest":
                return imgRestM;
            case "Elite":
                return imgEliteM;
            case "Merchant":
                return imgMerchantM;
            case "Treasure":
                return imgTreasureM;
            default:
                break;
        }
        return null;
    }

    private ImageIcon iconC(String type) {
        switch (type) {
            case "Enemy":
                return imgEnemyC;
            case "Unknown":
                return imgUnknownC;
            case "Rest":
                return imgRestC;
            case "Elite":
                return imgEliteC;
            case "Merchant":
                return imgMerchantC;
            case "Treasure":
                return imgTreasureC;
            default:
                break;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        availableRoutesIconLabel = new javax.swing.JLabel();
        cameraButton = new javax.swing.JButton();
        leftLabel = new javax.swing.JButton();
        rightLabel = new javax.swing.JButton();
        trackLabel = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        titlePanel = new javax.swing.JPanel();
        steam = new javax.swing.JButton();
        routeIDLabel = new javax.swing.JLabel();
        routeIDContainerLabel = new javax.swing.JLabel();
        routeAlgoLabel = new javax.swing.JLabel();
        close = new javax.swing.JButton();
        minimize = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        legend = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        optionButton = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        informationPanel = new javax.swing.JPanel();
        routeInfoLabel = new javax.swing.JLabel();
        enemyIcon1 = new javax.swing.JLabel();
        merchantIcon1 = new javax.swing.JLabel();
        eliteIcon1 = new javax.swing.JLabel();
        restIcon1 = new javax.swing.JLabel();
        unknownIcon1 = new javax.swing.JLabel();
        treasureIcon1 = new javax.swing.JLabel();
        restInfo = new javax.swing.JLabel();
        eliteInfo = new javax.swing.JLabel();
        unknownInfo = new javax.swing.JLabel();
        merchantInfo = new javax.swing.JLabel();
        treasureInfo = new javax.swing.JLabel();
        enemyInfo = new javax.swing.JLabel();
        setPrioritiesPanel = new javax.swing.JPanel();
        titlePrio = new javax.swing.JLabel();
        enemyIcon = new javax.swing.JLabel();
        merchantIcon = new javax.swing.JLabel();
        eliteIcon = new javax.swing.JLabel();
        restIcon = new javax.swing.JLabel();
        treasureIcon = new javax.swing.JLabel();
        unknownIcon = new javax.swing.JLabel();
        eliteCombo = new javax.swing.JComboBox<>();
        restCombo = new javax.swing.JComboBox<>();
        merchantCombo = new javax.swing.JComboBox<>();
        unknownCombo = new javax.swing.JComboBox<>();
        enemyCombo = new javax.swing.JComboBox<>();
        skipElite = new javax.swing.JCheckBox();
        startRouting = new javax.swing.JButton();
        optionsPanel = new javax.swing.JPanel();
        resetPrioButton = new javax.swing.JButton();
        algorithmButton = new javax.swing.JButton();
        routeFromCurButton = new javax.swing.JButton();
        changeRouteButton = new javax.swing.JButton();
        changeRoutePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        mapDraggerPanel = new javax.swing.JPanel();
        iconPanel = new javax.swing.JPanel();
        stepPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Slay the Spire - Auto Route");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(925, 745));
        setMinimumSize(new java.awt.Dimension(925, 745));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(935, 745));
        setResizable(false);
        setSize(new java.awt.Dimension(925, 745));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        availableRoutesIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/route.png"))); // NOI18N
        availableRoutesIconLabel.setToolTipText("Multiple routes found!");
        getContentPane().add(availableRoutesIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 135, 30, 30));

        cameraButton.setToolTipText("Take a screenshot");
        cameraButton.setBorderPainted(false);
        cameraButton.setContentAreaFilled(false);
        cameraButton.setEnabled(false);
        cameraButton.setFocusPainted(false);
        cameraButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cameraButtonActionPerformed(evt);
            }
        });
        getContentPane().add(cameraButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 210, 30, 30));

        leftLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/leftSpacer.png"))); // NOI18N
        leftLabel.setBorderPainted(false);
        leftLabel.setContentAreaFilled(false);
        leftLabel.setFocusPainted(false);
        leftLabel.setPreferredSize(new java.awt.Dimension(30, 30));
        leftLabel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/leftSpacerH.png"))); // NOI18N
        leftLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftLabelActionPerformed(evt);
            }
        });
        getContentPane().add(leftLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        rightLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/rightSpacer.png"))); // NOI18N
        rightLabel.setBorderPainted(false);
        rightLabel.setContentAreaFilled(false);
        rightLabel.setFocusPainted(false);
        rightLabel.setPreferredSize(new java.awt.Dimension(30, 30));
        rightLabel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/rightSpacerH.png"))); // NOI18N
        rightLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightLabelActionPerformed(evt);
            }
        });
        getContentPane().add(rightLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 170, -1, -1));

        trackLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/currentActive1.png"))); // NOI18N
        trackLabel.setToolTipText("Route from current room is active");
        getContentPane().add(trackLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 90, 30, 30));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setEnabled(false);
        getContentPane().add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(696, 60, -1, -1));

        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new java.awt.Dimension(850, 95));
        titlePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        steam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/Steam2.png"))); // NOI18N
        steam.setToolTipText("Launches Slay the Spire from Steam.");
        steam.setBorderPainted(false);
        steam.setContentAreaFilled(false);
        steam.setFocusPainted(false);
        steam.setPreferredSize(new java.awt.Dimension(30, 30));
        steam.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/Steam2L.png"))); // NOI18N
        steam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                steamActionPerformed(evt);
            }
        });
        titlePanel.add(steam, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 35, -1, -1));

        routeIDLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        routeIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        routeIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        routeIDLabel.setPreferredSize(new java.awt.Dimension(250, 30));
        titlePanel.add(routeIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 32, 35, 35));

        routeIDContainerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/id.png"))); // NOI18N
        titlePanel.add(routeIDContainerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 25, 50, 50));

        routeAlgoLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        routeAlgoLabel.setForeground(new java.awt.Color(255, 255, 255));
        routeAlgoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/bruteforce.png"))); // NOI18N
        routeAlgoLabel.setToolTipText("Click here to change algorithm.");
        routeAlgoLabel.setPreferredSize(new java.awt.Dimension(250, 30));
        routeAlgoLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                routeAlgoLabelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                routeAlgoLabelMouseMoved(evt);
            }
        });
        routeAlgoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                routeAlgoLabelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                routeAlgoLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                routeAlgoLabelMouseReleased(evt);
            }
        });
        titlePanel.add(routeAlgoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 270, 60));

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
        titlePanel.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, -1, -1));

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
        titlePanel.add(minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, -1, -1));

        title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/top.png"))); // NOI18N
        title.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                titleMouseMoved(evt);
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
        titlePanel.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(titlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        legend.setMaximumSize(new java.awt.Dimension(286, 350));
        legend.setMinimumSize(new java.awt.Dimension(286, 350));
        legend.setOpaque(false);
        legend.setPreferredSize(new java.awt.Dimension(286, 350));
        legend.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/return1.png"))); // NOI18N
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setFocusable(false);
        backButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/return1H.png"))); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        legend.add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 30, 30));

        optionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/option1.png"))); // NOI18N
        optionButton.setBorderPainted(false);
        optionButton.setContentAreaFilled(false);
        optionButton.setFocusPainted(false);
        optionButton.setFocusable(false);
        optionButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/option1H.png"))); // NOI18N
        optionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionButtonActionPerformed(evt);
            }
        });
        legend.add(optionButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 30, 30));

        cardPanel.setOpaque(false);
        cardPanel.setLayout(new java.awt.CardLayout());

        informationPanel.setOpaque(false);
        informationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        routeInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        routeInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/route info.png"))); // NOI18N
        informationPanel.add(routeInfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 140, -1));

        enemyIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/enemy2.png"))); // NOI18N
        informationPanel.add(enemyIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 191, -1, 34));

        merchantIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/merchant2.png"))); // NOI18N
        informationPanel.add(merchantIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 111, -1, 34));

        eliteIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/elite2.png"))); // NOI18N
        informationPanel.add(eliteIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 71, -1, 34));

        restIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/rest2.png"))); // NOI18N
        informationPanel.add(restIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 31, -1, 34));

        unknownIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/unknown2.png"))); // NOI18N
        informationPanel.add(unknownIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 151, -1, 34));

        treasureIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/chest2.png"))); // NOI18N
        informationPanel.add(treasureIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 231, -1, 34));

        restInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        restInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        restInfo.setText("4");
        restInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(restInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 31, -1, -1));

        eliteInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        eliteInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eliteInfo.setText("4");
        eliteInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(eliteInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

        unknownInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unknownInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unknownInfo.setText("4");
        unknownInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(unknownInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 151, -1, -1));

        merchantInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        merchantInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        merchantInfo.setText("4");
        merchantInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(merchantInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 111, -1, -1));

        treasureInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        treasureInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        treasureInfo.setText("4");
        treasureInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(treasureInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 231, -1, -1));

        enemyInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        enemyInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        enemyInfo.setText("4");
        enemyInfo.setPreferredSize(new java.awt.Dimension(84, 34));
        informationPanel.add(enemyInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 191, -1, -1));

        cardPanel.add(informationPanel, "c1");

        setPrioritiesPanel.setOpaque(false);
        setPrioritiesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titlePrio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titlePrio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/setPrio.png"))); // NOI18N
        setPrioritiesPanel.add(titlePrio, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 5, 120, 20));

        enemyIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/enemy2.png"))); // NOI18N
        setPrioritiesPanel.add(enemyIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 188, -1, 34));

        merchantIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/merchant2.png"))); // NOI18N
        setPrioritiesPanel.add(merchantIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 108, -1, 34));

        eliteIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/elite2.png"))); // NOI18N
        setPrioritiesPanel.add(eliteIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 68, -1, 34));

        restIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/rest2.png"))); // NOI18N
        setPrioritiesPanel.add(restIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 31, -1, 34));

        treasureIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/chest2.png"))); // NOI18N
        setPrioritiesPanel.add(treasureIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 228, -1, 34));

        unknownIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/unknown2.png"))); // NOI18N
        setPrioritiesPanel.add(unknownIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 148, -1, 34));

        eliteCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        eliteCombo.setSelectedIndex(1);
        eliteCombo.setFocusable(false);
        eliteCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActions(evt);
            }
        });
        setPrioritiesPanel.add(eliteCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 68, 58, 34));

        restCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        restCombo.setBorder(null);
        restCombo.setFocusable(false);
        restCombo.setOpaque(false);
        restCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActions(evt);
            }
        });
        setPrioritiesPanel.add(restCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 31, 58, 34));

        merchantCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        merchantCombo.setSelectedIndex(2);
        merchantCombo.setFocusable(false);
        merchantCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActions(evt);
            }
        });
        setPrioritiesPanel.add(merchantCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 108, 58, 34));

        unknownCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        unknownCombo.setSelectedIndex(3);
        unknownCombo.setFocusable(false);
        unknownCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActions(evt);
            }
        });
        setPrioritiesPanel.add(unknownCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 148, 58, 34));

        enemyCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        enemyCombo.setSelectedIndex(4);
        enemyCombo.setFocusable(false);
        enemyCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActions(evt);
            }
        });
        setPrioritiesPanel.add(enemyCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 188, 58, 34));

        skipElite.setToolTipText("Skip Elite if possible");
        skipElite.setOpaque(false);
        skipElite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipEliteActionPerformed(evt);
            }
        });
        setPrioritiesPanel.add(skipElite, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 68, -1, 34));

        startRouting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/find.png"))); // NOI18N
        startRouting.setBorderPainted(false);
        startRouting.setContentAreaFilled(false);
        startRouting.setFocusPainted(false);
        startRouting.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/findH.png"))); // NOI18N
        startRouting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startRoutingActionPerformed(evt);
            }
        });
        setPrioritiesPanel.add(startRouting, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 224, 110, 33));

        cardPanel.add(setPrioritiesPanel, "c2");
        setPrioritiesPanel.getAccessibleContext().setAccessibleName("");

        optionsPanel.setOpaque(false);

        resetPrioButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button3.png"))); // NOI18N
        resetPrioButton.setBorderPainted(false);
        resetPrioButton.setContentAreaFilled(false);
        resetPrioButton.setFocusPainted(false);
        resetPrioButton.setPreferredSize(new java.awt.Dimension(150, 40));
        resetPrioButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button3H.png"))); // NOI18N
        resetPrioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPrioButtonActionPerformed(evt);
            }
        });
        optionsPanel.add(resetPrioButton);

        algorithmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button4.png"))); // NOI18N
        algorithmButton.setBorderPainted(false);
        algorithmButton.setContentAreaFilled(false);
        algorithmButton.setFocusPainted(false);
        algorithmButton.setPreferredSize(new java.awt.Dimension(150, 40));
        algorithmButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button4H.png"))); // NOI18N
        algorithmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithmButtonActionPerformed(evt);
            }
        });
        optionsPanel.add(algorithmButton);

        routeFromCurButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button5.png"))); // NOI18N
        routeFromCurButton.setToolTipText("Select your path in the map.");
        routeFromCurButton.setBorderPainted(false);
        routeFromCurButton.setContentAreaFilled(false);
        routeFromCurButton.setEnabled(false);
        routeFromCurButton.setFocusPainted(false);
        routeFromCurButton.setPreferredSize(new java.awt.Dimension(150, 40));
        routeFromCurButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button5H.png"))); // NOI18N
        routeFromCurButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeFromCurButtonActionPerformed(evt);
            }
        });
        optionsPanel.add(routeFromCurButton);

        changeRouteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button2.png"))); // NOI18N
        changeRouteButton.setBorderPainted(false);
        changeRouteButton.setContentAreaFilled(false);
        changeRouteButton.setFocusPainted(false);
        changeRouteButton.setPreferredSize(new java.awt.Dimension(150, 40));
        changeRouteButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/button2H.png"))); // NOI18N
        changeRouteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeRouteButtonActionPerformed(evt);
            }
        });
        optionsPanel.add(changeRouteButton);

        cardPanel.add(optionsPanel, "c3");

        changeRoutePanel.setOpaque(false);
        cardPanel.add(changeRoutePanel, "c4");

        legend.add(cardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, 260));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/legend2.png"))); // NOI18N
        legend.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(legend, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 200, -1, -1));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(850, 700));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(850, 700));
        jScrollPane1.setOpaque(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(850, 657));
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseMoved(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);
        jPanel1.setMaximumSize(new java.awt.Dimension(690, 1750));
        jPanel1.setMinimumSize(new java.awt.Dimension(690, 1750));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(690, 1750));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mapDraggerPanel.setMaximumSize(new java.awt.Dimension(690, 1750));
        mapDraggerPanel.setMinimumSize(new java.awt.Dimension(690, 1750));
        mapDraggerPanel.setOpaque(false);
        mapDraggerPanel.setPreferredSize(new java.awt.Dimension(690, 1750));
        mapDraggerPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mapDraggerPanelMouseDragged(evt);
            }
        });
        mapDraggerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mapDraggerPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mapDraggerPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout mapDraggerPanelLayout = new javax.swing.GroupLayout(mapDraggerPanel);
        mapDraggerPanel.setLayout(mapDraggerPanelLayout);
        mapDraggerPanelLayout.setHorizontalGroup(
            mapDraggerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        mapDraggerPanelLayout.setVerticalGroup(
            mapDraggerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1750, Short.MAX_VALUE)
        );

        jPanel1.add(mapDraggerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 690, 1550));

        iconPanel.setMaximumSize(new java.awt.Dimension(500, 1400));
        iconPanel.setMinimumSize(new java.awt.Dimension(500, 1400));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new java.awt.Dimension(500, 1400));
        iconPanel.setLayout(new java.awt.GridLayout(57, 7, 46, 0));
        jPanel1.add(iconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        stepPanel.setMaximumSize(new java.awt.Dimension(500, 1400));
        stepPanel.setMinimumSize(new java.awt.Dimension(500, 1400));
        stepPanel.setOpaque(false);
        stepPanel.setPreferredSize(new java.awt.Dimension(500, 1400));
        stepPanel.setLayout(new java.awt.GridLayout(57, 19));
        jPanel1.add(stepPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/map2.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 55, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/back5.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(850, 1550));
        jLabel3.setMinimumSize(new java.awt.Dimension(870, 745));
        jLabel3.setPreferredSize(new java.awt.Dimension(870, 745));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboActions(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboActions
        // TODO add your handling code here:
        if (!actionEventActive) {
            actionEventActive = true;

            JComboBox cb = (JComboBox) evt.getSource(); //find which box clicked
            int previousIndex = -1;
            for (int i = 0; i < 6; i++) {
                if (cb.equals(comboBoxInfo[i])) {
                    previousIndex = i; // find which index the combo had b4 click
                    break;
                }
            }
            int newIndex = cb.getSelectedIndex(); //find which index the combo choose
            if (newIndex == 4 && skipEliteBool) {
                cb.setSelectedIndex(previousIndex);
            } else {
                JComboBox cbOld = comboBoxInfo[newIndex]; //find which box was there b4
                //swapping
                //the old box needs to get the index that the box had b4 click
                comboBoxInfo[newIndex].setSelectedIndex(previousIndex);
                //now both boxes have swapped indexes.
                //now we need to swap graphics (if possible)

                //now we update the combo box info for new values
                //in the old index put the cbOld
                comboBoxInfo[previousIndex] = cbOld;
                comboBoxInfo[newIndex] = cb;
                //update map of keys
                String comboMapSwapper = comboKeyMap.get(newIndex);
                comboKeyMap.put(newIndex, comboKeyMap.get(previousIndex));
                comboKeyMap.put(previousIndex, comboMapSwapper);
            }
            actionEventActive = false;
        }

    }//GEN-LAST:event_comboActions

    private void skipEliteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipEliteActionPerformed
        // TODO add your handling code here:
        JCheckBox cb = (JCheckBox) evt.getSource();
        skipEliteBool = cb.isSelected();
        if (skipEliteBool) {
            eliteCombo.setEnabled(false);
            skipEliteBool = false;
            eliteCombo.setSelectedItem("5");
            skipEliteBool = true;
        } else {
            skipEliteBool = true;
            eliteCombo.setEnabled(true);
            skipEliteBool = false;
        }
    }//GEN-LAST:event_skipEliteActionPerformed

    private void startRoutingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startRoutingActionPerformed
        // TODO add your handling code here:
        String[] comboDecoder = new String[]{comboKeyMap.get(0), comboKeyMap.get(1), comboKeyMap.get(2),
            comboKeyMap.get(3), comboKeyMap.get(4), comboKeyMap.get(5),};
        availableRoutesIconLabel.setVisible(false);
        rightLabel.setVisible(false);
        leftLabel.setVisible(false);
        if (activeRoute != -1) {
            removeRoute(activeRoute);
        }
        if (trackFromCurrentRoom) {
            if (!roomSelectionInXYMap.isEmpty()) {
                Integer[] roomXY = roomSelectionInXYMap.get(roomSelectionInXYMap.size() - 1);
                Room room = roomForMapSelection(roomXY[0], roomXY[1]);
                map.routeFromCurrentNode(room, activeRoute);
                map.findBestRoutesFromCurrentNode(comboDecoder, skipEliteBool);
            }
        } else { // need many many checks to work properly such as check if active route = -1 ??
            map.findBestRoutes(comboDecoder, skipEliteBool);
        }
        if (routesChangeArray != null) {
            for (JButton jb : routesChangeArray) {
                changeRoutePanel.remove(jb);
            }
        }
        routesChangeArray = new JButton[map.getBestRoutes().size()];
        int counter = 0;
        for (Route route : map.getBestRoutes()) {
            routesChangeArray[counter] = new JButton();
            changeRoutePanel.add(routesChangeArray[counter]);
            routesChangeArray[counter].setPreferredSize(new java.awt.Dimension(150, 40));
            routesChangeArray[counter].setBorderPainted(false);
            routesChangeArray[counter].setContentAreaFilled(false);
            routesChangeArray[counter].setFocusPainted(false);
            routesChangeArray[counter].setText("");
            routesChangeArray[counter].addActionListener((ActionEvent e) -> {
                changeRoutePressed(e);
            });
            counter++;
            routesChangeArray[counter - 1].setIcon(new javax.swing.ImageIcon(getClass().getResource("/map/route" + counter + ".png")));
            routesChangeArray[counter - 1].setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/map/route" + counter + "H.png")));
        }
        if (counter > 1) {
            availableRoutesIconLabel.setVisible(true);
            rightLabel.setVisible(true);
        }
        availableRoutes = counter;
        //availableRoutesLabel.setText(counter + "");

        activeRoute = 0;
        showRoute(activeRoute);
        trackLabel.setVisible(false);
        trackFromCurrentRoom = false;
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c1");
        currentCard = "c1";
        optionButton.setVisible(true);
        backButton.setVisible(false);
        prevCard = "c2";
    }//GEN-LAST:event_startRoutingActionPerformed

    private void changeRoutePressed(java.awt.event.ActionEvent evt) {
        int newRouteID = -1;
        for (int i = 0; i < routesChangeArray.length; i++) {
            if (routesChangeArray[i].equals(evt.getSource())) {
                newRouteID = i;
                break;
            }
        }
        if (newRouteID != -1) {
            removeRoute(activeRoute);
            activeRoute = newRouteID;
            showRoute(activeRoute);
        }
        if (activeRoute == availableRoutes - 1) {
            rightLabel.setVisible(false);
        } else {
            rightLabel.setVisible(true);
        }
        if (activeRoute == 0) {
            leftLabel.setVisible(false);
        } else {
            leftLabel.setVisible(true);
        }

    }

    private void optionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionButtonActionPerformed
        // TODO add your handling code here:
        optionButton.setVisible(false);
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c3");
        currentCard = "c3";
        backButton.setVisible(true);
        prevCard = "c1"; //bug
    }//GEN-LAST:event_optionButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:

        CardLayout cl = (CardLayout) cardPanel.getLayout();
        if (!prevCard.equals("c3")) {
            backButton.setVisible(false);
            optionButton.setVisible(true);
        }
        cl.show(cardPanel, prevCard);

        if (currentCard.equals("c3")) {
            cl.show(cardPanel, "c1");
            currentCard = "c1";
            prevCard = "c3";
            backButton.setVisible(false);
            optionButton.setVisible(true);
        } else {
            currentCard = prevCard;
        }
    }//GEN-LAST:event_backButtonActionPerformed

    private void changeRouteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeRouteButtonActionPerformed
        // TODO add your handling code here:
        prevCard = "c3";
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c4");
        currentCard = "c4";
    }//GEN-LAST:event_changeRouteButtonActionPerformed

    private void resetPrioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPrioButtonActionPerformed
        // TODO add your handling code here:
        prevCard = "c3";
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c2");
        currentCard = "c2";
    }//GEN-LAST:event_resetPrioButtonActionPerformed

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

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_closeActionPerformed

    private boolean uShouldClick = false;

    private void mapDraggerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mapDraggerPanelMousePressed
        // TODO add your handling code here:
        origin = new Point(evt.getPoint());
        uShouldClick = true;
    }//GEN-LAST:event_mapDraggerPanelMousePressed

    private void mapDraggerPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mapDraggerPanelMouseDragged
        // TODO add your handling code here:
        uShouldClick = false;
        JViewport viewPort = jScrollPane1.getViewport();
        Point vpp = viewPort.getViewPosition();
        vpp.translate(0, origin.y - evt.getY());
        jPanel1.scrollRectToVisible(new Rectangle(vpp, viewPort.getSize()));

    }//GEN-LAST:event_mapDraggerPanelMouseDragged

    private boolean customContains(Rectangle r, int X, int Y) {
        int x = r.x - 20;
        int y = r.y - 25;
        int w = r.width + 40;
        int h = r.height + 50;

        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...q
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X)
                && (h < y || h > Y));

    }

    private void mapDraggerPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mapDraggerPanelMouseReleased
        // TODO add your handling code here:
        if (uShouldClick) {
            if (evt.getY() < 516) {
                outerloop1:
                for (int j = 0; j < 20; j++) {
                    for (int i = 0; i < 7; i++) {
                        if (iconArray[i][j].getIcon() != null && customContains(iconArray[i][j].getBounds(), evt.getX() - 120, evt.getY() - 80)) {
                            clickLabel(i, j);
                            //System.out.println("Found on 1st part.");
                            break outerloop1;
                        }
                    }
                }
            } else if (evt.getY() >= 516 && evt.getY() < 1032) {
                outerloop2:
                for (int j = 15; j < 40; j++) {
                    for (int i = 0; i < 7; i++) {
                        if (iconArray[i][j].getIcon() != null && customContains(iconArray[i][j].getBounds(), evt.getX() - 120, evt.getY() - 80)) {
                            clickLabel(i, j);
                            //System.out.println("Found on 2nd part.");
                            break outerloop2;
                        }
                    }
                }
            } else if (evt.getY() >= 1032) {
                outerloop3:
                for (int j = 30; j < 57; j++) {
                    for (int i = 0; i < 7; i++) {
                        if (iconArray[i][j].getIcon() != null && customContains(iconArray[i][j].getBounds(), evt.getX() - 120, evt.getY() - 80)) {
                            clickLabel(i, j);
                            //System.out.println("Found on 3rd part.");
                            break outerloop3;
                        }
                    }
                }
            } else {
                outerloop:
                for (int j = 0; j < 57; j++) {
                    for (int i = 0; i < 7; i++) {
                        if (iconArray[i][j].getIcon() != null && customContains(iconArray[i][j].getBounds(), evt.getX() - 120, evt.getY() - 80)) {
                            clickLabel(i, j);
                            //System.out.println("I had to reloop.");
                            break outerloop;
                        }
                    }
                }
            }

            uShouldClick = false;
        }
    }//GEN-LAST:event_mapDraggerPanelMouseReleased

    private boolean trackFromCurrentRoom = false;

    private void routeFromCurButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeFromCurButtonActionPerformed
        // TODO add your handling code here:
        trackFromCurrentRoom = true;
        trackLabel.setVisible(true);
        prevCard = "c3";
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "c2");
        currentCard = "c2";
    }//GEN-LAST:event_routeFromCurButtonActionPerformed

    private void algorithmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algorithmButtonActionPerformed
        // TODO add your handling code here:
        if (map.currentAlgorithm == 0) {
            map.currentAlgorithm = 1;
            routeAlgoLabel.setIcon(weight);
        } else {
            map.currentAlgorithm = 0;
            routeAlgoLabel.setIcon(brute);
        }
        startRoutingActionPerformed(evt);
    }//GEN-LAST:event_algorithmButtonActionPerformed

    private boolean dontdragme = false;
    private boolean dragLock = false;

    private void titleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMousePressed
        // TODO add your handling code here:
        this.start_drag = this.getScreenLocation(evt);
        this.start_loc = this.getFrame(this.title).getLocation();
        //System.out.println(evt.getX() + " " + evt.getY());
        if (evt.getX() > 100 && evt.getX() < 750 && evt.getY() > 56 && evt.getY() < 95) {
            dontdragme = true;
            MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
            if (jPanel1.getLocation().y < -66) {
                dragLock = true;
                mapDraggerPanel.dispatchEvent(convertMouseEvent);
            }
        } else {
            dontdragme = false;
        }
    }//GEN-LAST:event_titleMousePressed

    private void titleMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseReleased
        // TODO add your handling code here:
        if (dragLock) {
            dragLock = false;
        }
        if (evt.getX() > 100 && evt.getX() < 750 && evt.getY() > 56 && evt.getY() < 95) {
            MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
            mapDraggerPanel.dispatchEvent(convertMouseEvent);
        }
    }//GEN-LAST:event_titleMouseReleased

    private void titleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseDragged
        // TODO add your handling code here
        if (!dontdragme) {
            Point current = this.getScreenLocation(evt);
            Point offset = new Point((int) current.getX() - (int) start_drag.getX(),
                    (int) current.getY() - (int) start_drag.getY());
            JFrame frame = this.getFrame(title);
            Point new_location = new Point(
                    (int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc
                    .getY() + offset.getY()));
            frame.setLocation(new_location);
        } else {
            MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
            if (dragLock) {
                mapDraggerPanel.dispatchEvent(convertMouseEvent);
            }
        }
    }//GEN-LAST:event_titleMouseDragged

    private void titleMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleMouseMoved
        // TODO add your handling code here:
        //System.out.println("CURSOR : " + evt.getLocationOnScreen().x + "" + evt.getLocationOnScreen().y);
    }//GEN-LAST:event_titleMouseMoved

    private void cameraButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cameraButtonActionPerformed
        // TODO add your handling code here:
        screenshot();
    }//GEN-LAST:event_cameraButtonActionPerformed

    private void leftLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftLabelActionPerformed
        // TODO add your handling code here:
        activeRoute--;
        rightLabel.setVisible(true);
        //0  2
        if (activeRoute == 0) {
            leftLabel.setVisible(false);
        }
        removeRoute(activeRoute + 1);
        showRoute(activeRoute);
    }//GEN-LAST:event_leftLabelActionPerformed

    private void rightLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightLabelActionPerformed
        // TODO add your handling code here:
        activeRoute++;
        leftLabel.setVisible(true);
        //0  2
        if (activeRoute == availableRoutes - 1) {
            rightLabel.setVisible(false);
        }
        removeRoute(activeRoute - 1);
        showRoute(activeRoute);
    }//GEN-LAST:event_rightLabelActionPerformed

    private void jScrollPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseMoved
        // TODO add your handling code here:
        //System.out.println("CURSOR : " + evt.getLocationOnScreen().x + "" + evt.getLocationOnScreen().y);
    }//GEN-LAST:event_jScrollPane1MouseMoved

    private boolean killSteam = false;
    private Controller parent;

    public void passReference(Controller parent) {
        this.parent = parent;
    }

    private boolean algoLabelPress = false;

    private void steamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_steamActionPerformed
        // TODO add your handling code here:
        parent.killSteam();
    }//GEN-LAST:event_steamActionPerformed

    private void minimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeActionPerformed
        // TODO add your handling code here:
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizeActionPerformed

    private void routeAlgoLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_routeAlgoLabelMouseClicked
        // TODO add your handling code here:
        if (activeRoute != -1) {
            algorithmButtonActionPerformed(null);
        }
    }//GEN-LAST:event_routeAlgoLabelMouseClicked

    private void routeAlgoLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_routeAlgoLabelMousePressed
        // TODO add your handling code here:
        MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
        title.dispatchEvent(convertMouseEvent);
    }//GEN-LAST:event_routeAlgoLabelMousePressed

    private void routeAlgoLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_routeAlgoLabelMouseReleased
        // TODO add your handling code here:
        MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
        title.dispatchEvent(convertMouseEvent);
    }//GEN-LAST:event_routeAlgoLabelMouseReleased

    private void routeAlgoLabelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_routeAlgoLabelMouseDragged
        // TODO add your handling code here:
        MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
        title.dispatchEvent(convertMouseEvent);
    }//GEN-LAST:event_routeAlgoLabelMouseDragged

    private void routeAlgoLabelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_routeAlgoLabelMouseMoved
        // TODO add your handling code here:
        MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(evt.getComponent(), evt, mapDraggerPanel);
        title.dispatchEvent(convertMouseEvent);
    }//GEN-LAST:event_routeAlgoLabelMouseMoved

    private void screenshot() {
        try {
            // TODO add your handling code here:
            screen2image.screenCapture(this);
            ScreenshotDialog sd = new ScreenshotDialog(this, true, jLabel3);
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
            Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showInformation() {
        int[] info = map.getBestRoutes().get(activeRoute).getStats();
        restInfo.setText(info[0] + "");
        eliteInfo.setText(info[1] + "");
        merchantInfo.setText(info[2] + "");
        unknownInfo.setText(info[3] + "");
        treasureInfo.setText(info[4] + "");
        enemyInfo.setText(info[5] + "");
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
        UIManager.put("RootPane.setupButtonVisible", false);
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

        } catch (Exception ex) {
            Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //IManager.put( "TabbedPane.tabAreaInsets", new javax.Swing.plaf.InsetsUIResource( 3 , 20 , 2 , 20 ));
        UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI(null).setVisible(true);
            }
        });
    }
    private int[] skip = new int[]{1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15, 17, 18, 19, 21, 22, 23, 25, 26, 27, 29, 30, 31, 33, 34, 35, 37, 38, 39, 41,
        42, 43, 45, 46, 47, 49, 50, 51, 53, 54, 55};
    //private int[] skip = new int[]{1, 2, 3, 6, 7, 8, 10, 11, 12, 14, 15, 16, 18, 19, 20, 22, 23, 24, 26, 27, 28, 30, 31, 32,
    // 34, 35, 36, 38, 39, 40, 42, 43, 44, 46, 47, 48, 50, 51, 52, 54, 55, 56};
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton algorithmButton;
    private javax.swing.JLabel availableRoutesIconLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JButton cameraButton;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JButton changeRouteButton;
    private javax.swing.JPanel changeRoutePanel;
    private javax.swing.JButton close;
    private javax.swing.JComboBox<String> eliteCombo;
    private javax.swing.JLabel eliteIcon;
    private javax.swing.JLabel eliteIcon1;
    private javax.swing.JLabel eliteInfo;
    private javax.swing.JComboBox<String> enemyCombo;
    private javax.swing.JLabel enemyIcon;
    private javax.swing.JLabel enemyIcon1;
    private javax.swing.JLabel enemyInfo;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton leftLabel;
    private javax.swing.JPanel legend;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel mapDraggerPanel;
    private javax.swing.JComboBox<String> merchantCombo;
    private javax.swing.JLabel merchantIcon;
    private javax.swing.JLabel merchantIcon1;
    private javax.swing.JLabel merchantInfo;
    private javax.swing.JButton minimize;
    private javax.swing.JButton optionButton;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JButton resetPrioButton;
    private javax.swing.JComboBox<String> restCombo;
    private javax.swing.JLabel restIcon;
    private javax.swing.JLabel restIcon1;
    private javax.swing.JLabel restInfo;
    private javax.swing.JButton rightLabel;
    private javax.swing.JLabel routeAlgoLabel;
    private javax.swing.JButton routeFromCurButton;
    private javax.swing.JLabel routeIDContainerLabel;
    private javax.swing.JLabel routeIDLabel;
    private javax.swing.JLabel routeInfoLabel;
    private javax.swing.JPanel setPrioritiesPanel;
    private javax.swing.JCheckBox skipElite;
    private javax.swing.JButton startRouting;
    private javax.swing.JButton steam;
    private javax.swing.JPanel stepPanel;
    private javax.swing.JLabel title;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JLabel titlePrio;
    private javax.swing.JLabel trackLabel;
    private javax.swing.JLabel treasureIcon;
    private javax.swing.JLabel treasureIcon1;
    private javax.swing.JLabel treasureInfo;
    private javax.swing.JComboBox<String> unknownCombo;
    private javax.swing.JLabel unknownIcon;
    private javax.swing.JLabel unknownIcon1;
    private javax.swing.JLabel unknownInfo;
    // End of variables declaration//GEN-END:variables
}
