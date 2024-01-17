package cc.carm.app.easyupdater.action;

import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TransferAction {

    protected final @NotNull String sourcePattern;
    protected final @NotNull String targetPattern;

    protected final @NotNull ConfigurationSection options;

    public TransferAction(@NotNull String sourcePattern, @NotNull String targetPattern,
                          @NotNull ConfigurationSection options) {
        this.sourcePattern = sourcePattern;
        this.targetPattern = targetPattern;
        this.options = options;
    }

    public void execute() throws Exception {
        List<String> matches = new ArrayList<>();
        File source = getFile(sourcePattern,File.listRoots(),matches);
        File target = getFile(targetPattern,File.listRoots(),new ArrayList<>());
        if(source==null){
            throw new Exception("此源文件不存在");
        }
        if(target==null){
            throw new Exception("此对象文件不存在");
        }
        String rename = options.getString("rename");
        String exists = options.getString("exists");
        List<String> filter = options.getStringList("filter");
        exists = exists==null?"OVERRIDE":exists;
        rename = rename!=null?rename(rename, matches):target.getName();
        File newTarget = null;
        if(source.isFile()) {
            newTarget = new File(target.isFile()?target.getParentFile():target, rename);
            if (target.delete()) {
                if (!newTarget.exists()) {
                    newTarget.createNewFile();
                }
                copy(source, newTarget);
                return;
            }
            throw new Exception("target file deleted in failure");
        }
        if(target.isDirectory()){
            File[] oldFiles = source.listFiles();
            if(oldFiles==null){
                return;
            }
            for(File file:oldFiles){
                File newFile = new File(target,file.getName());
                if(!file.exists()||exists.equalsIgnoreCase("override")){
                    if(!newFile.delete()){
                        throw new Exception("target file deleted in failure");
                    }
                    if(filter(file.getName(),filter)){
                        continue;
                    }
                    copy(file, newFile);
                    continue;
                }
                if(exists.equalsIgnoreCase("skip")){
                    continue;
                }
            }
            return;
        }
        Logging.warning("请不要让source作为文件夹的同时让target为文件");
    }

    public @NotNull ConfigurationSection options() {
        return options;
    }

    public @NotNull String getSourcePattern() {
        return sourcePattern;
    }

    public @NotNull String getTargetPattern() {
        return targetPattern;
    }

    public File getFile(String sourcePattern,File[] roots,List<String> matches){
        List<String> pathes = Arrays.stream(sourcePattern.split(File.pathSeparator)).collect(Collectors.toList());
        String path = pathes.get(0);
        File[] files = roots;
        File f = new File(path);
        for(int var1=1;var1<pathes.size();var1++){
            f = parsePath(path,files,matches);
            if(f==null){
                matches.clear();
                return null;
            }
            path = pathes.get(var1);
            files = f.listFiles();
        }
        return f;
    }

    public File parsePath(String name,File[] files,List<String> matches){
        if(files==null){
            return null;
        }
        Pattern pattern = Pattern.compile("^"+name+"(?!.)");
        for(File file:files){
            if(file.getName().equals(name)||file.getName().isEmpty()){
                return file;
            }
            Matcher matcher = pattern.matcher(file.getName());
            if(!matcher.matches()){
                continue;
            }
            for(int var1=1;var1<=matcher.groupCount();var1++){
                matches.add(matcher.group(var1));
            }
            return file;
        }
        return null;
    }

    public String rename(String source,List<String> list){
        for(int var1=1;var1<=list.size();var1++){
            source = source.replaceFirst("\\$"+var1,list.get(var1));
        }
        return source;
    }

    public void copy(File sourceFile,File targerFile) throws IOException {
        if(!targerFile.exists()) {
            targerFile.createNewFile();
        }
        InputStream inputStream = Files.newInputStream(sourceFile.toPath());
        OutputStream outputStream = Files.newOutputStream(targerFile.toPath());

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }

    private boolean filter(String name,List<String> filters){
        for(String filter:filters){
            if(Pattern.compile(filter).matcher(name).matches()){
                return true;
            }
        }
        return false;
    }
}
