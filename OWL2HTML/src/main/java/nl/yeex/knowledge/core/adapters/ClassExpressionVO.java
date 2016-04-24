package nl.yeex.knowledge.core.adapters;

import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by herman on 30-03-16.
 */
public class ClassExpressionVO {
    protected OWLOntology ontology;
    protected OWLClassExpression owlClassExpression;

    /**
     * Constructor for this OWLClass wrapper.
     *
     * @param ontology
     *            the ontology.
     * @param owlClassExpression
     *            the OWLClassExpression to wrap.
     */
    public ClassExpressionVO(OWLOntology ontology, OWLClassExpression owlClassExpression) {
        this.ontology = ontology;
        this.owlClassExpression = owlClassExpression;
    }

    public boolean hasOperands()  {
        return owlClassExpression instanceof OWLNaryBooleanClassExpression;
    }

    public String toString()  {
        StringBuffer buf = new StringBuffer();
        if (hasOperands())  {
            for (ClassVO operandClassVO: getOperands()) {
                buf.append(operandClassVO.toString());
            }
        }
        return buf.toString();
    }


    public boolean isIntersectionOf()  {
        return owlClassExpression instanceof OWLObjectIntersectionOf;
    }

    public boolean isUnionOf()  {
        return owlClassExpression instanceof OWLObjectUnionOf;
    }

    public boolean isComplementOf()  {
        return owlClassExpression instanceof OWLObjectComplementOf;
    }

    public boolean isSomeValuesFrom()  {
        return owlClassExpression instanceof OWLObjectSomeValuesFrom;
    }

    public boolean isAllValuesFrom()  {
        return owlClassExpression instanceof OWLObjectAllValuesFrom;
    }

    public boolean isHasFiller()  {
        return owlClassExpression instanceof HasFiller;
    }

    public boolean isHasValueRestriction()  {
        return owlClassExpression instanceof OWLHasValueRestriction;
    }

    /**
     * Before calling this method. make sure you checked first if it isHasFiller.
     *
     * @return a mixed list of filler objects (class/instance).
     */
    public List<AbstractOWLEntityVO> getFiller()  {
        assert isHasFiller();
        List<AbstractOWLEntityVO> fillerEntities = new ArrayList<AbstractOWLEntityVO>();
        OWLObject filler = ((HasFiller) owlClassExpression).getFiller();
        for (OWLEntity owlEntity: filler.getSignature())  {
            if (owlEntity instanceof OWLIndividual) {
                fillerEntities.add(new IndividualVO(ontology, (OWLIndividual) owlEntity));
            }
            if (owlEntity instanceof OWLClass) {
                fillerEntities.add(new ClassVO(ontology, owlEntity.asOWLClass()));
            }
        }
        return fillerEntities;
    }

    public List<ClassExpressionVO> getNestedExpressions()  {
        List<ClassExpressionVO> nestedExpressions = new ArrayList<ClassExpressionVO>();
        for(OWLClassExpression owlClassExpression: this.owlClassExpression.getNestedClassExpressions()) {
            nestedExpressions.add(new ClassExpressionVO(ontology, owlClassExpression));
        };
        return nestedExpressions;
    }

    public List<ObjectPropertyVO> getProperties()  {
        List<ObjectPropertyVO> properties = new ArrayList<ObjectPropertyVO>();
        for(OWLObjectProperty owlObjectProperty: owlClassExpression.getObjectPropertiesInSignature()) {
            properties.add(new ObjectPropertyVO(ontology, owlObjectProperty));
        };
        return properties;
    }


    public List<ClassVO> getOperands() {
        List<ClassVO> operands = new ArrayList<ClassVO>();
        if (hasOperands()) {
            for (OWLClassExpression operand : ((OWLNaryBooleanClassExpression) owlClassExpression).getOperands()) {
                if (!operand.isAnonymous()) {
                    ClassVO operandClassVO = new ClassVO(ontology, operand.asOWLClass());
                    operands.add(operandClassVO);
                }
            }
        }
        return operands;
    }

    public Object getValueRestriction() {
        assert isHasValueRestriction();
        OWLHasValueRestriction owlHasValueRestriction = (OWLHasValueRestriction) owlClassExpression;
        OWLObject owlObject = owlHasValueRestriction.getFiller();
        if (owlObject instanceof OWLIndividual)  {
            return new IndividualVO(ontology, (OWLIndividual) owlObject);
        }
        return owlObject;
    }

}
