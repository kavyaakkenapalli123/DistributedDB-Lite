package com.distributeddb.model;

public class NodeInfo {

    private final int id;
    private final String host;
    private final int port;

    public NodeInfo(int id,
                    String host,
                    int port) {

        this.id = id;
        this.host = host;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {

        return "NodeInfo{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}