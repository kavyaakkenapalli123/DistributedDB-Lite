package com.distributeddb.cluster;

import com.distributeddb.model.NodeInfo;

import java.util.List;

public class ClusterManager {

    private final NodeInfo currentNode;
    private final List<NodeInfo> nodes;

    public ClusterManager(NodeInfo currentNode,
                          List<NodeInfo> nodes) {

        this.currentNode = currentNode;
        this.nodes = nodes;
    }

    public NodeInfo getCurrentNode() {
        return currentNode;
    }

    public List<NodeInfo> getNodes() {
        return nodes;
    }

    public void printClusterInfo() {

        System.out.println(
                "\n===== Cluster Info ====="
        );

        System.out.println(
                "Current Node: "
                        + currentNode.getId()
        );

        System.out.println(
                "Port: "
                        + currentNode.getPort()
        );

        System.out.println(
                "Cluster Size: "
                        + nodes.size()
        );

        for (NodeInfo node : nodes) {

            System.out.println(node);
        }

        System.out.println("========================\n");
    }
}