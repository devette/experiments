package nl.yeex.knowledge.core.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

/**
 * Wrapper class for OWLIndividual objects. This class hides all visitor
 * patterns and make the OWLIndividual available for template usage.
 * 
 */
public class IndividualVO extends AbstractOWLEntityVO implements Comparable<IndividualVO> {

    private OWLIndividual owlIndividual;

    public IndividualVO(OWLOntology ontology, OWLIndividual owlIndividual) {
        this.ontology = ontology;
        this.owlIndividual = owlIndividual;
    }

    public int compareTo(IndividualVO object) {
        return this.getLabel().compareTo(object.getLabel());
    }

    @Override
    public OWLEntity getOWLEntity() {
        return (OWLEntity) owlIndividual;
    }

    @Override
    public boolean isIndividual() {
        return true;
    }

    @Override
    public boolean isClass() {
        return false;
    }

    public Collection<ObjectPropertyVO> getReferencingAxioms() {
        return getReferencingAxioms(null, true);
    }

    public Collection<ObjectPropertyVO> getReferencingAxioms(final String name, final boolean sorted) {
        final Collection<ObjectPropertyVO> referencingAxioms = new HashSet<ObjectPropertyVO>();
        Set<OWLAxiom> owlReferencingAxioms = this.ontology.getReferencingAxioms((OWLEntity) owlIndividual);
        for (OWLAxiom owlAxiom : owlReferencingAxioms) {
            owlAxiom.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                    if (!IndividualVO.this.getOWLEntity().getIRI()
                            .equals(axiom.getSubject().asOWLNamedIndividual().getIRI())) {
                        ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, axiom);
                        if (name == null || name.equalsIgnoreCase(objectPropertyVO.getPropertyLabel())) {
                            referencingAxioms.add(objectPropertyVO);
                        }
                    }
                }
            });
        }
        if (sorted) {
            Collections.sort(new ArrayList<ObjectPropertyVO>(referencingAxioms));
        }
        return referencingAxioms;
    }

    /**
     * Gets the objectproperties of this individual (sorted).
     * 
     * @return a sorted collection of objectproperties .
     */
    public Collection<ObjectPropertyVO> getObjectProperties() {
        return getObjectProperties(null, true);
    }

    /**
     * Gets the objectproperties of this individual (sorted) filterd by properties with name.
     *
     * @param name the name filter.
     *
     * @return a sorted collection of objectproperties .
     */
    public Collection<ObjectPropertyVO> getObjectProperties(final String name) {
        return getObjectProperties(name, true);
    }

    /**
     * Lookup a object with the given name.
     *
     * @param name
     * @return only the first object property with the given name.
     */
    public ObjectPropertyVO getObjectProperty(final String name)  {
        Collection<ObjectPropertyVO> objectProperties = getObjectProperties(name, false);
        return (objectProperties.isEmpty())? null: objectProperties.iterator().next();
    }

    /**
     * Gets the objectproperties of this individual (sorted).
     *
     * @return a sorted collection of objectproperties .
     */
    public Collection<ObjectPropertyVO> getObjectProperties(final String name, final boolean sorted) {
        final Collection<ObjectPropertyVO> objectProperties = new HashSet<ObjectPropertyVO>();
        Set<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxioms = ontology
                .getObjectPropertyAssertionAxioms(owlIndividual);
        for (OWLAxiom owlAxiom : objectPropertyAssertionAxioms) {
            owlAxiom.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom) {
                    ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, owlObjectPropertyAssertionAxiom);
                    if (name == null || name.equalsIgnoreCase(objectPropertyVO.getPropertyLabel())) {
                        objectProperties.add(objectPropertyVO);
                    }
                }
            });
        }
        if (sorted)  {
            Collections.sort(new ArrayList<ObjectPropertyVO>(objectProperties));
        }
        return objectProperties;
    }

    /**
     * Gets the all objectproperties related to this individual within at
     * maximum 'stepsAway' steps away from this individual. (with duplicates
     * removed).
     * 
     * example: <br/>
     * thisindividual --[prop1]--> individual2 --[prop2]--> individual3
     * --[prop3]--> individual4 <br/>
     * when stepsAway is 1, it will return {prop1} when stepsAway is 2, it will
     * return {prop1, prop2} <br/>
     * when stepsAway is 3, it will return {prop1, prop2, prop3}
     * 
     * etc. Kind of a workaround but duplicates will be removed based on the
     * combination of "subjectlabel, propertylabel and objectlabel".
     * 
     * @return a sorted collection of objectproperties .
     */
    public Map<String, ObjectPropertyVO> getObjectPropertiesGraph(final int stepsAway) {
        final Map<String, ObjectPropertyVO> allProperties = new HashMap<String, ObjectPropertyVO>();
        if (stepsAway > 0) {

            Collection<ObjectPropertyVO> myObjectProperties = getObjectProperties();
            for (ObjectPropertyVO myObjectProperty : myObjectProperties) {
                String ref = myObjectProperty.getSubject().getLabel() + myObjectProperty.getPropertyLabel()
                        + myObjectProperty.getObject().getLabel();
                allProperties.put(ref, myObjectProperty);
                // add all properties of 'object' (recursion)
                allProperties.putAll(myObjectProperty.getObject().getObjectPropertiesGraph(stepsAway - 1));
            }

            Collection<ObjectPropertyVO> myReferences = getReferencingAxioms();
            for (ObjectPropertyVO myObjectProperty : myReferences) {
                String ref = myObjectProperty.getSubject().getLabel() + myObjectProperty.getPropertyLabel()
                        + myObjectProperty.getObject().getLabel();
                allProperties.put(ref, myObjectProperty);
                // add all properties of 'subject' (recursion)
                allProperties.putAll(myObjectProperty.getSubject().getObjectPropertiesGraph(stepsAway - 1));
            }
        }
        return allProperties;
    }


    /**
     * Gets the negativeobjectproperties of this individual (sorted).
     *
     * @return a sorted collection of (negative) objectproperties .
     */
    public Collection<ObjectPropertyVO> getNegativeObjectProperties() {
        return getNegativeObjectProperties(null, true);
    }

    /**
     * Gets the negativeobjectproperties of this individual (sorted) filtered by properties with name.
     *
     * @param name the name filter.
     *
     * @return a sorted collection of negativeobjectproperties .
     */
    public Collection<ObjectPropertyVO> getNegativeObjectProperties(final String name) {
        return getNegativeObjectProperties(name, true);
    }

    /**
     * Gets the negativeobjectproperties of this individual (sorted).
     * 
     * @return a sorted collection of (negative) objectproperties .
     */
    public Collection<ObjectPropertyVO> getNegativeObjectProperties(final String name, final boolean sorted) {
        final Collection<ObjectPropertyVO> objectProperties = new HashSet<ObjectPropertyVO>();
        Set<OWLNegativeObjectPropertyAssertionAxiom> negativeObjectPropertyAssertionAxioms = ontology
                .getNegativeObjectPropertyAssertionAxioms(owlIndividual);
        for (OWLAxiom owlAxiom : negativeObjectPropertyAssertionAxioms) {
            owlAxiom.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLNegativeObjectPropertyAssertionAxiom owlNegativeObjectPropertyAssertionAxiom) {
                    ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology,
                            owlNegativeObjectPropertyAssertionAxiom);
                    if (name == null || name.equalsIgnoreCase(objectPropertyVO.getPropertyLabel())) {
                        objectProperties.add(objectPropertyVO);
                    }
                }
            });
        }
        if (sorted) {
            Collections.sort(new ArrayList<ObjectPropertyVO>(objectProperties));
        }
        return objectProperties;
    }



    /**
     * Gets the dataproperties of this individual (sorted).
     * 
     * @return a sorted collection of dataproperties .
     */
    public Collection<DataPropertyVO> getDataProperties() {
        final Collection<DataPropertyVO> dataProperties = getDataProperties(null, true);
        Collections.sort(new ArrayList<DataPropertyVO>(dataProperties));
        return dataProperties;
    }

    /**
     * Lookup a dataproperty with the given name.
     *
     * @param name
     * @return only the first data property with the given name.
     */
    public DataPropertyVO getDataProperty(final String name)  {
        Collection<DataPropertyVO> dataProperties = getDataProperties(name, false);
        return (dataProperties.isEmpty())? null: dataProperties.iterator().next();
    }

    public Collection<DataPropertyVO> getDataProperties(final String name)  {
        return getDataProperties(name, true);
    }

    /**
     * @param name the name of the property
     *            the property to include in the result.
     * @param sorted when true the result will be sorted in natural order.
     * @return a sorted collection of dataproperties filtered by the given
     *         nameOfProperty.
     */
    public Collection<DataPropertyVO> getDataProperties(final String name, final boolean sorted)  {
        final Collection<DataPropertyVO> dataProperties = new HashSet<DataPropertyVO>();
        Set<OWLDataPropertyAssertionAxiom> dataPropertyAssertionAxioms = ontology
                .getDataPropertyAssertionAxioms(owlIndividual);
        for (OWLAxiom owlAxiom : dataPropertyAssertionAxioms) {
            owlAxiom.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom) {
                    DataPropertyVO dataPropertyVO = new DataPropertyVO(ontology, owlDataPropertyAssertionAxiom);
                    if (name == null || name.equalsIgnoreCase(dataPropertyVO.getPropertyLabel())) {
                        dataProperties.add(dataPropertyVO);
                    }
                }
            });
        }
        if (sorted)  {
            Collections.sort(new ArrayList<DataPropertyVO>(dataProperties));
        }
        return dataProperties;
    }

    /**
     * Gets the negativedataproperties of this individual (sorted).
     * 
     * @return a sorted collection of dataproperties .
     */
    public Collection<DataPropertyVO> getNegativeDataProperties() {
        return getNegativeDataProperties(null, true);
    }


    /**
     * Gets the negativedataproperties of this individual (sorted).
     *
     * @return a sorted collection of dataproperties .
     */
    public Collection<DataPropertyVO> getNegativeDataProperties(final String name) {
        return getNegativeDataProperties(name, true);
    }


    /**
     * Gets the dataproperties of this individual (sorted).
     *
     * @return a sorted collection of dataproperties .
     */
    public Collection<DataPropertyVO> getNegativeDataProperties(final String name, final boolean sorted) {
        final Collection<DataPropertyVO> negativeDataProperties = new HashSet<DataPropertyVO>();
        Set<OWLNegativeDataPropertyAssertionAxiom> negativeDataPropertyAssertionAxioms = ontology
                .getNegativeDataPropertyAssertionAxioms(owlIndividual);
        for (OWLAxiom owlAxiom : negativeDataPropertyAssertionAxioms) {
            owlAxiom.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLNegativeDataPropertyAssertionAxiom owlNegativeDataPropertyAssertionAxiom) {
                    DataPropertyVO dataPropertyVO = new DataPropertyVO(ontology, owlNegativeDataPropertyAssertionAxiom);
                    if (name == null || name.equalsIgnoreCase(dataPropertyVO.getPropertyLabel())) {
                        negativeDataProperties.add(dataPropertyVO);
                    }
                }
            });
        }
        if (sorted) {
            Collections.sort(new ArrayList<DataPropertyVO>(negativeDataProperties));
        }
        return negativeDataProperties;
    }

    /**
     * Get the classes that are associated with this individual.
     * 
     * @return classes.
     */
    public Collection<ClassVO> getClasses() {
        return getClasses(null, true);
    }

    /**
     * Get the classes that are associated with this individual.
     * @param name the name of a class to filter on.
     * @param sorted when true the result will be sorted in natural order.
     *
     * @return classes.
     */
    public Collection<ClassVO> getClasses(final String name, final boolean sorted) {
        final Collection<ClassVO> classes = new HashSet<ClassVO>();
        Collection<OWLClassExpression> owlClassExpressions = EntitySearcher.getTypes(owlIndividual, ontology);
        for (OWLClassExpression owlClassExpression : owlClassExpressions) {
            owlClassExpression.accept(new OWLClassExpressionVisitorAdapter() {
                @Override
                public void visit(OWLClass owlClass) {
                    ClassVO classVO = new ClassVO(ontology, owlClass);
                    if (name == null || name.equalsIgnoreCase(classVO.getLabel())) {
                        classes.add(classVO);
                    }
                }
            });
        }
        if (sorted) {
            Collections.sort(new ArrayList<ClassVO>(classes));
        }
        return classes;
    }

}
