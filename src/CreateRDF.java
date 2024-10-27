import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class CreateRDF {
    public void create() {
        String baseURI = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";
        Model model = ModelFactory.createDefaultModel();

        // Set the ontology
        model.add(model.createResource(baseURI), RDF.type, OWL.Ontology);

        // Vehicle Class
        String vehicleURI = baseURI + "Vehicle";
        Resource vehicleClass = model.createResource(vehicleURI);
        model.add(vehicleClass, RDF.type, OWL.Class);

        // Subclasses of Vehicle
        String bicycleURI = baseURI + "Bicycle";
        String combustionVehicleURI = baseURI + "CombustionVehicle";
        String electricVehicleURI = baseURI + "ElectricVehicle";
        String scooterURI = baseURI + "Scooter";

        Resource bicycleClass = model.createResource(bicycleURI);
        Resource combustionVehicleClass = model.createResource(combustionVehicleURI);
        Resource electricVehicleClass = model.createResource(electricVehicleURI);
        Resource scooterClass = model.createResource(scooterURI);

        model.add(bicycleClass, RDF.type, OWL.Class);
        model.add(combustionVehicleClass, RDF.type, OWL.Class);
        model.add(electricVehicleClass, RDF.type, OWL.Class);
        model.add(scooterClass, RDF.type, OWL.Class);

        // Define subclass relationships
        model.add(bicycleClass, RDFS.subClassOf, vehicleClass);
        model.add(combustionVehicleClass, RDFS.subClassOf, vehicleClass);
        model.add(electricVehicleClass, RDFS.subClassOf, vehicleClass);
        model.add(scooterClass, RDFS.subClassOf, vehicleClass);

        // WeatherCondition Class
        String weatherConditionURI = baseURI + "WeatherCondition";
        Resource weatherConditionClass = model.createResource(weatherConditionURI);
        model.add(weatherConditionClass, RDF.type, OWL.Class);

        // Subclasses of WeatherCondition
        String rainyURI = baseURI + "Rainy";
        String sunnyURI = baseURI + "Sunny";
        String windyURI = baseURI + "Windy";

        Resource rainyClass = model.createResource(rainyURI);
        Resource sunnyClass = model.createResource(sunnyURI);
        Resource windyClass = model.createResource(windyURI);

        model.add(rainyClass, RDF.type, OWL.Class);
        model.add(sunnyClass, RDF.type, OWL.Class);
        model.add(windyClass, RDF.type, OWL.Class);

        // Define subclass relationships
        model.add(rainyClass, RDFS.subClassOf, weatherConditionClass);
        model.add(sunnyClass, RDFS.subClassOf, weatherConditionClass);
        model.add(windyClass, RDFS.subClassOf, weatherConditionClass);

        // Output the model
        model.write(System.out, "RDF/XML");
    }

}
