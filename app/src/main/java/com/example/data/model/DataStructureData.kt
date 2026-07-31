package com.example.data.model

object DataStructureData {

    val allDataStructures = listOf(
        // 1. Array / Dynamic Array
        DataStructure(
            id = "array_list",
            name = "Dynamic Array (ArrayList)",
            category = ComplexityCategory.LINEAR,
            tagLine = "Contiguous block of memory with dynamic resizing.",
            overview = "Dynamic Arrays back elements in contiguous memory. They allow instantaneous O(1) random index access via pointer arithmetic. When capacity is exceeded, resizing allocates a larger array (usually 1.5x or 2x size) and copies elements, yielding O(1) amortized insertion at tail.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_1, accessWorst = BigO.O_1,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_N, // O(n) on resize or insert at middle
                deleteBest = BigO.O_1, deleteAvg = BigO.O_N, deleteWorst = BigO.O_N, // shift elements
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Contiguous cache-friendly memory block. Minimal per-element overhead, but unused reserved capacity causes memory buffer slack.",
            pros = listOf(
                "O(1) instant random access by index",
                "High CPU cache locality due to sequential physical memory storage",
                "Amortized O(1) append operations at the tail"
            ),
            cons = listOf(
                "Expensive O(n) element shifts when inserting/deleting at start or middle",
                "O(n) resize penalty when buffer capacity is reached",
                "Wasted unused memory allocation buffer"
            ),
            useCases = listOf(
                "Random access lookup heavy workloads",
                "Sequences where size changes infrequently",
                "Backing buffers for CPU cache-efficient processing"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
// Dynamic Array in Kotlin
val list = mutableListOf<Int>()
list.add(10) // Amortized O(1)
list.add(20)
val element = list[0] // O(1) Access
list.removeAt(0) // O(n) shift
                """.trimIndent()),
                CodeSnippet("Java", """
ArrayList<Integer> list = new ArrayList<>();
list.add(10);
list.add(20);
int value = list.get(0); // O(1)
list.remove(0); // O(n)
                """.trimIndent()),
                CodeSnippet("Python", """
arr = []
arr.append(10) # Amortized O(1)
val = arr[0]   # O(1)
arr.pop(0)     # O(n)
                """.trimIndent()),
                CodeSnippet("C++", """
#include <vector>
std::vector<int> vec;
vec.push_back(10); // O(1) amortized
int val = vec[0];  // O(1)
vec.erase(vec.begin()); // O(n)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function addAtEnd(val):",
                "  if size == capacity:",
                "    resizeBuffer(capacity * 2) // O(n) copy",
                "  array[size] = val",
                "  size++",
                "function insertAt(index, val):",
                "  shiftRight(from = index, to = size)",
                "  array[index] = val",
                "  size++"
            ),
            defaultInteractiveType = VisualizerType.ARRAY
        ),

        // 2. Singly Linked List
        DataStructure(
            id = "linked_list",
            name = "Singly Linked List",
            category = ComplexityCategory.LINEAR,
            tagLine = "Nodes linked sequentially via explicit next pointer references.",
            overview = "A linear collection of nodes where each node stores a data value and a pointer reference to the next node in the sequence. Insertion and deletion at the head are instantaneous O(1) operations, but random access requires O(n) sequential traversal.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_N, accessWorst = BigO.O_N,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1, // Head insertion O(1)
                deleteBest = BigO.O_1, deleteAvg = BigO.O_N, deleteWorst = BigO.O_N,
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Fragmented non-contiguous heap allocation. Every node bears additional memory overhead for storing the next pointer.",
            pros = listOf(
                "Dynamic memory sizing with zero array reallocation penalty",
                "Instantaneous O(1) insertion and deletion at the head pointer",
                "No wasted pre-allocated buffer capacity"
            ),
            cons = listOf(
                "No random access: reaching element i requires traversing i nodes O(n)",
                "Poor CPU cache locality due to heap fragmentation",
                "Memory overhead for 64-bit next pointers on every node"
            ),
            useCases = listOf(
                "Implementing Stacks and Queues",
                "Undo buffers and symbol tables",
                "Hash Table bucket collision chaining"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
class Node<T>(val data: T, var next: Node<T>? = null)

class LinkedList<T> {
    var head: Node<T>? = null
    
    fun pushHead(value: T) { // O(1)
        val newNode = Node(value, head)
        head = newNode
    }
}
                """.trimIndent()),
                CodeSnippet("Java", """
class Node<T> {
    T data;
    Node<T> next;
    Node(T data) { this.data = data; }
}
                """.trimIndent()),
                CodeSnippet("Python", """
class Node:
    def __init__(self, val):
        self.val = val
        self.next = None
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function insertAtHead(value):",
                "  newNode = Node(value)",
                "  newNode.next = head",
                "  head = newNode",
                "function deleteNode(target):",
                "  prev = findPrevious(target) // O(n)",
                "  prev.next = target.next"
            ),
            defaultInteractiveType = VisualizerType.LINKED_LIST
        ),

        // 3. Doubly Linked List
        DataStructure(
            id = "doubly_linked_list",
            name = "Doubly Linked List",
            category = ComplexityCategory.LINEAR,
            tagLine = "Nodes with dual forward and backward pointer links.",
            overview = "Extends the singly linked list by equipping each node with both a 'next' and a 'prev' pointer. This permits bidirectional navigation and O(1) deletion of a node when given direct reference to that node.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_N, accessWorst = BigO.O_N,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1,
                deleteBest = BigO.O_1, deleteAvg = BigO.O_1, deleteWorst = BigO.O_1,
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Non-contiguous heap allocations. Highest pointer overhead per node (two 64-bit memory addresses for prev & next pointers).",
            pros = listOf(
                "Bidirectional traversal (forward and backward)",
                "Instant O(1) node removal given a node pointer (no need to scan for prev)",
                "Instant tail insertions and removals when paired with a tail pointer"
            ),
            cons = listOf(
                "Higher memory usage (stores two pointers per node)",
                "More pointer manipulation code overhead during insertions/deletions",
                "No cache-friendly contiguous block benefit"
            ),
            useCases = listOf(
                "Browser navigation history (Back and Forward buttons)",
                "LRU Cache implementation (paired with Hash Map)",
                "Deque (Double-ended queue) implementations"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
class DoubleNode<T>(
    var value: T,
    var prev: DoubleNode<T>? = null,
    var next: DoubleNode<T>? = null
)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function removeNode(node):",
                "  if node.prev != null:",
                "    node.prev.next = node.next",
                "  if node.next != null:",
                "    node.next.prev = node.prev"
            ),
            defaultInteractiveType = VisualizerType.LINKED_LIST
        ),

        // 4. Stack
        DataStructure(
            id = "stack",
            name = "Stack (LIFO)",
            category = ComplexityCategory.LINEAR,
            tagLine = "Last-In, First-Out collection restricted to top access.",
            overview = "A abstract data type adhering strictly to the LIFO rule. Elements are added (pushed) and removed (popped) exclusively from the top. Can be backed by a dynamic array or a singly linked list.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_N, accessWorst = BigO.O_N,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1, // Push
                deleteBest = BigO.O_1, deleteAvg = BigO.O_1, deleteWorst = BigO.O_1, // Pop
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Depends on backing storage. Array-backed yields O(1) amortized push and high cache locality. Linked-list backed offers strict O(1) push without resize pauses.",
            pros = listOf(
                "Strict enforce of LIFO ordering prevents state pollution",
                "O(1) time complexity for Push, Pop, and Peek operations",
                "Simple, lightweight linear memory structure"
            ),
            cons = listOf(
                "No random access to middle elements without popping top items",
                "Fixed size stack can overflow (StackOverflowError)"
            ),
            useCases = listOf(
                "Function call stack & recursion tracking",
                "Expression parsing and evaluation (Shunting-yard algorithm)",
                "Undo history in text editors",
                "DFS graph traversal"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
val stack = ArrayDeque<Int>()
stack.addLast(10) // Push O(1)
val top = stack.removeLast() // Pop O(1)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function push(item):",
                "  topIndex++",
                "  buffer[topIndex] = item",
                "function pop():",
                "  item = buffer[topIndex]",
                "  topIndex--",
                "  return item"
            ),
            defaultInteractiveType = VisualizerType.STACK_QUEUE
        ),

        // 5. Queue
        DataStructure(
            id = "queue",
            name = "Queue (FIFO)",
            category = ComplexityCategory.LINEAR,
            tagLine = "First-In, First-Out sequence processing elements in arrival order.",
            overview = "A FIFO structure where items enter at the rear (Enqueue) and leave from the front (Dequeue). Crucial for fair scheduling and ordered event handling.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_N, accessWorst = BigO.O_N,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1, // Enqueue
                deleteBest = BigO.O_1, deleteAvg = BigO.O_1, deleteWorst = BigO.O_1, // Dequeue
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Can be implemented via circular buffer array (cache friendly) or linked list pointers (dynamic expansion).",
            pros = listOf(
                "Guarantees fair FIFO arrival-order execution",
                "O(1) time complexity for Enqueue and Dequeue",
                "Prevents resource starvation in concurrent task queues"
            ),
            cons = listOf(
                "No direct random indexing into middle queued elements",
                "Array implementations require circular wrap-around arithmetic to avoid O(n) element shifts"
            ),
            useCases = listOf(
                "OS process CPU task scheduling",
                "BFS (Breadth-First Search) graph traversal",
                "Printer job queues and web server request handling"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
val queue = java.util.LinkedList<String>()
queue.add("Task 1") // Enqueue
val task = queue.poll() // Dequeue
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function enqueue(item):",
                "  buffer[rear] = item",
                "  rear = (rear + 1) % capacity",
                "function dequeue():",
                "  item = buffer[front]",
                "  front = (front + 1) % capacity",
                "  return item"
            ),
            defaultInteractiveType = VisualizerType.STACK_QUEUE
        ),

        // 6. Hash Table / Hash Map
        DataStructure(
            id = "hash_table",
            name = "Hash Table (HashMap)",
            category = ComplexityCategory.HASH_BASED,
            tagLine = "Key-value pair mapping powered by hash code functions.",
            overview = "Maps unique keys to array bucket indices using a deterministic Hash Function. Enables average O(1) search, insertion, and deletion. Collisions are handled via Chaining (linked list / tree in bucket) or Open Addressing (Linear Probing).",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_1, accessWorst = BigO.O_N, // Worst case O(n) on hash collision degradation
                searchBest = BigO.O_1, searchAvg = BigO.O_1, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_N,
                deleteBest = BigO.O_1, deleteAvg = BigO.O_1, deleteWorst = BigO.O_N,
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Array of bucket references pointing to dynamic lists (separate chaining) or contiguous open addressing slot array with load factor thresholds.",
            pros = listOf(
                "Average O(1) lightning fast lookups, insertions, and deletions",
                "Flexible key types (Strings, Custom Objects, Integers)",
                "Highly versatile core data structure across all programming languages"
            ),
            cons = listOf(
                "Worst-case O(n) performance degradation if poor hash function creates bucket collisions",
                "Unordered iteration: elements are not stored in sorted or insertion order",
                "High memory overhead due to load factor resizing reserves (e.g. 0.75 threshold)"
            ),
            useCases = listOf(
                "Database indexing and key-value stores (Redis, Memcached)",
                "Caching layers and frequency counting",
                "Duplicate detection and set representation"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
val map = hashMapOf<String, Int>()
map["Alice"] = 95 // Put O(1)
val score = map["Alice"] // Get O(1)
map.remove("Alice") // Delete O(1)
                """.trimIndent()),
                CodeSnippet("Python", """
d = {}
d["key"] = "val" # O(1)
val = d.get("key") # O(1)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function put(key, value):",
                "  hashIndex = hash(key) % numBuckets",
                "  bucket = buckets[hashIndex]",
                "  if key exists in bucket:",
                "    update(value)",
                "  else:",
                "    bucket.append(key, value)",
                "    if loadFactor > 0.75: rehash()"
            ),
            defaultInteractiveType = VisualizerType.HASH_TABLE
        ),

        // 7. Binary Search Tree (BST)
        DataStructure(
            id = "bst",
            name = "Binary Search Tree (BST)",
            category = ComplexityCategory.TREE_BASED,
            tagLine = "Hierarchical node tree maintaining left < parent < right order.",
            overview = "A binary tree where every node obeys the BST invariant: all keys in the left subtree are smaller than the node's key, and all keys in the right subtree are larger. Provides average O(log n) search and insertion.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_LOG_N, accessAvg = BigO.O_LOG_N, accessWorst = BigO.O_N, // Degenerates to linked list if unbalanced
                searchBest = BigO.O_1, searchAvg = BigO.O_LOG_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_LOG_N, insertAvg = BigO.O_LOG_N, insertWorst = BigO.O_N,
                deleteBest = BigO.O_LOG_N, deleteAvg = BigO.O_LOG_N, deleteWorst = BigO.O_N,
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Dynamic heap allocated nodes containing data, left child pointer, and right child pointer.",
            pros = listOf(
                "Maintains elements in sorted order dynamically",
                "In-order traversal yields elements in sorted sequence O(n)",
                "Efficient average O(log n) range queries and predecessor/successor searches"
            ),
            cons = listOf(
                "Unbalanced insertion order (e.g. sorted sequence 1, 2, 3, 4) degenerates tree into O(n) linked list",
                "Requires extra self-balancing algorithms (AVL, Red-Black) to guarantee log n worst-case"
            ),
            useCases = listOf(
                "Dynamic sorted datasets",
                "Abstract Syntax Trees (AST) in compilers",
                "Hierarchical decision trees"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
class TreeNode(
    var key: Int,
    var left: TreeNode? = null,
    var right: TreeNode? = null
)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function search(node, target):",
                "  if node == null or node.key == target:",
                "    return node",
                "  if target < node.key:",
                "    return search(node.left, target)",
                "  else:",
                "    return search(node.right, target)"
            ),
            defaultInteractiveType = VisualizerType.BINARY_SEARCH_TREE
        ),

        // 8. AVL Tree (Self-Balancing BST)
        DataStructure(
            id = "avl_tree",
            name = "AVL Tree (Balanced BST)",
            category = ComplexityCategory.TREE_BASED,
            tagLine = "Strictly height-balanced BST guaranteeing O(log n) operations.",
            overview = "Self-balancing BST that enforces a strict Balance Factor restriction: height difference between left and right subtrees of any node is at most 1. Rebalances via Single (LL, RR) or Double (LR, RL) rotations during insertion and deletion.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_LOG_N, accessAvg = BigO.O_LOG_N, accessWorst = BigO.O_LOG_N,
                searchBest = BigO.O_LOG_N, searchAvg = BigO.O_LOG_N, searchWorst = BigO.O_LOG_N,
                insertBest = BigO.O_LOG_N, insertAvg = BigO.O_LOG_N, insertWorst = BigO.O_LOG_N,
                deleteBest = BigO.O_LOG_N, deleteAvg = BigO.O_LOG_N, deleteWorst = BigO.O_LOG_N,
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Heap-allocated nodes storing left pointer, right pointer, key, and height metadata byte.",
            pros = listOf(
                "Strict height guarantee: Maximum height is 1.44 log₂ n",
                "Guaranteed O(log n) worst-case time complexity for search, insert, delete",
                "Faster lookup speed than Red-Black tree due to strict height balance"
            ),
            cons = listOf(
                "Frequent tree rotations during heavy write/insertion workloads",
                "Additional memory overhead per node to maintain height attribute"
            ),
            useCases = listOf(
                "Read-heavy database indexing where fast lookups are paramount",
                "Memory allocation managers requiring bounded search latency"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
class AVLNode(
    val key: Int,
    var height: Int = 1,
    var left: AVLNode? = null,
    var right: AVLNode? = null
)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function rebalance(node):",
                "  updateHeight(node)",
                "  balance = getBalanceFactor(node)",
                "  if balance > 1:",
                "    if getBalanceFactor(node.left) < 0: rotateLeft(node.left)",
                "    return rotateRight(node)"
            ),
            defaultInteractiveType = VisualizerType.BINARY_SEARCH_TREE
        ),

        // 9. Min/Max Heap (Binary Heap)
        DataStructure(
            id = "heap",
            name = "Binary Heap (Priority Queue)",
            category = ComplexityCategory.TREE_BASED,
            tagLine = "Complete binary tree stored efficiently in a contiguous array.",
            overview = "A complete binary tree satisfying the Heap Property: parent key is always smaller (Min-Heap) or larger (Max-Heap) than children. Top priority item is located at array index 0 in O(1) time. Percolate Up/Down rebalances heap in O(log n).",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_1, accessWorst = BigO.O_1, // Peek root element
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N,
                insertBest = BigO.O_1, insertAvg = BigO.O_LOG_N, insertWorst = BigO.O_LOG_N, // Sift Up
                deleteBest = BigO.O_LOG_N, deleteAvg = BigO.O_LOG_N, deleteWorst = BigO.O_LOG_N, // Extract Min/Max
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Flat contiguous array! Parent index i has children at 2i+1 and 2i+2. Zero pointer overhead.",
            pros = listOf(
                "Instantaneous O(1) top-priority element access",
                "Zero pointer memory overhead: packed densely into contiguous array",
                "Efficient O(n) array Heapify constructor"
            ),
            cons = listOf(
                "Searching for arbitrary elements requires O(n) full scan",
                "Does not support sorted order traversal efficiently"
            ),
            useCases = listOf(
                "Dijkstra's shortest path & Prim's MST graph algorithms",
                "Priority Queues in OS task schedulers",
                "HeapSort sorting algorithm O(n log n)"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
val minHeap = java.util.PriorityQueue<Int>()
minHeap.add(20) // Insert O(log n)
minHeap.add(5)
val top = minHeap.poll() // Extract min (5) O(log n)
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function insert(val):",
                "  array.append(val)",
                "  siftUp(array.size - 1)",
                "function extractMin():",
                "  min = array[0]",
                "  array[0] = array.popLast()",
                "  siftDown(0)",
                "  return min"
            ),
            defaultInteractiveType = VisualizerType.HEAP
        ),

        // 10. Trie (Prefix Tree)
        DataStructure(
            id = "trie",
            name = "Trie (Prefix Tree)",
            category = ComplexityCategory.SPECIALIZED,
            tagLine = "Tree data structure optimized for fast string prefix lookups.",
            overview = "A tree structure where nodes correspond to individual characters in string keys. All descendants of a node share a common prefix. Enables prefix searching in O(k) time where k is the length of the string, independent of total dataset size N.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_1, accessWorst = BigO.O_1, // O(k) key length
                searchBest = BigO.O_1, searchAvg = BigO.O_1, searchWorst = BigO.O_1, // O(k) length
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1, // O(k) length
                deleteBest = BigO.O_1, deleteAvg = BigO.O_1, deleteWorst = BigO.O_1, // O(k) length
                spaceWorst = BigO.O_N
            ),
            memoryLayout = "Nodes contain character child arrays (or HashMaps) pointing to next character nodes.",
            pros = listOf(
                "Search time depend solely on string key length k, NOT dataset size N",
                "Instant prefix matching (ideal for auto-complete & spell checkers)",
                "Shared prefix storage saves memory for common string prefixes"
            ),
            cons = listOf(
                "High node count pointer overhead if strings share few prefixes",
                "Sparse character child arrays can waste memory without compression"
            ),
            useCases = listOf(
                "Autocomplete search suggestion engines",
                "Spell checking and dictionary lookup",
                "IP routing table longest prefix matching"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
class TrieNode {
    val children = HashMap<Char, TrieNode>()
    var isWordEnd = false
}
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function insert(word):",
                "  curr = root",
                "  for char in word:",
                "    if char not in curr.children:",
                "      curr.children[char] = TrieNode()",
                "    curr = curr.children[char]",
                "  curr.isWordEnd = true"
            ),
            defaultInteractiveType = VisualizerType.GRAPH
        ),

