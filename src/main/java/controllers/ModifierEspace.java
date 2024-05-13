package controllers;

import entities.Categorie;
import entities.EspacePartenaire;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import services.CategorieServices;
import services.EspaceServices;
import utils.MyConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierEspace {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea descriptionTextField;

    @FXML
    private RadioButton idCouvert;

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
    private TextArea imageLinksLabel;
    @FXML
    private WebView mapView;

    private List<String> imagePaths = new ArrayList<>();

    private EspacePartenaire espacePartenaireToUpdate; // Variable pour stocker l'espace partenaire à modifier

    // Méthode pour initialiser le contrôleur après l'injection des éléments FXML
    @FXML
    void initialize() {
        assert idType != null : "fx:id=\"idType\" was not injected: check your FXML file 'ModifierEspace.fxml'.";
        idType.getItems().addAll("Salon de thé","Restaurant","Resto Bar","Espace ouvert","Cafeteria","Terrain Foot","Salle de jeux","Café Lounge");
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
    public boolean isLocalisationUnique(String localisation, int idEspaceActuel) {
        boolean unique = true;
        String requete = "SELECT COUNT(*) FROM EspacePartenaire WHERE localisation = ? AND id_espace != ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, localisation);
            pst.setInt(2, idEspaceActuel);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    unique = count == 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return unique;
    }




    @FXML
    void AfficherListe(ActionEvent event) {

        StageManager stageManager = StageManager.getInstance();
        stageManager.closeCurrentStage();
        stageManager.switchScene("/Afficher_espace.fxml");
    }
    public void setEspacePartenaireToUpdate(EspacePartenaire espacePartenaire) {
        // Populate the form fields with the data from the EspacePartenaire object
        this.espacePartenaireToUpdate = espacePartenaire;
        int id_categorie = espacePartenaire.getId_categorie();
        Optional<Categorie> optionalCategorie = getCategorie().stream()
                .filter(categorie -> categorie.getId_categorie() == id_categorie)
                .findFirst();
        Categorie categorie = optionalCategorie.get();

        idCouvert.setSelected(categorie.isEspaceCouvert());
        idEnfant.setSelected(categorie.isEspaceEnfants());
        idFumeur.setSelected(categorie.isEspaceFumeur());
        idService.setSelected(categorie.isServiceRestauration());

        nomTextField.setText(espacePartenaire.getNom());
        localisationTextField.setText(espacePartenaire.getLocalisation());
        descriptionTextField.setText(espacePartenaire.getDescription());
        idType.setValue(espacePartenaire.getType());
        displayPhotos(espacePartenaire.getPhotos());


    }

    private void displayPhotos(List<String> photos) {
        StringBuilder linksText = new StringBuilder("Image Links:\n");
        for (String photo : photos) {
            linksText.append(photo).append("\n");
        }
        imageLinksLabel.setText(linksText.toString());
    }

    // Méthode appelée lors du clic sur le bouton "Modifier"
    @FXML
    void Modifier(ActionEvent actionEvent) {
        // Récupérer les valeurs des champs
        String type = idType.getValue();
        String description = descriptionTextField.getText();
        String localisation = localisationTextField.getText();
        String nom = nomTextField.getText();

        boolean couvert = idCouvert.isSelected();
        boolean enfant = idEnfant.isSelected();
        boolean fumeur = idFumeur.isSelected();
        boolean service = idService.isSelected();
        int Id_categorie = espacePartenaireToUpdate.getId_categorie();

        // Vérifier si tous les champs sont remplis
        if (type.isEmpty() || description.isEmpty() || localisation.isEmpty() || nom.isEmpty()) {
            // Afficher un message d'erreur ou effectuer une action appropriée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        // Vérifier l'unicité de la localisation
        if (!isLocalisationUnique(localisation, espacePartenaireToUpdate.getId_espace())) {
            // Afficher un message d'erreur si la localisation n'est pas unique
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Une autre espace existe déjà avec cette localisation.");
            alert.show();
            return;
        }

        Categorie categorie = new Categorie(Id_categorie, couvert, enfant, fumeur, service);

        // Mettre à jour les détails de l'espace partenaire
        espacePartenaireToUpdate.setNom(nom);
        espacePartenaireToUpdate.setLocalisation(localisation);
        espacePartenaireToUpdate.setType(type);
        espacePartenaireToUpdate.setDescription(description);

        // Mettre à jour les photos seulement si de nouvelles photos ont été sélectionnées
        if (!imagePaths.isEmpty()) {
            espacePartenaireToUpdate.setPhotos(imagePaths);
        }

        // Appeler la méthode pour mettre à jour l'espace partenaire dans la base de données
        CategorieServices categorieService = new CategorieServices();
        EspaceServices espaceServices = new EspaceServices();
        try {
            categorieService.updateEntity(categorie);
            espaceServices.updateEntity(espacePartenaireToUpdate);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("L'espace a été modifié avec succès.");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }

        // Afficher un message de confirmation ou effectuer une action appropriée
        System.out.println("Espace partenaire modifié avec succès.");

        StageManager stageManager = StageManager.getInstance();
        stageManager.closeCurrentStage();
        stageManager.switchScene("/Afficher_espace.fxml");
    }

    // Méthode appelée lors du clic sur le bouton "Uploader"
    @FXML
    void onUploadButtonClick(ActionEvent actionEvent) {
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
            updateImageLinksLabel(); // Mettre à jour le label pour afficher les liens de toutes les images sélectionnées
        }
    }

    // Méthode pour mettre à jour le label avec les liens des images sélectionnées
    private void updateImageLinksLabel() {
        StringBuilder linksText = new StringBuilder("Image Links:\n");
        for (String path : imagePaths) {
            linksText.append(path).append("\n");
        }
        imageLinksLabel.setText(linksText.toString());
    }

    // Méthode appelée lors du clic sur le bouton "Afficher Liste"



    public ObservableList<Categorie> getCategorie() {
        ObservableList<Categorie> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM Categorie";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setId_categorie(rs.getInt("id_categorie"));
                categorie.setEspaceCouvert(rs.getBoolean("espaceCouvert"));
                categorie.setEspaceEnfants(rs.getBoolean("espaceEnfants"));
                categorie.setEspaceFumeur(rs.getBoolean("espaceFumeur"));
                categorie.setServiceRestauration(rs.getBoolean("serviceRestauration"));
                data.add(categorie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

}
