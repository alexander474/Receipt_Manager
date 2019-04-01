package no.ab.application2.IO

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [ReceiptEntity::class],version = 2, exportSchema = false)
public abstract class ReceiptRoomDatabase : RoomDatabase() {
    abstract fun userDao() : ReceiptDAO

    companion object {
        @Volatile
        private var INSTANCE: ReceiptRoomDatabase? = null

        fun getDatabase(context: Context): ReceiptRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ReceiptRoomDatabase::class.java,
                    "receipt_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}