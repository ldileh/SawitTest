package com.example.sawit.domain.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.sawit.domain.local.database.AuthUser

@Dao
interface AuthUserDao {

    @Insert
    fun insertUser(authUser: AuthUser)
}