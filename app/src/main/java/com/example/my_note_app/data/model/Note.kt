package com.example.my_note_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title: String,
    val content:String,
    val isFavorite:Boolean
)
