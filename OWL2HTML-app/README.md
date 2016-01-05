OWL2HTML 
=======

Java command line application
-----------

This project is for compiling and packaging the owl2html generation as a java command line app.

The idea is that when developing ontologies, it is easy to generate documentation by running a commandline in a terminal window.
The documentation should be understandable by someone without ontology knowledge.

No options, generates help:

    java -jar target/OWL2HTML-app-1.0-SNAPSHOT-jar-with-dependencies.jar

Simple example:

    java -jar target/OWL2HTML-app-1.0-SNAPSHOT-jar-with-dependencies.jar -generation offline -sourcefile "src/main/resources/owl/calim.owl" -inputdirectory "src/main/resources/input/" -outputdirectory "target/testOutput/outputCalim/" -themesdirectory "src/main/resources/themes/" -theme "eshopper" -locale "en_US"

Multilingual:

    java -jar target/OWL2HTML-app-1.0-SNAPSHOT-jar-with-dependencies.jar -generation offline -sourcefile "src/main/resources/owl/pproc_1.0.0.rdf" -inputdirectory "src/main/resources/input/" -outputdirectory "target/testOutput/pproc/" -themesdirectory "src/main/resources/themes/" -theme "eshopper" -locale "es"
    java -jar target/OWL2HTML-app-1.0-SNAPSHOT-jar-with-dependencies.jar -generation offline -sourcefile "src/main/resources/owl/pproc_1.0.0.rdf" -inputdirectory "src/main/resources/input/" -outputdirectory "target/testOutput/pproc/" -themesdirectory "src/main/resources/themes/" -theme "eshopper" -locale "en"
