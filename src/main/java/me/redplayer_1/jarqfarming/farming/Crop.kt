package me.redplayer_1.jarqfarming.farming

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.Material

@Serializable
data class Crop(val type: Material, val expRange: Pair<Int, Int>, val moneyRange: Pair<Int, Int>) {
    @Transient val dropRates = 1 to 3
    object Registry {
        @Transient val crops = mapOf(
            "WHEAT" to Crop(Material.WHEAT, 1 to 5, 5 to 10),
            "CARROTS" to Crop(Material.CARROTS, 5 to 10, 10 to 15),
            "POTATOES" to Crop(Material.POTATOES, 10 to 15, 15 to 20),
            "BEETROOTS" to Crop(Material.BEETROOTS, 15 to 20, 20 to 25)
        )
    }
}
