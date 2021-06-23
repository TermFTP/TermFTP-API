package at.termftp.backend.utils;



import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogger {
    public static final int ROOT_LEVEL = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_5 = 5;

    public static final String TYPE_INFO = "[INFO]";
    public static final String TYPE_WARN = "[warn]";
    public static final String TYPE_ERROR = "[error]";

    private static final String[] SYMBOLS = {
        "-", ">", "\t", " "
    };

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_DATE_TIME;

    private static final Logger logger = Logger.getGlobal();


    public static void logCustom(int level, Level type, String logMessage){
        String output = SYMBOLS[0].repeat(Math.max(0, level * 2)) + (level > 0 ? SYMBOLS[3] : "") + logMessage;
        logger.log(type, output);
    }

    public static void logDefault(String logMessage){
        logger.log(Level.INFO, logMessage);
    }

    public static void logError(String logMessage){
        logger.log(Level.SEVERE, logMessage);
    }
    public static void logWarning(String logMessage){
        logger.log(Level.WARNING, logMessage);
    }
}
