package com.distributeddb.replication;

import com.distributeddb.model.NodeInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        this.currentNodeId =
                currentNodeId;
    }

    public ReplicationResult replicate(
            String command) {

        int acknowledgements = 1;

        System.out.println(
                "Replicating command: "
                        + command
        );

        for (NodeInfo node : nodes) {

            if (node.getId() ==
                    currentNodeId) {

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
                            );

                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()
                                    )
                            )

            ) {

                writer.println(
                        "REPL " + command
                );

                String response =
                        reader.readLine();

                if ("REPLICATED"
                        .equals(response)) {

                    acknowledgements++;

                    System.out.println(
                            "Replicated to Node "
                                    + node.getId()
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Replication failed to Node "
                                + node.getId()
                );
            }
        }

        return new ReplicationResult(
                acknowledgements
        );
    }
}