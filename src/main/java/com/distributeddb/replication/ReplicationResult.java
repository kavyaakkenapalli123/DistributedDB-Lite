package com.distributeddb.replication;

public class ReplicationResult {

    private final int acknowledgements;

    public ReplicationResult(
            int acknowledgements) {

        this.acknowledgements =
                acknowledgements;
    }

    public int getAcknowledgements() {

        return acknowledgements;
    }
}