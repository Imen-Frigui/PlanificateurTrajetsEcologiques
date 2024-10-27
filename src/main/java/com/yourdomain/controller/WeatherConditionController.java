package com.yourdomain.controller;

import com.yourdomain.OntologyService;
import com.yourdomain.SPARQLQueries;
import com.yourdomain.model.Weather;
import com.yourdomain.model.WeatherTypeDTO;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

@RestController
@RequestMapping("/ontology")
public class WeatherConditionController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private SPARQLQueries sparqlQueries;  // Adding this line

    @GetMapping("/classes")
    public String listClasses() {
        Model model = ModelFactory.createDefaultModel();
        ontologyService.getModel().listClasses().toList().forEach(cls -> {
            model.add(cls, model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), cls.getLocalName());
        });

        StringWriter writer = new StringWriter();
        model.write(writer, "RDF/XML");
        return writer.toString();
    }

    @GetMapping("/properties")
    public String listProperties() {
        Model model = ModelFactory.createDefaultModel();
        ontologyService.getModel().listObjectProperties().toList().forEach(prop -> {
            model.add(prop, model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), prop.getLocalName());
        });
        ontologyService.getModel().listDatatypeProperties().toList().forEach(prop -> {
            model.add(prop, model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), prop.getLocalName());
        });

        StringWriter writer = new StringWriter();
        model.write(writer, "RDF/XML");
        return writer.toString();
    }

    @GetMapping("/weather")
    public String getWeather() {
        ResultSet results = sparqlQueries.queryWeather();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        return outputStream.toString();
    }

    @PostMapping("/createWeather")
    @ResponseBody
    public String createWeather(@RequestParam String weatherType) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        OntClass weatherClass = model.getOntClass(namespace + "WeatherCondition");

        if (weatherClass == null) {
            return "Weather class not found in ontology.";
        }

        // Creating a new individual of Weather
        Individual newWeather = weatherClass.createIndividual(namespace + weatherType);

        // Optionally add properties to the new individual
        Property label = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
        newWeather.addProperty(label, weatherType);

        // Save the updated model
        ontologyService.saveModel();

        // Output the result as verification
        StringWriter writer = new StringWriter();
        model.write(writer, "RDF/XML");
        return writer.toString();
    }

    // Method to create a weather condition and return JSON-LD
    @PostMapping("/createWeathers")
    public String createWeather(@RequestBody Weather weather) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";

        // Retrieve the WeatherCondition class from the ontology
        OntClass weatherClass = model.getOntClass(namespace + "WeatherCondition");

        if (weatherClass == null) {
            return "{\"error\": \"Weather class not found in ontology.\"}";
        }

        // Create a new weather individual
        Individual newWeather = weatherClass.createIndividual(namespace + weather.getWeatherType());

        // Fetch properties dynamically from the ontology
        Property weatherTypeProp = model.getProperty(namespace + "WeatherType");
        Property sunnyProp = model.getProperty(namespace + "Sunny");
        Property snowyProp = model.getProperty(namespace + "Snowy");
        Property rainyProp = model.getProperty(namespace + "Rainy");
        Property temperatureProp = model.getProperty(namespace + "Temperature");
        Property affectsTrafficConditionProp = model.getProperty(namespace + "affectsTrafficCondition");

        // Add data properties dynamically from ontology
        if (weatherTypeProp != null) newWeather.addProperty(weatherTypeProp, weather.getWeatherType());
        if (sunnyProp != null) newWeather.addLiteral(sunnyProp, weather.isSunny());
        if (snowyProp != null) newWeather.addLiteral(snowyProp, weather.isSnowy());
        if (rainyProp != null) newWeather.addLiteral(rainyProp, weather.isRainy());
        if (temperatureProp != null) newWeather.addLiteral(temperatureProp, weather.getTemperature());

        // Add object properties dynamically
        if (affectsTrafficConditionProp != null && weather.getAffectsTrafficCondition() != null) {
            Individual trafficCondition = model.getIndividual(namespace + weather.getAffectsTrafficCondition());
            if (trafficCondition != null) {
                newWeather.addProperty(affectsTrafficConditionProp, trafficCondition);
            }
        }

        // Save the updated ontology model
        ontologyService.saveModel();

        // Now query to get all WeatherCondition individuals
        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "SELECT ?weather ?weatherType ?sunny ?snowy ?rainy ?temperature " +
                        "WHERE { " +
                        "  ?weather rdf:type ont:WeatherCondition; " +
                        "          ont:WeatherType ?weatherType; " +
                        "          ont:Sunny ?sunny; " +
                        "          ont:Snowy ?snowy; " +
                        "          ont:Rainy ?rainy; " +
                        "          ont:Temperature ?temperature. " +
                        "}";

        // Execute the query
        QueryExecution qe = QueryExecutionFactory.create(queryString, model);
        ResultSet results = qe.execSelect();

        // Convert the results to JSON
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        String json = new String(outputStream.toByteArray());

        return json;
    }

    @GetMapping("/weathers")
    public String getAllWeathers() {
        OntModel model = ontologyService.getModel();

        String queryString = """
        PREFIX ont: <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        SELECT ?weather ?weatherType ?sunny ?snowy ?rainy ?temperature
        WHERE {
          ?weather rdf:type ont:WeatherCondition;
                   ont:WeatherType ?weatherType;
                   ont:Sunny ?sunny;
                   ont:Snowy ?snowy;
                   ont:Rainy ?rainy;
                   ont:Temperature ?temperature.
        }
    """;

        QueryExecution qe = QueryExecutionFactory.create(queryString, model);
        ResultSet results = qe.execSelect();

        // Convert to JSON
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        return new String(outputStream.toByteArray());
    }

    @GetMapping("/weathers/{id}")
    public String getWeatherById(@PathVariable String id) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        String weatherURI = namespace + id;

        // Use concatenation for the URI in the query
        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "SELECT ?weatherType ?sunny ?snowy ?rainy ?temperature " +
                        "WHERE { " +
                        "<" + weatherURI + "> rdf:type ont:WeatherCondition; " +
                        "ont:WeatherType ?weatherType; " +
                        "ont:Sunny ?sunny; " +
                        "ont:Snowy ?snowy; " +
                        "ont:Rainy ?rainy; " +
                        "ont:Temperature ?temperature. " +
                        "}";

        QueryExecution qe = QueryExecutionFactory.create(queryString, model);
        ResultSet results = qe.execSelect();

        if (!results.hasNext()) {
            return "{\"error\": \"Weather condition not found.\"}";
        }

        // Convert the results to JSON
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        return new String(outputStream.toByteArray());
    }







}
