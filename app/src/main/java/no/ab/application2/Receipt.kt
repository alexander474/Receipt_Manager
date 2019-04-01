package no.ab.application2

import android.arch.persistence.room.Ignore

data class Receipt(
    var name: String,
    var description: String,
    var sum: Double,
    var isExpanded: Boolean){

    @Ignore
    constructor(name: String, description: String, sum: Double) : this(name,description, sum,false)
}