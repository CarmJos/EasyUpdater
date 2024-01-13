package cc.carm.app.easyupdater.action;

import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

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
        //TODO: Apply changes
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

}
