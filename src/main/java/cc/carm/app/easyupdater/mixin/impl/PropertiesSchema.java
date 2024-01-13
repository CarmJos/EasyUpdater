package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Properties;

public class PropertiesSchema implements MixinSchema<Properties> {
    @Override
    public @Nullable Properties parse(@NotNull File file) {
        return null;
    }

    @Override
    public void apply(@NotNull Properties source, @NotNull File target) throws Exception {
        //TODO: Apply changes
    }
}
