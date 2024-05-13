package controllers;

import entities.Categorie;
import entities.EspacePartenaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.EspaceServices;
import utils.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class AfficherEspace {

    @FXML
    private Button back;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<EspacePartenaire> listViewEspace;

    @FXML
    private Button buttonConsulter;

    @FXML
    private Button buttonAjouter;

    @FXML
    private Button buttonModifier;

    @FXML
    private Button buttonSupprimer;

    @FXML
    void Ajouter(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Espace.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
    }

    @FXML
    void Consulter(ActionEvent event) {
        EspacePartenaire espacePartenaire = listViewEspace.getSelectionModel().getSelectedItem();
        if (espacePartenaire == null) {
            System.out.println("Aucun espace sélectionné pour la consultation.");
            return;
        }

        // Pass the selected EspacePartenaire object to the Modifier controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpaceView.fxml"));

        Parent root;
        try {
            root = loader.load();
            SpaceView spaceView = loader.getController();
            spaceView.AfficherEspace(espacePartenaire);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the update.fxml form and wait for it to be closed

            // Refresh the Afficher.fxml view after updating
            AfficherEspace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Modifier(ActionEvent event) {
        EspacePartenaire espacePartenaire = listViewEspace.getSelectionModel().getSelectedItem();

        try {
            if (espacePartenaire == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Aucun espace sélectionné pour la modification.");
                alert.show();}
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


        // Pass the selected EspacePartenaire object to the Modifier controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEspace.fxml"));

        Parent root;
        try {
            root = loader.load();
            controllers.ModifierEspace modifierController = loader.getController();
            modifierController.setEspacePartenaireToUpdate(espacePartenaire);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the update.fxml form and wait for it to be closed

            // Refresh the Afficher.fxml view after updating
            AfficherEspace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {
        // Get the selected item from the ListView
        EspacePartenaire espacePartenaire = listViewEspace.getSelectionModel().getSelectedItem();

        if (espacePartenaire == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Aucun espace sélectionné.");
            alert.show();
            return;
        }

        // Affichage de la boîte de dialogue de confirmation
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer cet espace ?");
        confirmDialog.setContentText("La suppression de l'espace est irréversible.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Suppression de l'espace
            try {
                EspaceServices es = new EspaceServices();
                es.deleteEntity(espacePartenaire);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("L'espace a été supprimé avec succès.");
                successAlert.show();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Erreur lors de la suppression de l'espace : " + e.getMessage());
                errorAlert.show();
            }

            // Mise à jour de l'affichage
            AfficherEspace();
        }
    }


    @FXML
    void initialize() {


        listViewEspace.getStyleClass().add("list-cell");

        listViewEspace.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AfficherEspace(); // This method should populate the ListView with data

    }

    public void AfficherEspace() {
        ObservableList<EspacePartenaire> espaces = getEspace();
        listViewEspace.setItems(espaces);
        listViewEspace.setCellFactory(param -> new ListCell<EspacePartenaire>() {
            @Override
            protected void updateItem(EspacePartenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Display nom and localisation
                    StringBuilder text = new StringBuilder(item.getNom() + " - " + item.getLocalisation());

                    // Retrieve the associated Categorie
                    Optional<Categorie> optionalCategorie = getCategorie().stream()
                            .filter(categorie -> categorie.getId_categorie() == item.getId_categorie())
                            .findFirst();

                    if (optionalCategorie.isPresent()) {
                        Categorie categorie = optionalCategorie.get();
                        List<String> attributes = new ArrayList<>();
                        if (categorie.isEspaceCouvert()) {
                            attributes.add("L'espace est couvert");
                        }
                        if (categorie.isEspaceEnfants()) {
                            attributes.add("Il y a un espace d'enfants");
                        }
                        if (categorie.isEspaceFumeur()) {
                            attributes.add("Espace fumeur");
                        }
                        if (categorie.isServiceRestauration()) {
                            attributes.add("Il y a un service de restauration");
                        }
                        // Append Categorie attributes to the text
                        text.append(" - ").append(String.join(", ", attributes));
                    }

                    // Set the text
                    setText(text.toString());
                }
            }
        });
    }




    public ObservableList<EspacePartenaire> getEspace() {
        int id = home.userID;
        ObservableList<EspacePartenaire> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM espacepartenaire where id_user='" + id + "'";
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
                espacePartenaire.setPhotos(Collections.singletonList(rs.getString("photos")));
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


    @FXML
    void back(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/AfficherClient.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }

}
