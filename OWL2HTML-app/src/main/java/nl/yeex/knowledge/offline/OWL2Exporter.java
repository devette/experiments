package nl.yeex.knowledge.offline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.yeex.knowledge.offline.generators.Generator;
import nl.yeex.knowledge.offline.generators.GeneratorContext;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {

            // Load the ontology/ontologies
            // loadOntologies(context, manager);
            ontology = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(context.getOwlSourceFile()));

            OWLOntology toGenerateOntology = ontology;
            if (context.isEnableInference()) {
                toGenerateOntology = materializeInferences(manager, ontology, context);
            } else {
                LOG.info("Reasoner disabled.\n");
            }

            for (Generator generator : context.getGenerators()) {
                generator.generate(context, toGenerateOntology);
            }

        } catch (OWLOntologyCreationException e) {
            LOG.error("Loading ontology failed.", e);
        }
        LOG.info("\n----------- Finished Export -----------\n");
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

    private OWLOntology materializeInferences(OWLOntologyManager manager, OWLOntology ontology, GeneratorContext context)
            throws OWLOntologyCreationException {
        LOG.info("Starting Reasoner...\n");
        Reasoner hermit = new Reasoner(new Configuration(), ontology);
        LOG.info("Reasoner is consistent: " + hermit.isConsistent() + "\n");

        // hermit.precomputeInferences(InferenceType.SAME_INDIVIDUAL);
        // //
        // hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        // hermit.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        // hermit.precomputeInferences(InferenceType.DISJOINT_CLASSES);

        // We can now create an instance of InferredOntologyGenerator.

        List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        generators.add(new MyInferredClassAssertionAxiomGenerator());
        generators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
        generators.add(new InferredEquivalentClassAxiomGenerator());
        generators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
        generators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
        generators.add(new InferredInverseObjectPropertiesAxiomGenerator());
        generators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());
        generators.add(new MyInferredPropertyAssertionGenerator());
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredSubDataPropertyAxiomGenerator());
        generators.add(new InferredSubObjectPropertyAxiomGenerator());

        InferredOntologyGenerator inferredOntologyGenerator = new InferredOntologyGenerator(hermit, generators);

        // Before we actually generate the axioms into an ontology, we first
        // have to create that ontology.
        // The manager creates the for now empty ontology for the inferred
        // axioms for us.
        OWLOntology inferredAxiomsOntology = manager.createOntology();
        // Now we use the inferred ontology generator to fill the ontology. That
        // might take some
        // time since it involves possibly a lot of calls to the reasoner.
        inferredOntologyGenerator.fillOntology(manager.getOWLDataFactory(), inferredAxiomsOntology);
        LOG.info("Finished Reasoner...\n");
        return inferredAxiomsOntology;
    }

    private class MyInferredPropertyAssertionGenerator extends
            InferredIndividualAxiomGenerator<OWLPropertyAssertionAxiom<?, ?>> {

        @Override
        protected void addAxioms(OWLNamedIndividual entity, OWLReasoner reasoner, OWLDataFactory dataFactory,
                Set<OWLPropertyAssertionAxiom<?, ?>> result) {
            for (OWLObjectProperty prop : reasoner.getRootOntology().getObjectPropertiesInSignature(true)) {
                for (OWLNamedIndividual value : reasoner.getObjectPropertyValues(entity, prop).getFlattened()) {
                    if (!prop.isTopEntity()) {
                        result.add(dataFactory.getOWLObjectPropertyAssertionAxiom(prop, entity, value));
                    }
                }
            }
            for (OWLDataProperty prop : reasoner.getRootOntology().getDataPropertiesInSignature(true)) {
                for (OWLLiteral value : reasoner.getDataPropertyValues(entity, prop)) {
                    if (!prop.isTopEntity()) {
                        result.add(dataFactory.getOWLDataPropertyAssertionAxiom(prop, entity, value));
                    }
                }
            }
        }

        public String getLabel() {
            return "Property assertions (property values)";
        }

    }

    private class MyInferredClassAssertionAxiomGenerator extends
            InferredIndividualAxiomGenerator<OWLClassAssertionAxiom> {

        @Override
        protected void addAxioms(OWLNamedIndividual entity, OWLReasoner reasoner, OWLDataFactory dataFactory,
                Set<OWLClassAssertionAxiom> result) {
            for (OWLClass type : reasoner.getTypes(entity, false).getFlattened()) {
                if (!type.isTopEntity()) {
                    result.add(dataFactory.getOWLClassAssertionAxiom(type, entity));
                }
            }
        }

        public String getLabel() {
            return "Class assertions (individual types)";
        }
    }
}
