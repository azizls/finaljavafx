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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import services.EspaceServices;
import utils.MyConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AfficherAdmin {

    @FXML
    private Button buttonAccepter;

    @FXML
    private Button buttonConsulter;

    @FXML
    private Button buttonRefuser;

    @FXML
    private ListView<EspacePartenaire> listViewEspace;

    @FXML
    private ChoiceBox<String> choiceBoxAffichage;

    private boolean afficherEspacesAcceptes;

    @FXML
    private Button home;

    @FXML
    private TextField recherche;

    @FXML
    void Accepter(ActionEvent event) {
        // Get the selected items
        ObservableList<EspacePartenaire> espacesSelectionnes = listViewEspace.getSelectionModel().getSelectedItems();

        // Check if any items are selected
        if (espacesSelectionnes.isEmpty()) {
            System.out.println("Aucun espace sélectionné.");
            return;
        }

        // Affichage de la boîte de dialogue de confirmation
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation d'acceptation");
        confirmDialog.setHeaderText("Êtes-vous sûr de vouloir accepter les espaces sélectionnés ?");
        confirmDialog.setContentText("L'acceptation des espaces est irréversible.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Mettre à jour la base de données pour marquer les espaces sélectionnés comme acceptés
            for (EspacePartenaire espace : espacesSelectionnes) {
                espace.setAccepted(true); // Supposons que vous avez un setter pour l'attribut "Accepted"
                // Code pour mettre à jour la base de données ici
                EspaceServices es = new EspaceServices();
                es.updateEntity(espace);
            }

            // Rafraîchir l'affichage pour refléter le changement
            AfficherEspace();
        }
    }

    @FXML
    void Refuser(ActionEvent event) {
        // Get the selected items from the ListView
        EspacePartenaire espacePartenaire = listViewEspace.getSelectionModel().getSelectedItem();

        if (espacePartenaire == null) {
            System.out.println("Aucune espace sélectionnée.");
            return;
        }

        // Affichage de la boîte de dialogue de confirmation
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de refus");
        confirmDialog.setHeaderText("Êtes-vous sûr de vouloir refuser cet espace ?");
        confirmDialog.setContentText("Le refus de l'espace est irréversible.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer l'espace de la base de données
            String req = "DELETE FROM Categorie WHERE id_categorie = ?";
            try {
                PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
                ps.setInt(1, espacePartenaire.getId_categorie());
                ps.executeUpdate();
                System.out.println("Catégorie supprimée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            // Supprimer l'espace du service
            EspaceServices es = new EspaceServices();
            es.deleteEntity(espacePartenaire);

            // Rafraîchir l'affichage pour refléter le changement
            AfficherEspace();
        }
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
    void initialize() {
        listViewEspace.getStyleClass().add("list-cell");

        listViewEspace.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Add items to the ChoiceBox
        choiceBoxAffichage.getItems().addAll("Acceptés", "En attente");

        // Set the default choice in the choice box
        choiceBoxAffichage.setValue("En attente");
        choiceBoxAffichage.setStyle("-fx-text-fill: white;");

        // Ajouter un gestionnaire d'événements pour détecter les changements de sélection
        choiceBoxAffichage.setOnAction(this::updateDisplay);

        // Call AfficherEspace() after the ChoiceBox has been initialized
        AfficherEspace();
    }

    public void AfficherEspace() {
        ObservableList<EspacePartenaire> espaces = getEspace();

        // Filter the spaces based on the value of afficherEspacesAcceptes
        if (afficherEspacesAcceptes) {
            // Show accepted spaces
            espaces = espaces.filtered(EspacePartenaire::isAccepted);
        } else {
            // Show spaces that are not accepted (i.e., waiting)
            espaces = espaces.filtered(espace -> !espace.isAccepted());
        }

        // Update the ListView with the filtered list of spaces
        listViewEspace.setItems(espaces);
        listViewEspace.setCellFactory(param -> new ListCell<EspacePartenaire>() {
            @Override
            protected void updateItem(EspacePartenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Display nom and localisation
                    StringBuilder text = new StringBuilder(item.getNom() + " - " + item.getType());

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
                espacePartenaire.setPhotos(Collections.singletonList(rs.getString("photos")));
                // Retrieve the accepted attribute
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

    public void updateDisplay(ActionEvent event) {
        String choixAffichage = choiceBoxAffichage.getValue();

        // Set afficherEspacesAcceptes based on the selected value
        afficherEspacesAcceptes = choixAffichage.equals("Acceptés");

        // Rafraîchir l'affichage en fonction de la sélection
        AfficherEspace();
    }

    @FXML
    void recherche(KeyEvent event) {
        String searchTerm = recherche.getText().trim();

        if (searchTerm.isEmpty()) {
            // If the search term is empty, refresh the list view to display all spaces
            AfficherEspace();
        } else {
            // Perform the search and update the list view with the filtered list
            ObservableList<EspacePartenaire> filteredList = searchEspace(searchTerm);
            listViewEspace.setItems(filteredList);
        }
    }

    private ObservableList<EspacePartenaire> searchEspace(String searchTerm) {
        ObservableList<EspacePartenaire> allSpaces = getEspace();
        ObservableList<EspacePartenaire> filteredList = FXCollections.observableArrayList();

        for (EspacePartenaire space : allSpaces) {
            // Check if the space matches the search term
            if (space.getNom().toLowerCase().contains(searchTerm.toLowerCase()) || space.getType().toLowerCase().contains(searchTerm.toLowerCase())) {
                // Check if the space is in the current display mode
                if ((afficherEspacesAcceptes && space.isAccepted()) || (!afficherEspacesAcceptes && !space.isAccepted())) {
                    filteredList.add(space);
                }
            }
        }
        return filteredList;
    }

    @FXML
    void back1(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
    }
}
