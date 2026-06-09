package com.distributeddb.replication;

import com.distributeddb.model.NodeInfo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ReplicationManager {

    private final List<NodeInfo> nodes;
    private final int currentNodeId;

    public ReplicationManager(
            List<NodeInfo> nodes,
            int currentNodeId) {

        this.nodes = nodes;
        this.currentNodeId = currentNodeId;
    }

    public void replicate(String command) {

        System.out.println(
                "Replicating command: " + command
        );

        for (NodeInfo node : nodes) {

            // Don't replicate to self
            if (node.getId() == currentNodeId) {
                continue;
            }

            try (
                    Socket socket =
                            new Socket(
                                    node.getHost(),
                                    node.getPort()
                            );

                    PrintWriter writer =
                            new PrintWriter(
                                    socket.getOutputStream(),
                                    true
                            )
            ) {

                writer.println(
                        "REPL " + command
                );

                System.out.println(
                        "Replicated to Node "
                                + node.getId()
                );

            } catch (Exception e) {

                System.out.println(
                        "Replication failed to Node "
                                + node.getId()
                );

            }
        }
    }
}