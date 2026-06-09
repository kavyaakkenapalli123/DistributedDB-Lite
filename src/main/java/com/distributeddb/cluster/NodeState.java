package com.distributeddb.cluster;

public class NodeState {

    private NodeRole role;
    private int currentTerm;
    private int leaderId;

    public NodeState() {

        role = NodeRole.FOLLOWER;
        currentTerm = 0;
        leaderId = -1;
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
}