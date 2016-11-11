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
public final class StaticFilesGenerator implements Generator {
    private static final Logger LOG = LoggerFactory.getLogger(StaticFilesGenerator.class);

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        // does nothing with the ontology. it just copies the static files.
        context.log("\nCopying static files\n");
        copyStaticFiles(context);
    }

    /**
     * Copy the static files that are needed for the generated files. i.e. css and javascript files.
     *
     * @param context the generator context, containing configuration of paths.
     */
    public static void copyStaticFiles(final GeneratorContext context) {
        try {
            FileUtils.copyDirectory(new File(context.getStaticDirectory()), new File(context.getOutputDirectory()));
        } catch (IOException e) {
            LOG.error("Error while copying static files.", e);
        }
    }

}
