package com.distributeddb.recovery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SyncClient {

    public void requestSync(
            String host,
            int port) {

        try (

                Socket socket =
                        new Socket(
                                host,
                                port
                        );

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        );

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        )

        ) {

            writer.println(
                    "SYNC_REQUEST"
            );

            String response =
                    reader.readLine();

            System.out.println(
                    "Sync Response: "
                            + response
            );

        } catch (Exception e) {

            System.out.println(
                    "Synchronization failed."
            );
        }
    }
}