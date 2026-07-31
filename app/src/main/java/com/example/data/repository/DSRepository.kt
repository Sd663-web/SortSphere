package com.example.data.repository

import com.example.data.local.BookmarkEntity
import com.example.data.local.DataDao
import com.example.data.local.NoteEntity
import com.example.data.local.QuizHistoryEntity
import com.example.data.model.DataStructure
import com.example.data.model.DataStructureData
import kotlinx.coroutines.flow.Flow

class DSRepository(private val dao: DataDao) {

    val allDataStructures: List<DataStructure> = DataStructureData.allDataStructures

    val bookmarkedIds: Flow<List<String>> = dao.getBookmarkedDsIds()
    val quizHistory: Flow<List<QuizHistoryEntity>> = dao.getQuizHistory()

    fun getDsById(id: String): DataStructure? {
        return DataStructureData.getById(id)
    }

    suspend fun toggleBookmark(dsId: String, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            dao.removeBookmark(dsId)
        } else {
            dao.addBookmark(BookmarkEntity(dsId = dsId))
        }
    }

    fun getNote(dsId: String): Flow<NoteEntity?> {
        return dao.getNoteForDs(dsId)
    }

    suspend fun saveNote(dsId: String, content: String) {
        dao.saveNote(NoteEntity(dsId = dsId, content = content))
    }

    suspend fun recordQuizScore(score: Int, total: Int, category: String) {
        dao.recordQuizScore(QuizHistoryEntity(score = score, totalQuestions = total, category = category))
    }
}
