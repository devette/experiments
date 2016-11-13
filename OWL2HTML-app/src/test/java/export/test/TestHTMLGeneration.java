package export.test;

import nl.yeex.knowledge.offline.OWL2Exporter;
import nl.yeex.knowledge.offline.generators.GeneratorContext;
import org.junit.Test;

public class TestHTMLGeneration {


    @Test
    public void testOWLExportPizza() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/pizza.owl");
        configuration.setOutputDirectory("target/testOutput/outputPizza/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("fancy");

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }

    @Test
    public void testOWLExportWine() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/wine.xml");
        configuration.setOutputDirectory("target/testOutput/outputWine/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("fancy");

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }

    @Test
    public void testOWLExportCalimWithoutInference() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/calim.owl");
        configuration.setInputDirectory("src/main/resources/input/");
        configuration.setOutputDirectory("target/testOutput/outputCalim/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("fancy");
        configuration.setEnableInference(false);

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }

    @Test
    public void testOWLExportCalimWithInference() {

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOwlSourceFileName("src/main/resources/owl/calim.owl");
        configuration.setInputDirectory("src/main/resources/input/");
        configuration.setOutputDirectory("target/testOutput/outputCalimInferred/");
        configuration.setThemesDirectory("src/main/resources/themes/");
        configuration.setTheme("fancy");
        configuration.setEnableInference(true);

        OWL2Exporter exporter = new OWL2Exporter();
        exporter.start(configuration);
    }

}
