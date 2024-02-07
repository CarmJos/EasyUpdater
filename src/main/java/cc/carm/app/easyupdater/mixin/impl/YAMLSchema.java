package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class YAMLSchema implements MixinSchema<YamlConfiguration> {
    @Override
    public @Nullable YamlConfiguration parse(@NotNull File file) {
        try {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);
            return yamlConfiguration;
        } catch (IOException | InvalidConfigurationException e) {
            Logging.error("Failed to parse yaml file " + file.getAbsolutePath() + "!", e);
            return null;
        }
    }

    @Override
    public void apply(@NotNull YamlConfiguration source, @NotNull File target) throws Exception {
        YamlConfiguration targetYml = parse(target);
        if (targetYml == null) throw new Exception("File " + target.getAbsolutePath() + " is a yaml file!");

        source.getKeys(true).forEach(s -> targetYml.set(s, source.get(s)));
        targetYml.save(target);
    }
}
