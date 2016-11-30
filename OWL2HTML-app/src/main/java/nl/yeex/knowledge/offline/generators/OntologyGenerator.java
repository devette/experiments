package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.OntologyVO;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class OntologyGenerator implements Generator {

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        context.log("\nExport index (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        OntologyVO ontologyVO = new OntologyVO(ontology);

        Templates.generateOntology(context, ontologyVO, "index.html");

        for (OWLOntology imported : ontology.getImports()) {
            generate(context, imported);
        }
    }


}
