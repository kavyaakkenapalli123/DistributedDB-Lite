package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.cluster.HeartbeatReceiver;
import com.distributeddb.cluster.NodeState;
import com.distributeddb.model.Response;
import com.distributeddb.recovery.SyncManager;
import com.distributeddb.recovery.SyncResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final CommandProcessor processor;
    private final HeartbeatReceiver heartbeatReceiver;
    private final NodeState state;
    private final SyncManager syncManager;

    public ConnectionHandler(
            Socket socket,
            CommandProcessor processor,
            NodeState state,
            SyncManager syncManager) {

        this.socket = socket;
        this.processor = processor;
        this.state = state;
        this.syncManager = syncManager;

        this.heartbeatReceiver =
                new HeartbeatReceiver(state);
    }

    @Override
    public void run() {

        try (

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        );

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        )

        ) {

            String command;

            while ((command = reader.readLine()) != null) {

                /*
                 * Sync Handling
                 */
                if ("SYNC_REQUEST"
                        .equals(command)) {

                    SyncResponse response =
                            syncManager.exportData();

                    writer.println(
                            "SYNC_OK "
                                    + response
                                    .getData()
                                    .size()
                                    + " entries"
                    );

                    continue;
                }

                /*
                 * Heartbeat Handling
                 */
                if ("HEARTBEAT".equals(command)) {

                    heartbeatReceiver
                            .receiveHeartbeat();

                    writer.println("OK");

                    continue;
                }

                /*
                 * Vote Request Handling
                 */
                if (command.startsWith(
                        "REQUEST_VOTE")) {

                    String[] parts =
                            command.split("\\s+");

                    int candidateId =
                            Integer.parseInt(parts[1]);

                    if (state.getVotedFor() == -1) {

                        state.setVotedFor(
                                candidateId
                        );

                        System.out.println(
                                "Voted for Node "
                                        + candidateId
                        );

                        writer.println(
                                "VOTE_GRANTED"
                        );

                    } else {

                        writer.println(
                                "VOTE_DENIED"
                        );
                    }

                    continue;
                }

                /*
                 * Replication Handling
                 */
                if (command.startsWith("REPL ")) {

                    String replicatedCommand =
                            command.substring(5);

                    System.out.println(
                            "Received replication: "
                                    + replicatedCommand
                    );

                    processor.processReplication(
                            replicatedCommand
                    );

                    writer.println(
                            "REPLICATED"
                    );

                    continue;
                }

                /*
                 * Normal Commands
                 */
                Response response =
                        processor.process(
                                command
                        );

                writer.println(
                        response.getMessage()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Client disconnected."
            );

        } finally {

            try {

                socket.close();

            } catch (IOException ignored) {
            }
        }
    }
}