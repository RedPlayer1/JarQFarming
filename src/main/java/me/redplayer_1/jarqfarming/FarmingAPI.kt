package me.redplayer_1.jarqfarming

import me.redplayer_1.jarqfarming.farming.Farmer
import java.util.*

object FarmingAPI {
    /**
     * Change to your own object that implements [FarmerSerializer].
     * Use if your plugin is storing all player-related data in one file.
     */
    var serializer: FarmerSerializer = Manager.DefaultSerializer

    /**
     * Returns the [Farmer] associated with the provided player's [UUID]
     */
    fun getFarmer(uuid: UUID): Farmer = Manager.loadFarmer(uuid)
    //TODO: farmer data edit? (is farmer returned by getFarmer a copy?)
    // https://stackoverflow.com/questions/45875491/what-is-a-receiver-in-kotlin#45875492
}