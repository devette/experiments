package nl.yeex.knowledge.offline.generators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copy all static files (stylesheets, javascript etc) to the output directory.
 * 
 */
public class StaticFilesGenerator implements Generator {
    private static final Logger LOG = LoggerFactory.getLogger(StaticFilesGenerator.class);

    public void generate(GeneratorContext context, OWLOntology ontology) {
        // does nothing with the ontology. it just copies the static files.
        context.log("\nCopying static files\n");
        try {
            FileUtils.copyDirectory(new File(context.getStaticDirectory()), new File(context.getOutputDirectory()));
        } catch (IOException e) {
            LOG.error("Error while copying static files.", e);
        }
    }

}
