package com.distributeddb.cluster;

public class HeartbeatReceiver {

    private final NodeState state;

    public HeartbeatReceiver(
            NodeState state) {

        this.state = state;
    }

    public void receiveHeartbeat() {

        state.updateHeartbeat();

        System.out.println(
                "Heartbeat received"
        );
    }
}