package nl.yeex.knowledge.core.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

/**
 * Wrapper class for OWLDataProperty objects. This class hides all visitor
 * patterns and make the OWLDataProperty available for template usage.
 * 
 */
public class DataPropertyVO implements Comparable<DataPropertyVO> {

    private OWLOntology ontology;
    private IndividualVO subject;
    private OWLLiteral object;
    private OWLDataProperty owlDataProperty;

    public DataPropertyVO(OWLOntology ontology, OWLDataPropertyAssertionAxiom axiom) {
        this.subject = new IndividualVO(ontology, axiom.getSubject());
        this.object = axiom.getObject();
        this.owlDataProperty = axiom.getProperty().asOWLDataProperty();
        this.ontology = ontology;
    }

    public DataPropertyVO(OWLOntology ontology, OWLNegativeDataPropertyAssertionAxiom axiom) {
        this.subject = new IndividualVO(ontology, axiom.getSubject());
        this.object = axiom.getObject();
        this.owlDataProperty = axiom.getProperty().asOWLDataProperty();
        this.ontology = ontology;
    }

    public DataPropertyVO(OWLOntology ontology, OWLDataProperty dataProperty) {
        this.ontology = ontology;
        this.owlDataProperty = dataProperty;
    }

    public IndividualVO getSubject() {
        return subject;
    }

    public OWLLiteral getObject() {
        return object;
    }

    public String getPropertyLabel() {
        return owlDataProperty.getIRI().getRemainder().orNull();
    }

    /**
     * Get the locale specific label for this Entity. It will return the first
     * rdfs:label found in the given locale. Otherwise it will return the
     * default label #.
     * 
     * @return
     */
    public String getLocalLabel(final Locale locale) {
        return LabelExtractor.getLocalLabel(owlDataProperty, ontology, locale);

    }

    public String getIri() {
        return owlDataProperty.getIRI().toString();
    }

    public Collection<DataPropertyVO> getReferencingAxioms() {
        final Collection<DataPropertyVO> referencingAxioms = new ArrayList<DataPropertyVO>();
        Collection<OWLAxiom> owlReferencingAxioms = EntitySearcher.getReferencingAxioms(this.owlDataProperty, ontology);
        for (OWLAxiom owlAxiom : owlReferencingAxioms) {
            owlAxiom.accept(
            /* Direct subclass of visitor pattern */
            new OWLAxiomVisitorAdapter() {

                public void visit(OWLDataPropertyAssertionAxiom axiom) {
                    referencingAxioms.add(new DataPropertyVO(DataPropertyVO.this.ontology, axiom));
                }
            });
        }

        Collections.sort((ArrayList<DataPropertyVO>) referencingAxioms);
        return referencingAxioms;
    }

    public int compareTo(DataPropertyVO compare) {
        if (this.getPropertyLabel() != null) {
            int resultLabel = this.getPropertyLabel().compareTo(compare.getPropertyLabel());
            if (resultLabel == 0) {
                this.getObject().compareTo(compare.getObject());
            } else {
                return resultLabel;
            }
        }
        return 0;
    }
}
