package me.redplayer_1.jarqfarming.event

import me.redplayer_1.jarqfarming.JarQFarming
import me.redplayer_1.jarqfarming.Manager
import me.redplayer_1.jarqfarming.farming.Crop
import me.redplayer_1.jarqfarming.farming.Hoe
import org.bukkit.Sound
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.*

internal class EventManager : Listener {
    //private val hoeUpgradeNPCName = "Upgrade Hoe"

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        val itemMeta = event.player.inventory.itemInMainHand.itemMeta
        if ( itemMeta != null && itemMeta.persistentDataContainer.has(Hoe.namespacedKey) && event.block.blockData is Ageable) {
            val farmer = Manager.farmers[event.player]
            val ageable = (event.block.blockData as Ageable)

            if (ageable.age == ageable.maximumAge) {
                event.player.playSound(event.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 10f)
                Crop.Registry.crops[event.block.type.name]?.let { farmer?.farm(it, event.player) }
                ageable.age = 0
                event.block.blockData = ageable
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.world.name == JarQFarming.WORLD_NAME) {
            val farmer = Manager.loadFarmer(player.uniqueId)
            Manager.farmers[player] = farmer
            player.inventory.clear()
            player.inventory.setItemInMainHand(farmer.hoe.item())
        }
    }

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        Manager.saveFarmer(event.player)
        Manager.farmers.remove(event.player)
    }
/*
    @EventHandler
    fun npcClick(event: NPCClickEvent) {
        //if (event.npc.name.equals(hoeUpgradeNPCName, true))
    }

 */

    @EventHandler
    fun entityDamageEntity(event: EntityDamageByEntityEvent) {
        //to allow for the creation of fireworks that don't deal damage upon exploding
        if (event.damager is Firework) {
            if ((event.damager as Firework).hasMetadata("nodamage")) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun dropItem(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.itemMeta.persistentDataContainer.has(Hoe.namespacedKey)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun worldChange(event: PlayerChangedWorldEvent) {
        //doesn't fire on world leave if PerWorldPlugins is enabled
        if (event.from.name == JarQFarming.WORLD_NAME) {
            Manager.saveFarmer(event.player)
            Manager.farmers.remove(event.player)
            event.player.inventory.clear()
            return
        }
        if (event.player.world.name == JarQFarming.WORLD_NAME) {
            val player = event.player
            val farmer = Manager.loadFarmer(player.uniqueId)
            Manager.farmers[player] = farmer
            player.inventory.clear()
            player.inventory.setItemInMainHand(farmer.hoe.item())
        }
    }

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        //prevent players from trampling crops (also stops tripwires, pressure plates and redstone ore)
        if (event.action == Action.PHYSICAL) {
            event.isCancelled = true
        }
    }
}