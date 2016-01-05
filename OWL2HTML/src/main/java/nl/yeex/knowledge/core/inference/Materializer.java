package nl.yeex.knowledge.core.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.yeex.knowledge.core.generation.GeneratorContext;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Materializer {
    private static final Logger LOG = LoggerFactory.getLogger(Materializer.class);

    /**
     * Private constructor for Utility class.
     */
    private Materializer() {
        super();
    }

    public static OWLOntology materializeInferences(OWLOntologyManager manager, OWLOntology ontology,
            GeneratorContext context) throws OWLOntologyCreationException {
        LOG.info("Starting Reasoner...\n");
        ReasonerFactory factory = new ReasonerFactory();
        // We don't want HermiT to thrown an exception for inconsistent
        // ontologies because then we
        // can't explain the inconsistency. This can be controlled via a
        // configuration setting.
        Configuration configuration = new Configuration();
        configuration.throwInconsistentOntologyException = false;
        // The factory can now be used to obtain an instance of HermiT as an
        // OWLReasoner.
        OWLReasoner reasoner = factory.createReasoner(ontology, configuration);
        // Reasoner reasoner = new Reasoner(ontology);
        boolean consistent = reasoner.isConsistent();
        LOG.info("Reasoner is consistent: " + consistent + "\n");
        if (!consistent) {
            logUnsatifiable(reasoner);
        }
        // reasoner.precomputeInferences(InferenceType.SAME_INDIVIDUAL);
        // reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        // reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        // reasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);

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

        InferredOntologyGenerator inferredOntologyGenerator = new InferredOntologyGenerator(reasoner, generators);

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

    private static void logUnsatifiable(OWLReasoner reasoner) {
        Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
        // This node contains owl:Nothing and all the classes that are
        // equivalent to owl:Nothing - i.e. the unsatisfiable classes. We just
        // want to print out the unsatisfiable classes excluding owl:Nothing,
        // and we can used a convenience method on the node to get these
        Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
        if (!unsatisfiable.isEmpty()) {
            LOG.info("The following classes are unsatisfiable: ");
            for (OWLClass cls : unsatisfiable) {
                LOG.info(" " + cls);
            }
        } else {
            LOG.info("There are no unsatisfiable classes");
        }
    }
}