        // 11. Graph (Adjacency List vs Matrix)
        DataStructure(
            id = "graph",
            name = "Graph (Adjacency List)",
            category = ComplexityCategory.GRAPH_BASED,
            tagLine = "Networks of vertices connected by directional or weighted edges.",
            overview = "Graphs model pairwise relationships between objects (Vertices V and Edges E). Adjacency Lists store neighbors per vertex in dynamic lists, optimizing memory for sparse graphs. Supports BFS and DFS traversal algorithms.",
            complexity = ComplexityProfile(
                accessBest = BigO.O_1, accessAvg = BigO.O_N, accessWorst = BigO.O_N,
                searchBest = BigO.O_1, searchAvg = BigO.O_N, searchWorst = BigO.O_N, // Search node O(V + E)
                insertBest = BigO.O_1, insertAvg = BigO.O_1, insertWorst = BigO.O_1, // Add vertex/edge O(1)
                deleteBest = BigO.O_1, deleteAvg = BigO.O_N, deleteWorst = BigO.O_N, // Remove edge
                spaceWorst = BigO.O_N // O(V + E)
            ),
            memoryLayout = "Array or HashMap of vertices, where each entry holds a linked/dynamic list of neighbor edges.",
            pros = listOf(
                "Optimal memory usage O(V + E) for real-world sparse graphs",
                "Instant neighbor iteration during graph traversal (BFS / DFS)",
                "Flexible edge weights and directed/undirected representation"
            ),
            cons = listOf(
                "Checking edge presence between arbitrary u and v requires searching u's list O(degree(u))",
                "Less cache locality than dense 2D Adjacency Matrices"
            ),
            useCases = listOf(
                "Social media friend networks & recommendation engines",
                "GPS navigation & shortest path routing (Google Maps)",
                "Dependency resolution graphs (Build systems, Maven, Gradle)"
            ),
            codeSnippets = listOf(
                CodeSnippet("Kotlin", """
val graph = mutableMapOf<Int, MutableList<Int>>()
fun addEdge(u: Int, v: Int) {
    graph.getOrPut(u) { mutableListOf() }.add(v)
    graph.getOrPut(v) { mutableListOf() }.add(u)
}
                """.trimIndent())
            ),
            pseudocode = listOf(
                "function BFS(startNode):",
                "  queue = [startNode]",
                "  visited.add(startNode)",
                "  while queue is not empty:",
                "    curr = queue.dequeue()",
                "    for neighbor in graph[curr]:",
                "      if neighbor not in visited:",
                "        visited.add(neighbor)",
                "        queue.enqueue(neighbor)"
            ),
            defaultInteractiveType = VisualizerType.GRAPH
        )
    )

    fun getById(id: String): DataStructure? {
        return allDataStructures.find { it.id == id }
    }
}
