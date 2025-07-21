package com.example.my_note_app.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.material3.Switch
import com.example.my_note_app.viewmodel.NoteViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.my_note_app.data.model.Note


@Composable
fun NoteScreen(viewModel: NoteViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    val notes by viewModel.notes.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Добавить заметку", fontWeight = FontWeight.Bold)

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Избранное")
            Switch(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it }
            )
        }

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.addNote(title, content, isFavorite)
                    title = ""
                    content = ""
                    isFavorite = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && content.isNotBlank()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Список заметок", fontWeight = FontWeight.Bold)

        LazyColumn {
            items(notes.size) { index ->
                val note = notes[index]
                NoteItem(
                    note = note,
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { /* Можно добавить детали */ }) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(note.title, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить",
                    modifier = Modifier.clickable { onDelete() }
                )
            }
            Text(note.content)
            if (note.isFavorite) {
                Text("⭐ Избранное")
            }
        }
    }
}
