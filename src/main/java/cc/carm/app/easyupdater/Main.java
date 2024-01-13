package cc.carm.app.easyupdater;

import cc.carm.app.easyupdater.action.ActionResult;
import cc.carm.app.easyupdater.action.MixinAction;
import cc.carm.app.easyupdater.action.TransferAction;
import cc.carm.app.easyupdater.conf.UpdateConfig;
import cc.carm.app.easyupdater.function.ThrowableTask;
import cc.carm.app.easyupdater.utils.Logging;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class Main {

    protected static boolean debugging;

    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : "updates.yml";
        File configFile = new File(configPath);

        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
            Logging.error("Failed to load project configurations. File not found or not readable.");
            return;
        }

        Logging.info("Loading project configurations (" + configPath + ") ...");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(configFile);
        Main.debugging = conf.getBoolean("debug", false);
        UpdateConfig updates;

        try {
            updates = UpdateConfig.parse(conf);
        } catch (Exception e) {
            Logging.error("Failed to load project configurations." + e);
            e.printStackTrace();
            return;
        }

        Logging.info("Executing transfer actions ...");
        ActionResult transferResult = executeAll(updates.getTransferActions(), TransferAction::execute);
        Logging.info("Transfer actions executed. " + transferResult.success() + " success, " + transferResult.failed() + " failed.");

        Logging.info("Executing mixin actions ...");
        ActionResult mixinResult = executeAll(updates.getMixinActions(), MixinAction::execute);
        Logging.info("Mixin actions executed. " + mixinResult.success() + " success, " + mixinResult.failed() + " failed.");

        Logging.info(" ");
        Logging.info("Update completed.");
    }

    public static boolean isDebugging() {
        return debugging;
    }

    private static <T> ActionResult executeAll(Map<String, T> tasks, ThrowableTask<T> executor) {
        int i = 0, success = 0, failed = 0;
        for (Map.Entry<String, T> entry : tasks.entrySet()) {
            Logging.debug(" #" + i + " -> Executing " + entry.getKey());
            try {
                executor.execute(entry.getValue());
                success++;
            } catch (Exception e) {
                Logging.error("Failed to execute action " + entry.getKey() + ". " + e);
                e.printStackTrace();
                failed++;
            }
        }
        return new ActionResult(success, failed);
    }

}
