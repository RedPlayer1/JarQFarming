package me.redplayer_1.jarqfarming

import me.redplayer_1.jarqfarming.Manager.saveFarmers
import me.redplayer_1.jarqfarming.command.Lookup
import me.redplayer_1.jarqfarming.command.Upgrade
import me.redplayer_1.jarqfarming.command.Util
import me.redplayer_1.jarqfarming.event.EventManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class JarQFarming : JavaPlugin() {
    private val dataFolderPath = dataFolder.absolutePath
    override fun onEnable() {
        //TODO: migrate javadocs to kdocs
        // load online players into farmer registry (on reload)
        // auto-update system
        // leaderboards (level, collections)
        // PlaceholerAPI integration
        // cooldown command (implements tabexecutor)

        // Data files
        INSTANCE = this
        CONFIG = config
        saveDefaultConfig()
        HOE_LEVELS = loadFromResources(File("$dataFolderPath/hoe_levels.json"))
        FARMER_FOLDER = File("$dataFolderPath/farmers")
        FARMER_FOLDER!!.mkdir()
        WORLD_NAME = config.getString("farming_world_name", "")
        if (WORLD_NAME!!.isBlank()) {
            logger.severe("WORLD_NAME isn't set in the config! Disabling. . .")
            server.pluginManager.disablePlugin(this)
            return
        }

        //Event listeners
        server.pluginManager.registerEvents(EventManager(), this)

        //Commands
        getCommand("lookup")!!.setExecutor(Lookup())
        getCommand("upgrade")!!.setExecutor(Upgrade())
        getCommand("util")!!.setExecutor(Util())
    }

    override fun onDisable() {
        // Plugin shutdown logic
        saveFarmers()
    }

    private fun loadFromResources(file: File): File {
        if (!file.exists()) saveResource(file.getName(), false)
        return file
    }

    companion object {
        var INSTANCE: Plugin? = null
        var HOE_LEVELS: File? = null
        var FARMER_FOLDER: File? = null
        var WORLD_NAME: String? = null //The name of the world that this plugin is supposed to run in
        var CONFIG: FileConfiguration? = null
    }
}
