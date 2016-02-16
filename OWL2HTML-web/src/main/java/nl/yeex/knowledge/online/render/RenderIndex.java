package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderIndex {

    private static final Logger LOG = LoggerFactory.getLogger(RenderIndex.class);

    /**
     * @param sessionData
     * @throws IOException
     */
    public void generateIndexResponse(@Nonnull Session sessionData, @Nonnull OutputStream out)
            throws IOException {

        GeneratorContext context = sessionData.getContext();

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // GENERATE
        context.generate("__index.ftl", model, out);
    }

}
