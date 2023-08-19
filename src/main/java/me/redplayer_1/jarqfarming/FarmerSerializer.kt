package me.redplayer_1.jarqfarming

import me.redplayer_1.jarqfarming.farming.Farmer
import java.util.*

interface FarmerSerializer {
    /**
     * Takes a [Farmer] and its associated player's [UUID] as input and expects the function to
     * serialize the data to a file
     */
    fun save(uuid: UUID, farmer: Farmer)

    /**
     * Takes the target player's [UUID] and expects the function to deserialize the data stored
     * by the [save] function and return a [Farmer]. If no data is found, return a new [Farmer]
     */
    fun load(uuid: UUID): Farmer
}