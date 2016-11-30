package nl.yeex.knowledge.offline.inference;

import nl.yeex.knowledge.offline.generators.GeneratorContext;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Materializer {
    private static final Logger LOG = LoggerFactory.getLogger(Materializer.class);

    public static OWLOntology materializeInferences(OWLOntologyManager manager, OWLOntology ontology, GeneratorContext context)
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

    private static class MyInferredPropertyAssertionGenerator extends
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

    private static class MyInferredClassAssertionAxiomGenerator extends
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
