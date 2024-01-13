package cc.carm.app.easyupdater.mixin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface MixinSchema<T> {

    default void apply(@NotNull File source, @NotNull File target) throws Exception {
        T values = parse(source);
        if (values == null) return;

        if (!target.exists()) {
            target.getParentFile().mkdirs();
            target.createNewFile();
        }

        if(!target.canWrite()) throw new Exception("Cannot write to file " + target.getAbsolutePath() + "!");

        apply(values, target);
    }

    /**
     * Parse the file to data schematic,
     * if the file is not valid, return null.
     *
     * @param file the file to parse
     * @return the data schematic
     */
    @Nullable T parse(@NotNull File file); // Parse the file to values

    /**
     * Apply the changes to target file
     *
     * @param source the values to apply
     * @param target the target file, may not exist.
     * @throws Exception if any error occurred
     */
    void apply(@NotNull T source, @NotNull File target) throws Exception; // Apply the changes to file

}
