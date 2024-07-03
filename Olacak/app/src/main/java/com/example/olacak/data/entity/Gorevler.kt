package com.example.olacak.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "Gorevler")
data class Gorevler(@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name="gorev_id") @NotNull var gorev_id:Long,
                    @ColumnInfo(name="gorev_adi") @NotNull var gorev_adi: String,
                    @ColumnInfo(name="gorev_aciklamasi") @NotNull var gorev_aciklamasi:String,
                    @ColumnInfo(name="user_id") @NotNull var user_id:Long): Serializable {
}






