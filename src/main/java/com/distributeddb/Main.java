package com.distributeddb;

import com.distributeddb.api.CommandProcessor;
import com.distributeddb.model.Response;
import com.distributeddb.storage.KeyValueStore;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        KeyValueStore store =
                new KeyValueStore();

        CommandProcessor processor =
                new CommandProcessor(store);

        Scanner scanner =
                new Scanner(System.in);

        System.out.println(
                "DistributedDB Lite Started");
        System.out.println(
                "Commands: SET GET DELETE KEYS STATUS");
        System.out.println(
                "Type EXIT to quit");

        while (true) {

            System.out.print("> ");

            String command =
                    scanner.nextLine();

            if (command.equalsIgnoreCase("EXIT")) {
                break;
            }

            Response response =
                    processor.process(command);

            System.out.println(
                    response.getMessage()
            );
        }

        scanner.close();

        System.out.println(
                "Database Shutdown"
        );
    }
}