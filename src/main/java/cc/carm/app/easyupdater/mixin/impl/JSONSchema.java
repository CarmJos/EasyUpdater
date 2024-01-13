package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class JSONSchema implements MixinSchema<JsonObject> {
    @Override
    public @Nullable JsonObject parse(@NotNull File file) {
        return null;
    }

    @Override
    public void apply(@NotNull JsonObject source, @NotNull File target) throws Exception {
        //TODO: Apply changes
    }
}
