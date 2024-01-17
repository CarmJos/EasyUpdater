package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class JSONSchema implements MixinSchema<JsonObject> {
    private static Gson gson = new Gson();
    @Override
    public @Nullable JsonObject parse(@NotNull File file) {
        try(Reader reader=new FileReader(file)) {
            JsonElement jsonObject = JsonParser.parseReader(reader);
            reader.close();
            return jsonObject.isJsonObject()?(JsonObject)jsonObject:null;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void apply(@NotNull JsonObject source, @NotNull File target) throws Exception {
        //TODO: Apply changes
        JsonObject jsonObject = parse(target);
        if(jsonObject==null){
            throw new Exception("not a json");
        }
        for(String key:source.keySet()){
            jsonObject.add(key,source.get(key));
        }
        String s = gson.toJson(jsonObject);
        try (JsonWriter writer = new JsonWriter(new FileWriter(target))) {
            writer.jsonValue(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
