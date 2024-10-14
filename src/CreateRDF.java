import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;

public class CreateRDF {
    public void create(){
        String baseURI = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        Model model = ModelFactory.createDefaultModel();

        Resource ontology = model.createResource("http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques")
                .addProperty(OWL.sameAs, baseURI);

        model.write(System.out, "RDF/XML");
    }
}
