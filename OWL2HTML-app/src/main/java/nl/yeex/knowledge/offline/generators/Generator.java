package nl.yeex.knowledge.offline.generators;

import org.semanticweb.owlapi.model.OWLOntology;

public interface Generator {

    public void generate(GeneratorContext context, OWLOntology ontology);

}
