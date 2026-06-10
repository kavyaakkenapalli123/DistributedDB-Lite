package com.distributeddb.cluster;

public class NodeState {

    private NodeRole role;
    private int currentTerm;
    private int leaderId;

    private int votedFor;

    private volatile long lastHeartbeatTime;

    public NodeState() {

        role = NodeRole.FOLLOWER;
        currentTerm = 0;
        leaderId = -1;

        votedFor = -1;

        lastHeartbeatTime =
                System.currentTimeMillis();
    }

    public NodeRole getRole() {
        return role;
    }

    public void setRole(NodeRole role) {
        this.role = role;
    }

    public int getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(int currentTerm) {
        this.currentTerm = currentTerm;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public int getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(int votedFor) {
        this.votedFor = votedFor;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void updateHeartbeat() {

        lastHeartbeatTime =
                System.currentTimeMillis();
    }
}