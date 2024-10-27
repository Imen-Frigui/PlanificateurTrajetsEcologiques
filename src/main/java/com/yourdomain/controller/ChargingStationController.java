package com.yourdomain.controller;

import com.yourdomain.OntologyService;
import com.yourdomain.SPARQLQueries;
import com.yourdomain.model.ChargingStationDTO;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

@RestController
@RequestMapping("/ontology")
@CrossOrigin(origins = "http://localhost:3000/")
public class ChargingStationController {

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private SPARQLQueries sparqlQueries;

    @GetMapping("/charging")
    public String listChargingStations() {
        ResultSet results = sparqlQueries.queryAllChargingStations();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        String json = new String(outputStream.toByteArray());

        JSONObject j = new JSONObject(json);
        return j.getJSONObject("results").getJSONArray("bindings").toString();
    }

    @PostMapping("/create-charging")
    public String createChargingStation(@RequestBody ChargingStationDTO chargingStationDTO) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";

        OntClass chargingStationClass = model.getOntClass(namespace + "ChargingStation");

        if (chargingStationClass == null) {
            return "{\"error\": \"ChargingStation class not found in ontology.\"}";
        }

        Individual newStation = chargingStationClass.createIndividual(namespace + chargingStationDTO.getStationName());

        Property chargesVehicle = model.getProperty(namespace + "chargesVehicle");
        Property chargingSpeed = model.getProperty(namespace + "ChargingSpeed");
        Property fastCharging = model.getProperty(namespace + "FastCharging");
        Property stationType = model.getProperty(namespace + "StationType");

        newStation.addProperty(chargesVehicle, model.createResource(namespace + chargingStationDTO.getVehicleId()));
        newStation.addLiteral(chargingSpeed, chargingStationDTO.getChargingSpeed());
        newStation.addLiteral(fastCharging, chargingStationDTO.isFastCharging());
        newStation.addLiteral(stationType, chargingStationDTO.getStationType());

        ontologyService.saveModel();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }

    @GetMapping("/station/{stationId}")
    public String getChargingStationDetails(@PathVariable String stationId) {
        OntModel model = ontologyService.getModel();
        String namespace = "http://www.semanticweb.org/imenfrigui/ontologies/2024/8/PlanificateurTrajetsEcologiques#";

        Individual station = model.getIndividual(namespace + stationId);
        if (station == null) {
            return "{\"error\": \"ChargingStation not found.\"}";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        station.listProperties().forEachRemaining(statement -> {
            model.add(statement);
        });

        RDFDataMgr.write(outputStream, model, Lang.JSONLD);
        return outputStream.toString();
    }
}
