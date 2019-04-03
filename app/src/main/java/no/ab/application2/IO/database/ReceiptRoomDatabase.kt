package no.ab.application2.IO.database

import android.arch.persistence.room.*
import android.content.Context

@Database(entities = [ReceiptEntity::class],version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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