package me.redplayer_1.jarqfarming

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.io.File
import java.util.*

object Manager { //contains utility functions and data
    private val json = Json { allowStructuredMapKeys = true } //NOTE: this should be used for all (de)serialization
    val farmers: MutableMap<Player, Farmer> = mutableMapOf()
    val hoe_levels: List<Hoe.Level> = json.decodeFromString(JarQFarming.HOE_LEVELS.readText())

    /**
     * Serializes all registered farmers and then clears the registry
     */
    fun saveFarmers() {
        //serialize all farmer instances to a file named with their UUID
        //should only be run on plugin disable
        for (entry in farmers.entries) {
            File(JarQFarming.FARMER_FOLDER.path + "/" + entry.key.uniqueId + ".json").writeText(
                json.encodeToString(
                    entry.value
                )
            )
        }
        farmers.clear()
    }

    /**
     * Serialize the farmer instance of a player.
     * @param player The target player
     */
    fun saveFarmer(player: Player) {
        if (farmers[player] != null) {
            File(JarQFarming.FARMER_FOLDER.path + "/" + player.uniqueId + ".json").writeText(json.encodeToString(farmers[player]))
        } else {
            JarQFarming.INSTANCE.logger.warning("${player.name}'s farming data wasn't saved because there was no farmer associated with them.")
        }
    }


    /**
     * Deserialize the farmer instance associated with the provided UUID
     *
     * NOTE: this doesn't add the Farmer to the registry!
     * @param uuid The UUID of the player
     * @return The farmer for this player (farmer will have no stats if it doesn't exist)
     */
    fun loadFarmer(uuid: UUID): Farmer {
        val file = File(JarQFarming.FARMER_FOLDER.path + "/" + uuid + ".json")
        return if (file.exists()) {
            json.decodeFromString<Farmer>(file.readText())
        } else {
            Farmer()
        }
    }
}

/**
 * Finds the amount of each of the specified items
 * @param materials The materials to search for
 * @return A map of the material to the amount found
 */
fun Inventory.amountOf(vararg materials: Material): Map<Material, Int> {
    val amounts = mutableMapOf<Material, Int>()
    materials.forEach { amounts[it] = 0 }
    contents.forEach { if (it != null && materials.contains(it.type)) amounts[it.type] = amounts[it.type]!! + it.amount }
    return amounts
}