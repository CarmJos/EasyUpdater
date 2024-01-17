package cc.carm.app.easyupdater.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileFinder {

    public static List<File> findFiles(String pattern) {
        String[] parts = pattern.split(Pattern.quote(File.separator));
        return findFiles(new File(parts[0]), parts, 1);
    }

    private static List<File> findFiles(File directory, String[] parts, int index) {
        File[] files = directory.listFiles();
        if (files == null) return new ArrayList<>();
        // TODO #8
        return new ArrayList<>();
    }

}
