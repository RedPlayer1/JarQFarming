package me.redplayer_1.jarqfarming;

import me.redplayer_1.jarqfarming.command.Lookup;
import me.redplayer_1.jarqfarming.command.Upgrade;
import me.redplayer_1.jarqfarming.command.Util;
import me.redplayer_1.jarqfarming.event.EventManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class JarQFarming extends JavaPlugin {
    public static Plugin INSTANCE;
    private final String DATA_FOLDER = getDataFolder().getAbsolutePath();
    public static File HOE_LEVELS;
    public static File FARMER_FOLDER;
    public static String WORLD_NAME; //The name of the world that this plugin is supposed to run in

    @Override
    public void onEnable() {
        //TODO: load online players into farmer registry (on reload)
        //TODO: cooldown command (extends tabexecutor)

        // Data files
        INSTANCE = this;
        saveDefaultConfig();
        HOE_LEVELS = loadFromResources(new File(DATA_FOLDER + "/hoe_levels.json"));
        FARMER_FOLDER = new File(DATA_FOLDER + "/farmers");
        FARMER_FOLDER.mkdir();
        WORLD_NAME = getConfig().getString("farming_world_name", "");
        if (WORLD_NAME.isBlank()) {
            getLogger().severe("WORLD_NAME isn't set in the config! Disabling. . .");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Event listeners
        getServer().getPluginManager().registerEvents(new EventManager(), this);

        //Commands
        getCommand("lookup").setExecutor(new Lookup());
        getCommand("upgrade").setExecutor(new Upgrade());
        getCommand("util").setExecutor(new Util());
    }

    private File loadFromResources(File file) {
        if (!file.exists()) saveResource(file.getName(), false);
        return file;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //save farmer data (kotlinx.serialization)
        Manager.INSTANCE.saveFarmers();
    }
}
