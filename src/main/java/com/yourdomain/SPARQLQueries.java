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

    public ResultSet queryAllChargingStations() {
        String queryString = "PREFIX ont: <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "SELECT ?station ?chargingSpeed ?fastCharging ?stationType " +
                "WHERE { ?station rdf:type ont:ChargingStation; " +
                "              ont:ChargingSpeed ?chargingSpeed; " +
                "              ont:FastCharging ?fastCharging; " +
                "              ont:StationType ?stationType. }";

        QueryExecution qe = QueryExecutionFactory.create(queryString, model);
        ResultSet results = qe.execSelect();
        return results;
    }

//    public ResultSet queryChargingStationDetails(String stationName) {
//        String queryString = """
//            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
//            PREFIX : <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#>
//            SELECT ?chargingSpeed ?fastCharging ?stationType ?vehicle WHERE {
//                ?station rdf:type :ChargingStation .
//                ?station rdf:type :""" + stationName + """ .
//                ?station :ChargingSpeed ?chargingSpeed .
//                ?station :FastCharging ?fastCharging .
//                ?station :StationType ?stationType .
//                ?station :chargesVehicle ?vehicle .
//            }
//            """;
//
//        Query query = QueryFactory.create(queryString);
//        QueryExecution qexec = QueryExecutionFactory.create(query, model);
//        return qexec.execSelect();
//    }
    public ResultSet queryFastChargingStations() {
        String queryString = """
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX : <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#>
            SELECT ?station WHERE {
                ?station rdf:type :ChargingStation .
                ?station :FastCharging true .
            }
            """;

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }
}
