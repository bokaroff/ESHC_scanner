package com.example.eshc_scanner.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eshc_scanner.model.Items


@Database(entities = [Items::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun getItemRoomDao(): ItemRoomDao

    companion object {

        @Volatile
        private var database: ItemRoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ItemRoomDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    ItemRoomDatabase::class.java,
                    "ESHC_DB"
                ).build()
                database as ItemRoomDatabase
            } else database as ItemRoomDatabase
        }
    }
}

