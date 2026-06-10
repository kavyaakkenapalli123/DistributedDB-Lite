package com.distributeddb.replication;

public class CommitManager {

    private final int clusterSize;

    public CommitManager(
            int clusterSize) {

        this.clusterSize =
                clusterSize;
    }

    public boolean majorityReached(
            int acknowledgements) {

        int majority =
                (clusterSize / 2) + 1;

        return acknowledgements >=
                majority;
    }
}