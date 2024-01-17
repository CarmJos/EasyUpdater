package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import cc.carm.app.easyupdater.utils.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class PropertiesSchema implements MixinSchema<Properties> {
    @Override
    public @Nullable Properties parse(@NotNull File file) {
        try (Reader reader = new FileReader(file)) {
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            Logging.error("Failed to parse properties file " + file.getAbsolutePath() + "!", e);
            return null;
        }
    }

    @Override
    public void apply(@NotNull Properties source, @NotNull File target) throws Exception {
        Properties properties = parse(target);
        if (properties == null) throw new Exception("File " + target.getAbsolutePath() + " is a properties!");

        source.keySet().stream()
                .filter(String.class::isInstance).map(String.class::cast)
                .forEach(s -> properties.put(s, properties.get(s)));

        try (FileOutputStream outputStream = new FileOutputStream(target)) {
            properties.store(outputStream, null);
        }
    }
}
