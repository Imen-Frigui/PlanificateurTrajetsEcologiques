package com.yourdomain.controller;

import com.yourdomain.OntologyService;
import com.yourdomain.SPARQLQueries;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
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

}
