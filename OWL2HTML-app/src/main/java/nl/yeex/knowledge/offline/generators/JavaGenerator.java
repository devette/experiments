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
public class JavaGenerator implements Generator {

    public void generate(GeneratorContext context, OWLOntology ontology) {
        OntologyVO ontologyVO = new OntologyVO(ontology);
        context.log("\nExport classes\n");

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        for (ClassVO classVO : ontologyVO.getClasses()) {
            model.put(classVO.getIriId(), classVO);

            // DETERMINE TEMPLATE:
            String templateFile = classVO.getIriId() + ".ftl"; // default
                                                               // template
            if (context.templateExists(templateFile)) {
                for (IndividualVO individualVO : classVO.getIndividuals()) {
                    String outputFile = individualVO.getIriId() + ".java";
                    model.put("individual", individualVO);

                    // GENERATE
                    context.generate(templateFile, model, outputFile);
                }
            }

        }
    }

}
