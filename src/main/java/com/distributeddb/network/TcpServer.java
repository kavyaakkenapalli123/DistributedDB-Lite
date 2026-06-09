package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    private final int port;
    private final CommandProcessor processor;

    public TcpServer(int port,
                     CommandProcessor processor) {

        this.port = port;
        this.processor = processor;
    }

    public void start() {

        ExecutorService pool =
                Executors.newCachedThreadPool();

        try (ServerSocket serverSocket =
                     new ServerSocket(port)) {

            System.out.println(
                    "Database Server Started on Port "
                            + port
            );

            while (true) {

                Socket socket =
                        serverSocket.accept();

                System.out.println(
                        "Client Connected: "
                                + socket.getInetAddress()
                );

                pool.submit(
                        new ConnectionHandler(
                                socket,
                                processor
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}