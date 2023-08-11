package me.redplayer_1.jarqfarming.command

import me.redplayer_1.jarqfarming.farming.Hoe
import me.redplayer_1.jarqfarming.Manager
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

internal class Upgrade: TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendPlainMessage("You must be a player to run this command!")
        }
        val farmer = Manager.farmers[sender]
        if (farmer == null) {
            sender.sendRichMessage("<bold><dark_red>ERROR!</bold> <red>You must be a farmer to run this command.")
            return true
        }
        if ((sender as Player).inventory.itemInMainHand.itemMeta.persistentDataContainer.has(Hoe.namespacedKey)) {
            val upgradeChecks = farmer.hoe.isUpgradable(farmer, sender.inventory)
            if (upgradeChecks.first) {
                farmer.hoe.currentLevel++
                sender.playSound(sender.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                sender.inventory.setItemInMainHand(farmer.hoe.item())
            } else {
                sender.playSound(sender.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 0.1f)
            }
            sender.sendRichMessage(upgradeChecks.second)
        } else {
            sender.sendRichMessage("<dark_red><bold>ERROR!</bold> <red>You must be holding your farming tool to run this command")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        return mutableListOf()
    }
}