package com.taint.analysis;

import analysis.CreateEdge;

import com.taint.analysis.config.Constant;
import com.taint.analysis.infoflow.IMyInfoFlow;
import com.taint.analysis.infoflow.MyInfoFlow;
import com.taint.analysis.result.ResultAggreator;
import com.taint.analysis.utils.CallGraphOption;
import com.taint.analysis.utils.ServiceBenchmarksConfig;
// import com.taint.analysis.utils.JasmineBenchmarksConfig;
// import com.taint.analysis.utils.JasmineEntryPointConfig;
import com.taint.analysis.utils.ServiceEntryPointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Main;
import soot.*;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.data.parsers.PermissionMethodParser;
import soot.jimple.infoflow.android.iccta.IccInstrumenter;
import soot.jimple.infoflow.android.source.AccessPathBasedSourceSinkManager;
import soot.jimple.infoflow.solver.memory.DefaultMemoryManagerFactory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.taintWrappers.ITaintWrapperDataFlowAnalysis;
import soot.options.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetUpApplication implements ITaintWrapperDataFlowAnalysis {
    private final Logger logger = LoggerFactory.getLogger(SetUpApplication.class);
    // The analysis results are taken from an external model that contains method summaries
    // (this can improve performance and help when the source is not available).
    private ITaintPropagationWrapper taintWrapper;
    // InfoflowAndroidConfiguration：Configuration class for Android specific data flow analysis
    private InfoflowAndroidConfiguration config;

    protected IccInstrumenter iccInstrumenter = null;

    @Override
    public void setTaintWrapper(ITaintPropagationWrapper taintWrapper) {
        this.taintWrapper = taintWrapper;
    }

    @Override
    public ITaintPropagationWrapper getTaintWrapper() {
        return taintWrapper;
    }

    public void runInfoflow(String sourceSinkFile) {
        try {
            // Read out the source and sink information and put it into pmp, which is a subclass of parser
            // If you add source and sink information, follow the format of the file and save to file or
            // set directly from the source program.
            ISourceSinkDefinitionProvider parser = PermissionMethodParser.fromFile(sourceSinkFile);
            config = new InfoflowAndroidConfiguration();

            // Create information flow (set config, taintWrapper, memory manager factory)
            IMyInfoFlow infoflow = createInfoflow();
            ResultAggreator resultAggregator = new ResultAggreator();
            infoflow.addResultsAvailableHandler(resultAggregator);


            initializeSoot();
            createMainMethod(); // Create a dummy main

            // The main part of the program
            // Perform taint analysis on the initialized call graph
            // AccessPathBasedSourceSinkManager：SourceSinkManager for Android applications. This class
            // uses precise source and sink definitions based on the access path.
            AccessPathBasedSourceSinkManager sourceSinkManager = new AccessPathBasedSourceSinkManager(
                    parser.getSources(), parser.getSinks(), Collections.emptySet(), config, null);
            infoflow.runAnalysis(sourceSinkManager, null);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("read file error%s", e.getStackTrace().toString());
        }
        PackManager.v().writeOutput(); // print out the modified jimple files modfiied by Jasmine
    }

    /**
     * create dummy main
     */
    private void createMainMethod() {
        if (Constant.CFG_OPTION.equals(CallGraphOption.CHA)) {
            String path = System.getProperty("user.dir") + File.separator + "dataleak/src/main/resources/config.properties"; // specify the file for beans
            logger.info(path);
            CreateEdge edge = new CreateEdge();
            edge.initCallGraph(path);
            logger.info("MainClass: " + edge.projectMainMethod.getDeclaringClass());
            Scene.v().setMainClass(edge.projectMainMethod.getDeclaringClass());
        } else {
            // ArrayList<SootMethod> entryPoints = JasmineEntryPointConfig.getEntryPoints(com.taint.analysis.Main.benchmark);
            ArrayList<SootMethod> entryPoints = ServiceEntryPointConfig.getEntryPoints(Constant.BENCHMARK_NAME);
            Scene.v().setEntryPoints(entryPoints);
        }
    }

    private IMyInfoFlow createInfoflow() {
        // Initialize and configure the data flow tracker
        IMyInfoFlow info = new MyInfoFlow();
        info.setConfig(config);
        info.setTaintWrapper(taintWrapper);

        // We use a specialized memory manager that knows about Android
        info.setMemoryManagerFactory(new DefaultMemoryManagerFactory());
        return info;
    }

    private void initializeSoot() {
        logger.info("Initializing Soot...");

        G.reset();
        // List<String> dir = JasmineBenchmarksConfig.getSourceProcessDir(com.taint.analysis.Main.benchmark);
        List<String> dir = ServiceBenchmarksConfig.getSourceProcessDir(Constant.BENCHMARK_NAME);

        if (Constant.CFG_OPTION.equals(CallGraphOption.SPARK) || Constant.CFG_OPTION.equals(CallGraphOption.JASMINE)) {
            // enable spark
            Options.v().setPhaseOption("cg.spark", "on");
            Options.v().setPhaseOption("cg.spark", "verbose:true");
            Options.v().setPhaseOption("cg.spark", "enabled:true");
            Options.v().setPhaseOption("cg.spark", "propagator:worklist");
            Options.v().setPhaseOption("cg.spark", "simple-edges-bidirectional:false");
            Options.v().setPhaseOption("cg.spark", "on-fly-cg:true");
            // Options.v().setPhaseOption("cg.spark", "pre-jimplify:true");
            Options.v().setPhaseOption("cg.spark", "double-set-old:hybrid");
            Options.v().setPhaseOption("cg.spark", "double-set-new:hybrid");
            Options.v().setPhaseOption("cg.spark", "set-impl:double");
            Options.v().setPhaseOption("cg.spark", "apponly:true");
            Options.v().setPhaseOption("cg.spark", "simple-edges-bidirectional:false");
            Options.v().set_verbose(true);
        } else {
            // use CHA
            Options.v().setPhaseOption("cg.cha", "on");
            Options.v().setPhaseOption("cg.cha", "enabled:true");
            Options.v().setPhaseOption("cg.cha", "verbose:true");
            Options.v().setPhaseOption("cg.cha", "apponly:true");
            Options.v().set_verbose(true);
        }

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_whole_program(true);
        Options.v().set_process_dir(dir);
        Options.v().set_src_prec(Options.src_prec_apk_class_jimple);
        Options.v().set_keep_offset(false);
        Options.v().set_keep_line_number(true);
        Options.v().set_throw_analysis(Options.throw_analysis_dalvik);
        Options.v().set_ignore_resolution_errors(true);
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_soot_classpath(getSootClassPath());
        Options.v().set_output_jar(false);
        Main.v().autoSetOptions();
        configureCallgraph();

        Scene.v().loadNecessaryClasses();

        // Make sure that we have valid Jimple bodies
        PackManager.v().getPack("wjpp").apply();

    }

    private String getClasspath(String workDir) {
        String sootCp = Scene.v().getSootClassPath();
        String sootCps = "";
        try (Stream<Path> paths = Files.walk(Paths.get(workDir))) {
            sootCps = paths.filter(path -> path.toString().endsWith("jar"))
                    .map(jarFile -> jarFile.toString())
                    .collect(Collectors.joining(File.pathSeparator));
        } catch (IOException e) {
        }
        sootCp += File.pathSeparator + sootCps;
        return sootCp;
    }

    private static String getSootClassPath() {
        String userdir = System.getProperty("user.dir");
        String javaHome = System.getProperty("java.home");
        if (javaHome == null || javaHome.equals(""))
            throw new RuntimeException("Could not get property java.home!");
        String sootCp = javaHome + File.separator + "lib" + File.separator + "rt.jar";
        sootCp += File.pathSeparator + javaHome + File.separator + "lib" + File.separator + "jce.jar";

        // String dependencyDirectory = JasmineBenchmarksConfig.getDependencyDir(com.taint.analysis.Main.benchmark);
        String dependencyDirectory = ServiceBenchmarksConfig.getDependencyDir(Constant.BENCHMARK_NAME);

        File file = new File(dependencyDirectory);
        File[] fs = file.listFiles();
        for (File f : Objects.requireNonNull(fs)) {
            if (!f.isDirectory())
                sootCp += File.pathSeparator + dependencyDirectory + File.separator + f.getName();
        }

        return sootCp;
    }

    private void configureCallgraph() {
    }

}
