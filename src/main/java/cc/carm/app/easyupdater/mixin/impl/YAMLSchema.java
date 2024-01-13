package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YAMLSchema implements MixinSchema<YamlConfiguration> {
    @Override
    public @Nullable YamlConfiguration parse(@NotNull File file) {
        return null;
    }

    @Override
    public void apply(@NotNull YamlConfiguration source, @NotNull File target) throws Exception {
        //TODO: Apply changes
    }
}
