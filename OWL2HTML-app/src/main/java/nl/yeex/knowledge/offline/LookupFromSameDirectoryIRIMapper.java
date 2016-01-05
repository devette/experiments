package nl.yeex.knowledge.offline;

import nl.yeex.knowledge.offline.generators.GeneratorContext;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

public class LookupFromSameDirectoryIRIMapper implements OWLOntologyIRIMapper {

	private static final String MY_NAMESPACE = "http://www.NewOnto1.org/";
	private GeneratorContext context;

	public LookupFromSameDirectoryIRIMapper(GeneratorContext context)  {
		this.context = context;
	}
	
 	public IRI getDocumentIRI(IRI lookupIRI) {
		if (lookupIRI.toString().startsWith(MY_NAMESPACE))  {
			String importOnto = lookupIRI.toString().substring(MY_NAMESPACE.length());
			String mapped = "file://" + context.getOwlSourceFile().getAbsoluteFile().getParent() +"/" + importOnto+".owl";
			context.log("Loading " + lookupIRI +" from filesystem: " + mapped);
			return IRI.create(mapped);
		}
		return null;
	}

}
