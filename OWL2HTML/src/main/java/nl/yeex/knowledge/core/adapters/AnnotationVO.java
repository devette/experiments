package nl.yeex.knowledge.core.adapters;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

/**
 * This class is a wrapper class for the OWLAnnotation class. It provides method
 * for easy access to information contained in the ontology for this annotation.
 * 
 */
public class AnnotationVO {

    private OWLAnnotation annotation;

    public AnnotationVO(OWLAnnotation annotation) {
        this.annotation = annotation;
    }

    public String getProperty() {
        String iri = annotation.getProperty().getIRI().toString();
        return iri.substring(iri.indexOf('#') + 1, iri.length());
    }

    public OWLAnnotationValue getValue() {
        return annotation.getValue();
    }

}
