package nl.yeex.knowledge.core.adapters;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitor;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

/**
 * This class is provides a default implementation for all the ontology visitor
 * methods. So when you need a vistor, you can subclass this file and only
 * implement the method you want to implement. So create a direct subclass of
 * this class and override the method you need to use.
 * 
 */
public class AbstractOntologyVisitor extends OWLAxiomVisitorAdapter implements OWLAnnotationObjectVisitor {

    public void visit(IRI iri) {
        /* Visitor interface is too verbose */
    }

    public void visit(OWLAnonymousIndividual individual) {
        /* Visitor interface is too verbose */
    }

    public void visit(OWLLiteral literal) {
        /* Visitor interface is too verbose */
    }

    public void visit(OWLAnnotation node) {
        /* Visitor interface is too verbose */
    }

}
