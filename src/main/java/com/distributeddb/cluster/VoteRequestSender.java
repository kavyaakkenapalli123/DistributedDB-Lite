package com.distributeddb.cluster;

import com.distributeddb.model.NodeInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class VoteRequestSender {

    private final List<NodeInfo> nodes;
    private final int currentNodeId;

    public VoteRequestSender(
            List<NodeInfo> nodes,
            int currentNodeId) {

        this.nodes = nodes;
        this.currentNodeId = currentNodeId;
    }

    public int requestVotes() {

        int votes = 1;

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
                        "REQUEST_VOTE "
                                + currentNodeId
                );

                String response =
                        reader.readLine();

                if ("VOTE_GRANTED"
                        .equals(response)) {

                    votes++;

                    System.out.println(
                            "Vote granted by Node "
                                    + node.getId()
                    );
                }

            } catch (Exception ignored) {
            }
        }

        return votes;
    }
}