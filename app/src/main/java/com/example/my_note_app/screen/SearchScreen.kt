package com.example.my_note_app.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.my_note_app.data.model.Note
import com.example.my_note_app.viewmodel.NoteViewModel
@Composable
fun SearchScreen(viewModel: NoteViewModel = viewModel(), navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0F0C0))
            .padding(20.dp)
            .padding(top = 24.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
        ) {
            Text("Назад на главный экран")
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 4
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Избранное", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
        }

        Button(
            onClick = { showResults = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            enabled = title.isNotBlank() || content.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
        ) {
            Text("Искать", color = Color.White)
        }

        val results by viewModel.search(title, content, isFavorite).collectAsState(initial = emptyList())

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(results) { note ->
                NoteItem(
                    note = note,
                    onEdit = { updatedNote ->
                        viewModel.updateNote(updatedNote)
                    },
                    onDelete = { viewModel.deleteNote(it) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onEdit: (Note) -> Unit = {},
    onDelete: (Note) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var isFavorite by remember { mutableStateOf(note.isFavorite) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(
                if (isEditing) Modifier else Modifier.clickable { isEditing = true }
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                // Режим редактирования
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Заголовок") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Текст") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Избранное")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            val updatedNote = note.copy(
                                title = title,
                                content = content,
                                isFavorite = isFavorite
                            )
                            onEdit(updatedNote)
                            isEditing = false
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            } else {
                // Режим просмотра
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = content, style = MaterialTheme.typography.bodyMedium)
                if (isFavorite) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "⭐ Избранная", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}