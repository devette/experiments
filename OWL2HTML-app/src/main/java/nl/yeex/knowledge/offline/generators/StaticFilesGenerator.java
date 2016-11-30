package nl.yeex.knowledge.offline.generators;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Copy all static files (stylesheets, javascript etc) to the output directory.
 * 
 */
public final class StaticFilesGenerator implements Generator {


    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        // does nothing with the ontology. it just copies the static files.
        context.log("\nCopying static files\n");
        Templates.copyStaticFiles(context);
    }


}
