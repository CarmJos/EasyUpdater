package cc.carm.app.easyupdater.mixin.impl;

import cc.carm.app.easyupdater.mixin.MixinSchema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class PropertiesSchema implements MixinSchema<Properties> {
    @Override
    public @Nullable Properties parse(@NotNull File file) {
        try(Reader reader=new FileReader(file)) {
            Properties properties = new Properties();
            properties.load(reader);
            reader.close();
            return properties;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void apply(@NotNull Properties source, @NotNull File target) throws Exception {
        //TODO: Apply changes
        Properties properties = parse(target);
        if(properties==null){
            throw new Exception("not a properties");
        }
        for(Object oval:source.keySet()){
            if(!(oval instanceof String)){
                continue;
            }
            String s = (String)oval;
            properties.put(s,properties.get(s));
        }
        FileOutputStream outputStream = new FileOutputStream(target);
        properties.store(outputStream,null);
        outputStream.close();
    }
}
