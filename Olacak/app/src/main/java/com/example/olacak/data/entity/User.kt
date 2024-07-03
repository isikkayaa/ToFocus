package com.example.olacak.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "users")
data class User(@PrimaryKey(autoGenerate = true)
                @ColumnInfo(name = "userId") @NotNull var userId: Long=0,
                @ColumnInfo(name = "email") @NotNull var email: String,
                @ColumnInfo(name = "passwordHash") @NotNull var passwordHash: String) : Serializable {

}

