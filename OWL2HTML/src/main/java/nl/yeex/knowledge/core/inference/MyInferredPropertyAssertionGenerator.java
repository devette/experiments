package nl.yeex.knowledge.core.inference;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;

public class MyInferredPropertyAssertionGenerator extends
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