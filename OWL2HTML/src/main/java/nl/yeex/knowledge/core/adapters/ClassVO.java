package nl.yeex.knowledge.core.adapters;

import java.util.*;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.SimpleRootClassChecker;

import javax.annotation.Nonnull;

/**
 * Wrapper class for OWLClass objects. This class hides all visitor patterns and
 * make the OWLClass available for template usage.
 * 
 */
public class ClassVO extends AbstractOWLEntityVO implements Comparable<ClassVO> {

    private OWLClass owlClass;

    // Performance optimalizations
    private boolean rootClass = false;
    private boolean rootClassVisited = false;
    private int individualCount = -1;
    private String extra;

    /**
     * Constructor for this OWLClass wrapper.
     * 
     * @param ontology
     *            the ontology.
     * @param owlClass
     *            the OWLClass to wrap.
     */
    public ClassVO(OWLOntology ontology, OWLClass owlClass) {
        this.ontology = ontology;
        this.owlClass = owlClass;
    }

    /**
     * Get all individuals that are instances of this OWLClass, that are in the
     * associated ontology.
     * 
     * @return individuals.
     */
    public Collection<IndividualVO> getIndividuals() {
        Collection<IndividualVO> individuals = new ArrayList<IndividualVO>();
        Collection<OWLIndividual> owlIndividuals = EntitySearcher.getIndividuals(owlClass, ontology);
        for (OWLIndividual owlIndividual : owlIndividuals) {
            IndividualVO individualVO = new IndividualVO(ontology, owlIndividual);
            individuals.add(individualVO);
        }
        return individuals;
    }

    /**
     * Get all objectproperties that are defined for this OWLClass, that are in
     * the associated ontology.
     * 
     * @return objectproperties.
     */
    public Collection<ObjectPropertyVO> getObjectProperties() {
        Collection<ObjectPropertyVO> objectProperties = new ArrayList<ObjectPropertyVO>();
        Collection<OWLObjectProperty> owlObjectProperties = ontology.getObjectPropertiesInSignature();
        for (OWLObjectProperty owlObjectProperty : owlObjectProperties) {

            ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, owlObjectProperty);
            boolean isDomainClass = false;
            // is this class in the domain of the property
            for (ClassVO domainClass : objectPropertyVO.getDomain()) {
                if (getIri().equalsIgnoreCase(domainClass.getIri())) {
                    isDomainClass = true;
                    break;
                }
            }
            // is this class in the range of the property
            boolean isRangeClass = false;
            if (!isDomainClass) {
                for (ClassVO rangeClass : objectPropertyVO.getRange()) {
                    if (getIri().equalsIgnoreCase(rangeClass.getIri())) {
                        isRangeClass = true;
                        break;
                    }
                }
            }
            // if in the domain or range of the property, it is releated to this
            // class.
            if (isDomainClass || isRangeClass) {
                objectProperties.add(objectPropertyVO);
            }

        }
        return objectProperties;
    }

    /**
     * Get the amount of individuals that are instances of this OWLClass, that
     * are in the associated ontology.
     * 
     * @return the amount of individuals.
     */
    public int getIndividualsCount() {
        // performance optimalization. Do the counting only once
        if (individualCount < 0) {
            int count = this.getIndividuals().size();
            for (ClassVO subClass : getSubClasses()) {
                count = count + subClass.getIndividualsCount();
            }
            individualCount = count;
        }
        return individualCount;
    }

    /**
     * Check to find out if this OWLClass is a root class (a direct subclass of
     * owl:Thing). Do this check only once for performance optimalization.
     * 
     * @return
     */
    public boolean isRootClass() {
        if (!rootClassVisited) {
            // this method is slow, so do the check only once.
            Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
            ontologies.add(this.ontology);
            SimpleRootClassChecker simpleRootClassChecker = new SimpleRootClassChecker(ontologies);
            rootClass = simpleRootClassChecker.isRootClass(this.owlClass);
        }
        return rootClass;
    }

    public Collection<ClassVO> getSuperClasses() {
        final Collection<ClassVO> superClasses = new HashSet<ClassVO>();
        Collection<OWLClassExpression> owlSuperClasses = EntitySearcher.getSuperClasses(owlClass, ontology);
        for (OWLClassExpression owlClassExpression : owlSuperClasses) {
            if (!owlClassExpression.isAnonymous()) {
                OWLClass owlSuperClass = owlClassExpression.asOWLClass();
                superClasses.add(new ClassVO(ontology, owlSuperClass));
            }
        }
        return superClasses;
    }


    /**
     * Get all classes that are a direct subclass of this class.
     * 
     * @return classes.
     */
    public Collection<ClassVO> getSubClasses() {
        return getSubClasses(null);
    }

    /**
     * Get all classes that are a direct subclass of this class.
     *
     * @return classes.
     */
    public Collection<ClassVO> getSubClasses(final String name) {
        Collection<ClassVO> subClasses = new ArrayList<ClassVO>();
        Collection<OWLClassExpression> owlClasses = EntitySearcher.getSubClasses(owlClass, ontology);
        for (OWLClassExpression owlClassExpression : owlClasses) {
            if (!owlClassExpression.isAnonymous()) {
                ClassVO classVO = new ClassVO(ontology, owlClassExpression.asOWLClass());
                if (name == null || name.equalsIgnoreCase(classVO.getLabel())) {
                    subClasses.add(classVO);
                }
            }
        }
        return subClasses;
    }

    public String toString() {
        return "ClassVO [owlClass=" + owlClass + ", ontology=" + ontology + ", label=" + label + "]";
    }

    public String getLabel() {
        if (label == null) {
            // find label in annotations.
            getAnnotations();
            // if label is still null
            if (label == null) {
                if (owlClass != null && owlClass.getIRI() != null) {
                    label = getIriId();
                } else {
                    label = "undefined";
                }
            }
        }
        return label;
    }

    public List getExpressions()  {
        // GETTING the WhiteWine case right.
        final List expressions = new ArrayList();

        synchronized (ontology) {
            for(OWLClassAxiom ax : ontology.getAxioms(owlClass, Imports.EXCLUDED)) {
                for(OWLClassExpression owlClassExpression: ax.getNestedClassExpressions()) {
                    owlClassExpression.accept(new OWLClassExpressionVisitorAdapter() {
                        protected void handleDefault(OWLClassExpression owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }

                        @Override
                        public void visit(OWLObjectUnionOf owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }

                        @Override
                        public void visit(OWLObjectComplementOf owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }

                        @Override
                        public void visit(OWLObjectSomeValuesFrom owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }

                        @Override
                        public void visit(OWLObjectAllValuesFrom owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }

                        public void visit(OWLObjectIntersectionOf owlClassExpression) {
                            expressions.add(new ClassExpressionVO(ontology, owlClassExpression));
                        }
                    });
                }
            }
        }


        return expressions;
    }

    @Override
    public OWLEntity getOWLEntity() {
        return (OWLEntity) owlClass;
    }


    @Override
    public boolean isIndividual() {
        return false;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(ClassVO object) {
        return this.getLabel().compareTo(object.getLabel());
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
