package nl.yeex.knowledge.offline.generators;

import nl.yeex.knowledge.core.adapters.*;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Static methods to use the templates with ontology elements (ontology, individual, class, objectproperty, dataproperty).
 */
public class Templates {
    private static final Logger LOG = LoggerFactory.getLogger(Templates.class);

    /**
     * Given a class from an ontology, does the following:
     * Determine the template for the individual.
     *   - by default this will be the file __class.ftl
     *   - or when a file exists with the name of the class, that template will be used: class_{classname}.ftl
     *  Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param classVO the class to generate.
     * @param outputFile The name of the outputfile that will be generated.
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


    /**
     * Given a dataproperty from an ontology, does the following:
     * Determine the template for the dataproperty.
     *   - by default this will be the file __dataproperty.ftl
     *   - or when a file exists with the name of the class, that template will be used: dataproperty_{propertylabel}.ftl
     *  Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param dataProperty the class to generate.
     * @param outputFile The name of the outputfile that will be generated.
     */
    public static void generateDataProperty(final GeneratorContext context,
                                            final OntologyVO ontologyVO,
                                            final DataPropertyVO dataProperty,
                                            final String outputFile) {
        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dataProperty", dataProperty);
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__dataproperty.ftl"; // default;

        String specificTemplate = "dataproperty_" + dataProperty.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, outputFile);
    }

    /**
     * Given a individual from an ontology, does the following:
     * Determine the template for the individual.
     *   - by default this will be the file __individual.ftl
     *   - or when a file exists with the name of the class, that template will be used: individual_{classname}.ftl
     * Then generate the output using the matching template
     *
     * @param context the generation context
     * @param ontologyVO the ontology
     * @param individualVO the individual to generate.
     * @param outputFile The name of the outputfile that will be generated.
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
     * @param outputFile The name of the outputfile that will be generated.
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


    /**
     * Generate an index file for the ontology.
     *
     * @param context the generation context, containing paths.
     * @param ontologyVO the ontology.
     * @param outputFile The name of the outputfile that will be generated.
     */
    public static void generateOntology(final GeneratorContext context,
                                     final OntologyVO ontologyVO,
                                     final String outputFile) {
        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // GENERATE
        context.generate("__ontology.ftl", model, outputFile);
    }

    /**
     * Copy the static files that are needed for the generated files. i.e. css and javascript files.
     *
     * @param context the generator context, containing configuration of paths.
     */
    public static void copyStaticFiles(final GeneratorContext context) {
        try {
            FileUtils.copyDirectory(new File(context.getStaticDirectory()), new File(context.getOutputDirectory()));
        } catch (IOException e) {
            LOG.error("Error while copying static files.", e);
        }
    }

}
