package com.taint.analysis.utils;

import java.util.ArrayList;
import java.util.List;

public class ServiceBenchmarksConfig {
    private static String basePath = "/data/yingying/mista/jasmineData/case-studies";
    
    public static List<String> getSourceProcessDir(BenchmarkName benchmark) {
        List<String> dir = new ArrayList<>();
        switch (benchmark) {
            case PIGGYMETRICS_ACCOUNT_SERVICE:
                dir.add(basePath + "/piggymetrics/account-service/target/account-service/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_AUTH_SERVICE:
                dir.add(basePath + "/piggymetrics/account-service/target/auth-service/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_STATISTICS_SERVICE:
                dir.add(basePath + "/piggymetrics/account-service/target/statistics-service/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_NOTIFICATION_SERVICE:
                dir.add(basePath + "/piggymetrics/account-service/target/notification-service/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_CONFIG:
                dir.add(basePath + "/piggymetrics/account-service/target/config/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_MONITORING:
                dir.add(basePath + "/piggymetrics/monitoring/target/config/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_REGISTRY:
                dir.add(basePath + "/piggymetrics/registry/target/config/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_GATEWAY:
                dir.add(basePath + "/piggymetrics/gateway/target/config/BOOT-INF/classes");
                return dir;
            case PIGGYMETRICS_TURBINE_STREAM_SERVICE:
                dir.add(basePath + "/piggymetrics/turbine-stream-service/target/config/BOOT-INF/classes");
                return dir;
            default:
                return null;
        }
    }

    public static String getDependencyDir(BenchmarkName benchmark) {
        switch (benchmark) {
            case PIGGYMETRICS_ACCOUNT_SERVICE:
                return basePath + "/piggymetrics/account-service/target/account-service/BOOT-INF/lib";
            case PIGGYMETRICS_AUTH_SERVICE:
                return basePath + "/piggymetrics/auth-service/target/auth-service/BOOT-INF/lib";
            case PIGGYMETRICS_STATISTICS_SERVICE:
                return basePath + "/piggymetrics/statistics-service/target/statistics-service/BOOT-INF/lib";
            case PIGGYMETRICS_NOTIFICATION_SERVICE:
                return basePath + "/piggymetrics/notification-service/target/notification-service/BOOT-INF/lib";
            case PIGGYMETRICS_CONFIG:
                return basePath + "/piggymetrics/config/target/config/BOOT-INF/lib";
            case PIGGYMETRICS_MONITORING:
                return basePath + "/piggymetrics/monitoring/target/monitoring/BOOT-INF/lib";
            case PIGGYMETRICS_REGISTRY:
                return basePath + "/piggymetrics/registry/target/registry/BOOT-INF/lib";
            case PIGGYMETRICS_GATEWAY:
                return basePath + "/piggymetrics/gateway/target/gateway/BOOT-INF/lib";
            case PIGGYMETRICS_TURBINE_STREAM_SERVICE:
                return basePath + "/piggymetrics/turbine-stream-service/target/turbine-stream-service/BOOT-INF/lib";
            default:
                return "";
        }
    }
}
