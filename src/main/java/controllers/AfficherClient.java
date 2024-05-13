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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
public class AfficherClient {
    @FXML
    private Button button_part;



    @FXML
    private ImageView home;
    @FXML
    private VBox partnerAreaContainer;
    @FXML
    private TextField recherche;
    @FXML
    public void initialize() throws FileNotFoundException {
        ObservableList<EspacePartenaire> partnerAreas = getEspace();
        displayPartnerAreas(getEspace(), "");
    }


    @FXML
    void recherche(KeyEvent event) {
        String searchTerm = recherche.getText().trim();
        if (searchTerm.isEmpty()) {
            // If search term is empty, display popular and other spaces
            displayPartnerAreas(getEspace(), "");
        } else {
            // If search term is not empty, display searched spaces
            ObservableList<EspacePartenaire> filteredList = searchEspace(searchTerm);
            displayPartnerAreas(filteredList, searchTerm);
        }
    }




    private ObservableList<EspacePartenaire> searchEspace(String searchTerm) {
        ObservableList<EspacePartenaire> filteredList = FXCollections.observableArrayList();
        for (EspacePartenaire partnerArea : getEspace()) {
            if (partnerArea.getNom().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    partnerArea.getType().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredList.add(partnerArea);
            }
        }
        return filteredList;
    }

    @FXML
    void home(MouseEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Frontmodifier.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
    }





    // Method to display partner areas in a scrollable grid
    private void displayPartnerAreas(List<EspacePartenaire> partnerAreas, String searchTerm) {
        // Create a VBox to hold the partner areas
        VBox mainContainer = new VBox();
        mainContainer.setSpacing(20);
        mainContainer.setAlignment(Pos.CENTER);

        // Add label for "Espaces Populaires" if the search term is empty
        if (searchTerm.isEmpty()) {
            Label popularLabel = new Label("#Espaces Populaires");
            popularLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2350d9;-fx-underline: true; ");
            mainContainer.getChildren().add(popularLabel);

            // Retrieve the two most popular spaces if requested
            List<EspacePartenaire> mostPopularSpaces = getMostPopularSpaces(2);
            if (!mostPopularSpaces.isEmpty()) {
                // Create an HBox to display the two most popular spaces
                HBox popularSpacesContainer = new HBox();
                popularSpacesContainer.setAlignment(Pos.CENTER);
                popularSpacesContainer.setSpacing(20);

                for (EspacePartenaire popularSpace : mostPopularSpaces) {
                    Node popularSpaceNode = createPartnerAreaBox(popularSpace);
                    popularSpacesContainer.getChildren().add(popularSpaceNode);
                }

                // Add the popular spaces HBox to the main container
                mainContainer.getChildren().add(popularSpacesContainer);
            }

            // Add label for "Autres Espaces" if requested
            Label otherLabel = new Label("#Tous les Espaces");
            otherLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2350d9; -fx-underline: true;");
            mainContainer.getChildren().add(otherLabel);
        } else {
            // Add label for "#Espace Cherché"
            Label searchedLabel = new Label("#Espaces Cherchés");
            searchedLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2350d9;-fx-underline: true; ");
            mainContainer.getChildren().add(searchedLabel);
        }

        // Create a GridPane to display the partner areas in a grid layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        // Add partner areas to the GridPane
        int column = 0;
        int row = 0;
        for (EspacePartenaire partnerArea : partnerAreas) {
            Node partnerAreaNode = createPartnerAreaBox(partnerArea);
            GridPane.setMargin(partnerAreaNode, new Insets(10)); // Add margins for consistent spacing
            gridPane.add(partnerAreaNode, column, row);
            column++;
            if (column == 3) { // Display three areas per row
                column = 0;
                row++;
            }
        }

        // Add the GridPane containing partner areas to the main container
        mainContainer.getChildren().add(gridPane);

        // Create a ScrollPane to allow scrolling
        ScrollPane scrollPane = new ScrollPane(mainContainer);
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
            incrementNbClick(espacePartenaire.getId_espace());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Space View");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void incrementNbClick(int espaceId) {
        try {
            String updateQuery = "UPDATE espacepartenaire SET nbclick = nbclick + 1 WHERE id_espace = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
            pst.setInt(1, espaceId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<EspacePartenaire> getEspace() {
        ObservableList<EspacePartenaire> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM espacepartenaire where accepted=1 ";
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

    private ObservableList<EspacePartenaire> getMostPopularSpaces(int limit) {
        ObservableList<EspacePartenaire> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM espacepartenaire WHERE accepted = 1 ORDER BY nbclick DESC LIMIT ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, limit);
            ResultSet rs = pst.executeQuery();
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


    @FXML
    void partenaire1(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Afficher_espace.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);


    }


}
