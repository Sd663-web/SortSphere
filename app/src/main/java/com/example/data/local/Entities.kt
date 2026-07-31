package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val dsId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ds_notes")
data class NoteEntity(
    @PrimaryKey val dsId: String,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_history")
data class QuizHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val totalQuestions: Int,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
