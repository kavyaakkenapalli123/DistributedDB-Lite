package com.distributeddb.recovery;

import java.util.Map;

public class SyncResponse {

    private final Map<String, String> data;

    public SyncResponse(
            Map<String, String> data) {

        this.data = data;
    }

    public Map<String, String> getData() {

        return data;
    }
}