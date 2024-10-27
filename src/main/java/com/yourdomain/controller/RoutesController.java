package com.yourdomain.controller;

import com.yourdomain.OntologyService;
import com.yourdomain.SPARQLQueries;
import com.yourdomain.model.RouteDTO;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

@RestController
@RequestMapping("/routes")
public class RoutesController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private SPARQLQueries sparqlQueries;

    // List all classes in the ontology
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

    // List all properties (object and datatype) in the ontology
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

    // Retrieve all Route instances
    @GetMapping("/allRoutes")
    public String getAllRoutes() {
        ResultSet results = sparqlQueries.queryRoutes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        return outputStream.toString();
    }

    // Create a new Route instance and return JSON-LD
    @PostMapping("/createRoute")
    public String createRoute(@RequestBody RouteDTO routeDTO) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        OntClass routeClass = model.getOntClass(namespace + "Route");

        if (routeClass == null) {
            return "{\"error\": \"Route class not found in ontology.\"}";
        }

        // Creating a new individual for the Route class
        Individual newRoute = routeClass.createIndividual(namespace + routeDTO.getRouteName());
        Property distanceValue = ResourceFactory.createProperty(namespace + "DistanceValue");
        Property durationValue = ResourceFactory.createProperty(namespace + "DurationValue");

        // Adding properties to the new route individual
        newRoute.addProperty(distanceValue, model.createTypedLiteral(routeDTO.getDistanceValue()));
        newRoute.addProperty(durationValue, model.createTypedLiteral(routeDTO.getDurationValue()));

        ontologyService.saveModel();

        // Output as JSON-LD
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }
}
