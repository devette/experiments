package nl.yeex.knowledge.offline.generators;

import java.util.HashMap;
import java.util.Map;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public class ClassesGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport classes (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        int counter = 0;
        for (ClassVO classVO : ontologyVO.getClasses()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }

            // SETUP TEMPLATE MODEL
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("clazz", classVO);
            model.put("ontology", ontologyVO);
            model.put("context", context);
            model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

            String outputFile = classVO.getFilename() + ".html";

            // DETERMINE TEMPLATE:
            String templateFile = "__class.ftl"; // default;

            String specificTemplate = "class_" + classVO.getFilename() + ".ftl";
            if (context.templateExists(specificTemplate)) {
                templateFile = specificTemplate;
            }

            // GENERATE
            context.generate(templateFile, model, outputFile);

            for (OWLOntology imported : ontology.getImports()) {
                generate(context, imported);
            }
        }
    }

}
