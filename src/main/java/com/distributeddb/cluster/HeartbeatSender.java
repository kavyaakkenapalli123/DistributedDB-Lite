package com.distributeddb.cluster;

import com.distributeddb.model.NodeInfo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class HeartbeatSender implements Runnable {

    private final List<NodeInfo> nodes;
    private final int currentNodeId;

    public HeartbeatSender(
            List<NodeInfo> nodes,
            int currentNodeId) {

        this.nodes = nodes;
        this.currentNodeId = currentNodeId;
    }

    @Override
    public void run() {

        while (true) {

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
                                )

                ) {

                    writer.println(
                            "HEARTBEAT"
                    );

                    System.out.println(
                            "Heartbeat sent to Node "
                                    + node.getId()
                    );

                } catch (Exception ignored) {
                }
            }

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {

                Thread.currentThread()
                        .interrupt();
            }
        }
    }
}