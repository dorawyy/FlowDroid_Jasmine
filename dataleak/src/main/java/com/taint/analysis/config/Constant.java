package com.taint.analysis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.taint.analysis.utils.BenchmarkName;
import com.taint.analysis.utils.CallGraphOption;

/**
 * Constant
 */
public class Constant {

    private static final Logger LOGGER = LoggerFactory.getLogger(Constant.class);

    public static String SOURCE_FILE_NAME;
    public static String MAIN_CLASS;
    public static String EDGE_CONFIG_PROPERTIES;
    public static BenchmarkName BENCHMARK_NAME;
    public static CallGraphOption CFG_OPTION;

    public static void loadConstant() throws IOException {
        // Creating instance to avoid static member methods
        Constant instance = new Constant();
        InputStream is = instance.getFileAsIOStream("dataleak.properties");
        instance.printFileContent(is);
    }

    private InputStream getFileAsIOStream(final String fileName) {
        InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    private void printFileContent(InputStream is) throws IOException {
        try {
            Properties properties = new Properties();
            properties.load(is);
            BENCHMARK_NAME = convertStringToBenchmarkName(properties.getProperty("benchmark"));
            CFG_OPTION = convertStringToCallGraphOption(properties.getProperty("cfg"));
            SOURCE_FILE_NAME = properties.getProperty("source");
            MAIN_CLASS = properties.getProperty("main_class");
            EDGE_CONFIG_PROPERTIES = properties.getProperty("edge_config");

            LOGGER.info("BENCHMARK_NAME: " + BENCHMARK_NAME);
            LOGGER.info("CFG_OPTION: " + CFG_OPTION);
            LOGGER.info("SOURCE_FILE_NAME: " + SOURCE_FILE_NAME);
            LOGGER.info("MAIN_CLASS: " + MAIN_CLASS);
            LOGGER.info("EDGE_CONFIG_PROPERTIES: " + EDGE_CONFIG_PROPERTIES);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BenchmarkName convertStringToBenchmarkName(String str) {
        if (str.toLowerCase().equals("piggymetrics-account-service")) {
            return BenchmarkName.PIGGYMETRICS_ACCOUNT_SERVICE;
        } else {
            return BenchmarkName.DEFAULT;
        }
    }

    private CallGraphOption convertStringToCallGraphOption(String str) {
        if (str.toLowerCase().equals("cha")) {
            return CallGraphOption.CHA;
        } else if (str.toLowerCase().equals("jasmine")) {
            return CallGraphOption.JASMINE;
        } else {
            return CallGraphOption.DEFAULT;
        }
    }
}
