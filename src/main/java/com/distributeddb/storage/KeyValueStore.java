package com.distributeddb.storage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    private final Map<String, String> store;

    public KeyValueStore() {
        this.store = new ConcurrentHashMap<>();
    }

    public void set(String key, String value) {
        store.put(key, value);
    }

    public String get(String key) {
        return store.get(key);
    }

    public boolean delete(String key) {
        return store.remove(key) != null;
    }

    public Set<String> keys() {
        return store.keySet();
    }

    public int size() {
        return store.size();
    }
}