package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderOntology {

    private static final Logger LOG = LoggerFactory.getLogger(RenderOntology.class);

    /**
     * @param sessionData
     * @throws IOException
     */
    public void generateOntologyResponse(@Nonnull Session sessionData, String name, @Nonnull OutputStream out)
            throws IOException {

        // TODO: security!!!!

        sessionData.setCurrentOntology(sessionData.getLocalBasePath() + "/owl/" + name);

        GeneratorContext context = sessionData.getContext();

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // GENERATE
        context.generate("__ontology.ftl", model, out);
    }

}
