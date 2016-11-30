import static nl.yeex.knowledge.offline.generators.Templates.*;
import java.io.File;
import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.offline.OWL2Exporter;
import nl.yeex.knowledge.offline.generators.GeneratorContext;
import org.semanticweb.owlapi.model.*;

configuration = new GeneratorContext();
configuration.setOwlSourceFileName(basedir.getAbsolutePath() + "/src/main/codegeneration/Countries.owl");
configuration.setOutputDirectory(basedir.getAbsolutePath() + "/src/main/java/nl/demo/");
configuration.setThemesDirectory(basedir.getAbsolutePath() + "/src/main/resources/themes/");
configuration.setTheme("codegeneration");

def ClassVO getClass(OWLOntology owlOntology, String iri)  {
    manager = owlOntology.getOWLOntologyManager();
    factory = manager.getOWLDataFactory();
    owlClass = factory.getOWLClass(IRI.create(iri));
    classVO = new ClassVO(owlOntology, owlClass);
}

try {
    new File(configuration.getOutputDirectory()).mkdirs();
    owlOntology = OWL2Exporter.loadOntology(configuration);
    ontologyVO = new OntologyVO(owlOntology);

    // Generate a class
    def namespace = "http://www.bpiresearch.com/BPMO/2004/03/03/cdl/Countries"
    def country = getClass(owlOntology, namespace + "#ISO3166DefinedCountry")
    generateClass(configuration, ontologyVO, country, "ISO3166DefinedCountry.java");


} catch (OWLOntologyCreationException e) {
    e.printStackTrace();
}