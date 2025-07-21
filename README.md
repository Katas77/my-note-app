# 📝 Notes App на Kotlin + Jetpack Compose + Room

## Это простое Android-приложение для управления заметками. Позволяет:
- Добавлять заметки с заголовком и текстом
- Отмечать заметки как "Избранные"
- Удалять заметки
- Просматривать список заметок

---

## 📱 Технологии

- **Kotlin** — основной язык
- **Jetpack Compose** — для построения интерфейса
- **Room Database** — локальное хранение данных
- **ViewModel + Coroutines** — для управления состоянием
- **Flow** — реактивное обновление UI
## 🧱 Архитектура проекта

## com.example.notesapp
###│
###├── data/
###│ ├── model/Note.kt
###│ ├── dao/NoteDao.kt
###│ └── db/AppDatabase.kt
###│
###├── viewmodel/NoteViewModel.kt
###│
###└── ui/
###└── screen/NoteScreen.kt

## 🧩 Возможности

- Добавление заметок
- Отображение списка
- Удаление заметок
- Флаг "Избранное"
- Реактивное обновление интерфейса
- Хранение данных локально через Room

---

## 📋 Пример: Добавление заметки

```kotlin
fun addNote(title: String, content: String, isFavorite: Boolean = false) {
    val note = Note(title = title, content = content, isFavorite = isFavorite)
    viewModelScope.launch {
        noteDao.insert(note)
    }
}
```
✉ Почта для обратной связи:
<a href="">krp77@mail.ru</a>
