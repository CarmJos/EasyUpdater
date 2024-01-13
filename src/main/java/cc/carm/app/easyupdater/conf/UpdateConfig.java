package cc.carm.app.easyupdater.conf;

import cc.carm.app.easyupdater.action.MixinAction;
import cc.carm.app.easyupdater.action.TransferAction;
import cc.carm.app.easyupdater.function.ThrowableFunction;
import cc.carm.app.easyupdater.mixin.MixinSchemaTypes;
import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class UpdateConfig {

    public static UpdateConfig parse(String filePath) throws Exception {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead() ? parse(file) : null;
    }

    public static UpdateConfig parse(File file) throws Exception {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return parse(configuration);
    }

    public static UpdateConfig parse(Configuration conf) throws Exception {
        Map<String, TransferAction> transferActions = loadSection(conf, "transfer", UpdateConfig::parseTransferAction);
        Map<String, MixinAction> mixinActions = loadSection(conf, "mixin", UpdateConfig::parseMixinAction);

        List<UpdateConfig> imports = new ArrayList<>();
        for (String path : conf.getStringList("imports")) {
            try {
                UpdateConfig config = parse(path);
                if (config != null) imports.add(config);
            } catch (Exception e) {
                Logging.error("Failed to load import file " + path + "!");
                e.printStackTrace();
            }
        }

        for (UpdateConfig config : imports) { // Put if not exists
            transferActions.forEach(transferActions::putIfAbsent);
            mixinActions.forEach(mixinActions::putIfAbsent);
        }

        return new UpdateConfig(transferActions, mixinActions);
    }

    public static TransferAction parseTransferAction(@Nullable ConfigurationSection conf) {
        if (conf == null) return null;
        return new TransferAction(
                Objects.requireNonNull(conf.getString("source"), "Source file not found."),
                Objects.requireNonNull(conf.getString("target"), "Target file not found."),
                Optional.ofNullable(conf.getConfigurationSection("options")).orElse(conf.createSection("options"))
        );
    }

    public static MixinAction parseMixinAction(@Nullable ConfigurationSection conf) throws Exception {
        if (conf == null) return null;
        String source = Objects.requireNonNull(conf.getString("source"), "Source file not found.");
        String target = Objects.requireNonNull(conf.getString("target"), "Target file not found.");
        String schema = conf.getString("schema", getExtension(target));
        MixinSchemaTypes schemaType = MixinSchemaTypes.parse(schema);
        if (schemaType == null) throw new Exception("Schema type is not valid");
        return new MixinAction(schemaType.get(), new File(source), new File(target));
    }

    public static @NotNull String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index + 1);
    }

    public static <T> Map<String, T> loadSection(@NotNull ConfigurationSection rootSection, @NotNull String path,
                                                 @NotNull ThrowableFunction<ConfigurationSection, T> function) {
        ConfigurationSection section = rootSection.getConfigurationSection(path);
        if (section == null) return new LinkedHashMap<>(0);

        Map<String, T> map = new LinkedHashMap<>();
        for (String id : section.getKeys(false)) {
            ConfigurationSection subSection = section.getConfigurationSection(id);
            if (subSection == null) continue;
            try {
                T value = function.apply(subSection);
                map.put(id, value);
            } catch (Exception e) {
                Logging.error("Failed to load " + path + "." + id + "!");
                e.printStackTrace();
            }
        }
        return map;
    }

    protected final @NotNull Map<String, TransferAction> transferActions;
    protected final @NotNull Map<String, MixinAction> mixinActions;

    public UpdateConfig(@NotNull Map<String, TransferAction> transferActions,
                        @NotNull Map<String, MixinAction> mixinActions) {
        this.transferActions = transferActions;
        this.mixinActions = mixinActions;
    }

    public @NotNull Map<String, TransferAction> getTransferActions() {
        return transferActions;
    }

    public @NotNull Map<String, MixinAction> getMixinActions() {
        return mixinActions;
    }

}
