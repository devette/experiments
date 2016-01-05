package nl.yeex.knowledge.core.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Wrapper class for OWLOntology object. This class hides all visitor patterns
 * and make the OWLOntology available for template usage.
 * 
 */
public class OntologyVO {
    private OWLOntology ontology;
    private String label;

    public OntologyVO(OWLOntology ontology) {
        this.ontology = ontology;
    }

    /**
     * Get the ID of the ontology.
     * 
     * @return id
     */
    public OWLOntologyID getOntologyID() {
        return ontology.getOntologyID();
    }

    /**
     * Get all individuals that are in this ontology. The individuals are
     * instances of various OWLClasses.
     * 
     * @return
     */
    public Collection<IndividualVO> getIndividuals() {
        ArrayList<IndividualVO> individuals = new ArrayList<IndividualVO>();
        Set<OWLNamedIndividual> individualsInSignature = ontology.getIndividualsInSignature();
        for (OWLNamedIndividual individual : individualsInSignature) {
            IndividualVO individualVO = new IndividualVO(ontology, individual);
            individuals.add(individualVO);
        }

        Collections.sort(individuals);
        return individuals;
    }

    public Collection<AnnotationVO> getAnnotations() {
        final Collection<AnnotationVO> annotations = new HashSet<AnnotationVO>();
        Collection<OWLAnnotation> owlAnnotations = ontology.getAnnotations();
        for (OWLAnnotation annotation : owlAnnotations) {
            annotation.accept(new AbstractOntologyVisitor() {
                @Override
                public void visit(OWLAnnotation annotation) {
                    /*
                     * If it's a label, grab it as the result. Note that if
                     * there are multiple labels, the last one will be used.
                     */
                    if (annotation.getProperty().isLabel()) {
                        OWLLiteral c = (OWLLiteral) annotation.getValue();
                        OntologyVO.this.label = c.getLiteral();
                    }
                    AnnotationVO annotationVO = new AnnotationVO(annotation);
                    annotations.add(annotationVO);
                }
            });
        }
        // Collections.sort(new ArrayList(annotations));
        return annotations;
    }

    public String getLabel() {
        if (label == null) {
            // find label in annotations
            getAnnotations();
        }
        return label;
    }

    /**
     * Get the locale specific label for this Entity. It will return the first
     * rdfs:label found in the given locale. Otherwise it will return the
     * default label #.
     * 
     * @return
     */
    public String getLocalLabel(final Locale locale) {
        final List<String> localLabels = new ArrayList<String>();
        // find label in annotations.
        Collection<OWLAnnotation> owlAnnotations = ontology.getAnnotations();
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
                        if (locale.getLanguage().equalsIgnoreCase(c.getLang())) {
                            localLabels.add(c.getLiteral());
                        }
                    }
                }
            });
        }
        // when a local label is found return the first, otherwise return the
        // default label ...
        return (!localLabels.isEmpty()) ? localLabels.get(0) : getLabel();

    }

    /**
     * Get the Root Class of the ontology.
     * 
     * @return id
     */
    public ClassVO getRoot() {
        return new ClassVO(ontology, ontology.getOWLOntologyManager().getOWLDataFactory().getOWLThing());
    }

    /**
     * Get all object properties that are in this ontology.
     * 
     * @return object properties.
     */
    public Collection<ObjectPropertyVO> getObjectProperties() {
        List<ObjectPropertyVO> objectProperties = new ArrayList<ObjectPropertyVO>();
        Set<OWLObjectProperty> objectPropertiesInSignature = ontology.getObjectPropertiesInSignature();
        for (OWLObjectProperty owlObjectProperty : objectPropertiesInSignature) {
            ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, owlObjectProperty);
            objectProperties.add(objectPropertyVO);
        }
        return objectProperties;
    }

    /**
     * Get all dataproperties that are in this ontology.
     * 
     * @return dataproperties.
     */
    public Collection<DataPropertyVO> getDataProperties() {
        Collection<DataPropertyVO> dataProperties = new ArrayList<DataPropertyVO>();
        Set<OWLDataProperty> dataPropertiesInSignature = ontology.getDataPropertiesInSignature();
        for (OWLDataProperty dataProperty : dataPropertiesInSignature) {
            DataPropertyVO dataPropertyVO = new DataPropertyVO(ontology, dataProperty);
            dataProperties.add(dataPropertyVO);
        }
        return dataProperties;
    }

    /**
     * Get all classes that are in this ontology.
     * 
     * @return classes
     */
    public Collection<ClassVO> getClasses() {
        Collection<ClassVO> classes = new ArrayList<ClassVO>();
        Set<OWLClass> classesInSignature = ontology.getClassesInSignature();
        for (OWLClass owlClass : classesInSignature) {
            ClassVO classVO = new ClassVO(ontology, owlClass);
            classes.add(classVO);
        }

        Collections.sort((ArrayList<ClassVO>) classes);
        return classes;
    }

    /**
     * Get all imported ontologies.
     * 
     * @return importDeclarations
     */
    public Collection<OWLImportsDeclaration> getImportDeclarations() {
        return ontology.getImportsDeclarations();
    }
}