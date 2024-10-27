package com.yourdomain;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
public class OntologyService {
    private OntModel model;

    @Autowired
    private Environment env; // Spring's environment to access properties

    @PostConstruct
    public void initModel() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        loadModel();
    }

    private void loadModel() {
        // Attempt to load from the external path
        String filePath = env.getProperty("app.ontology.path");
        try (InputStream in = new FileInputStream(filePath)) {
            if (in == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            model.read(in, null, "RDF/XML");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OntModel getModel() {
        return model;
    }

    public void saveModel() {
        String filePath = env.getProperty("app.ontology.path");
        try (OutputStream out = new FileOutputStream(filePath)) {
            model.write(out, "RDF/XML");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

