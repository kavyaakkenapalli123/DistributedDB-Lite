package com.distributeddb.cluster;

public class ElectionManager {

    private final NodeState state;
    private final int nodeId;

    public ElectionManager(
            NodeState state,
            int nodeId) {

        this.state = state;
        this.nodeId = nodeId;
    }

    public void startElection() {

        state.setRole(
                NodeRole.CANDIDATE
        );

        state.setCurrentTerm(
                state.getCurrentTerm() + 1
        );

        state.setVotedFor(
                nodeId
        );

        System.out.println(
                "Node "
                        + nodeId
                        + " started election."
        );
    }
}