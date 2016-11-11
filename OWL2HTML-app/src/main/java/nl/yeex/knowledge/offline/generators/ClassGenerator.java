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
public class ClassGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport classes (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        int counter = 0;
        for (ClassVO classVO : ontologyVO.getClasses()) {
            context.log("#");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            generateClass(context, ontologyVO, classVO, classVO.getFilename() + ".html");

            for (OWLOntology imported : ontology.getImports()) {
                generate(context, imported);
            }
        }
    }

    /**
     * Given a class from an ontology, does the following:
     * Determine the template for the individual.
     *   - by default this will be the file __class.ftl
     *   - or when a file exists with the name of the class, that template will be used: __class_{classname}.ftl
     *  Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param classVO the class to generate.
     */
    public static void generateClass(final GeneratorContext context,
                                     final OntologyVO ontologyVO,
                                     final ClassVO classVO,
                                     final String outputFile) {
        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("clazz", classVO);
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__class.ftl"; // default;

        String specificTemplate = "class_" + classVO.getFilename() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, outputFile);
    }

}
