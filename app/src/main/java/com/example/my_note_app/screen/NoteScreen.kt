package com.example.my_note_app.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.my_note_app.data.model.Note
import com.example.my_note_app.viewmodel.NoteViewModel

/**
 * Экран заметок: добавление, показ и удаление.
 */
@Composable
fun NoteScreen(viewModel: NoteViewModel = viewModel(), navController: NavController) {
    // Локальные состояния формы
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    // Подписка на список заметок из ViewModel
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    // Локальные константы оформления
    val bgColor = Color(0xFFE0F7FA)
    val cardCorner = 12.dp
    val actionBtnColor = Color(0xFF1E88E5)
    val navBtnColor = Color(0xFF6200EE)
    val outerPadding = 20.dp

    // Основной контейнер экрана
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(outerPadding)
            .padding(top = 16.dp)
    ) {
        // Заголовок раздела с небольшим акцентом
        Text(
            text = "Добавить заметку",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Поле для заголовка
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cardCorner)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле для текста заметки
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cardCorner),
            maxLines = 4
        )

        // Флаг "Избранное"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Избранное", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
        }

        // Кнопка сохранения заметки — активна только когда поля заполнены
        Button(
            onClick = {
                // Добавление заметки и сброс полей формы
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.addNote(title, content, isFavorite)
                    title = ""
                    content = ""
                    isFavorite = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            enabled = title.isNotBlank() && content.isNotBlank(),
            shape = RoundedCornerShape(cardCorner),
            colors = ButtonDefaults.buttonColors(containerColor = actionBtnColor)
        ) {
            Text("Сохранить", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка навигации к экрану поиска/редактирования
        Button(
            onClick = { navController.navigate("search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(cardCorner),
            colors = ButtonDefaults.buttonColors(containerColor = navBtnColor)
        ) {
            Text("Перейти к поиску и редактированию", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Заголовок списка заметок
        Text(
            text = "Список заметок",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Список заметок с отступами между элементами
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }
    }
}

/**
 * Компонент отдельной карточки заметки.
 * Поддерживает кликабельность и аккуратное обрезание текста.
 */
@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { /* Возможен переход на экран деталей */ }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )

                // Иконка удаления — небольшая область для клика
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDelete() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )

            if (note.isFavorite) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Избранное",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Избранное", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
