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
public class ObjectPropertiesGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport object properties\n");
        int counter = 0;
        for (ObjectPropertyVO objectPropertyVO : ontologyVO.getObjectProperties()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }

            // SETUP TEMPLATE MODEL
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("objectProperty", objectPropertyVO);
            model.put("ontology", ontologyVO);
            model.put("context", context);
            model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

            String outputFile = objectPropertyVO.getPropertyLabel() + ".html";

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

}
