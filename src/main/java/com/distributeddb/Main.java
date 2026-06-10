package com.distributeddb;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.cluster.ClusterManager;
import com.distributeddb.cluster.HeartbeatSender;
import com.distributeddb.cluster.NodeRole;
import com.distributeddb.cluster.NodeState;
import com.distributeddb.config.ConfigLoader;
import com.distributeddb.model.NodeInfo;
import com.distributeddb.network.TcpClient;
import com.distributeddb.network.TcpServer;
import com.distributeddb.replication.ReplicationManager;
import com.distributeddb.storage.KeyValueStore;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {

            System.out.println("Usage:");
            System.out.println(
                    "Server Mode : java com.distributeddb.Main server <nodeId>"
            );
            System.out.println(
                    "Client Mode : java com.distributeddb.Main client"
            );

            return;
        }

        /*
         * SERVER MODE
         */
        if ("server".equalsIgnoreCase(args[0])) {

            if (args.length < 2) {

                System.out.println(
                        "Please provide node id."
                );

                return;
            }

            try {

                int nodeId =
                        Integer.parseInt(args[1]);

                KeyValueStore store =
                        new KeyValueStore(nodeId);

                CommandProcessor processor =
                        new CommandProcessor(store);

                List<NodeInfo> nodes =
                        ConfigLoader.loadNodes();

                if (nodeId < 1 ||
                        nodeId > nodes.size()) {

                    System.out.println(
                            "Invalid Node ID"
                    );

                    return;
                }

                NodeInfo currentNode =
                        nodes.get(nodeId - 1);

                ClusterManager clusterManager =
                        new ClusterManager(
                                currentNode,
                                nodes
                        );

                clusterManager.printClusterInfo();

                ReplicationManager replicationManager =
                        new ReplicationManager(
                                nodes,
                                currentNode.getId()
                        );

                processor.setReplicationManager(
                        replicationManager
                );

                NodeState state =
                        new NodeState();

                /*
                 * Temporary Leader
                 * Node1 acts as Leader
                 */
                if (nodeId == 1) {

                    state.setRole(
                            NodeRole.LEADER
                    );

                    Thread heartbeatThread =
                            new Thread(
                                    new HeartbeatSender(
                                            nodes,
                                            nodeId
                                    )
                            );

                    heartbeatThread.setDaemon(
                            true
                    );

                    heartbeatThread.start();

                    System.out.println(
                            "Leader Heartbeat Started"
                    );
                }

                TcpServer server =
                        new TcpServer(
                                currentNode.getPort(),
                                processor,
                                state
                        );

                server.start();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        /*
         * CLIENT MODE
         */
        else if (
                "client".equalsIgnoreCase(args[0])) {

            try {

                TcpClient client =
                        new TcpClient();

                client.start(
                        "localhost",
                        5001
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        else {

            System.out.println(
                    "Invalid mode."
            );
        }
    }
}