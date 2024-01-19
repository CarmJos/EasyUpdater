package cc.carm.app.easyupdater.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileFinder {
    private FileFinder() {
    }

    public static List<File> find(String pattern) {
        String[] parts = pattern.split(Pattern.quote(File.separator));
        if (parts.length == 0) return new ArrayList<>();

        int index;
        File root;
        if (parts[0].isEmpty()) {
            root = new File(File.separator); // linux root
            index = 1; // start at next part
        } else if (parts[0].contains(":")) {
            root = new File(parts[0] + File.separator);  // windows root
            index = 1;  // start at next part
        } else {
            root = new File("." + File.separator);   // relative path
            index = 0;   // start at current part
        }

        return recursiveSearch(root, parts, index);
    }

    private static List<File> recursiveSearch(File directory, String[] parts, int index) {
        File[] files = directory.listFiles();

        List<File> res = new ArrayList<>();
        if (files == null) return res;

        for (File current : files) {
            if (!current.getName().matches(parts[index])) continue; // none matched, next.
            if (parts.length - 1 == index) {
                res.add(current);   // match the final file
            } else if (current.isDirectory()) {
                // match parent directory. Then match files in this directory
                res.addAll(recursiveSearch(current, parts, index + 1));
            }
        }

        return res;
    }

}
