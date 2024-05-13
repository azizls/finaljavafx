

package controllers;

import entities.Categorie;
import entities.EspacePartenaire;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import services.CategorieServices;
import services.EspaceServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Espace {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // @FXML
    //private TextField descriptionTextField;

    @FXML
    private RadioButton idCouvert;
    @FXML
    private TextArea descriptionTextField;
    @FXML
    private RadioButton idEnfant;

    @FXML
    private RadioButton idFumeur;

    @FXML
    private RadioButton idService;

    @FXML
    private ChoiceBox<String> idType;

    @FXML
    private TextField localisationTextField;

    @FXML
    private TextField nomTextField;
    @FXML
    private Button AfficherBtn;

    @FXML
    private TextField photosTextField;
    @FXML
    private Button uploadImgBtn;
    private String imageData;
    @FXML
    private TextArea imageLinksLabel; // Assuming you have a Label in your FXML file to display the image links

    private List<String> imagePaths = new ArrayList<>(); // List to store paths of selected images

    @FXML
    private WebView mapView;

    @FXML
    private void onUploadButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                imagePaths.add(file.getPath());
            }
            updateImageLinksLabel(); // Update the label to display links of all selected images
        }
    }


    @FXML
    void Ajouter(ActionEvent event) throws IOException {
        boolean couvert = idCouvert.isSelected();
        boolean enfant = idEnfant.isSelected();
        boolean fumeur = idFumeur.isSelected();
        boolean service = idService.isSelected();
        Categorie categorie = new Categorie(couvert, enfant, fumeur, service);
        CategorieServices categorieService = new CategorieServices();

        // Vérification de la localisation unique
        String localisation = localisationTextField.getText();
        EspaceServices espaceService = new EspaceServices();
        if (espaceService.isLocalisationUnique(localisation)) {
            if (nomTextField.getText().isEmpty() || localisation.isEmpty() || idType.getValue() == null || descriptionTextField.getText().isEmpty() || this.imagePaths.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.show();
                return;
            }

            EspacePartenaire espacePartenaire = new EspacePartenaire(nomTextField.getText(), localisation, idType.getValue(), descriptionTextField.getText(), this.imagePaths);

            try {
                categorieService.addEntity(categorie);
                espaceService.addEntity(espacePartenaire);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("L'espace a été ajouté.");
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }

            Stage nouveauStage;
            Parent root = FXMLLoader.load(getClass().getResource("/Afficher_espace.fxml"));
            nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            nouveauStage.setScene(scene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Un espace avec la même localisation existe déjà.");
            alert.show();
        }
    }

    private void updateImageLinksLabel() {
        StringBuilder linksText = new StringBuilder("Image Links:\n");
        for (String path : imagePaths) {
            linksText.append(path).append("\n");
        }
        imageLinksLabel.setText(linksText.toString());
        System.out.println(linksText);
    }

    @FXML
    void initialize() {
        assert idType != null : "fx:id=\"idType\" was not injected: check your FXML file 'Espace.fxml'.";
        idType.getItems().addAll("Salon de thé", "Restaurant", "Resto Bar", "Espace ouvert", "Cafeteria", "Terrain Foot", "Salle de jeux", "Café Lounge");
        //WebEngine webEngine = mapView.getEngine();

        // Load Google Maps webpage in an iframe
        //  String htmlContent = "<iframe width=\"600\" height=\"450\" frameborder=\"0\" style=\"border:0\" src=\"https://www.google.com/maps/embed/v1/place?q=Ariana,Tunisia&key=AIzaSyBvGfBCJhvd5MJCSnY6XQIoKVAj3qEN0UY\" allowfullscreen></iframe>";
        //webEngine.loadContent(htmlContent);
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/googlemaps.html").toExternalForm());

        // Enable JavaScript communication
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this);
            }
        });
    }





    public void handleSelectedLocation(double latitude, double longitude) {
        Platform.runLater(() -> {
            System.out.println("Selected Location: " + latitude + ", " + longitude);
            String placeName = getPlaceName(latitude, longitude);
            String locationInfo = "";
            if (placeName != null && !placeName.isEmpty()) {
                locationInfo += placeName + " ";
            }
            locationInfo += "( "+ latitude + "," + longitude + ")";
            localisationTextField.setText(locationInfo);
            // You can perform any necessary actions with the received latitude, longitude, and placeName here
        });
    }

    private String getPlaceName(double latitude, double longitude) {
        String apiKey = "AIzaSyBvGfBCJhvd5MJCSnY6XQIoKVAj3qEN0UY";
        String fullAddress = null;

        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?" +
                    "latlng=" + latitude + "," + longitude +
                    "&key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("results") && jsonResponse.getJSONArray("results").length() > 0) {
                JSONObject result = jsonResponse.getJSONArray("results").getJSONObject(0);
                fullAddress = result.getString("formatted_address");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullAddress;
    }


    @FXML
    void AfficherListe(ActionEvent event) throws IOException {

        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Afficher_espace.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }





}