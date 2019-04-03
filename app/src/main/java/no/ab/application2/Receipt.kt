package no.ab.application2

import android.arch.persistence.room.Ignore
import java.io.Serializable
import java.util.*

data class Receipt(
    var id: Long,
    var name: String,
    var description: String,
    var sum: Double,
    var currency: String,
    var imagePath: String,
    var dateCreated: Date,
    var dateLastModified: Date,
    var isExpanded: Boolean
    ) : Serializable {

    @Ignore
    constructor(id: Long, name: String, description: String, sum: Double, currency: String, imagePath: String, dateCreated: Date, dateLastModified: Date) :
            this(id,name,description, sum, currency, imagePath, dateCreated, dateLastModified, false)
}