package no.ab.application2.IO.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "receipt_table")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receiptID")
    var id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "sum")
    var sum: Double,
    @ColumnInfo(name = "currency")
    var currency: String,
    @ColumnInfo(name = "imagePath")
    var imagePath: String){

    @Ignore
    constructor(name: String, description: String, sum: Double, currency: String, imagePath: String) :
            this(0,name,description, sum, currency, imagePath)
}
