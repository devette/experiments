package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.IndividualVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class IndividualGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport individuals (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        int counter = 0;
        for (IndividualVO individualVO : ontologyVO.getIndividuals()) {
            context.log(".");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            Templates.generateIndividual(context, ontologyVO, individualVO, individualVO.getFilename() + ".html");
            for (OWLOntology imported : ontology.getImports()) {
                generate(context, imported);
            }
        }
    }
}
