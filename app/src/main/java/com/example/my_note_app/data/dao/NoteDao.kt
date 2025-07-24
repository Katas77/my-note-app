package com.example.my_note_app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.my_note_app.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): Flow<List<Note>>

    @Query("""
        SELECT *
        FROM notes
        WHERE (:title IS NULL OR title LIKE '%' || :title || '%')
          AND (:content IS NULL OR content LIKE '%' || :content || '%')
          AND (:isFavorite IS NULL OR isFavorite = :isFavorite)
    """)
    fun search(title: String?, content: String?, isFavorite: Boolean): Flow<List<Note>>



}