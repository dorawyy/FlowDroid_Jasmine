package com.taint.analysis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 * Constant
 */
public class Constant {

  // private static final String CONFIG_FILE_NAME = Constant.class.getClassLoader().getResource("config.json").getPath();
  public static String SOURCE_FILE_NAME;
  public static String MAIN_CLASS;
  public static String EDGE_CONFIG_PROPERTIES;

  public static void loadConstant() throws IOException {
    // Creating instance to avoid static member methods
    Constant instance = new Constant();
    InputStream is = instance.getFileAsIOStream("dataleak.properties");
    instance.printFileContent(is);
  }

  private InputStream getFileAsIOStream(final String fileName) {
    InputStream ioStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(fileName);

    if (ioStream == null) {
      throw new IllegalArgumentException(fileName + " is not found");
    }
    return ioStream;
  }

  private void printFileContent(InputStream is) throws IOException {
    try {
      Properties properties = new Properties();
      properties.load(is);
      SOURCE_FILE_NAME = properties.getProperty("source");
      MAIN_CLASS = properties.getProperty("main_class");
      EDGE_CONFIG_PROPERTIES = properties.getProperty("edge_config");
      System.out.println("SOURCE_FILE_NAME: " + SOURCE_FILE_NAME);
      System.out.println("MAIN_CLASS: " + MAIN_CLASS);
      System.out.println("EDGE_CONFIG_PROPERTIES: " + EDGE_CONFIG_PROPERTIES);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
