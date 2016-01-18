package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.adapters.DataPropertyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderDataProperty {

    private static final Logger LOG = LoggerFactory.getLogger(RenderDataProperty.class);

    /**
     * @param sessionData
     * @throws IOException
     */
    public void generateDataPropertyResponse(@Nonnull Session sessionData, String iri, @Nonnull OutputStream out)
            throws IOException {

        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLDataProperty dataPropertyByName = sessionData.getDataPropertyByIri(iri);
        DataPropertyVO dataPropertyVO = new DataPropertyVO(ontology, dataPropertyByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_DATAPROPERTY, dataPropertyVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__dataproperty.ftl"; // default;

        String specificTemplate = "dataproperty_" + dataPropertyVO.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, out);
    }

}
