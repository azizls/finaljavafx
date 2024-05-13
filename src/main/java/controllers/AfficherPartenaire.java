package controllers;

import entities.Categorie;
import entities.EspacePartenaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyConnection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class AfficherPartenaire {

    @FXML
    private VBox partnerAreaContainer;

    @FXML
    private Button buttonAjouter;

    @FXML
    private Button buttonModifier;

    @FXML
    private Button buttonSupprimer;

    public void initialize() throws FileNotFoundException {
        ObservableList<EspacePartenaire> partnerAreas = getEspace();

        // Display partner areas
        displayPartnerAreas(partnerAreas);
    }

    // Method to display partner areas in a scrollable grid
    private void displayPartnerAreas(List<EspacePartenaire> partnerAreas) {
        // Create a GridPane to display partner areas in a grid layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        // Add partner areas to the GridPane
        int column = 0;
        int row = 0;
        for (EspacePartenaire partnerArea : partnerAreas) {
            Node partnerAreaNode = createPartnerAreaBox(partnerArea);
            gridPane.add(partnerAreaNode, column, row);
            column++;
            if (column == 3) { // Display two areas per row
                column = 0;
                row++;
            }
        }

        // Create a ScrollPane to allow scrolling
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(20));

        // Clear existing content in the partner area container
        partnerAreaContainer.getChildren().clear();

        // Add the ScrollPane to the partner area container
        partnerAreaContainer.getChildren().add(scrollPane);
    }

    private Node createPartnerAreaBox(EspacePartenaire espacePartenaire) {
        // Create a VBox to hold the ImageView and labels
        VBox box = new VBox(10); // Spacing between children
        box.setAlignment(Pos.CENTER); // Center align children

        try {
            String firstPhoto = espacePartenaire.getPhotos().get(0);
            String[] filePaths = firstPhoto.split(",");

            // Get the first file path
            String firstImagePath = filePaths[0].trim();
            System.out.println("File path: " + firstImagePath); // Print file path for debugging

            // Create an Image object using the first file path
            Image image = new Image(new FileInputStream(firstImagePath));

            // Create a new ImageView with the image
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true); // Preserve aspect ratio
            imageView.setSmooth(true); // Enable smooth rendering
            imageView.setCache(true); // Enable image caching for better performance

            // Add labels for name and type
            Label nameLabel = new Label(espacePartenaire.getNom());
            Label typeLabel = new Label(espacePartenaire.getType());

            // Optionally, add styling or effects to the labels
            nameLabel.setStyle("-fx-font-weight: bold;");
            typeLabel.setStyle("-fx-text-fill: #666;");

            // Add the ImageView and labels to the VBox
            box.getChildren().addAll(imageView, nameLabel, typeLabel);

            // Add event handlers for animation and opening SpaceView
            box.setOnMouseEntered(event -> {
                // Add animation here, e.g., scale or rotate the box
                box.setScaleX(1.1);
                box.setScaleY(1.1);
            });

            box.setOnMouseExited(event -> {
                // Reset the box to its original size
                box.setScaleX(1.0);
                box.setScaleY(1.0);
            });

            box.setOnMouseClicked(event -> {
                // Open SpaceView on click
                openSpaceView(espacePartenaire);
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Handle file not found exception
        }

        // Optionally, add styling or effects to the VBox
        box.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 10px;");

        return box;
    }

    // Method to open SpaceView
    private void openSpaceView(EspacePartenaire espacePartenaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpaceView.fxml"));
            Parent root = loader.load();

            controllers.SpaceView controller = loader.getController();
            controller.AfficherEspace(espacePartenaire);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Space View");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<EspacePartenaire> getEspace() {
        ObservableList<EspacePartenaire> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM espacepartenaire";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                EspacePartenaire espacePartenaire = new EspacePartenaire();
                espacePartenaire.setId_categorie(rs.getInt("id_categorie"));
                espacePartenaire.setId_espace(rs.getInt("id_espace"));
                espacePartenaire.setNom(rs.getString("nom"));
                espacePartenaire.setLocalisation(rs.getString("localisation"));
                espacePartenaire.setType(rs.getString("type"));
                espacePartenaire.setDescription(rs.getString("description"));
                // Assuming photos is a list of URLs
                espacePartenaire.setPhotos(Collections.singletonList(rs.getString("photos")));
                espacePartenaire.setAccepted(rs.getBoolean("accepted"));
                data.add(espacePartenaire);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

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

    public void Ajouter(ActionEvent event) {
    }

    public void Modifier(ActionEvent event) {
    }

    public void Supprimer(ActionEvent event) {
    }
}
