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

        System.out.println("DEBUG A");

        state.setRole(
                NodeRole.CANDIDATE
        );

        System.out.println("DEBUG B");

        state.setCurrentTerm(
                state.getCurrentTerm() + 1
        );

        System.out.println("DEBUG C");

        state.setVotedFor(
                nodeId
        );

        System.out.println(
                "Node "
                        + nodeId
                        + " started election."
        );

        System.out.println("DEBUG D");

        state.setRole(
                NodeRole.LEADER
        );

        System.out.println("DEBUG E");

        state.setLeaderId(
                nodeId
        );

        System.out.println("DEBUG F");

        System.out.println(
                "Node "
                        + nodeId
                        + " became LEADER."
        );

        System.out.println(
                "### LEADER PROMOTION COMPLETE ###"
        );
    }
}