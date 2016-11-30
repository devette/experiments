package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.ObjectPropertyVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class ObjectPropertyGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport object properties\n");
        int counter = 0;
        for (ObjectPropertyVO objectPropertyVO : ontologyVO.getObjectProperties()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            Templates.generateObjectProperty(context, ontologyVO, objectPropertyVO, objectPropertyVO.getPropertyLabel() + ".html");
        }
    }

}
