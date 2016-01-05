package nl.yeex.knowledge.core.inference;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;

public class MyInferredClassAssertionAxiomGenerator extends InferredIndividualAxiomGenerator<OWLClassAssertionAxiom> {

    @Override
    protected void addAxioms(OWLNamedIndividual entity, OWLReasoner reasoner, OWLDataFactory dataFactory,
            Set<OWLClassAssertionAxiom> result) {
        if (entity != null) {
            for (OWLClass type : reasoner.getTypes(entity, false).getFlattened()) {
                if (!type.isTopEntity()) {
                    result.add(dataFactory.getOWLClassAssertionAxiom(type, entity));
                }
            }
        }
    }

    public String getLabel() {
        return "Class assertions (individual types)";
    }
}