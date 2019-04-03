package no.ab.application2.IO.database

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class ReceiptRepository(private val receiptDAO: ReceiptDAO){
    val allReceiptsLive: LiveData<List<ReceiptEntity>> = receiptDAO.getAllReceiptsLive()

    @WorkerThread
    suspend fun insert(receiptEntity: ReceiptEntity){
        receiptDAO.insert(receiptEntity)
    }

    @WorkerThread
    suspend fun update(receiptEntity: ReceiptEntity){
        receiptDAO.update(receiptEntity)
    }

    @WorkerThread
    suspend fun deleteAll(){
        receiptDAO.deleteAll()
    }

    @WorkerThread
    suspend fun delete(receiptEntity: ReceiptEntity){
        receiptDAO.delete(receiptEntity)
    }
}