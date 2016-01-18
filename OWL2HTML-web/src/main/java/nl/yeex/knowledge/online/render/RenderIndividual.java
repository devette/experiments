package nl.yeex.knowledge.online.render;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.IndividualVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;
import nl.yeex.knowledge.online.Session;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.yeex.knowledge.online.ModelID.*;

public class RenderIndividual {

    private static final Logger LOG = LoggerFactory.getLogger(RenderIndividual.class);

    /**
     * @param sessionData
     * @param iri
     * @throws IOException
     */
    public void generateIndividualResponse(@Nonnull Session sessionData, @Nonnull String iri, @Nonnull OutputStream out)
            throws IOException {

        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLIndividual owlIndividual = sessionData.getIndividualByIRI(iri);
        IndividualVO individualVO = new IndividualVO(ontology, owlIndividual);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_INDIVIDUAL, individualVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__individual.ftl"; // default;

        for (ClassVO individualClassVO : individualVO.getClasses()) {

            // it might be really useful to have
            // "class specific layout templates",
            // so if there is a template with the name of the class, it will be
            // used
            // instead of the default.
            String classSpecificTemplate = "individual_" + individualClassVO.getFilename() + ".ftl";
            if (context.templateExists(classSpecificTemplate)) {
                templateFile = classSpecificTemplate;
            }
        }

        // GENERATE
        context.generate(templateFile, model, out);
    }

}
