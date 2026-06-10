package com.distributeddb.cluster;

public class HeartbeatManager {

    private final NodeState state;

    public HeartbeatManager(
            NodeState state) {

        this.state = state;
    }

    public boolean leaderAlive() {

        long elapsed =
                System.currentTimeMillis()
                        - state.getLastHeartbeatTime();

        return elapsed < 5000;
    }
}