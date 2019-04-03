package no.ab.application2.IO.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ReceiptDAO{

    @Insert
    fun insert(vararg receipt: ReceiptEntity)

    @Update
    fun update(vararg receipt: ReceiptEntity)

    @Delete
    fun delete(vararg receipt: ReceiptEntity)

    @Query("DELETE FROM receipt_table")
    fun deleteAll()

    @Query("SELECT * FROM receipt_table ORDER BY dateLastModified DESC, dateCreated DESC")
    fun getAllUsersLive() : LiveData<List<ReceiptEntity>>
}