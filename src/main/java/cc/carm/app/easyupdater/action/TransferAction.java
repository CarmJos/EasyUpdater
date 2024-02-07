package cc.carm.app.easyupdater.action;

import cc.carm.app.easyupdater.utils.FileFinder;
import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<File> sources = FileFinder.find(sourcePattern);
        if (sources.isEmpty()) throw new Exception("No any matched source file");

        if (targetPattern.endsWith(File.separator)) {
            transferDirectory(sources, new File(targetPattern));
        } else {
            int index = targetPattern.lastIndexOf(File.separator);

            File targetDirectory = new File(targetPattern.substring(0, index));
            if (!targetDirectory.exists()) targetDirectory.mkdirs();

            String targetFilePattern = targetPattern.substring(index + 1);
            transferFile(sources, targetDirectory, targetFilePattern);
        }
    }

    public @NotNull ConfigurationSection options() {
        return options;
    }

    public void transferFile(@NotNull List<File> sourceFiles,
                             @NotNull File targetDirectory, String targetFilePattern) {
        File sourceFile = lastModifiedIn(sourceFiles); // Fetch last modified file
        String targetName = sourceFile.getName();

        String renamePattern = options.getString("rename");
        if (renamePattern != null) {
            List<String> matches = matchPaths(sourceFile.getAbsolutePath(), sourcePattern);
            targetName = rename(renamePattern, matches);
        }

        File[] existFiles = targetDirectory.listFiles();
        if (existFiles != null) {
            Pattern pattern = Pattern.compile("^" + targetFilePattern + "(?!.)");
            Arrays.stream(existFiles).filter(f -> f.isFile() && pattern.matcher(f.getName()).matches()).forEach(File::delete);
        }

        File targetFile = new File(targetDirectory, targetName);
        if (targetFile.exists()) targetFile.delete();

        try {
            copy(sourceFile, targetFile);
            Logging.debug("Updated [" + sourceFile.getAbsolutePath() + " -> " + targetFile.getAbsolutePath() + "]");
        } catch (IOException e) {
            Logging.error("Cannot copy file " + sourceFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath() + "!", e);
        }

    }

    public void transferDirectory(List<File> sourceFiles, File targetDirectory) {
        if (sourceFiles.isEmpty()) return;
        if (!targetDirectory.exists()) targetDirectory.mkdirs();

        List<Pattern> filters = new HashSet<>(options().getStringList("filter"))
                .stream().map(Pattern::compile).collect(Collectors.toList());
        boolean override = options().getBoolean("override", false);

        for (File file : sourceFiles) {
            if (filtered(file.getName(), filters)) continue; // Filtered

            File targetFile = new File(targetDirectory, file.getName());
            if (targetFile.exists() && !override) continue; // Skip

            if (!targetFile.delete()) {
                Logging.warning("Cannot purge file " + targetFile.getAbsolutePath() + "!");
                continue;
            }

            try {
                copy(file, targetFile);
                Logging.debug("Updated [" + file.getAbsolutePath() + " -> " + targetFile.getAbsolutePath() + "]");
            } catch (IOException e) {
                Logging.warning("Cannot copy file " + file.getAbsolutePath() + " to " + targetFile.getAbsolutePath() + "!");
                e.printStackTrace();
            }

        }
    }

    public File lastModifiedIn(List<File> files) {
        return files.stream().max(Comparator.comparingLong(File::lastModified)).orElse(null);
    }

    public List<String> matchPaths(String filePath, String pathPattern) {
        List<String> matches = new ArrayList<>();

        // Separate paths and pattern by file separator, then check each part of it.
        String[] paths = filePath.split(File.pathSeparator);
        String[] patterns = pathPattern.split(File.pathSeparator);
        for (int i = 0; i < paths.length; i++) {
            Matcher matcher = Pattern.compile("^" + patterns[i] + "(?!.)").matcher(paths[i]);
            if (!matcher.matches()) break;
            IntStream.rangeClosed(1, matcher.groupCount()).mapToObj(matcher::group).forEach(matches::add);
        }

        return matches;
    }

    public File parsePath(String name, File[] files, List<String> matches) {
        if (files == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("^" + name + "(?!.)");
        for (File file : files) {
            if (file.getName().equals(name) || file.getName().isEmpty()) {
                return file;
            }
            Matcher matcher = pattern.matcher(file.getName());
            if (!matcher.matches()) {
                continue;
            }
            for (int var1 = 1; var1 <= matcher.groupCount(); var1++) {
                matches.add(matcher.group(var1));
            }
            return file;
        }
        return null;
    }

    public String rename(String source, List<String> list) {
        for (int var1 = 1; var1 <= list.size(); var1++) {
            source = source.replaceFirst("\\$" + var1, list.get(var1 - 1));
        }
        return source;
    }

    public void copy(File sourceFile, File targerFile) throws IOException {
        if (!targerFile.exists()) targerFile.createNewFile();

        try (InputStream inputStream = Files.newInputStream(sourceFile.toPath())) {
            try (OutputStream outputStream = Files.newOutputStream(targerFile.toPath())) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }

    }

    private boolean filtered(String name, List<Pattern> filters) {
        return filters.stream().anyMatch(pattern -> pattern.matcher(name).matches());
    }

}
