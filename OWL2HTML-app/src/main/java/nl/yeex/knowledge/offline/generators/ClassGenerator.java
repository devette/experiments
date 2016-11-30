package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public class ClassGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport classes (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        int counter = 0;
        for (ClassVO classVO : ontologyVO.getClasses()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            Templates.generateClass(context, ontologyVO, classVO, classVO.getFilename() + ".html");

            for (OWLOntology imported : ontology.getImports()) {
                generate(context, imported);
            }
        }
    }

}
