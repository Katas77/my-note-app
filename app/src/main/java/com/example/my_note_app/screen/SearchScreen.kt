package com.example.my_note_app.screen

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.my_note_app.data.model.Note
import com.example.my_note_app.viewmodel.NoteViewModel

/**
  Экран для поиска заметок.
 */
@Composable
fun SearchScreen(viewModel: NoteViewModel = viewModel(), navController: NavController) {
    // Локальные состояния ввода
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }

    // Цвета/отступы вынесены в локальные константы для удобства изменения
    val screenPadding = 16.dp
    val cornerRadius = 12.dp
    val primaryBtnColor = Color(0xFF1E88E5)
    val secondaryBtnColor = Color(0xFF6200EE)

    // Основной контейнер экрана
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Фоновый цвет можно вынести в тему; оставил прежний
            .background(Color(0xFFE0F7FA))
            .padding(screenPadding)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // Заголовок экрана
        Text(
            text = "Поиск заметок",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Поля ввода: Заголовок и Текст
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius),
            maxLines = 4
        )

        // Фильтр "Избранное"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Избранное", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
        }

        // Кнопки действий: Искать и Назад
        Button(
            onClick = { showResults = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBtnColor)
        ) {
            Text("Искать", color = Color.White)
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = secondaryBtnColor)
        ) {
            Text("Назад на главный экран", color = Color.White)
        }

        // Подписываемся на поток результатов поиска из ViewModel
        val results by viewModel.search(title, content, isFavorite)
            .collectAsState(initial = emptyList())

        // Показываем список результатов, если поиск выполнен и есть элементы
        if (showResults && results.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(results) { note ->
                    NoteItem(
                        note = note,
                        onEdit = { updatedNote ->
                            // Передаём обновлённую заметку во ViewModel
                            viewModel.updateNote(updatedNote)
                        },
                        onDelete = { viewModel.deleteNote(note) }
                    )
                }
            }
        }

        // Подсказка при пустом результате: теперь центрированная и читабельная
        if (showResults && results.isEmpty()) {
            Text(
                text = "Результатов не найдено",
                color = Color(0xFFE64A19),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp // увеличенный размер — можно подогнать под тему
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

/**
 * Отдельный элемент списка заметки. Поддерживает режим редактирования и просмотра.
 * onEdit передаёт обновлённую заметку наверх, onDelete вызывает удаление.
 */
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

    // Карточка с анимацией изменения размеров при переключении режимов
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
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
                    modifier = Modifier.padding(top = 8.dp)
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
                            // Отмена — откат локальных изменений и выход из режима
                            title = note.title
                            content = note.content
                            isFavorite = note.isFavorite
                            isEditing = false
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            // Формируем обновлённую заметку и передаём через onEdit
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

                    // Иконка удаления — теперь в конце группы кнопок
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
                //  Режим просмотра
                Text(text = note.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = note.content, style = MaterialTheme.typography.bodyMedium)

                if (note.isFavorite) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Избранное",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Избранное", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
