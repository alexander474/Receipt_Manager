package no.ab.application2.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.ab.application2.IO.ReceiptEntity
import no.ab.application2.IO.ReceiptRepository
import no.ab.application2.IO.ReceiptRoomDatabase
import kotlin.coroutines.CoroutineContext

class ReceiptViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReceiptRepository
    val allReceiptsLive: LiveData<List<ReceiptEntity>>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init{
        val userDAO = ReceiptRoomDatabase.getDatabase(application.applicationContext).userDao()
        repository = ReceiptRepository(userDAO)
        allReceiptsLive = repository.allReceiptsLive
    }

    fun insert(receiptEntity: ReceiptEntity) = scope.launch(Dispatchers.IO) {
        repository.insert(receiptEntity)
    }

    fun update(receiptEntity: ReceiptEntity) = scope.launch(Dispatchers.IO) {
        repository.update(receiptEntity)
    }

    fun delete(receiptEntity: ReceiptEntity) = scope.launch(Dispatchers.IO) {
        repository.delete(receiptEntity)
    }

    fun deleteAll() = scope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

}