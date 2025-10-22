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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.my_note_app.data.model.Note
import com.example.my_note_app.viewmodel.NoteViewModel

@Composable
fun SearchScreen(viewModel: NoteViewModel = viewModel(), navController: NavController) {
    // Локальные состояния ввода
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }
    // Основной контейнер экрана
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp)
            .padding(top = 24.dp)
    )

    {

        // Заголовок экрана
        Text(
            text = "Поиск заметок",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )


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
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text("Искать", color = Color.White)
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Назад на главный экран")
        }


        val results by viewModel.search(title, content, isFavorite)
            .collectAsState(initial = emptyList())

        if (showResults && results.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()

            ) {
                items(results) { note ->
                    NoteItem(
                        note = note,
                        onEdit = { updatedNote ->
                            viewModel.updateNote(updatedNote)
                        },
                        onDelete = { viewModel.deleteNote(note) }
                    )
                }
            }
        }
        else if (showResults && results.isEmpty()) {
            // Подсказка при пустом результате — жирный красный текст
            Text(
                text = "Результатов не найдено",
                color = Color(0xFFE64A19),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

    }
}

@Composable
fun NoteItem(
    note: Note,
    onEdit: (Note) -> Unit = {},
    onDelete: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember(note) { mutableStateOf(note.title) }
    var content by remember(note) { mutableStateOf(note.content) }
    var isFavorite by remember(note) { mutableStateOf(note.isFavorite) }


    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isEditing) Modifier else Modifier.clickable { isEditing = true }
            )
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isEditing) {
                //  Режим редактирования
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
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("Избранное", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопки действий: Отмена и Сохранить
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            isEditing = false
                        }, // отмена редактирования — не сохраняем изменения
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            // Формируем обновлённую заметку и передаём через onEdit (логика прежняя)
                            val updatedNote = note.copy(
                                title = title,
                                content = content,
                                isFavorite = isFavorite
                            )
                            onEdit(updatedNote)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                    ) {
                        Text("Сохранить", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Иконка удаления — выполняет onDelete
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onDelete() }
                    )
                }


            } else {
                // 👁 Режим просмотра (оригинальный)
                Text(text = note.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                if (note.isFavorite) {
                    Text(text = "✅ Избранная", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}