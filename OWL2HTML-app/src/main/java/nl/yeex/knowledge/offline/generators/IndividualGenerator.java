package nl.yeex.knowledge.offline.generators;

import java.util.HashMap;
import java.util.Map;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.IndividualVO;
import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public final class IndividualGenerator implements Generator {
    private static final int LINE_MAXIMUM = 40;

    @Override
    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport individuals (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        int counter = 0;
        for (IndividualVO individualVO : ontologyVO.getIndividuals()) {
            context.log(".");
            if (++counter % LINE_MAXIMUM == 0) {
                context.log("\n");
            }
            generateIndividual(context, ontologyVO, individualVO, individualVO.getFilename() + ".html");
            for (OWLOntology imported : ontology.getImports()) {
                generate(context, imported);
            }
        }
    }

    /**
     * Given a individual from an ontology, does the following:
     * Determine the template for the individual.
     *   - by default this will be the file __individual.ftl
     *   - or when a file exists with the name of the class, that template will be used: __individual_{classname}.ftl
     * Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param individualVO the individual to generate.
     */
    public static void generateIndividual(final GeneratorContext context,
                                          final OntologyVO ontologyVO,
                                          final IndividualVO individualVO,
                                          final String outputFile) {
        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("individual", individualVO);
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__individual.ftl"; // default;

        for (ClassVO individualClassVO : individualVO.getClasses()) {

            // it might be really useful to have
            // "class specific layout templates",
            // so if there is a template with the name of the class, it will
            // be used
            // instead of the default.
            String classSpecificTemplate = "individual_" + individualClassVO.getFilename() + ".ftl";
            if (context.templateExists(classSpecificTemplate)) {
                templateFile = classSpecificTemplate;
            }
        }

        // GENERATE
        context.generate(templateFile, model, outputFile);
    }

}
