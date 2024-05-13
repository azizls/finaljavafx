package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class Maps implements Initializable {

    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize your controller if needed
    }

    public void loadGoogleMaps(String latitude, String longitude) {
        String googleMapsUrl = "https://www.google.com/maps?q=" + latitude + "," + longitude;
        WebEngine webEngine = webView.getEngine();
        webEngine.load(googleMapsUrl);
    }

    public void loadStreetView(String latitude, String longitude) throws Exception {
        // Initialize Velocity engine
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate("/streetview_template.vm");
        // Create a Velocity context and add latitude and longitude variables
        VelocityContext context = new VelocityContext();
        context.put("latitude", latitude);
        context.put("longitude", longitude);
        // Merge the template with the context
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        // Write HTML content to a temporary file
        File tempFile = File.createTempFile("streetview", ".html");
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(writer.toString());
        }
        System.out.println(tempFile);
        // Load the temporary HTML file into WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.load(tempFile.toURI().toString());
        openHtmlFileInChrome(String.valueOf(tempFile));
    }

    public static void openHtmlFileInChrome(String filePath) {
        try {
            // Get the desktop object
            Desktop desktop = Desktop.getDesktop();

            // Check if desktop is supported to ensure cross-platform compatibility
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop not supported");
                return;
            }

            // Check if the file exists
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist");
                return;
            }

            // Open the file in the default browser (Chrome)
            desktop.browse(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

//AIzaSyBvGfBCJhvd5MJCSnY6XQIoKVAj3qEN0UY