package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ComplexityCategory
import com.example.data.model.DataStructure
import com.example.data.model.DataStructureData
import com.example.data.remote.GeminiService
import com.example.data.repository.DSRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "User" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DSRepository

    init {
        val dao = AppDatabase.getDatabase(application).dataDao()
        repository = DSRepository(dao)
    }

    // --- State Flow Properties ---
    val allDSList: List<DataStructure> = DataStructureData.allDataStructures

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ComplexityCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val bookmarkedIds = repository.bookmarkedIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredDSList: StateFlow<List<DataStructure>> = combine(
        searchQuery,
        selectedCategory
    ) { query, cat ->
        allDSList.filter { ds ->
            val matchesQuery = query.isBlank() || ds.name.contains(query, ignoreCase = true) ||
                    ds.tagLine.contains(query, ignoreCase = true) ||
                    ds.overview.contains(query, ignoreCase = true)
            val matchesCat = cat == null || ds.category == cat
            matchesQuery && matchesCat
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = allDSList
    )

    // --- Active Detail & Visualizer State ---
    private val _activeDS = MutableStateFlow<DataStructure>(allDSList[0])
    val activeDS = _activeDS.asStateFlow()

    // Interactive Visualizer State
    private val _visualizerStep = MutableStateFlow(0)
    val visualizerStep = _visualizerStep.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private var autoPlayJob: Job? = null

    // Sample data buffers for visualizers
    val arrayDataState = MutableStateFlow(listOf(15, 28, 42, 67, 89, 94))
    val linkedListDataState = MutableStateFlow(listOf(10, 20, 30, 40, 50))
    val stackDataState = MutableStateFlow(listOf(5, 12, 19, 27))
    val queueDataState = MutableStateFlow(listOf(101, 102, 103, 104))
    val hashTableDataState = MutableStateFlow(
        listOf(
            listOf("Alice" to 95),
            listOf("Bob" to 82, "Charlie" to 90),
            emptyList(),
            listOf("David" to 78),
            emptyList()
        )
    )
    val treeNodeDataState = MutableStateFlow(listOf(50, 30, 70, 20, 40, 60, 80))

    // --- Comparator Selection State ---
    private val _comparatorSelectedIds = MutableStateFlow(listOf("array_list", "linked_list", "hash_table"))
    val comparatorSelectedIds = _comparatorSelectedIds.asStateFlow()

    val comparatorSelectedDS: StateFlow<List<DataStructure>> = comparatorSelectedIds.map { ids ->
        ids.mapNotNull { id -> repository.getDsById(id) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(allDSList[0], allDSList[1], allDSList[5])
    )

    // --- AI Tutor Chat State ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("AI", "Hello! I am your CS Data Structures Tutor. Ask me anything about Big-O complexity, amortized runtime, memory layouts, or tree rotations!")
        )
    )
    val chatMessages = _chatMessages.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading = _isAILoading.asStateFlow()

    // --- Student Notes State ---
    fun getNoteForDs(dsId: String) = repository.getNote(dsId)

    fun saveNote(dsId: String, content: String) {
        viewModelScope.launch {
            repository.saveNote(dsId, content)
        }
    }

    // --- Actions ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: ComplexityCategory?) {
        _selectedCategory.value = category
    }

    fun setActiveDS(ds: DataStructure) {
        _activeDS.value = ds
        _visualizerStep.value = 0
        pauseAutoPlay()
    }

    fun toggleBookmark(dsId: String) {
        viewModelScope.launch {
            val isBookmarked = bookmarkedIds.value.contains(dsId)
            repository.toggleBookmark(dsId, isBookmarked)
        }
    }

    fun toggleComparatorDS(dsId: String) {
        val current = _comparatorSelectedIds.value.toMutableList()
        if (current.contains(dsId)) {
            if (current.size > 1) { // Keep at least 1
                current.remove(dsId)
            }
        } else {
            if (current.size < 3) { // Limit to 3 max side-by-side
                current.add(dsId)
            }
        }
        _comparatorSelectedIds.value = current
    }

    // Visualizer Controls
    fun nextVisualizerStep() {
        val maxSteps = _activeDS.value.pseudocode.size - 1
        if (_visualizerStep.value < maxSteps) {
            _visualizerStep.value += 1
        } else {
            _visualizerStep.value = 0
        }
    }

    fun prevVisualizerStep() {
        if (_visualizerStep.value > 0) {
            _visualizerStep.value -= 1
        }
    }

    fun toggleAutoPlay() {
        if (_isPlaying.value) {
            pauseAutoPlay()
        } else {
            startAutoPlay()
        }
    }

    private fun startAutoPlay() {
        _isPlaying.value = true
        autoPlayJob = viewModelScope.launch {
            while (_isPlaying.value) {
                delay(1200)
                nextVisualizerStep()
            }
        }
    }

    private fun pauseAutoPlay() {
        _isPlaying.value = false
        autoPlayJob?.cancel()
        autoPlayJob = null
    }

    // AI Tutor Question
    fun askAITutor(question: String) {
        if (question.isBlank()) return
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(ChatMessage("User", question))
        _chatMessages.value = currentList
        _isAILoading.value = true

        viewModelScope.launch {
            val response = GeminiService.askCSAI(question, contextDSName = _activeDS.value.name)
            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add(ChatMessage("AI", response))
            _chatMessages.value = updatedList
            _isAILoading.value = false
        }
    }
}
