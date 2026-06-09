package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.model.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final CommandProcessor processor;

    public ConnectionHandler(
            Socket socket,
            CommandProcessor processor) {

        this.socket = socket;
        this.processor = processor;
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
                 * Replication command
                 *
                 * Example:
                 * REPL SET name Kavya
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
                 * Normal client command
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