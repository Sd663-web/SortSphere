package com.example.data.model

enum class ComplexityCategory(val label: String) {
    LINEAR("Linear Data Structures"),
    HASH_BASED("Hash-based Structures"),
    TREE_BASED("Tree-based Structures"),
    GRAPH_BASED("Graph-based Structures"),
    SPECIALIZED("Specialized & Advanced")
}

enum class BigO(
    val notation: String,
    val description: String,
    val rating: Int // 1 = Excellent (Green), 5 = Bad (Red)
) {
    O_1("O(1)", "Constant Time", 1),
    O_LOG_N("O(log n)", "Logarithmic Time", 2),
    O_N("O(n)", "Linear Time", 3),
    O_N_LOG_N("O(n log n)", "Linearithmic Time", 4),
    O_N_SQUARED("O(n²)", "Quadratic Time", 5),
    O_2_N("O(2ⁿ)", "Exponential Time", 5)
}

data class ComplexityProfile(
    val accessBest: BigO,
    val accessAvg: BigO,
    val accessWorst: BigO,
    val searchBest: BigO,
    val searchAvg: BigO,
    val searchWorst: BigO,
    val insertBest: BigO,
    val insertAvg: BigO,
    val insertWorst: BigO,
    val deleteBest: BigO,
    val deleteAvg: BigO,
    val deleteWorst: BigO,
    val spaceWorst: BigO
)

data class CodeSnippet(
    val language: String,
    val code: String
)

data class OperationStep(
    val stepIndex: Int,
    val title: String,
    val description: String,
    val highlightedLine: Int,
    val stateSnapshot: String // JSON or string encoding of visualizer state
)

data class DataStructure(
    val id: String,
    val name: String,
    val category: ComplexityCategory,
    val tagLine: String,
    val overview: String,
    val complexity: ComplexityProfile,
    val memoryLayout: String, // Contiguous, Non-contiguous, Hybrid, Node-pointer overhead
    val pros: List<String>,
    val cons: List<String>,
    val useCases: List<String>,
    val codeSnippets: List<CodeSnippet>,
    val pseudocode: List<String>,
    val defaultInteractiveType: VisualizerType
)

enum class VisualizerType {
    ARRAY,
    LINKED_LIST,
    STACK_QUEUE,
    HASH_TABLE,
    BINARY_SEARCH_TREE,
    HEAP,
    GRAPH
}
