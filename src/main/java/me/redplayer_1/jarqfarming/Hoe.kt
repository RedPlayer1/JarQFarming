package me.redplayer_1.jarqfarming

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@Serializable
class Hoe(@Serializable var currentLevel: Int = 0) {
    companion object {
        @Transient val namespacedKey = NamespacedKey(JarQFarming.INSTANCE, "HOE")
    }
    private val hoeUpgradeBorder = "<bold><yellow>-------------------</yellow></bold>"
    private val hoeUpgradeEntryTrue = "<newline><dark_green><bold>\uD83D\uDDF8</bold><i> %s</i> <white>- (<green>%d<gold>/<green>%d)" //name, (amount, neededAmount)
    private val hoeUpgradeEntryFalse = "<newline><dark_red><bold>\uD835\uDC65</bold><i> %s</i> <white>- (<red>%d<gold>/<green>%d)"

    /**
     * Checks if the hoe is upgradable and generates a result message
     * @param farmer the farmer to check
     * @param inv the farmer's inventory
     * @return Whether all the conditions were met and the mini-message to be sent to the farmer
     */
    fun isUpgradable(farmer: Farmer, inv: Inventory): Pair<Boolean, String> {
        var canUpgrade = true
        var resultStr = "$hoeUpgradeBorder<newline><white><u>Level</u> <grey>$currentLevel <gold><i>-></i> <dark_green>${currentLevel+1}"
        fun updateStr(name: String, amount: Int, neededAmount: Int): Boolean {
            val isMet = amount >= neededAmount
            resultStr += if (isMet) {
                hoeUpgradeEntryTrue.format(name, amount, neededAmount)
            } else {
                hoeUpgradeEntryFalse.format(name, amount, neededAmount)
            }
            return isMet
        }
        if (currentLevel >= Manager.hoe_levels.size) {
            return false to "<bold><dark_red>ERROR!</bold> <red>Your hoe is at the maximum level!"
        }
        Manager.hoe_levels[currentLevel].requirements.forEach { (type, value) ->
            when (type.lowercase()) {
                "money" -> if(!(updateStr("Money", farmer.money, value.toInt()) && canUpgrade)) canUpgrade = false
                "shards" -> if(!(updateStr("Shards", farmer.shards, value.toInt()) && canUpgrade)) canUpgrade = false
                "xp" -> if(!(updateStr("XP", farmer.xp, value.toInt()) && canUpgrade)) canUpgrade = false
                "level" -> if(!(updateStr("Level", farmer.level, value.toInt()) && canUpgrade)) canUpgrade = false
                "prestige" -> if(!(updateStr("Prestige", farmer.prestige, value.toInt()) && canUpgrade)) canUpgrade = false
                "material" -> {
                    val str = value.split(":")
                    val material = Material.valueOf(str[0])
                    if(!(updateStr(str[0], inv.amountOf(material)[material]!!, str[1].toInt()) && canUpgrade)) {
                        canUpgrade = false
                    } else {
                        inv.removeItemAnySlot(ItemStack(material, str[1].toInt()))
                    }
                }
            }
        }

        resultStr += "<newline>$hoeUpgradeBorder"
        return canUpgrade to resultStr
    }

    fun item(): ItemStack {
        val item = ItemStack(Material.GOLDEN_HOE)
        item.editMeta {
            it.displayName(Component.text("Basic §lHoe §r[§6$currentLevel§f]"))
            it.lore(mutableListOf(Component.text("The only farming tool you'll ever need ;)")))
            it.persistentDataContainer.set(namespacedKey, PersistentDataType.BOOLEAN, true)
        }
        return item
    }

    @Serializable
    class Level(val level: Int, val requirements: Map<String, String> /* TODO: use enum for key*/)
}