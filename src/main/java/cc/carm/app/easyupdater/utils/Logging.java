package cc.carm.app.easyupdater.utils;

import cc.carm.app.easyupdater.Main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

public final class Logging {

    private static final String PREFIX = "EasyUpdater";
    private static final BooleanSupplier DEBUGGING = Main::isDebugging;
    private static final Pattern CONTROL_CHARACTERS_FILTER = Pattern.compile("\\p{Cc}&&[^\r\n\t]");

    private Logging() {
    }

    public enum Level {
        DEBUG, INFO, WARNING, ERROR
    }

    public static void log(Level level, String message) {
        log(level, message, null);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

    public static void error(String message, Throwable e) {
        log(Level.ERROR, message, e);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void log(Level level, String message, Throwable e) {
        if (level == Level.DEBUG && !DEBUGGING.getAsBoolean()) return;

        String log = "[" + PREFIX + "] [" + level + "] " + message;
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
            log += sw.toString();
        }

        log = CONTROL_CHARACTERS_FILTER.matcher(log).replaceAll("");
        if (level == Level.ERROR) {
            System.err.println(log);
        } else {
            System.out.println(log);
        }
    }

}
