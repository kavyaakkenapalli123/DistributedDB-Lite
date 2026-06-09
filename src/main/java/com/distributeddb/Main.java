package com.distributeddb;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.cluster.ClusterManager;
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

        KeyValueStore store =
                new KeyValueStore();

        CommandProcessor processor =
                new CommandProcessor(store);

        /*
         * SERVER MODE
         */
        if ("server".equalsIgnoreCase(args[0])) {

            if (args.length < 2) {

                System.out.println(
                        "Please provide node id."
                );

                System.out.println(
                        "Example: java com.distributeddb.Main server 1"
                );

                return;
            }

            try {

                int nodeId =
                        Integer.parseInt(args[1]);

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

                /*
                 * Batch 5 Replication Setup
                 */
                ReplicationManager replicationManager =
                        new ReplicationManager(
                                nodes,
                                currentNode.getId()
                        );

                processor.setReplicationManager(
                        replicationManager
                );

                TcpServer server =
                        new TcpServer(
                                currentNode.getPort(),
                                processor
                        );

                server.start();

            } catch (NumberFormatException e) {

                System.out.println(
                        "Node ID must be a number."
                );

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

            System.out.println(
                    "Use 'server' or 'client'"
            );
        }
    }
}