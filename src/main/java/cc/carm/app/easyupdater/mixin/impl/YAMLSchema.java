package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Set;

public class YAMLSchema implements MixinSchema<YamlConfiguration> {
    @Override
    public @Nullable YamlConfiguration parse(@NotNull File file) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try{
            yamlConfiguration.load(file);
        }catch (IOException | InvalidConfigurationException e){
            Logging.warning("无效的文件"+file.getAbsolutePath());
        }
        return yamlConfiguration;
    }

    @Override
    public void apply(@NotNull YamlConfiguration source, @NotNull File target) throws Exception {
        //TODO: Apply changes
        YamlConfiguration targetYml = parse(target);
        Set<String> keys = targetYml.getKeys(true);
        for(String s:source.getKeys(true)){
            targetYml.set(s,source.getString(s));
        }
        targetYml.save(target);
    }
}
