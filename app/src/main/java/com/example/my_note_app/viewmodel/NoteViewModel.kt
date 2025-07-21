package com.example.my_note_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_note_app.data.dao.NoteDao
import com.example.my_note_app.data.model.Note
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {
    val notes = noteDao.getAll()

    fun addNote(title: String, content: String, isFavorite: Boolean = false) {
        val note = Note(title = title, content = content, isFavorite = isFavorite)
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
}