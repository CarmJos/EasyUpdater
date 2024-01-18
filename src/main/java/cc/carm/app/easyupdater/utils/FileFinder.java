package cc.carm.app.easyupdater.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FileFinder {

    public static List<File> findFiles(String pattern) {
        String[] parts = pattern.split(Pattern.quote(File.separator));
        if(parts.length == 0) return new ArrayList<>();

        int index;
        File root;
        if(parts[0].isEmpty()){
            // linux root
            root = new File(File.separator);
            // start at next part
            index = 1;
        } else if (parts[0].contains(":")){
            // windows root
            root = new File(parts[0]+File.separator);
            // start at next part
            index = 1;
        } else {
            // relative path
            root = new File("."+File.separator);
            // start at current part
            index = 0;
        }

        return findFiles(root, parts, index);
    }

    private static List<File> findFiles(File directory, String[] parts, int index) {
        File[] files = directory.listFiles();

        List<File> res = new ArrayList<>();
        if (files == null) return res;

        for(File cur : files){
            if(!cur.getName().matches(parts[index])) continue;

            // node matched

            if(parts.length-1 == index){
                // match the final file
                res.add(cur);
            } else if(cur.isDirectory()){
                // match parent directory. Then match files in this directory
                res.addAll(findFiles(cur, parts, index+1));
            }
        }

        return res;
    }

}
