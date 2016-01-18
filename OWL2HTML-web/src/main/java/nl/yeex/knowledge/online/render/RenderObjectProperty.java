package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.adapters.ObjectPropertyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderObjectProperty {

    private static final Logger LOG = LoggerFactory.getLogger(RenderObjectProperty.class);

    /**
     * @param sessionData
     * @throws IOException
     */
    public void generateObjectPropertyResponse(@Nonnull Session sessionData, String iri, @Nonnull OutputStream out)
            throws IOException {

        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLObjectProperty objectPropertyByName = sessionData.getObjectPropertyByIri(iri);
        ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, objectPropertyByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_OBJECTPROPERTY, objectPropertyVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__objectproperty.ftl"; // default;

        String specificTemplate = "objectproperty_" + objectPropertyVO.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, out);
    }

}
