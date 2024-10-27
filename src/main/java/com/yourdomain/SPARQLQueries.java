package com.yourdomain;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

@Service
public class SPARQLQueries {

    private OntModel model;

    public SPARQLQueries() {
        // Initialization of the model should be done here, or it should be injected.
        this.model = ModelFactory.createOntologyModel();
        // Assuming the RDF model is loaded here from your RDF file
    }

    public ResultSet queryWeather() {
        String queryString = """
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            SELECT ?weather WHERE {
                ?weather rdf:type <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#WeatherCondition>
            }
            """;

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }
}
