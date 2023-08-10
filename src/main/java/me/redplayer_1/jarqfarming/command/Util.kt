package me.redplayer_1.jarqfarming.command

import me.redplayer_1.jarqfarming.Manager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class Util: TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            sender.inventory.clear()
            Manager.farmers[sender]?.hoe?.item()?.let { sender.inventory.addItem(it) }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        return null
    }
}