package com.taint.analysis;

import com.taint.analysis.config.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    // public static String benchmark = "mall-admin";
    // public static String benchmark = "favorites-web";
    // public static String benchmark = "pybbs";
    public static String benchmark = "shopizer";
    // public static String benchmark = "SpringBlog";
    // public static String benchmark = "WebGoat";
    // public static String benchmark = "mall-search";
    // public static String benchmark = "mall-portal";
    // public static String benchmark = "FEBS-Cloud-auth";
    // public static String benchmark = "FEBS-Cloud-system";
    // public static String benchmark = "jeesite";
    // public static String benchmark = "FEBS-Shiro";
    // public static String benchmark = "ForestBlog";
    // public static String benchmark = "Jeecg-boot";
    // public static String benchmark = "My-Blog";
    // public static String benchmark = "Halo";
    // public static String benchmark = "ruoyi";
    public static String analysisAlgorithm = "cha";

    public static void main(String[] args) throws IOException {
        //Load the path information in source.json file.
        Constant.loadConstant();

        //SetUpApplication implements a common interface that supports all data flow analysis of taint wrappers.
        SetUpApplication application = new SetUpApplication();

        //Create and set up a taint wrapper

        File taintWrapperFile = new File(System.getProperty("user.dir") + File.separator + "soot-infoflow/EasyTaintWrapperSource.txt");
        application.setTaintWrapper((new EasyTaintWrapper(taintWrapperFile)));

        //The main part of the program
        application.runInfoflow(System.getProperty("user.dir") + File.separator + "SourceAndSinks/SourcesAndSinks-"+ benchmark +".txt");
    }
}
