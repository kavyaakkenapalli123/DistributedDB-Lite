package com.distributeddb.storage;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceManager {

    private final String fileName;

    public PersistenceManager(int nodeId) {

        this.fileName =
                "data/node"
                        + nodeId
                        + ".db";

        System.out.println(
                "Using file: "
                        + fileName
        );
    }

    public void save(
            Map<String, String> store) {

        try {

            File dir =
                    new File("data");

            if (!dir.exists()) {

                dir.mkdirs();
            }

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(
                                    fileName
                            )
                    );

            for (Map.Entry<String, String> entry :
                    store.entrySet()) {

                writer.write(
                        entry.getKey()
                                + "="
                                + entry.getValue()
                );

                writer.newLine();
            }

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Map<String, String> load() {

        Map<String, String> store =
                new ConcurrentHashMap<>();

        File file =
                new File(fileName);

        if (!file.exists()) {

            return store;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] parts =
                        line.split("=");

                if (parts.length == 2) {

                    store.put(
                            parts[0],
                            parts[1]
                    );
                }
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return store;
    }

    /*
     * Batch 9 Recovery Support
     */

    public String getFileName() {

        return fileName;
    }
}