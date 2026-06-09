package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.model.Response;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final CommandProcessor processor;

    public ConnectionHandler(Socket socket,
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
                                        socket.getInputStream()));

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true)
        ) {

            String command;

            while ((command = reader.readLine()) != null) {

                Response response =
                        processor.process(command);

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