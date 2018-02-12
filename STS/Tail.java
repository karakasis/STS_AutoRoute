package STS;

// Import the Java classes
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import javax.swing.JFrame;

/**
 * Implements console-based log file tailing, or more specifically, tail
 * following: it is somewhat equivalent to the unix command "tail -f"
 */
public class Tail implements LoggerListener {

    /**
     * The log file tailer
     */
    private Logger tailer;
    private ArrayList<String> map;
    private ArrayList<String> log;
    private boolean mapFound;
    private boolean mappedInput;
    private Controller parent;
    private File file;
    private BufferedWriter out;

    /**
     * Creates a new Tail instance to follow the specified file
     */
    public Tail() {

    }

    public void setUpTailer(File file, Controller parent) {
        this.file = file;
        tailer = new Logger(this.file, 1000, false);
        tailer.addLogFileTailerListener(this);
        tailer.start();
        mapFound = false;
        mappedInput = false;
        this.parent = parent;
        log = new ArrayList<>();
    }

    public void actionIsComing() {
        try {

            out = new BufferedWriter(new FileWriter(file, false));
            //code goes here
            //out.write("PID | Arr. Time | Burst Time"); // ΤΙΤΛΟΣ ΕΓΓΡΑΦΩΝ ΣΤΟ ΑΡΧΕΙΟ 
            //out.newLine();
            out.append("2018-01-30 16:49:40.267 [LWJGL Application] INFO  com.megacrit.cardcrawl.dungeons.AbstractDungeon - Generated the following dungeon map:\n"
                    + "2018-01-30 16:49:40.267 [LWJGL Application] INFO  com.megacrit.cardcrawl.dungeons.AbstractDungeon - \n"
                    + "        /  /      \\  \\  \\  \n"
                    + "14     R  R        R  R  R \n"
                    + "       |    \\    /  /  /   \n"
                    + "13     E     M  M  E  M    \n"
                    + "         \\ /  / |/  /      \n"
                    + "12        R  ?  $  M       \n"
                    + "          |/  / |    \\     \n"
                    + "11        M  R  ?     M    \n"
                    + "        / | \\|/       |    \n"
                    + "10     R  $  M        R    \n"
                    + "       |/  / | \\        \\  \n"
                    + "9      M  ?  ?  $        M \n"
                    + "       |/  /  /          | \n"
                    + "8      T  T  T           T \n"
                    + "       | \\|    \\       /   \n"
                    + "7      E  E     M     E    \n"
                    + "       |  | \\ /         \\  \n"
                    + "6      R  ?  M           ? \n"
                    + "         \\|/ |         /   \n"
                    + "5         E  R        E    \n"
                    + "          | \\|      /      \n"
                    + "4         M  M     M       \n"
                    + "        /  / |     |       \n"
                    + "3      M  ?  M     ?       \n"
                    + "       |/    | \\ /         \n"
                    + "2      ?     M  ?          \n"
                    + "         \\ /   \\|          \n"
                    + "1         ?     M          \n"
                    + "        /     /            \n"
                    + "0      M     M             \n"
                    + "2018-01-30 16:49:40.267 [LWJGL Application] INFO  com.megacrit.cardcrawl.dungeons.AbstractDungeon - Game Seed: -8963552135154800752");
            out.append("\n");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Tail.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Tail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * A new line has been added to the tailed log file
     *
     * @param line The new line that has been added to the tailed log file
     */
    public void newLogFileLine(String line) {
        log.add(line);
        System.out.println(line);
        if (line.contains("Game Seed:")) {
            mappedInput = true;
        }
        if (line.contains("Generated the following dungeon map:")) {
            mapFound = true;
        }
        if (mappedInput && mapFound) {
            tailer.stopTailing();

            mappedInput = false;
            mapFound = false;

            parent._logCheck(new ArrayList<>(log));
            log.clear();
        }

    }

    public void _startTailing() {
        tailer = new Logger(file, 1000, false);
        tailer.addLogFileTailerListener(this);
        tailer.start();
    }

    public void _stopTailing() {
        tailer.stopTailing();
    }

}
