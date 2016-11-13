package nl.yeex.knowledge.offline;

import nl.yeex.knowledge.offline.generators.Generator;
import nl.yeex.knowledge.offline.generators.GeneratorContext;
import nl.yeex.knowledge.offline.inference.Materializer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * This class is the main entry point for starting the generation of the HTML
 * files from the OWL file.
 * 
 */
public class OWL2Exporter {

    private static final Logger LOG = LoggerFactory.getLogger(OWL2Exporter.class);

    /**
     * Start generation.
     * 
     * @param context
     */
    public void start(GeneratorContext context) {
        LOG.info("----------- Start Export -----------\n");
        try {
            OWLOntology ontology = loadOntology(context);
            for (Generator generator : context.getGenerators()) {
                generator.generate(context, ontology);
            }
        } catch (OWLOntologyCreationException e) {
            LOG.error("Loading ontology failed.", e);
        }
        LOG.info("\n----------- Finished Export -----------\n");
    }

    public static OWLOntology loadOntology(GeneratorContext context) throws OWLOntologyCreationException  {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        // TODO: the alternative does not work?
        // OWLOntologyLoaderConfiguration#setMissingImportHandlingStrategy(MissingImportHandlingStrategy)
        // manager.setSilentMissingImportsHandling(true);
        // String mapped = "file://" +
        // context.getOwlSourceFile().getAbsoluteFile().getParent()
        // +"/ontology2.owl";
        // context.log(mapped);
        // manager.addIRIMapper(new SimpleIRIMapper(
        // IRI.create("http://www.NewOnto2.org/ontology2"),
        // IRI.create(mapped)));
        // manager.addIRIMapper(new LookupFromSameDirectoryIRIMapper(context));

        OWLOntology ontology;

        // Load the ontology/ontologies
        // loadOntologies(context, manager);
        ontology = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(context.getOwlSourceFile()));

        OWLOntology toGenerateOntology = ontology;
        if (context.isEnableInference()) {
            toGenerateOntology = Materializer.materializeInferences(manager, ontology, context);
        } else {
            LOG.info("Reasoner disabled.\n");
        }

        return toGenerateOntology;
    }

    private void loadOntologies(GeneratorContext context, OWLOntologyManager manager) {
        final File inputDirectory = new File(context.getInputDirectory());
        loadOntologiesFromInputDirectory(inputDirectory, manager, context);
        // OWLOntologyMerger merger = new OWLOntologyMerger(manager);
        // IRI mergedOntologyIRI =
        // IRI.create("http://www.semanticweb.com/mymergedont");
        // ontology = merger.createMergedOntology(manager, mergedOntologyIRI);
    }

    private void loadOntologiesFromInputDirectory(final File folder, OWLOntologyManager manager,
            GeneratorContext context) {
        if (folder != null && folder.exists()) {
            LOG.info("Loading input files from: " + folder);
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    loadOntologiesFromInputDirectory(fileEntry, manager, context);
                } else {
                    LOG.info("Loading input file: " + fileEntry.getName());
                    try {
                        manager.loadOntologyFromOntologyDocument(new FileDocumentSource(context.getOwlSourceFile()));
                    } catch (OWLOntologyCreationException e) {
                        LOG.error("Loading failed: " + fileEntry.getName(), e);
                    }
                }
            }
        } else {
            LOG.error("Input folder not found: " + folder);
        }
    }


}
