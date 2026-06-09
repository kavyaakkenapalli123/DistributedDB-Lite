package com.distributeddb.config;

import com.distributeddb.model.NodeInfo;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {

    public static List<NodeInfo> loadNodes() {

        List<NodeInfo> nodes =
                new ArrayList<>();

        try {

            Properties properties =
                    new Properties();

            properties.load(
                    new FileInputStream(
                            "cluster.properties"
                    )
            );

            for (int i = 1; i <= 3; i++) {

                String host =
                        properties.getProperty(
                                "node" + i + ".host"
                        );

                int port =
                        Integer.parseInt(
                                properties.getProperty(
                                        "node" + i + ".port"
                                )
                        );

                nodes.add(
                        new NodeInfo(
                                i,
                                host,
                                port
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return nodes;
    }
}