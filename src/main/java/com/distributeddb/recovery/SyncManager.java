package com.distributeddb.recovery;

import com.distributeddb.storage.KeyValueStore;

import java.util.Map;

public class SyncManager {

    private final KeyValueStore store;

    public SyncManager(
            KeyValueStore store) {

        this.store = store;
    }

    public SyncResponse exportData() {

        Map<String, String> data =
                store.getAll();

        return new SyncResponse(
                data
        );
    }

    public void importData(
            SyncResponse response) {

        store.replaceAll(
                response.getData()
        );

        System.out.println(
                "Synchronization complete."
        );
    }
}