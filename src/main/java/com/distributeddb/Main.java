package com.distributeddb;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.network.TcpClient;
import com.distributeddb.network.TcpServer;
import com.distributeddb.storage.KeyValueStore;
import com.distributeddb.util.Constants;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {

            System.out.println(
                    "Usage:"
            );

            System.out.println(
                    "server"
            );

            System.out.println(
                    "client"
            );

            return;
        }

        KeyValueStore store =
                new KeyValueStore();

        CommandProcessor processor =
                new CommandProcessor(store);

        if ("server".equalsIgnoreCase(args[0])) {

            TcpServer server =
                    new TcpServer(
                            Constants.SERVER_PORT,
                            processor
                    );

            server.start();

        } else if (
                "client".equalsIgnoreCase(args[0])) {

            TcpClient client =
                    new TcpClient();

            client.start(
                    "localhost",
                    Constants.SERVER_PORT
            );
        }
    }
}