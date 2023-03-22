package com.example.sawit.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sawit.domain.local.database.AuthUser
import com.example.sawit.domain.local.database.dao.AuthUserDao

@Database(entities = [(AuthUser::class)], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userData(): AuthUserDao

    companion object{
        fun getAppDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, GlobalConfig.DB_NAME)
                .allowMainThreadQueries()
                .build()
    }
}