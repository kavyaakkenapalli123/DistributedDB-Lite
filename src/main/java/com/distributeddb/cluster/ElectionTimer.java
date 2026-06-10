package com.distributeddb.cluster;

public class ElectionTimer implements Runnable {

    private final HeartbeatManager heartbeatManager;
    private final ElectionManager electionManager;
    private final NodeState state;

    public ElectionTimer(
            HeartbeatManager heartbeatManager,
            ElectionManager electionManager,
            NodeState state) {

        this.heartbeatManager =
                heartbeatManager;

        this.electionManager =
                electionManager;

        this.state = state;
    }

    @Override
    public void run() {

        while (true) {

            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {

                Thread.currentThread()
                        .interrupt();
            }

            if (state.getRole() ==
                    NodeRole.LEADER) {

                continue;
            }

            if (!heartbeatManager
                    .leaderAlive()) {

                electionManager
                        .startElection();

                break;
            }
        }
    }
}