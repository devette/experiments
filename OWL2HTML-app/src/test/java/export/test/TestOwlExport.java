package export.test;

import nl.yeex.knowledge.offline.OWL2Exporter;
import nl.yeex.knowledge.offline.generators.GeneratorContext;

import org.junit.Ignore;
import org.junit.Test;

public class TestOwlExport {


    @Test
    public void testOWLExportPizza() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/pizza.owl");
        configuration.setOutputDirectory("target/testOutput/outputPizza/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("eshopper");

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }

    @Test
    public void testOWLExportCalim() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/calim.owl");
        configuration.setInputDirectory("src/main/resources/input/");
        configuration.setOutputDirectory("target/testOutput/outputCalim/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("eshopper");
        configuration.setEnableInference(true);

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }




}
