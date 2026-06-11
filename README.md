# DistributedDB-Lite

A distributed key-value database built in Java implementing replication, persistence, heartbeat-based failure detection, leader election, majority voting, and majority commit mechanisms inspired by the Raft consensus algorithm.

---

## Overview

DistributedDB-Lite is a distributed systems project designed to demonstrate how modern distributed databases coordinate multiple nodes to maintain consistency and availability.

The system implements a leader-follower architecture where a leader node coordinates writes, replicates data to followers, monitors cluster health through heartbeats, and performs leader election when failures occur.

The project was built to gain hands-on experience with distributed systems concepts that power technologies such as Apache Kafka, etcd, Consul, CockroachDB, and other consensus-based distributed platforms.

---

## Why This Project Was Built

Modern applications rely heavily on distributed systems, yet many developers only interact with them at a high level.

This project was created to understand how distributed databases work internally by implementing:

* Data replication
* Persistent storage
* Leader election
* Heartbeat protocols
* Majority voting
* Majority commit
* Node recovery mechanisms

Instead of using existing frameworks, every component was implemented from scratch using Java sockets and multithreading to provide a deeper understanding of distributed database internals.

---

## Problem Statement

A traditional single-node database introduces a single point of failure.

```text
Application
     |
     v
 Database
```

If the database crashes, the entire application becomes unavailable.

DistributedDB-Lite addresses this problem by replicating data across multiple nodes.

```text
             Leader
               |
      +--------+--------+
      |                 |
      v                 v
  Follower         Follower
```

This architecture provides:

* Fault tolerance
* Data redundancy
* Automatic failover
* Improved reliability

---

## Key Features

### Distributed Key-Value Storage

Supports:

```text
SET <key> <value>
GET <key>
DELETE <key>
KEYS
STATUS
```

---

### Persistent Storage

Each node maintains its own persistent database file:

```text
data/
├── node1.db
├── node2.db
└── node3.db
```

Data survives process restarts.

---

### Data Replication

All write operations are replicated from the leader to follower nodes.

Example:

```text
SET name Kavya
```

Replication Flow:

```text
Leader
  |
  +------------+
  |            |
  v            v
Node2       Node3
```

---

### Heartbeat-Based Failure Detection

The leader continuously sends heartbeat messages to followers.

Purpose:

* Detect failures
* Maintain cluster membership
* Prevent unnecessary elections

---

### Leader Election

If the leader becomes unavailable:

```text
Leader Failure
      |
      v
Heartbeat Timeout
      |
      v
Candidate Election
      |
      v
Majority Voting
      |
      v
New Leader
```

The cluster automatically elects a new leader.

---

### Majority Voting

Leadership requires approval from a majority of nodes.

For a 3-node cluster:

```text
Majority = 2
```

A node becomes leader only after receiving at least two votes.

---

### Majority Commit

Writes are considered committed only after replication reaches a majority.

```text
Client
  |
  v
Leader
  |
Replication
  |
Majority ACKs
  |
Commit
```

This prevents acknowledging writes that exist on only one node.

---

### Recovery Framework

Recovering nodes can reconnect to the cluster and initiate synchronization procedures with the leader.

---

# System Architecture

## High-Level Architecture

```text
                    CLIENT
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

---

## Write Request Flow

```text
Client
   |
SET name Kavya
   |
   v
Leader
   |
Write Local State
   |
Replicate
   |
+-----------+
|           |
v           v
Node2     Node3

ACK       ACK
 \         /
  \       /
   Majority
      |
      v
   Commit
```

---

## Leader Election Flow

```text
Leader Failure
      |
      v

Heartbeat Timeout
      |
      v

Candidate State
      |
      v

REQUEST_VOTE
      |
      v

Majority Vote
      |
      v

Leader Promotion
```

---

# Project Structure

```text
DistributedDB-Lite
│
├── src/main/java/com/distributeddb
│
├── api
│   └── CommandProcessor
│
├── cluster
│   ├── NodeState
│   ├── NodeRole
│   ├── ElectionManager
│   ├── ElectionTimer
│   ├── HeartbeatManager
│   └── HeartbeatSender
│
├── config
│   └── ConfigLoader
│
├── model
│   ├── NodeInfo
│   └── Response
│
├── network
│   ├── TcpServer
│   ├── TcpClient
│   └── ConnectionHandler
│
├── replication
│   ├── ReplicationManager
│   ├── CommitManager
│   └── ReplicationResult
│
├── recovery
│   ├── SyncManager
│   ├── SyncRequest
│   ├── SyncResponse
│   └── SyncClient
│
├── storage
│   ├── KeyValueStore
│   └── PersistenceManager
│
└── Main.java
```

---

# Running the Project

## Compile

```bash
javac com\distributeddb\*.java ^
com\distributeddb\api\*.java ^
com\distributeddb\model\*.java ^
com\distributeddb\storage\*.java ^
com\distributeddb\network\*.java ^
com\distributeddb\util\*.java ^
com\distributeddb\cluster\*.java ^
com\distributeddb\config\*.java ^
com\distributeddb\replication\*.java ^
com\distributeddb\recovery\*.java
```

---

## Start Node 1

```bash
java -cp src\main\java com.distributeddb.Main server 1
```

## Start Node 2

```bash
java -cp src\main\java com.distributeddb.Main server 2
```

## Start Node 3

```bash
java -cp src\main\java com.distributeddb.Main server 3
```

## Start Client

```bash
java -cp src\main\java com.distributeddb.Main client
```

---

# Technologies Used

* Java
* TCP Sockets
* Multithreading
* ConcurrentHashMap
* File-Based Persistence
* Distributed Systems Concepts
* Consensus Algorithms (Raft-Inspired)

---

# Current Capabilities

* Distributed key-value storage
* Persistent storage
* Multi-node clustering
* Replication
* Heartbeats
* Leader election
* Majority voting
* Majority commit
* Recovery handshake framework

---

# Future Enhancements

* Full Raft Log Replication
* Snapshotting
* Log Compaction
* Dynamic Cluster Membership
* Automatic State Synchronization
* REST API Layer
* Docker Deployment
* Monitoring Dashboard

---

# Author

**Kavya Akkenapalli**

DistributedDB-Lite was developed as a practical exploration of distributed database internals, fault tolerance mechanisms, and consensus-driven system design.
