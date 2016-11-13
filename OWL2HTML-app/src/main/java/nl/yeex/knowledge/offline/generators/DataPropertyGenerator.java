package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.DataPropertyVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class DataPropertyGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport data properties\n");
        int counter = 0;
        for (DataPropertyVO dataProperty : ontologyVO.getDataProperties()) {
            context.log("%");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            Templates.generateDataProperty(context, ontologyVO, dataProperty, dataProperty.getPropertyLabel() + ".html");
        }
    }

}
