package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderClass {

    private static final Logger LOG = LoggerFactory.getLogger(RenderClass.class);

    /**
     * @param sessionData
     * @param iri
     * @throws IOException
     */
    public void generateClassResponse(@Nonnull Session sessionData, @Nonnull String iri, @Nonnull OutputStream out)
            throws IOException {
        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLClass classByName = sessionData.getClassByIri(iri);
        ClassVO classVO = new ClassVO(ontology, classByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_CLAZZ, classVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        LOG.debug("Class: " + classVO);

        // DETERMINE TEMPLATE:
        String templateFile = "__class.ftl"; // default;

        String specificTemplate = "class_" + classVO.getFilename() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, out);
    }
}
