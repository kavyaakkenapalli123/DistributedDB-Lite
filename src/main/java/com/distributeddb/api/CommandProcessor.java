package com.distributeddb.api;

import com.distributeddb.model.Response;
import com.distributeddb.replication.CommitManager;
import com.distributeddb.replication.ReplicationManager;
import com.distributeddb.replication.ReplicationResult;
import com.distributeddb.storage.KeyValueStore;

public class CommandProcessor {

    private final KeyValueStore store;

    private ReplicationManager replicationManager;

    public CommandProcessor(KeyValueStore store) {
        this.store = store;
    }

    public void setReplicationManager(
            ReplicationManager replicationManager) {

        this.replicationManager =
                replicationManager;
    }

    /*
     * Normal Client Commands
     */
    public Response process(String input) {

        if (input == null || input.isBlank()) {

            return new Response(
                    false,
                    "Empty Command"
            );
        }

        String[] parts =
                input.trim().split("\\s+");

        String command =
                parts[0].toUpperCase();

        try {

            switch (command) {

                case "SET":

                    if (parts.length < 3) {

                        return new Response(
                                false,
                                "Usage: SET <key> <value>"
                        );
                    }

                    store.set(
                            parts[1],
                            parts[2]
                    );

                    if (replicationManager != null) {

                        ReplicationResult result =
                                replicationManager
                                        .replicate(input);

                        CommitManager commitManager =
                                new CommitManager(3);

                        if (!commitManager
                                .majorityReached(
                                        result.getAcknowledgements()
                                )) {

                            return new Response(
                                    false,
                                    "WRITE FAILED"
                            );
                        }

                        System.out.println(
                                "Majority achieved."
                        );

                        System.out.println(
                                "COMMIT SUCCESS"
                        );
                    }

                    return new Response(
                            true,
                            "OK"
                    );

                case "GET":

                    if (parts.length < 2) {

                        return new Response(
                                false,
                                "Usage: GET <key>"
                        );
                    }

                    String value =
                            store.get(parts[1]);

                    if (value == null) {

                        return new Response(
                                false,
                                "Key Not Found"
                        );
                    }

                    return new Response(
                            true,
                            value
                    );

                case "DELETE":

                    if (parts.length < 2) {

                        return new Response(
                                false,
                                "Usage: DELETE <key>"
                        );
                    }

                    boolean deleted =
                            store.delete(parts[1]);

                    if (deleted &&
                            replicationManager != null) {

                        ReplicationResult result =
                                replicationManager
                                        .replicate(input);

                        CommitManager commitManager =
                                new CommitManager(3);

                        if (!commitManager
                                .majorityReached(
                                        result.getAcknowledgements()
                                )) {

                            return new Response(
                                    false,
                                    "DELETE FAILED"
                            );
                        }

                        System.out.println(
                                "Majority achieved."
                        );

                        System.out.println(
                                "DELETE COMMIT SUCCESS"
                        );
                    }

                    return new Response(
                            deleted,
                            deleted
                                    ? "Deleted"
                                    : "Key Not Found"
                    );

                case "KEYS":

                    return new Response(
                            true,
                            store.keys().toString()
                    );

                case "STATUS":

                    return new Response(
                            true,
                            "Keys Stored: "
                                    + store.size()
                    );

                default:

                    return new Response(
                            false,
                            "Unknown Command"
                    );
            }

        } catch (Exception e) {

            return new Response(
                    false,
                    "Error: "
                            + e.getMessage()
            );
        }
    }

    /*
     * Replicated Commands
     * No further replication
     */
    public Response processReplication(
            String input) {

        if (input == null ||
                input.isBlank()) {

            return new Response(
                    false,
                    "Empty Command"
            );
        }

        String[] parts =
                input.trim().split("\\s+");

        String command =
                parts[0].toUpperCase();

        try {

            switch (command) {

                case "SET":

                    if (parts.length < 3) {

                        return new Response(
                                false,
                                "Usage: SET <key> <value>"
                        );
                    }

                    store.set(
                            parts[1],
                            parts[2]
                    );

                    return new Response(
                            true,
                            "REPLICATED"
                    );

                case "DELETE":

                    if (parts.length < 2) {

                        return new Response(
                                false,
                                "Usage: DELETE <key>"
                        );
                    }

                    store.delete(
                            parts[1]
                    );

                    return new Response(
                            true,
                            "REPLICATED"
                    );

                default:

                    return new Response(
                            false,
                            "Unsupported Replication Command"
                    );
            }

        } catch (Exception e) {

            return new Response(
                    false,
                    "Error: "
                            + e.getMessage()
            );
        }
    }
}