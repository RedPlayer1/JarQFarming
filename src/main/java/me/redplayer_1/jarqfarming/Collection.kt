package me.redplayer_1.jarqfarming

import kotlinx.serialization.Serializable

@Serializable
data class Collection(var collected: Long /* Includes added drops from fortune */, var broken: Long /* block of that type broken */)