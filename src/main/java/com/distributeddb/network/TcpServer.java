package com.distributeddb.network;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.cluster.NodeState;
import com.distributeddb.recovery.SyncManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    private final int port;
    private final CommandProcessor processor;
    private final NodeState state;
    private final SyncManager syncManager;

    public TcpServer(
            int port,
            CommandProcessor processor,
            NodeState state,
            SyncManager syncManager) {

        this.port = port;
        this.processor = processor;
        this.state = state;
        this.syncManager = syncManager;
    }

    public void start() {

        ExecutorService pool =
                Executors.newCachedThreadPool();

        try (

                ServerSocket serverSocket =
                        new ServerSocket(port)

        ) {

            System.out.println(
                    "Database Server Started on Port "
                            + port
            );

            while (true) {

                Socket socket =
                        serverSocket.accept();

                pool.submit(

                        new ConnectionHandler(
                                socket,
                                processor,
                                state,
                                syncManager
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}