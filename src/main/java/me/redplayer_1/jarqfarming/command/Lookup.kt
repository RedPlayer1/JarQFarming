package me.redplayer_1.jarqfarming.command

import me.redplayer_1.jarqfarming.farming.Farmer
import me.redplayer_1.jarqfarming.Manager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

internal class Lookup: TabExecutor {
    private val statString = "<bold><yellow>-------------------</bold><newline><white><b>%s</b><white><gray>'s</gray> <red>Statistics<newline><dark_red>Level<white> - <red>%d (%d/%d)<newline><dark_red>Prestige<white> - <red>%d<newline><dark_red>Money<white> - <red><dark_green>$</dark_green>%d<newline><dark_red>Shards<white> - <red>%d<newline><bold><yellow>-------------------</bold>"
    private val collectPrefix = "<bold><yellow>-------------------</bold><newline><white><b>%s</b><white><gray>'s</gray> <red>Collections"
    private val collectEntry = "<newline><dark_red>%s<white> - <red>%,d"
    private val collectSuffix = "<newline><bold><yellow>-------------------"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        //usage: /lookup <stat|collection> <player (optional)>
        //TODO: Test
        if (args.isNullOrEmpty()) return false
        if (sender is Player) {
            val farmer: Farmer
            val name: String
            if (args.size == 1) {
                //if no player name is specified, respond with the executor's stats
                farmer = Manager.farmers[sender]!!
                name = sender.name
            } else {
                name = args[1]
                farmer = Manager.loadFarmer(Bukkit.getOfflinePlayer(args[1]).uniqueId)
            }
            when(args[0].lowercase()) {
                "stat" -> sender.sendRichMessage(statString.format(name,farmer.level, farmer.xp, farmer.maxXp, farmer.prestige, farmer.money, farmer.shards))
                "collection" -> {
                    var collectStr = collectPrefix.format(name)
                    for (i in farmer.collections) {
                        collectStr += collectEntry.format(i.key.type.name.lowercase().capitalize(), i.value.collected)
                    }
                    collectStr += collectSuffix
                    sender.sendRichMessage(collectStr)
                }
            }

        } else {
            sender.sendMessage("You must be a player to run this command!")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {
        return when (args?.size) {
            1 -> {
                mutableListOf("stat", "collection")
            }
            2 -> {
                val result = mutableListOf<String>()
                Manager.farmers.keys.forEach { result.add(it.name) }
                result
            }
            else -> {
                mutableListOf()
            }
        }
    }
}