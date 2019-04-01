package no.ab.application2.IO

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

    @Query("SELECT * FROM receipt_table")
    fun getAllUsersLive() : LiveData<List<ReceiptEntity>>
}