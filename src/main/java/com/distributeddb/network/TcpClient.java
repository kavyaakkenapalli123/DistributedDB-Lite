package com.distributeddb.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {

    public void start(String host,
                      int port) {

        try (
                Socket socket =
                        new Socket(host, port);

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true)
        ) {

            Scanner scanner =
                    new Scanner(System.in);

            System.out.println(
                    "Connected To Database"
            );

            while (true) {

                System.out.print("> ");

                String command =
                        scanner.nextLine();

                if (command.equalsIgnoreCase("EXIT")) {
                    break;
                }

                writer.println(command);

                String response =
                        reader.readLine();

                System.out.println(response);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}