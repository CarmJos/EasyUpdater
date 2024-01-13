package cc.carm.app.easyupdater.action;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MixinAction {
    
    protected final @NotNull MixinSchema<?> schema;

    protected final @NotNull File source;
    protected final @NotNull File target;

    public MixinAction(@NotNull MixinSchema<?> schema, @NotNull File source, @NotNull File target) {
        this.schema = schema;
        this.source = source;
        this.target = target;
    }

    public void execute() throws Exception {
        if (!source.exists() || !source.isFile() || !source.canRead()) {
            throw new Exception("Cannot read file " + source.getAbsolutePath() + "!");
        }

        if (!target.exists()) { // Create file if not exists
            boolean success = target.getParentFile().mkdirs() && target.createNewFile();
            if (!success) throw new Exception("Cannot create file " + target.getAbsolutePath() + "!");
        }

        if (!target.canWrite()) { // Check permission
            throw new Exception("Cannot write to file " + target.getAbsolutePath() + "!");
        }

        schema.apply(source, target); // Apply changes
    }

}
