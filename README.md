# FlowDroid_Jasmine
[FlowDroid](https://github.com/secure-software-engineering/FlowDroid), in concert with Jasmine, could support taint analysis for  Spring programs.

### 1. Pull the `FlowDroid_Jasmine` project and the benchmark to the local.

```sh
$ git clone https://github.com/dorawyy/FlowDroid_Jasmine.git # my clone
```

### 2. Open `FlowDroid_Jasmine` project in an IDE

### 3. Modify the relevant configuration under the `FlowDroid_Jasmine/dataleak` module

3.1 Modify `FlowDroid_Jasmine/dataleak/src/main/java/com/taint/analysis/utils/ServiceBenchmarksConfig.java` file to specify the benchmark application configuration, if the benchmark was not specified previously 

```java
// com.taint.analysis.utils.ServiceBenchmarksConfig
public class ServiceBenchmarksConfig {
    // set the base directory of where the application code to run FlowDroid_Jasmine on is  
    private static String basePath = "/data/yingying/mista/jasmineData/case-studies";
    ...
    // should also modify the process dir (where application code is ) for the benchmark app
    public static List<String> getSourceProcessDir(BenchmarkName benchmark) { ... }

    // should also modify the dependency dir for the benchmark app
    public static String getDependencyDir(BenchmarkName benchmark) { ... }
```

3.2 Modify `FlowDroid_Jasmine/dataleak/src/main/java/com/taint/analysis/config/Constant.java` to include the string benchmark name to enum `BenchmarkName` in the method `convertStringToBenchmarkName(String str)`

3.3 Configure files in `FlowDroid_Jasmine/dataleak/src/main/resources/` folder to specify the benchmark to run, main class of the benchmark app, and call graph algorithm to use
    * `dataleak.properties` 

3.4 (optional) Configure `FlowDroid_Jasmine/SourceAndSinks/SourcesAndSinks.txt` to specify sources and sinks to use for the taint analysis 

3.5 (optional) Configure `FlowDroid_Jasmine/soot-infoflow/EasyTaintWrapperSource.txt` to speicfy the taint wrapper details for the taint analysis 

### 4. Compile and run `FlowDroid_Jasmine` 

Compile `FlowDroid_Jasmine`: 
```bash
> cd FlowDroid_Jasmine
> mvn clean install -DskipTests
```

Run `FlowDroid_Jasmine` for taint analysis: 
```bash
> java -cp ./dataleak/libs/PointerAnalysis-1.0-SNAPSHOT.jar:./dataleak/target/dataleak-jar-with-dependencies.jar com.taint.analysis.Main
```

Taint analysis result will be produced to `FlowDroid_Jasmine/log` directory; the jimple files will be produced to `FlowDroid_Jasmine/sootOutput/` directory.
