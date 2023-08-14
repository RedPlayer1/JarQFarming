package me.redplayer_1.jarqfarming.farming

import kotlinx.serialization.Serializable
import me.redplayer_1.jarqfarming.JarQFarming
import me.redplayer_1.jarqfarming.capitalize
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

/**
 * A Farmer represents a player (with some extra data) that is in the specified farming world
 */
@Serializable
data class Farmer(
    var prestige: Int = 0,
    var level: Int = 0,
    var xp: Int = 0,
    var money: Int = 0,
    var shards: Int = 0,
    var farmingFortune: Int = 0,
    var hoe: Hoe = Hoe()
) {
    private val baseXp = 10
    private val baseMoney = 15
    private val baseShards = 3
    private val maxLevel = JarQFarming.CONFIG?.getInt("max_farmer_level", 100)!!
    var maxXp = baseXp + floor(.8 * level.toDouble().pow(3)).toInt()
    var collections: MutableMap<Crop, Collection> = mutableMapOf()

    init {
        if (collections.isEmpty()) {
            for (i in Crop.Registry.crops.values) {
                collections[i] = Collection(0, 0)
            }
        }
    }

    /**
     * Make the farmer farm a crop
     * @param crop the [Crop] to farm
     * @param player the [Player] tied to this farmer
     * @param addCollection whether the harvested crops should be counted in the farmer's [Collection]
     */
    fun farm(crop: Crop, player: Player, addCollection: Boolean = true) {
        //returns an action bar to be shown to the player
        val addedXp = Random.nextInt(crop.expRange.first, crop.expRange.second)
        val addedMoney = Random.nextInt(crop.moneyRange.first, crop.moneyRange.second)
        val harvestedCrops = round(Random.nextInt(crop.dropRates.first, crop.dropRates.second) * (1 + (farmingFortune*.01))).toInt() //https://wiki.hypixel.net/Farming_Fortune
        xp += addedXp
        money += addedMoney
        if (addCollection) collections[crop]?.let { it.broken++; it.collected += harvestedCrops}
        player.sendActionBar(Component.text("§6+ §e$harvestedCrops §6 ${crop.type.name.replace("_", " ").capitalize()} §f| §5+ §d$addedXp §5xp §7§f| §a+ §2$§a$addedMoney"))

        //check if farmer is eligible for level up
        while (xp >= maxXp && level <= maxLevel) {
            val lvlUp = levelUp()
            player.sendRichMessage(lvlUp[0])
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(lvlUp[1]), MiniMessage.miniMessage().deserialize(lvlUp[2])))
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f)
            //spawn firework that deals no damage
            val f = player.world.spawnEntity(player.location, EntityType.FIREWORK, true) as Firework
            val fMeta = f.fireworkMeta
            fMeta.addEffect(FireworkEffect.builder().trail(false).flicker(false).with(FireworkEffect.Type.STAR).withColor(Color.RED).build())
            fMeta.power = 0
            f.fireworkMeta = fMeta
            f.setMetadata("nodamage", FixedMetadataValue(JarQFarming.INSTANCE!!, true))
            f.detonate()
        }
    }

    /**
     * Increments the farmer's level, recalculates the needed xp and rewards the farmer
     * @return An array of strings in the mini-message format: `[0] = chat message [1] = mainTitle [2] = subTitle`
     */
    private fun levelUp(): Array<String> {
        xp -= maxXp
        maxXp = baseXp + ceil(.7 * level.toDouble().pow(3)).toInt()
        val rewardShards = baseShards + level + level * baseShards
        val rewardMoney = baseMoney + ceil(.2 * level.toDouble().pow(3)).toInt()
        val rewardFortune = 1 + ceil(level * .2).toInt()
        level++
        shards += rewardShards
        money += rewardMoney
        farmingFortune += rewardFortune
        return arrayOf( //[0] = chat message [1] = mainTitle [2] = subTitle
            "<bold><yellow>-------------------<newline><gradient:#058fff:#00FFF6>Level Up! <reset>(<dark_gray><b>${level - 1}</b></dark_gray> <white>-></white> <b><color:#ffc929>$level</color></b>)<newline><green>+ <dark_green>$$rewardMoney</dark_green><newline>+ <light_purple>$rewardShards Shards</light_purple><newline>+ <yellow>$rewardFortune Farming Fortune (WIP)</yellow><newline><yellow><bold>-------------------",
            "<gradient:#ffca00:#ffff55>Level Up!",
            "<dark_gray><b>${level - 1}</b></dark_gray> <white>-></white> <b><color:#ffc929>$level</color></b>"
        )
    }
}