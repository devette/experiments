OWL2HTML 
=======

A Project for experiments with reasoning and visualization of knowledge representated in the OWL2 language. (see http://www.w3.org/TR/owl2-syntax/)
The OWL 2 Web Ontology Language, informally OWL 2, is an ontology language for the Semantic Web with formally defined meaning.
OWL 2 ontologies provide classes, properties, individuals, and data values and are stored as Semantic Web documents.
OWL2 is a W3C Recommendation and is a

I noticed the power of OWL2 knowledge representation in complex software projects, where it was necessary to relate many facts with each other.
OWL2 provides a way to relate these facts, and provides flexibility, changeability of these facts. It also provided a possibility to find referring facts,
which is extremely useful for impact analysis.

- Webapp:
This project contains a webapp to get a runtime visualization of knowledge in OWL2 files.

- App:
This project contains an app to generate offline html files with browsable and visualized knowledge of the OWL2 file. Useful to show the knowledge to

- Maven plugin:
This project contains a maven plugin to start offline (generated html) or online (web server) visualization of the owl2 files.

=======

Tools necessary to build this project

- Java JDK 1.7 - http://www.oracle.com/technetwork/java/javase/downloads/index.html
- Maven - http://maven.apache.org

Optional:
- Node - https://nodejs.org/en/
- Bower - http://bower.io
- IntelliJ - https://www.jetbrains.com/idea/download/

=======

Before building this project, it is necessary to build the Hermit Reasoner.
This reasoner has yet not been updated to the OWL4 api. The source of this reasoner is a clone of
https://github.com/phillord/hermit-reasoner, and is the Hermit reasoner, updated with OWL-api v4 changes.

1. Open a terminal window in the root of the project.
2. cd HermiT-mvn
3. mvn clean install

Next Build the OWL2HTML project, this depends on the previous step, and needs the build Hermit Snaphot: 1.3.8.5-SNAPSHOT.

1. Open a terminal window in the root of the project.
2. mvn clean install
3. cd OWL2HTML-web
4. mvn jetty:run
6. open browser and go to url: http://localhost:8080/OWL2HTML-web/select?
