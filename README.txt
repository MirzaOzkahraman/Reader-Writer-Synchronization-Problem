
# Reader-Writer Synchronization Problem

This project provides a solution to the classic Reader-Writer problem in operating systems using Java. The implementation ensures proper synchronization between readers and writers accessing shared resources using semaphores.

## Problem Statement

The Reader-Writer problem arises when multiple readers and writers attempt to access shared data simultaneously. The objective is to ensure:

1. Writers have exclusive access to the shared data while writing.
2. Multiple readers can access the shared data simultaneously without any conflict.
3. Writers are blocked until all readers have completed reading.

## Solution Overview

This solution uses Java's `Semaphore` to synchronize access between readers and writers. The implementation includes the following key guarantees:

- **Writer Priority**: If a writer begins writing, no other writer or reader can access the shared data.
- **Reader Concurrency**: Multiple readers can access the shared data simultaneously, provided no writer is writing.
- **Shared Resource Management**: Proper locking and unlocking mechanisms ensure the safety and consistency of shared data.

### Key Classes

1. **`ReadWriteLock`**
   - Manages the synchronization logic.
   - Uses two semaphores:
     - `readerSemaphore`: Controls access to the reader count.
     - `writerSemaphore`: Ensures only one writer can write at a time.
   - Tracks the number of active readers.

2. **`Writer`**
   - Represents a writer thread.
   - Requests permission to write, performs writing, and releases the lock.

3. **`Reader`**
   - Represents a reader thread.
   - Requests permission to read, performs reading, and releases the lock.

4. **`Main`**
   - Simulates multiple readers and writers accessing shared data concurrently.

### Code Execution Flow

- The `Main` class initializes a thread pool and creates multiple reader and writer threads.
- Readers and writers compete for access to shared data.
- Synchronization is managed by `ReadWriteLock`, ensuring no race conditions.

## How It Works

1. **Readers**:
   - Acquire a read lock.
   - Increment the active reader count.
   - Block writers if there is at least one active reader.
   - Perform the read operation.
   - Release the lock and decrement the reader count.

2. **Writers**:
   - Acquire a write lock.
   - Block all other threads (readers and writers) while writing.
   - Perform the write operation.
   - Release the lock after writing.

## Usage

### Prerequisites

- Java Development Kit (JDK) installed.

### Steps to Run

1. Clone the repository.
2. Compile the program:
   ```bash
   javac Main.java
   ```
3. Run the program:
   ```bash
   java Main
   ```

### Example Output

The program demonstrates the interaction between multiple readers and writers. Example console output:

```text
Reader asks permission
Thread-1 is reading. Active Readers: 1
Writer requesting permission
Thread-2 is writing.
Thread-1 finished reading. Active Readers: 0
Thread-2 finished writing.
```

## Project Highlights

- Implements a robust synchronization mechanism using Java's `Semaphore`.
- Ensures fairness and avoids deadlocks.
- Provides a real-world application of multithreading in operating systems.
