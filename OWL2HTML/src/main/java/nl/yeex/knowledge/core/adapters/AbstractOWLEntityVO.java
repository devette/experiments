package nl.yeex.knowledge.core.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;

import nl.yeex.knowledge.offline.util.FilenameHelper;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 * This class provides all methods shared between the ClassVO and the
 * IndividualVO. In the owl ontology model they have a shared parent
 * "OWLEntity". So this VO represents that shared parent.
 * 
 */
public abstract class AbstractOWLEntityVO {

    private static final Pattern PATTERN_UNDERSCORE = Pattern.compile("_");
    protected OWLOntology ontology;
    protected String label;
    protected String label_withoutUnderscores;
    protected String iriId;
    protected String filename;

    public AbstractOWLEntityVO() {
        super();
    }

    public abstract OWLEntity getOWLEntity();
    public abstract boolean isClass();
    public abstract boolean isIndividual();

    /**
     * Get the annotations that are associated with this Entity.
     * 
     * @return annotations.
     */
    public Collection<AnnotationVO> getAnnotations() {
        final Collection<AnnotationVO> annotations = new HashSet<AnnotationVO>();
        Collection<OWLAnnotation> owlAnnotations = EntitySearcher.getAnnotations(getOWLEntity(), ontology);
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
                        OWLLiteral c = (OWLLiteral) annotation.getValue();
                        AbstractOWLEntityVO.this.label = c.getLiteral();
                    }
                    AnnotationVO annotationVO = new AnnotationVO(annotation);
                    annotations.add(annotationVO);
                }
            });
        }
        // Collections.sort(new ArrayList(annotations));
        return annotations;
    }

    /**
     * Get the label for this Entity. It will return the first rdfs:label found.
     * Otherwise it will return the IRI part after the #.
     * 
     * @return A label for the entity
     */
    public String getLabel() {
        if (label == null) {
            // find label in annotations.
            getAnnotations();
            // when label is not in annotations, extract it from the iri ...
            if (label == null) {
                String iri = getOWLEntity().getIRI().toString();
                label = iri.substring(iri.indexOf('#') + 1, iri.length());
            }
        }
        return label;
    }

    /**
     * Get the locale specific label for this Entity. It will return the first
     * rdfs:label found in the given locale. Otherwise it will return the
     * default label #.
     * @param locale the locale, to search for a label.
     * @return A label for the entity matching the given locale, or a default label when no match found
     */
    public String getLocalLabel(final Locale locale) {
        return LabelExtractor.getLocalLabel(getOWLEntity(), ontology, locale);

    }

    public String getIriId() {
        if (iriId == null) {
            String iri = getIri();
            int index = iri.indexOf('#');
            if (index < 0) {
                // sometimes a class does not have a hash tag.
                // i.e. HTTP://XMLNS.COM/FOAF/0.1/PERSON
                index = iri.lastIndexOf('/');
            }
            iriId = iri.substring(index + 1, iri.length());
        }
        return iriId;
    }

    public String getIri() {
        return getOWLEntity().getIRI().toString();
    }

    /**
     * This method return the label with all underscores replaced by spaces.
     * This is useful for presentation because often the underscore is used in
     * defining the class name. For performance reasons, the result will be
     * stored in a variable so the replacement is done only once.
     * 
     * @return a label with underscores replaced by spaces
     */
    public String getLabelReplaceUnderscore() {
        if (label_withoutUnderscores == null) {
            label_withoutUnderscores = PATTERN_UNDERSCORE.matcher(getLabel()).replaceAll(" ");
        }
        return label_withoutUnderscores;
    }

    /**
     * Use the label to get a valid filename for this entity. It will replace
     * the illegal filename characters with a valid character replacement. For
     * performance reasons, the result will be stored in a variable so the
     * replacement is done only once.
     * 
     * @return
     */
    public String getFilename() {

        // remove the illegal filename characters \ / : * ? " < > | from the
        // label.
        if (filename == null) {
            filename = FilenameHelper.getFilename(getLabel());
        }
        return filename;
    }

    @Override
    public String toString() {
        return "AbstractOWLEntityVO [label=" + label + ", label_withoutUnderscores=" + label_withoutUnderscores
                + ", iriId=" + iriId + ", filename=" + filename;
    }

}