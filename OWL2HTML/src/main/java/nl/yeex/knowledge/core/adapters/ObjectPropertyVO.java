package nl.yeex.knowledge.core.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import nl.yeex.knowledge.offline.util.FilenameHelper;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

/**
 * Wrapper class for OWLObjectProperty objects. This class hides all visitor
 * patterns and make the OWLObjectProperty available for template usage.
 * 
 */
public class ObjectPropertyVO implements Comparable<ObjectPropertyVO> {
    private final static boolean PRINT_VISIT = false; // currently for development, to enable print.

    private OWLObjectProperty owlObjectProperty;
    private OWLOntology ontology;
    private IndividualVO subject;
    private IndividualVO object;
    private boolean negative;

    /**
     * ObjectPropertyAssertion states that a given property holds for the given
     * individuals.
     * 
     * @param ontology
     *            the ontology
     * @param axiom
     *            the axiom
     */
    public ObjectPropertyVO(OWLOntology ontology, OWLObjectPropertyAssertionAxiom axiom) {
        this.ontology = ontology;
        this.owlObjectProperty = axiom.getProperty().asOWLObjectProperty();
        this.subject = new IndividualVO(ontology, axiom.getSubject());
        this.object = new IndividualVO(ontology, axiom.getObject());
        this.negative = false;
    }

    /**
     * NegativeObjectPropertyAssertion states that a given property does not
     * hold for the given individuals.
     * 
     * @param ontology
     *            the ontology
     * @param axiom
     *            the axiom
     */
    public ObjectPropertyVO(OWLOntology ontology, OWLNegativeObjectPropertyAssertionAxiom axiom) {
        this.ontology = ontology;
        this.owlObjectProperty = axiom.getProperty().asOWLObjectProperty();
        this.subject = new IndividualVO(ontology, axiom.getSubject());
        this.object = new IndividualVO(ontology, axiom.getObject());
        this.negative = true;
    }

    public ObjectPropertyVO(OWLOntology ontology, OWLObjectProperty owlObjectProperty) {
        this.ontology = ontology;
        this.owlObjectProperty = owlObjectProperty;
    }

    public IndividualVO getSubject() {
        return subject;
    }

    public IndividualVO getObject() {
        return object;
    }

