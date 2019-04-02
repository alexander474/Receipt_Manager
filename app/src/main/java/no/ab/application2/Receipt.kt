package no.ab.application2

import android.arch.persistence.room.Ignore
import java.io.Serializable

data class Receipt(
    var id: Long,
    var name: String,
    var description: String,
    var sum: Double,
    var currency: String,
    var imagePath: String,
    var isExpanded: Boolean) : Serializable {

    @Ignore
    constructor(id: Long, name: String, description: String, sum: Double, currency: String, imagePath: String) :
            this(id,name,description, sum, currency, imagePath,false)
}