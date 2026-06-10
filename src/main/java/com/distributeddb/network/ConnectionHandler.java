package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.cluster.HeartbeatReceiver;
import com.distributeddb.cluster.NodeState;
import com.distributeddb.model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final CommandProcessor processor;

    private final HeartbeatReceiver heartbeatReceiver;

    public ConnectionHandler(
            Socket socket,
            CommandProcessor processor,
            NodeState state) {

        this.socket = socket;
        this.processor = processor;

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
                 * Heartbeat Handling
                 */
                if ("HEARTBEAT".equals(command)) {

                    heartbeatReceiver
                            .receiveHeartbeat();

                    writer.println("OK");

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
                 * Normal Client Command
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