package com.distributeddb.storage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    private final Map<String, String> store;

    private final PersistenceManager persistence;

    public KeyValueStore() {

        persistence =
                new PersistenceManager();

        store =
                new ConcurrentHashMap<>(
                        persistence.load()
                );
    }

    public void set(
            String key,
            String value) {

        store.put(key, value);

        persistence.save(store);
    }

    public String get(
            String key) {

        return store.get(key);
    }

    public boolean delete(
            String key) {

        boolean removed =
                store.remove(key) != null;

        persistence.save(store);

        return removed;
    }

    public Set<String> keys() {

        return store.keySet();
    }

    public int size() {

        return store.size();
    }
}