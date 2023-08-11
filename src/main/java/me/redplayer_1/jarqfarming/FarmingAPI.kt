package me.redplayer_1.jarqfarming

import me.redplayer_1.jarqfarming.farming.Farmer
import java.util.UUID

object FarmingAPI {
    fun getFarmer(uuid: UUID): Farmer = Manager.loadFarmer(uuid)
    //TODO: farmer data edit?
}