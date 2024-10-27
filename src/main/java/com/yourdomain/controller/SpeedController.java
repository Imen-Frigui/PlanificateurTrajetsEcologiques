package com.yourdomain.controller;

import com.yourdomain.OntologyService;
import com.yourdomain.SPARQLQueries;
import com.yourdomain.model.SpeedDTO;
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

import static javax.xml.stream.XMLStreamConstants.NAMESPACE;

@RestController
@RequestMapping("/speed")
public class SpeedController {

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

    // Retrieve all Speed instances
    @GetMapping("/allSpeeds")
    public String getAllSpeeds() {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        OntClass speedClass = model.getOntClass(namespace + "Speed");

        if (speedClass == null) {
            return "{\"error\": \"Speed class not found in ontology.\"}";
        }

        // List only individuals of class Speed
        StringWriter writer = new StringWriter();
        speedClass.listInstances().forEachRemaining(individual -> {
            writer.write("Individual: " + individual.getURI() + "\n");
        });

        return writer.toString();
    }
    // Create a new Speed instance and return JSON-LD
    @PostMapping("/createSpeed")
    public String createSpeed(@RequestBody SpeedDTO speedDTO) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        OntClass speedClass = model.getOntClass(namespace + "Speed");

        if (speedClass == null) {
            return "{\"error\": \"Speed class not found in ontology.\"}";
        }

        // Creating a new individual for the Speed class
        Individual newSpeed = speedClass.createIndividual(namespace + speedDTO.getSpeedName());

        // Properties from the ontology
        Property speedValue = ResourceFactory.createProperty(namespace + "SpeedValue");
        Property slowSpeed = ResourceFactory.createProperty(namespace + "SlowSpeed");
        Property mediumSpeed = ResourceFactory.createProperty(namespace + "MediumSpeed");
        Property fastSpeed = ResourceFactory.createProperty(namespace + "FastSpeed");

        // Adding properties to the new speed individual
        newSpeed.addProperty(speedValue, model.createTypedLiteral(speedDTO.getSpeedValue()));
        newSpeed.addProperty(slowSpeed, model.createTypedLiteral(speedDTO.isSlowSpeed()));
        newSpeed.addProperty(mediumSpeed, model.createTypedLiteral(speedDTO.isMediumSpeed()));
        newSpeed.addProperty(fastSpeed, model.createTypedLiteral(speedDTO.isFastSpeed()));

        ontologyService.saveModel();

        // Output as JSON-LD
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }

    // Retrieve a specific Speed instance by ID
    @GetMapping("/{id}")
    public String getSpeedById(@PathVariable String id) {
        OntModel model = ontologyService.getModel();
        Individual speedIndividual = model.getIndividual(NAMESPACE + id);

        if (speedIndividual == null) {
            return "{\"error\": \"Speed instance not found.\"}";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }

    // Update an existing Speed instance
    @PutMapping("/{id}")
    public String updateSpeed(@PathVariable String id, @RequestBody SpeedDTO speedDTO) {
        OntModel model = ontologyService.getModel();
        Individual speedIndividual = model.getIndividual(NAMESPACE + id);

        if (speedIndividual == null) {
            return "{\"error\": \"Speed instance not found.\"}";
        }

        // Update properties
        Property speedValue = model.getProperty(NAMESPACE + "SpeedValue");
        Property slowSpeed = model.getProperty(NAMESPACE + "SlowSpeed");
        Property mediumSpeed = model.getProperty(NAMESPACE + "MediumSpeed");
        Property fastSpeed = model.getProperty(NAMESPACE + "FastSpeed");

        speedIndividual.removeAll(speedValue)
                .addProperty(speedValue, model.createTypedLiteral(speedDTO.getSpeedValue()));
        speedIndividual.removeAll(slowSpeed)
                .addProperty(slowSpeed, model.createTypedLiteral(speedDTO.isSlowSpeed()));
        speedIndividual.removeAll(mediumSpeed)
                .addProperty(mediumSpeed, model.createTypedLiteral(speedDTO.isMediumSpeed()));
        speedIndividual.removeAll(fastSpeed)
                .addProperty(fastSpeed, model.createTypedLiteral(speedDTO.isFastSpeed()));

        ontologyService.saveModel();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }

    // Delete a Speed instance by ID
    @DeleteMapping("/{id}")
    public String deleteSpeed(@PathVariable String id) {
        OntModel model = ontologyService.getModel();
        Individual speedIndividual = model.getIndividual(NAMESPACE + id);

        if (speedIndividual == null) {
            return "{\"error\": \"Speed instance not found.\"}";
        }

        // Remove individual from the model
        speedIndividual.remove();
        ontologyService.saveModel();

        return "{\"success\": \"Speed instance deleted successfully.\"}";
    }
}
