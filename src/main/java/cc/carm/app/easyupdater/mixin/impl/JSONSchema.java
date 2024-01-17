package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import cc.carm.app.easyupdater.utils.Logging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class JSONSchema implements MixinSchema<JsonElement> {
    private static Gson gson = new Gson();

    @Override
    public @Nullable JsonObject parse(@NotNull File file) {
        try (Reader reader = new FileReader(file)) {
            JsonElement jsonObject = JsonParser.parseReader(reader);
            return jsonObject.isJsonObject() ? (JsonObject) jsonObject : null;
        } catch (IOException e) {
            Logging.error("Failed to parse json file " + file.getAbsolutePath() + "!", e);
            return null;
        }
    }

    @Override
    public void apply(@NotNull JsonElement source, @NotNull File target) throws Exception {
        JsonElement json = parse(target);
        if (json == null) throw new Exception("File " + target.getAbsolutePath() + " is a json file!");

        if (source.isJsonObject() && json.isJsonObject()) {
            JsonObject sourceObject = source.getAsJsonObject();
            JsonObject targetObject = json.getAsJsonObject();
            for (String key : sourceObject.keySet()) {
                targetObject.add(key, sourceObject.get(key));
            }
        } else if (source.isJsonArray() && json.isJsonArray()) {
            saveJson(source, target);
        } else if (source.isJsonPrimitive() && json.isJsonPrimitive()) {
            saveJson(source, target);
        }
    }


    public void saveJson(JsonElement json, File file) throws IOException {
        try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            writer.jsonValue(gson.toJson(json));
        }
    }

}
