# DataPipeline Examples
This repo contains [DataPipeline's Java code examples](https://northconcepts.com/docs/examples/).  These are the same examples you get when you download DataPipeline and requires a license to run.


## DataPipeline License

1. You can get your free trial license here: [https://northconcepts.com/contact/try/small-business/](https://northconcepts.com/contact/try/small-business/).
2. Once you receive your license by email, place the attached `NorthConcepts-DataPipeline.license` file from the email into the `src/main/resources` folder.
3. Start running the examples. 

## Setting up development environment
Developers on Windows need to
1. Have the HADOOP_HOME environment variable set
2. Add %HADOOP_HOME%\bin to PATH in my environment variable

See the following info:
- https://cwiki.apache.org/confluence/display/HADOOP2/WindowsProblems
- https://github.com/steveloughran/winutils
- https://github.com/steveloughran/winutils/tree/master/hadoop-3.0.0
- https://stackoverflow.com/questions/41851066/exception-in-thread-main-java-lang-unsatisfiedlinkerror-org-apache-hadoop-io

## Running Examples
This repo relies on the Gradle tool for build automation.  It also contains project files for the Eclipse IDE.

### Eclipse
Inside Eclipse, you can right-click any example and select `Run As` --> `Java Application`.

### Command Line / Gradle
You can also run examples with the following Gradle command.  Just replace `com.northconcepts.datapipeline.examples.cookbook.WriteACsvFileToFixedWidth` with the example you want to run.

    ./gradlew run --quiet -PclassToExecute="com.northconcepts.datapipeline.examples.cookbook.WriteACsvFileToFixedWidth"

## Data
Most examples read from the `example/data/input` folder and write to `example/data/output`.

## DataPipeline Resources
- [DataPipeline Home](https://northconcepts.com/)
- [Examples](https://northconcepts.com/docs/examples/)
- [User guide](https://northconcepts.com/docs/user-guide/)
- [Expression language](https://northconcepts.com/docs/expression-language/)
- [Javadocs](https://northconcepts.com/javadocs)
- [Code generator](https://northconcepts.com/tools/data-prep/)
