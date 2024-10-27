package com.yourdomain;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
//
//        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
//
//        // Creating the main classes
//        OntClass weather = model.createClass(namespace + "Weather");
//        OntClass vehicle = model.createClass(namespace + "Vehicle");
//
//        // Creating subclasses of Weather
//        OntClass sunnyWeather = model.createClass(namespace + "SunnyWeather");
//        OntClass rainyWeather = model.createClass(namespace + "RainyWeather");
//        sunnyWeather.addSuperClass(weather);
//        rainyWeather.addSuperClass(weather);
//
//        // Creating subclasses of Vehicle
//        OntClass scooter = model.createClass(namespace + "Scooter");
//        OntClass electricVehicle = model.createClass(namespace + "ElectricVehicle");
//        OntClass combustionVehicle = model.createClass(namespace + "CombustionVehicle");
//        OntClass bicycle = model.createClass(namespace + "Bicycle");
//        scooter.addSuperClass(vehicle);
//        electricVehicle.addSuperClass(vehicle);
//        combustionVehicle.addSuperClass(vehicle);
//        bicycle.addSuperClass(vehicle);
//
//        // Saving the model to an RDF/XML format
//        model.write(System.out, "RDF/XML");
        // Path to the RDF file
//        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
//
//        // Use ClassLoader to load the RDF file from resources
//        InputStream in = Main.class.getClassLoader().getResourceAsStream("ontology/planificateur.rdf");
//        if (in == null) {
//            throw new IllegalArgumentException("File not found in resources/ontology directory");
//        }
//
//        // Read the RDF/XML file
//        model.read(in, null, "RDF/XML");
//        System.out.println("Ontology loaded successfully.");
//
//        // List all the classes in the model
//        System.out.println("Classes in the ontology:");
//        for (OntClass cls : model.listClasses().toList()) {
//            System.out.println(cls.getURI());
//        }
//
//        // Listing Object Properties
//        System.out.println("Object Properties:");
//        model.listObjectProperties().toList().forEach(prop -> {
//            System.out.println(prop.getURI());
//        });
//
//        // Listing Data Properties
//        System.out.println("Data Properties:");
//        model.listDatatypeProperties().toList().forEach(prop -> {
//            System.out.println(prop.getURI());
//        });
        SpringApplication.run(Main.class, args);

    }
}