    public Collection<ClassVO> getRange() {
        Set<OWLObjectPropertyRangeAxiom> objectPropertyRangeAxioms = ontology
                .getObjectPropertyRangeAxioms(owlObjectProperty);
        final Collection<ClassVO> rangeClasses = new HashSet<ClassVO>();

        for (OWLObjectPropertyRangeAxiom clazz : objectPropertyRangeAxioms) {
            OWLClassExpression classExpression = clazz.getRange();
            classExpression.accept(new OWLClassExpressionVisitorAdapter() {

                @Override
                public void visit(OWLClass owlClass) {
                    rangeClasses.add(new ClassVO(ontology, owlClass));
                }

                @Override
                public void visit(OWLObjectSomeValuesFrom owlSomeValuesFrom) {
                    OWLClassExpression filler = owlSomeValuesFrom.getFiller();
                    if (!filler.isAnonymous()) {
                        ClassVO classVO = new ClassVO(ontology, filler.asOWLClass());
                        classVO.setExtra("some");
                        rangeClasses.add(classVO);
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
                 */
                @Override
                public void visit(OWLObjectIntersectionOf owlObjectIntersectionFrom) {
                    Set<OWLClassExpression> conjunctSet = owlObjectIntersectionFrom.asConjunctSet();
                    for (OWLClassExpression classExpression : conjunctSet) {
                        if (!classExpression.isAnonymous()) {
                            ClassVO classVO = new ClassVO(ontology, classExpression.asOWLClass());
                            classVO.setExtra("intersection");
                            rangeClasses.add(classVO);
                        }
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
                 */
                @Override
                public void visit(OWLObjectUnionOf owlObjectUnionOf) {
                    Set<OWLClassExpression> operands = owlObjectUnionOf.getOperands();
                    for (OWLClassExpression classExpression : operands) {
                        if (!classExpression.isAnonymous()) {
                            ClassVO classVO = new ClassVO(ontology, classExpression.asOWLClass());
                            classVO.setExtra("oneof");
                            rangeClasses.add(classVO);
                        }
                    }
                    if (PRINT_VISIT) System.out.println("Union Of: " + owlObjectUnionOf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectComplementOf)
                 */
                @Override
                public void visit(OWLObjectComplementOf owlObjectComplementOf) {
                    if (PRINT_VISIT) System.out.println("Complement Of: " + owlObjectComplementOf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectAllValuesFrom)
                 */
                @Override
                public void visit(OWLObjectAllValuesFrom owlAllValuesFrom) {
                    OWLClassExpression filler = owlAllValuesFrom.getFiller();
                    if (!filler.isAnonymous()) {
                        ClassVO classVO = new ClassVO(ontology, filler.asOWLClass());
                        classVO.setExtra("all");
                        rangeClasses.add(classVO);
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectHasValue)
                 */
                @Override
                public void visit(OWLObjectHasValue owlObjectHasValue) {
                    if (PRINT_VISIT) System.out.println("HAS VALUE: " + owlObjectHasValue);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectMinCardinality)
                 */
                @Override
                public void visit(OWLObjectMinCardinality owlObjectMinCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectMinCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit
                 * (org.semanticweb.owlapi.model.OWLObjectExactCardinality)
                 */
                @Override
                public void visit(OWLObjectExactCardinality owlObjectExactCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectExactCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
                 */
                @Override
                public void visit(OWLObjectMaxCardinality owlObjectMaxCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectMaxCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectHasSelf)
                 */
                @Override
                public void visit(OWLObjectHasSelf owlObjectHasSelf) {
                    if (PRINT_VISIT) System.out.println("HAS SELF: " + owlObjectHasSelf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
                 */
                @Override
                public void visit(OWLObjectOneOf owlObjectOneOf) {
                    Set<OWLIndividual> individuals = owlObjectOneOf.getIndividuals();
                    if (PRINT_VISIT) System.out.println("ONE OF: " + individuals);
                }

            });
        }
        return rangeClasses;
    }

    public Collection<ClassVO> getDomain() {
        Set<OWLObjectPropertyDomainAxiom> objectPropertyDomainAxioms = ontology
                .getObjectPropertyDomainAxioms(owlObjectProperty);
        final Collection<ClassVO> domainClasses = new HashSet<ClassVO>();
        for (OWLObjectPropertyDomainAxiom clazz : objectPropertyDomainAxioms) {
            OWLClassExpression classExpression = clazz.getDomain();
            classExpression.accept(new OWLClassExpressionVisitorAdapter() {

                @Override
                public void visit(OWLClass owlClass) {
                    domainClasses.add(new ClassVO(ontology, owlClass));
                }

                @Override
                public void visit(OWLObjectSomeValuesFrom owlSomeValuesFrom) {
                    OWLClassExpression filler = owlSomeValuesFrom.getFiller();
                    if (!filler.isAnonymous()) {
                        ClassVO classVO = new ClassVO(ontology, filler.asOWLClass());
                        classVO.setExtra("some");
                        domainClasses.add(classVO);
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectIntersectionOf)
                 */
                @Override
                public void visit(OWLObjectIntersectionOf owlObjectIntersectionFrom) {
                    Set<OWLClassExpression> conjunctSet = owlObjectIntersectionFrom.asConjunctSet();
                    for (OWLClassExpression classExpression : conjunctSet) {
                        if (!classExpression.isAnonymous()) {
                            ClassVO classVO = new ClassVO(ontology, classExpression.asOWLClass());
                            classVO.setExtra("intersection");
                            domainClasses.add(classVO);
                        }
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectUnionOf)
                 */
                @Override
                public void visit(OWLObjectUnionOf owlObjectUnionOf) {
                    Set<OWLClassExpression> operands = owlObjectUnionOf.getOperands();
                    for (OWLClassExpression classExpression : operands) {
                        if (!classExpression.isAnonymous()) {
                            ClassVO classVO = new ClassVO(ontology, classExpression.asOWLClass());
                            classVO.setExtra("oneof");
                            domainClasses.add(classVO);
                        }
                    }
                    if (PRINT_VISIT) System.out.println("Union Of: " + owlObjectUnionOf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectComplementOf)
                 */
                @Override
                public void visit(OWLObjectComplementOf owlObjectComplementOf) {
                    if (PRINT_VISIT) System.out.println("Complement Of: " + owlObjectComplementOf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectAllValuesFrom)
                 */
                @Override
                public void visit(OWLObjectAllValuesFrom owlAllValuesFrom) {
                    OWLClassExpression filler = owlAllValuesFrom.getFiller();
                    if (!filler.isAnonymous()) {
                        ClassVO classVO = new ClassVO(ontology, filler.asOWLClass());
                        classVO.setExtra("all");
                        domainClasses.add(classVO);
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectHasValue)
                 */
                @Override
                public void visit(OWLObjectHasValue owlObjectHasValue) {
                    if (PRINT_VISIT) System.out.println("HAS VALUE: " + owlObjectHasValue);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectMinCardinality)
                 */
                @Override
                public void visit(OWLObjectMinCardinality owlObjectMinCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectMinCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit
                 * (org.semanticweb.owlapi.model.OWLObjectExactCardinality)
                 */
                @Override
                public void visit(OWLObjectExactCardinality owlObjectExactCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectExactCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectMaxCardinality)
                 */
                @Override
                public void visit(OWLObjectMaxCardinality owlObjectMaxCardinality) {
                    if (PRINT_VISIT) System.out.println("HAS MAX CARDINALITY: " + owlObjectMaxCardinality);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectHasSelf)
                 */
                @Override
                public void visit(OWLObjectHasSelf owlObjectHasSelf) {
                    if (PRINT_VISIT) System.out.println("HAS SELF: " + owlObjectHasSelf);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter
                 * #visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
                 */
                @Override
                public void visit(OWLObjectOneOf owlObjectOneOf) {
                    Set<OWLIndividual> individuals = owlObjectOneOf.getIndividuals();
                    if (PRINT_VISIT) System.out.println("ONE OF: " + individuals);
                }

            });
        }
        return domainClasses;
    }

    public String getPropertyLabel() {
        return owlObjectProperty.getIRI().getRemainder().orNull();
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
        return FilenameHelper.getFilename(getPropertyLabel());
    }

    /**
     * Get the locale specific label for this Entity. It will return the first
     * rdfs:label found in the given locale. Otherwise it will return the
     * default label #.
     * 
     * @return
     */
    public String getLocalLabel(final Locale locale) {
        return LabelExtractor.getLocalLabel(owlObjectProperty, ontology, locale);
    }

    public Collection<ObjectPropertyVO> getReferencingAxioms() {
        final Collection<ObjectPropertyVO> referencingAxioms = new ArrayList<ObjectPropertyVO>();
        Collection<OWLAxiom> owlReferencingAxioms = EntitySearcher.getReferencingAxioms(this.owlObjectProperty,
                ontology);
        for (OWLAxiom owlAxiom : owlReferencingAxioms) {
            owlAxiom.accept(
            /* Direct subclass of visitor pattern */
            new OWLAxiomVisitorAdapter() {

                public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                    referencingAxioms.add(new ObjectPropertyVO(ObjectPropertyVO.this.ontology, axiom));
                }
            });
        }
        Collections.sort((ArrayList<ObjectPropertyVO>) referencingAxioms);
        return referencingAxioms;
    }

    public String getIri() {
        return owlObjectProperty.getIRI().toString();
    }

    public int compareTo(ObjectPropertyVO compare) {
        if (this.getPropertyLabel() != null) {

            int propertyLabelCompare = this.getPropertyLabel().compareTo(compare.getPropertyLabel());
            if (propertyLabelCompare == 0) {
                return this.getSubject().compareTo(compare.getSubject());
                // TODO: is subjectCompare == 0 do "object compare"
            } else {
                return propertyLabelCompare;
            }
        }
        return 0;
    }

    public boolean isNegative() {
        return negative;
    }

}
