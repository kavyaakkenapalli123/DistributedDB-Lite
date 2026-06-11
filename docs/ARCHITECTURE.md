# DistributedDB-Lite Architecture

## Introduction

DistributedDB-Lite is a distributed key-value database implemented in Java to explore the fundamental principles of distributed systems, fault tolerance, replication, and consensus.

The project demonstrates how multiple database nodes cooperate to provide a consistent and fault-tolerant storage system. Rather than relying on external frameworks or distributed databases, all major components were implemented from scratch using Java sockets, multithreading, and persistent file storage.

The architecture is inspired by the Raft consensus algorithm and incorporates several of its core ideas, including leader election, heartbeats, majority voting, and majority commit.

---

# System Goals

The project was designed around the following goals:

1. Understand distributed database internals.
2. Explore consensus and leader election.
3. Implement network-based replication.
4. Maintain persistent state across node restarts.
5. Simulate failure detection and recovery.
6. Demonstrate majority-based consistency.

The system prioritizes clarity and educational value over production-level complexity.

---

# High-Level Architecture

The cluster consists of three database nodes.

```text
                    CLIENT
                       |
                       |
                       v

                +-------------+
                |   LEADER    |
                |   Node 1    |
                +-------------+

                  /         \
                 /           \

                v             v

       +-------------+   +-------------+
       | FOLLOWER    |   | FOLLOWER    |
       |   Node 2    |   |   Node 3    |
       +-------------+   +-------------+
```

The leader coordinates writes while followers maintain replicated copies of the database.

---

# Architectural Components

## Client Layer

The client provides an interactive interface for issuing commands.

Supported operations:

```text
SET <key> <value>
GET <key>
DELETE <key>
KEYS
STATUS
```

The client communicates with the leader using TCP sockets.

---

## Networking Layer

### TcpServer

Responsibilities:

* Accept incoming connections
* Create connection handlers
* Manage concurrent clients

Each node runs an independent TCP server.

### TcpClient

Responsibilities:

* Connect to cluster nodes
* Send commands
* Display responses

### ConnectionHandler

Responsibilities:

* Parse incoming requests
* Route commands
* Process replication requests
* Handle vote requests
* Process heartbeat messages
* Manage synchronization requests

ConnectionHandler acts as the protocol gateway for every node.

---

# Storage Layer

## KeyValueStore

The KeyValueStore is the in-memory database engine.

Implementation:

```java
ConcurrentHashMap<String,String>
```

Responsibilities:

* Store key-value pairs
* Read values
* Delete values
* Maintain thread-safe access

### Why ConcurrentHashMap?

Multiple threads may access the database simultaneously.

ConcurrentHashMap provides:

* Thread safety
* High concurrency
* Efficient reads and writes

without requiring coarse-grained synchronization.

---

## Persistence Layer

### PersistenceManager

PersistenceManager ensures data survives node restarts.

Each node maintains its own database file.

```text
data/
├── node1.db
├── node2.db
└── node3.db
```

File format:

```text
name=Kavya
city=Hyderabad
country=India
```

Responsibilities:

* Save database state
* Load database state
* Recover state after restart

---

# Replication Architecture

Replication follows a leader-driven model.

Only the leader accepts client writes.

Followers receive replicated operations.

## Replication Flow

Example:

```text
SET city Hyderabad
```

Processing sequence:

```text
Client
   |
   v
Leader
   |
Local Write
   |
Replication
   |
+-----------+
|           |
v           v
Node2     Node3
```

Followers execute:

```text
REPL SET city Hyderabad
```

which updates their local state.

---

# Majority Commit Protocol

A write is not considered successful until a majority of nodes acknowledge replication.

For a cluster of three nodes:

```text
Majority = 2
```

Commit decision:

```text
Acknowledgements >= Majority
```

### Successful Example

```text
Leader ACK
Node2 ACK
Node3 ACK

Total ACKs = 3

Commit Success
```

### Failure Example

```text
Leader ACK
Node2 DOWN
Node3 DOWN

Total ACKs = 1

Commit Failed
```

This prevents a leader from acknowledging writes that only exist locally.

---

# Heartbeat System

The leader periodically sends heartbeat messages.

Purpose:

