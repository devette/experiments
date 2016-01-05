package nl.yeex.knowledge.core.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 * Wrapper class for OWLOntology object. This class hides all visitor patterns
 * and make the OWLOntology available for template usage.
 * 
 */
public class LabelExtractor {

    /**
     * Get the locale specific label for this Entity. It will return the first
     * rdfs:label found in the given locale. Otherwise it will return the
     * default label #.
     * 
     * @param ontology
     * 
     * @return
     */
    public static String getLocalLabel(final @Nonnull OWLEntity entity, final @Nonnull OWLOntology ontology,
            final Locale locale) {
        final List<String> localLabels = new ArrayList<String>();
        final List<String> notLocalLabels = new ArrayList<String>();
        // find label in annotations.
        Collection<OWLAnnotation> owlAnnotations = EntitySearcher.getAnnotations(entity, ontology);
        for (OWLAnnotation annotation : owlAnnotations) {
            annotation.accept(new AbstractOntologyVisitor() {
                @Override
                public void visit(OWLAnnotation annotation) {
                    /*
                     * If it's a label, grab it as the result. Note that if
                     * there are multiple labels, the last one will be used.
                     */
                    OWLAnnotationProperty property = annotation.getProperty();
                    boolean isPrefLabel = property.getIRI().getRemainder().isPresent()
                            && property.getIRI().toString().endsWith("prefLabel");
                    if (property.isLabel() || isPrefLabel) {
                        OWLLiteral label = (OWLLiteral) annotation.getValue();
                        if (locale.getLanguage().equalsIgnoreCase(label.getLang())) {
                            localLabels.add(label.getLiteral());
                        } else {
                            notLocalLabels.add(label.getLiteral());
                        }
                    }
                }
            });
        }
        // when a local label is found return the first
        if (!localLabels.isEmpty()) {
            return localLabels.get(0);
        }
        if (!notLocalLabels.isEmpty()) {
            // no local label found, return first other found label.
            return notLocalLabels.get(0);
        }
        // no labels found, return remainder part of iri.
        String iri = entity.getIRI().toString();
        int index = iri.indexOf('#');
        if (index < 0) {
            // sometimes a class does not have a hash tag.
            // i.e. HTTP://XMLNS.COM/FOAF/0.1/PERSON
            index = iri.lastIndexOf('/');
        }
        return iri.substring(index + 1, iri.length());
    }

}