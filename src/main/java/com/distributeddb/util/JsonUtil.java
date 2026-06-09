package com.distributeddb.util;

import java.util.Map;

public class JsonUtil {

    public static String toText(Map<String, String> map) {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry :
                map.entrySet()) {

            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }
}