package com.distributeddb.storage;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceManager {

    private static final String FILE_NAME =
            "data/database.txt";

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
                            new FileWriter(FILE_NAME)
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
                new File(FILE_NAME);

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
}