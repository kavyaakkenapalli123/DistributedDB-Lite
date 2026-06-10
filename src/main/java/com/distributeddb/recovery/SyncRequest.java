package com.distributeddb.recovery;

public class SyncRequest {

    private final int nodeId;

    public SyncRequest(
            int nodeId) {

        this.nodeId = nodeId;
    }

    public int getNodeId() {

        return nodeId;
    }
}