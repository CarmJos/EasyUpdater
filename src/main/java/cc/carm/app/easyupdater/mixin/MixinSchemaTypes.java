package cc.carm.app.easyupdater.mixin;

import cc.carm.app.easyupdater.mixin.impl.JSONSchema;
import cc.carm.app.easyupdater.mixin.impl.PropertiesSchema;
import cc.carm.app.easyupdater.mixin.impl.YAMLSchema;

import java.util.Arrays;

public enum MixinSchemaTypes {

    YAML(new YAMLSchema(), "yml"),
    JSON(new JSONSchema()),
    PROPERTIES(new PropertiesSchema(), "props");

    private final String[] aliases;
    private final MixinSchema<?> schema;

    MixinSchemaTypes(MixinSchema<?> schema, String... aliases) {
        this.schema = schema;
        this.aliases = aliases;
    }

    public MixinSchema<?> get() {
        return schema;
    }

    public boolean match(String extension) {
        return name().equalsIgnoreCase(extension) || Arrays.stream(aliases).anyMatch(alias -> alias.equalsIgnoreCase(extension));
    }

    public static MixinSchemaTypes parse(String type) {
        return Arrays.stream(MixinSchemaTypes.values()).filter(t -> t.match(type)).findFirst().orElse(null);
    }

}
