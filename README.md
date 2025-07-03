# Document Storage and Search Engine

A comprehensive document management system, implementing custom data structures and advanced search functionality. This project was developed over 6 progressive stages, demonstrating mastery of data structures, algorithms, and software engineering principles.

## Overview

This system provides efficient document storage, retrieval, and search capabilities with features including:
- In-memory and disk-based storage with automatic management
- Advanced search functionality (keyword, prefix, metadata)
- Complete undo support for all operations
- Memory management with LRU eviction
- JSON serialization for persistent storage

## Key Features

### 🔍 Advanced Search Capabilities
- **Keyword Search**: Find documents containing specific words with O(k) lookup time
- **Prefix Search**: Search for documents with words starting with given prefixes
- **Metadata Search**: Query documents based on custom key-value metadata
- **Combined Searches**: Complex queries combining keywords and metadata filters
- **Batch Operations**: Delete multiple documents matching search criteria

### 💾 Two-Tier Storage System
- **Automatic Memory Management**: Enforces document count and byte limits
- **LRU Eviction**: Least recently used documents automatically moved to disk
- **Seamless Transitions**: Documents move between RAM and disk transparently
- **JSON Persistence**: All documents serialized to disk using GSON library

### ↩️ Complete Undo Support
- **Global Undo**: Revert the last operation performed
- **Document-Specific Undo**: Undo the last operation on a specific document
- **Bulk Undo**: Handle operations affecting multiple documents
- **Functional Implementation**: Uses Java lambdas and command pattern

## Project Structure
```
├── stage1/          # Basic document store with HashMap
├── stage2/          # Custom HashTable implementation
├── stage3/          # Undo support with Stack
├── stage4/          # Trie-based search functionality
├── stage5/          # Memory management with MinHeap
└── stage6/          # Two-tier storage with BTree
```
### Data Structures Implemented

1. **HashTable** 
   - Custom implementation with separate chaining
   - Initial storage mechanism for documents
   - Array doubling for dynamic sizing
2. **Stack**
   - Powers the undo functionality
   - Stores command objects for operation reversal
   - Supports complex undo scenarios
3. **Trie**
   - Efficient prefix and keyword search
   - O(k) lookup time where k is key length
   - Supports sorted results by relevance
4. **MinHeap**
   - Tracks document usage for LRU eviction
   - Maintains documents sorted by last access time
   - Enables efficient memory management
5. **BTree**
   - Replaces HashTable in final implementation
   - Supports two-tier storage (RAM and disk)
   - Handles document persistence

## Technical Highlights

- **Language**: Java
- **Build System**: Maven
- **Testing**: Comprehensive JUnit test suite
- **Serialization**: GSON for JSON document persistence
- **Design Patterns**: Command pattern for undo functionality
- **Functional Programming**: Lambda expressions for undo operations

## Building and Testing

```bash
mvn clean install
mvn test
```

## Usage Example
```java
// Create document store
DocumentStore store = new DocumentStoreImpl();

// Set memory limits
store.setMaxDocumentCount(100);
store.setMaxDocumentBytes(10_000_000); // 10MB

// Add documents
store.put(new ByteArrayInputStream(text.getBytes()), uri, DocumentFormat.TXT);

// Search functionality
List<Document> results = store.search("keyword");
List<Document> prefixResults = store.searchByPrefix("pre");

// Undo operations
store.undo();              // Undo last operation
store.undo(specificURI);   // Undo last operation on specific document
```
