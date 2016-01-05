package nl.yeex.knowledge.offline.generators;

import java.util.HashMap;
import java.util.Map;

import nl.yeex.knowledge.core.adapters.OntologyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 *
 */
public class IndexGenerator implements Generator {

    public void generate(GeneratorContext context, OWLOntology ontology) {
        context.log("\nExport index (" + ontology.getOntologyID().getOntologyIRI() + ")\n");
        OntologyVO ontologyVO = new OntologyVO(ontology);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ontology", ontologyVO);
        model.put("context", context);
        model.put("messages", new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String outputFile = "index.html";

        // GENERATE
        context.generate("__ontology.ftl", model, outputFile);

        for (OWLOntology imported : ontology.getImports()) {
            generate(context, imported);
        }
    }
}
