package nl.yeex.knowledge.offline.generators;

import java.util.HashMap;
import java.util.Map;

import nl.yeex.knowledge.core.adapters.ObjectPropertyVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class ObjectPropertyGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport object properties\n");
        int counter = 0;
        for (ObjectPropertyVO objectPropertyVO : ontologyVO.getObjectProperties()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            generateObjectProperty(context, ontologyVO, objectPropertyVO, objectPropertyVO.getPropertyLabel() + ".html");
        }
    }

    /**
     * Given an object propertyl from an ontology, does the following:
     * Determine the template for the individual.
     *   - by default this will be the file __objectproperty.ftl
     *   - or when a file exists with the name of the class, that template will be used: objectproperty_{propertylabel}.ftl
     * Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param objectPropertyVO the object property to generate.
     */
    public static void generateObjectProperty(final GeneratorContext context,
                                              final OntologyVO ontologyVO,
                                              final ObjectPropertyVO objectPropertyVO,
                                              final String outputFile) {
        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectProperty", objectPropertyVO);
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__objectproperty.ftl"; // default;

        String specificTemplate = "objectproperty_" + objectPropertyVO.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE

        context.generate(templateFile, model, outputFile);
    }

}
