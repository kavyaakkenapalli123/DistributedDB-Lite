package com.distributeddb.cluster;

import com.distributeddb.model.NodeInfo;

import java.util.List;

public class ElectionManager {

    private final NodeState state;
    private final int nodeId;
    private final List<NodeInfo> nodes;

    public ElectionManager(
            NodeState state,
            int nodeId,
            List<NodeInfo> nodes) {

        this.state = state;
        this.nodeId = nodeId;
        this.nodes = nodes;
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

        VoteRequestSender sender =
                new VoteRequestSender(
                        nodes,
                        nodeId
                );

        int votes =
                sender.requestVotes();

        if (votes >
                nodes.size() / 2) {

            state.setRole(
                    NodeRole.LEADER
            );

            state.setLeaderId(
                    nodeId
            );

            System.out.println(
                    "Node "
                            + nodeId
                            + " became LEADER."
            );
        }
    }
}