* Detect failures
* Maintain leadership
* Prevent unnecessary elections

Heartbeat interval:

```text
2 Seconds
```

Heartbeat message:

```text
HEARTBEAT
```

Followers update:

```text
lastHeartbeatTime
```

whenever a heartbeat is received.

---

# Failure Detection

Followers continuously monitor:

```text
Current Time
-
Last Heartbeat Time
```

If the timeout threshold is exceeded:

```text
Leader Suspected Failed
```

An election is triggered.

---

# Leader Election

Leader election is inspired by Raft.

## States

A node can exist in one of three states:

```text
FOLLOWER
CANDIDATE
LEADER
```

### Follower

Default state.

Responsibilities:

* Receive heartbeats
* Process replication requests
* Participate in voting

### Candidate

Temporary state during elections.

Responsibilities:

* Request votes
* Attempt leadership acquisition

### Leader

Active coordinator.

Responsibilities:

* Accept writes
* Replicate data
* Send heartbeats

---

# Election Process

When heartbeat timeout occurs:

```text
Follower
    |
    v
Candidate
```

Candidate actions:

1. Increment term.
2. Vote for itself.
3. Request votes from peers.

Vote request:

```text
REQUEST_VOTE
```

Vote response:

```text
VOTE_GRANTED
```

If majority is achieved:

```text
Candidate
      |
      v
Leader
```

---

# Consensus Model

DistributedDB-Lite follows a simplified Raft-inspired consensus approach.

Implemented concepts:

* Leader election
* Majority voting
* Majority commit
* Heartbeat monitoring

Not implemented:

* Log replication
* Log compaction
* Snapshot installation
* Joint consensus
* Dynamic membership

The project intentionally focuses on core concepts rather than full Raft compliance.

---

# Recovery Framework

When a node rejoins the cluster:

```text
Node Restart
      |
      v
SYNC_REQUEST
      |
      v
Leader Response
      |
      v
Recovery Handshake
```

This mechanism forms the foundation for future state synchronization.

---

# Concurrency Model

Each incoming connection is processed independently.

Architecture:

```text
TcpServer
    |
Thread Pool
    |
ConnectionHandler
```

Benefits:

* Multiple clients simultaneously
* Parallel replication
* Concurrent cluster communication

Thread management is handled using:

```java
Executors.newCachedThreadPool()
```

---

# Design Tradeoffs

## Simplicity Over Completeness

The project intentionally avoids the full complexity of production distributed databases.

Implemented:

* Replication
* Elections
* Heartbeats
* Persistence

Deferred:

* Distributed logs
* Snapshots
* Dynamic reconfiguration

---

## Fixed Cluster Size

The cluster currently assumes:

```text
3 Nodes
```

Advantages:

* Simpler majority calculation
* Easier testing
* Reduced complexity

---

## File-Based Storage

Instead of using:

* RocksDB
* LevelDB
* PostgreSQL

the project uses plain text persistence.

Advantages:

* Transparency
* Simplicity
* Educational value

---

# Failure Scenarios

## Scenario 1: Leader Failure

```text
Node1 Crashes
```

Result:

```text
Heartbeat Timeout
      |
Election
      |
New Leader
```

Cluster continues operating.

---

## Scenario 2: Follower Failure

```text
Node2 Crashes
```

Result:

```text
Node1 + Node3
```

still form a majority.

Writes continue.

---

## Scenario 3: Majority Loss

```text
Node2 Down
Node3 Down
```

Result:

```text
Majority Unavailable
```

Writes fail.

This preserves consistency.

---

# Future Roadmap

Planned improvements:

1. Full Raft log replication
2. Snapshot installation
3. Log compaction
4. Automatic state synchronization
5. Dynamic cluster membership
6. REST API interface
7. Monitoring dashboard
8. Docker deployment
9. Kubernetes deployment
10. Distributed transaction support

---

# Technical Learning Outcomes

This project demonstrates practical understanding of:

* Distributed systems
* Consensus algorithms
* Replication strategies
* Fault tolerance
* Network programming
* Concurrent programming
* Persistence mechanisms
* Leader election protocols
* Majority-based consistency models

The implementation provides a foundation for understanding how modern distributed databases coordinate state across multiple machines while maintaining consistency and availability.
