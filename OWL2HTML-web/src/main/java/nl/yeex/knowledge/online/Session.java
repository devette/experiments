package nl.yeex.knowledge.online;

import javax.annotation.Nonnull;

import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.core.inference.Materializer;

import nl.yeex.knowledge.online.render.*;
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Session {
    public enum RenderType {ONTOLOGY, CLASS, INDIVIDUAL, OBJECTPROPERTY, DATAPROPERTY, INDEX};
    public enum ActiveOntology {LOADED, INFERRED};

    private static final Logger LOG = LoggerFactory.getLogger(Session.class);

    private GeneratorContext context;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLOntology ontology;
    private OWLOntology loadedOntology;
    private OWLOntology inferredOntology;
    private String localBasePath;
    private LastRendered lastRendered;
    private Integer graphDepth;
    private String theme;
    private OntologyVO loadedOntologyVO;
    private OntologyVO inferredOntologyVO;
    private OntologyVO ontologyVO;
    private Map<String, OWLOntology> loadedOntologies;

    private boolean materialized;

    public Session(String documentRoot) {
        super();

        setLocalBasePath(documentRoot);

        // initial values.
        graphDepth = 3;
        theme = "fancy";
        loadedOntologies = new HashMap<String, OWLOntology>();
        manager = OWLManager.createOWLOntologyManager();
        manager.addMissingImportListener(new MissingImportListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void importMissing(MissingImportEvent event) {
                LOG.error("Import failed: " + event.getImportedOntologyURI());
            }
        });

        // TODO: get a document from the document root, or generate an index page and let a user select.
        String defaultOntology = "calim.owl";
        setCurrentOntology(documentRoot + "/owl/" + defaultOntology);
        setLastRendered(RenderType.INDEX, defaultOntology);

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
            setActiveOntology(ActiveOntology.LOADED);
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

    /**
     * Toggle the ontology, from loaded to inferred or vice versa.
     *
     * @param activeOntology, LOADED of INFERRED
     */
    public void setActiveOntology(ActiveOntology activeOntology) {
        switch (activeOntology)  {
            case INFERRED:
                if (!materialized) {
                    materializeOntology();
                }
                // work with inferred ontology.
                ontology = inferredOntology;
                ontologyVO = inferredOntologyVO;
                break;
            case LOADED:
                // work with loaded ontology.
                ontology = loadedOntology;
                ontologyVO = loadedOntologyVO;
                break;
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

    public File getOntologyDirectory() {
        return new File(localBasePath +  "/owl/");
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

    public void updateGraphDepthInSession(String graphDepthParameter) {
        if (graphDepthParameter != null) {
            Integer graphDepth = 3;
            try {
                graphDepth = Integer.valueOf(graphDepthParameter);
            } catch (NumberFormatException ne) {
                LOG.warn("not a number: " + graphDepthParameter);
            }
            setGraphDepth(graphDepth);
        }
    }

    public void renderClass(@Nonnull String iri, @Nonnull OutputStream out)  throws IOException {
        new RenderClass().generateClassResponse(this, iri, out);
        setLastRendered(RenderType.CLASS, iri);
    }

    public void renderIndividual(@Nonnull String iri, @Nonnull OutputStream out)  throws IOException {
        new RenderIndividual().generateIndividualResponse(this, iri, out);
        setLastRendered(RenderType.INDIVIDUAL, iri);
    }

    public void renderObjectProperty(@Nonnull String iri, @Nonnull OutputStream out)  throws IOException {
        new RenderObjectProperty().generateObjectPropertyResponse(this, iri, out);
        setLastRendered(RenderType.OBJECTPROPERTY, iri);
    }

    public void renderDataProperty(@Nonnull String iri, @Nonnull OutputStream out)  throws IOException {
        new RenderDataProperty().generateDataPropertyResponse(this, iri, out);
        setLastRendered(RenderType.DATAPROPERTY, iri);
    }

    public void renderOntology(@Nonnull String name, @Nonnull OutputStream out)  throws IOException {
        new RenderOntology().generateOntologyResponse(this, name, out);
        setLastRendered(RenderType.ONTOLOGY, name);
    }

    public void toggleInferences(ActiveOntology activeOntology, @Nonnull OutputStream out)  throws IOException {
        setActiveOntology(activeOntology);
        renderLastObjectAgain(out);
    }

    public void renderLastObjectAgain(@Nonnull OutputStream out) throws IOException {
        switch (lastRendered.getRenderType())  {
            case CLASS:
                new RenderClass().generateClassResponse(this, lastRendered.getName(), out);
                break;
            case INDIVIDUAL:
                new RenderIndividual().generateIndividualResponse(this, lastRendered.getName(), out);
                break;
            case OBJECTPROPERTY:
                new RenderObjectProperty().generateObjectPropertyResponse(this, lastRendered.getName(), out);
                break;
            case DATAPROPERTY:
                new RenderDataProperty().generateDataPropertyResponse(this, lastRendered.getName(), out);
                break;
            case ONTOLOGY:
                new RenderOntology().generateOntologyResponse(this, lastRendered.getName(), out);
                break;
            default:
                new RenderIndex().generateIndexResponse(this, out);
                break;
        }
    }

    public void setLastRendered(@Nonnull RenderType renderType, @Nonnull String name)  {
        this.lastRendered = new LastRendered(renderType, name);
    }

    public LastRendered getLastRendered()  {
        return lastRendered;
    }

    public class LastRendered  {
        final RenderType renderType;
        final String name;

        protected LastRendered(final RenderType renderType, final String name) {
            this.renderType = renderType;
            this.name = name;
        }

        public String getName() {
            return name;
        }


        public RenderType getRenderType() {
            return renderType;
        }

    }

    public void setTheme(String theme) {
        if (theme != null) {
            this.theme = theme;
            context.setTheme(theme);
        }
    }
}
