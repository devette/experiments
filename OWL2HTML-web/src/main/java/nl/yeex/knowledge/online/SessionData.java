package nl.yeex.knowledge.online;

import javax.annotation.Nonnull;

import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.core.inference.Materializer;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportEvent;
import org.semanticweb.owlapi.model.MissingImportListener;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SessionData {
    private static final Logger LOG = LoggerFactory.getLogger(SessionData.class);

    private GeneratorContext context;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLOntology ontology;
    private OWLOntology loadedOntology;
    private OWLOntology inferredOntology;
    private String localBasePath;
    private Integer graphDepth;
    private String theme;
    private OntologyVO loadedOntologyVO;
    private OntologyVO inferredOntologyVO;
    private OntologyVO ontologyVO;
    private Map<String, OWLOntology> loadedOntologies;

    private boolean materialized;

    public SessionData() {
        super();

        // initial values.
        graphDepth = 3;
        theme = "eshopper";
        loadedOntologies = new HashMap<String, OWLOntology>();
        manager = OWLManager.createOWLOntologyManager();
        manager.addMissingImportListener(new MissingImportListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void importMissing(MissingImportEvent event) {
                LOG.error("Import failed: " + event.getImportedOntologyURI());
            }
        });
    }

    public void setCurrentOntology(String ontologyFile) {
        context = new GeneratorContext();
        context.setOwlSourceFileName(ontologyFile);
        context.setThemesDirectory(localBasePath + "/themes/");
        context.setTheme(theme);
        try {
            factory = manager.getOWLDataFactory();
            FileDocumentSource documentSource = new FileDocumentSource(context.getOwlSourceFile());
            IRI iri = documentSource.getDocumentIRI();
            if (loadedOntologies.get(iri.toString()) != null) {
                loadedOntology = loadedOntologies.get(iri.toString());
            } else {
                loadedOntology = manager.loadOntologyFromOntologyDocument(documentSource);
                loadedOntologies.put(iri.toString(), loadedOntology);
            }
            loadedOntologyVO = new OntologyVO(loadedOntology);
            materialized = false;
            setUseInferredOntology(false);
        } catch (OWLOntologyCreationException e) {
            LOG.error("Ontology loading failed.", e);
        }
    }

    private void materializeOntology() {
        try {
            inferredOntology = Materializer.materializeInferences(manager, loadedOntology, context);
            inferredOntologyVO = new OntologyVO(inferredOntology);
        } catch (InconsistentOntologyException e) {
            LOG.error("Ontology is not consistent.", e);
        } catch (OWLOntologyCreationException e) {
            LOG.error("Materialized Ontology creation failed.", e);
        }
    }

    public void setUseInferredOntology(boolean enableInference) {
        if (enableInference) {
            if (!materialized) {
                materializeOntology();
            }
            // work with inferred ontology.
            ontology = inferredOntology;
            ontologyVO = inferredOntologyVO;
        } else {

            // work with loaded ontology.
            ontology = loadedOntology;
            ontologyVO = loadedOntologyVO;
        }
    }

    /**
     * @param iriString an IRI identifying an owlClass
     * @return the owlClass
     */
    public OWLClass getClassByIri(@Nonnull String iriString) {
        IRI iri = IRI.create(iriString);
        return factory.getOWLClass(iri);
    }

    public OWLNamedIndividual getIndividualByIRI(@Nonnull String iriString) {
        IRI iri = IRI.create(iriString);
        return factory.getOWLNamedIndividual(iri);
    }

    /**
     * @param iriString an IRI identifying an owlObjectProperty
     * @return the owlObjectProperty
     */
    public OWLObjectProperty getObjectPropertyByIri(@Nonnull String iriString) {
        IRI iri = IRI.create(iriString);
        return factory.getOWLObjectProperty(iri);
    }

    /**
     * @param iriString an IRI identifying an owlDataProperty
     * @return the owlDataProperty
     */
    public OWLDataProperty getDataPropertyByIri(@Nonnull String iriString) {
        IRI iri = IRI.create(iriString);
        return factory.getOWLDataProperty(iri);
    }

    public String getLocalBasePath() {
        return localBasePath;
    }

    public void setLocalBasePath(String localBasePath) {
        this.localBasePath = localBasePath;
    }

    public GeneratorContext getContext() {
        return context;
    }

    public void setContext(GeneratorContext context) {
        this.context = context;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLOntology getLoadedOntology() {
        return loadedOntology;
    }

    public void setLoadedOntology(OWLOntology loadedOntology) {
        this.loadedOntology = loadedOntology;
    }

    public OWLOntology getInferredOntology() {
        return inferredOntology;
    }

    public void setInferredOntology(OWLOntology inferredOntology) {
        this.inferredOntology = inferredOntology;
    }

    public void setGraphDepth(Integer graphDepth) {
        this.graphDepth = graphDepth;
    }

    public Integer getGraphDepth() {
        return graphDepth;
    }

    public OntologyVO getOntologyVO() {
        return ontologyVO;
    }

}
