package com.distributeddb.cluster;

public class VoteResponse {

    private final boolean granted;

    public VoteResponse(
            boolean granted) {

        this.granted = granted;
    }

    public boolean isGranted() {
        return granted;
    }
}