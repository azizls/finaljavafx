package controllers;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import services.ReclamationServices;
import services.ReponseServices;
import utils.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class MesReclamations {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Reclamation> myListView;

    @FXML
    private Button ajouterBtn;

    @FXML
    private Button home1;

    @FXML
    private Button modifierBtn;

    @FXML
    private Button suppBtn;
    @FXML
    private Button filterAttBtn;

    @FXML
    private Button filterClotBtn;

    @FXML
    private Button filterTraitBtn;

    @FXML
    private ChoiceBox<String> chb;

    int id_user= home.userID;
    String filterClicked = "";

    ObservableList<Reclamation> ListeR;
    public ObservableList<Reclamation> getAllData(int id_user) throws SQLException {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String requete = "SELECT * FROM reclamation WHERE id_user="+id_user;
        try {

            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId_reclamation(rs.getInt("id_reclamation"));
                reclamation.setId_user(rs.getInt("id_user"));
                reclamation.setType(rs.getString("type"));
                reclamation.setContenu(rs.getString("contenu"));
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reclamations;
    }

    public ObservableList<Reclamation> getAllDataByStatus(String status) throws SQLException {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT * FROM reclamation WHERE status = ? AND id_user = ?";
        try {
            PreparedStatement st = MyConnection.getInstance().getCnx().prepareStatement(query);
            st.setString(1,status);
            st.setInt(2,id_user);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId_reclamation(rs.getInt("id_reclamation"));
                reclamation.setId_user(rs.getInt("id_user"));
                reclamation.setType(rs.getString("type"));
                reclamation.setContenu(rs.getString("contenu"));
                reclamation.setDate_env(rs.getDate("date_env"));
                reclamation.setStatus(rs.getString("status"));

                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reclamations;
    }



    @FXML
    void initialize() {
        assert ajouterBtn != null : "fx:id=\"ajouterBtn\" was not injected: check your FXML file 'mesReclamations.fxml'.";
        assert modifierBtn != null : "fx:id=\"modifierBtn\" was not injected: check your FXML file 'mesReclamations.fxml'.";
        assert myListView != null : "fx:id=\"myListView\" was not injected: check your FXML file 'mesReclamations.fxml'.";
        assert suppBtn != null : "fx:id=\"suppBtn\" was not injected: check your FXML file 'mesReclamations.fxml'.";

        showReclamation("All");

    }
    @FXML
    void onClickAjouter(ActionEvent event) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/ajouter.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            showReclamation("All");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onClickModif(ActionEvent event) {
        Reclamation R = this.myListView.getSelectionModel().getSelectedItem();
        if (R == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Vous devez selectionner une reclamation afin de la modifier!");
            alert.show();
        } else {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Confirmation");
            alert1.setHeaderText("Voulez vous vraiment modifier la reclamation N°?" + this.myListView.getSelectionModel().getSelectedItem().getId_reclamation());
            alert1.setContentText("Choisir l'option");

            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");

            // Add buttons to the alert dialog
            alert1.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Show the dialog and wait for user action
            Optional<ButtonType> result = alert1.showAndWait();

            // Handle the user's choice
            if (result.isPresent() && result.get() == buttonTypeYes) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier.fxml"));
                try {
                    Parent root = loader.load();

                    Modifier modControl = loader.getController();
                    modControl.setReclamation(R);
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.showAndWait();
                    showReclamation("All");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @FXML
    void onClickSupp(ActionEvent event) {
        Reclamation R = this.myListView.getSelectionModel().getSelectedItem();
        if(R == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Vous devez selectionner une reclamation afin de la supprimer!");
            alert.show();
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Confirmation");
            alert1.setHeaderText("Voulez vous vraiment supprimer la reclamation N°?" + this.myListView.getSelectionModel().getSelectedItem().getId_reclamation());
            alert1.setContentText("Choisir l'option");

            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");

            // Add buttons to the alert dialog
            alert1.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Show the dialog and wait for user action
            Optional<ButtonType> result = alert1.showAndWait();

            // Handle the user's choice
            if (result.isPresent() && result.get() == buttonTypeYes) {

                ReclamationServices RS = new ReclamationServices();
                RS.deleteEntity(R);
                this.showReclamation("All");
            }
        }


    }
    public void showReclamation(String status){
        try {

            ReponseServices rs = new ReponseServices();
            if (status.equals("All")){
                ListeR = getAllData(id_user);
                System.out.println(ListeR);
            }
            else {
                ListeR = getAllDataByStatus(status);

                System.out.println(ListeR);
            }
            myListView.setItems(ListeR);
            myListView.setCellFactory(param -> new ListCell<Reclamation>() {
                private final Button button = new Button("-> Montrer les réponses");

                private final HBox hbox = new HBox();

                {
                    hbox.setSpacing(8); // Set horizontal spacing between attributes
                }

                @Override
                protected void updateItem(Reclamation rec, boolean empty) {
                    super.updateItem(rec, empty);

                    if (empty || rec == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label t = new Label(rec.getType());
                        TextArea c = new TextArea(rec.getContenu());
                        c.setFocusTraversable(false);
                        c.setWrapText(true);
                        c.setEditable(false);
                        t.setStyle("-fx-padding: 10px;");
                        c.setStyle("-fx-padding: 10px;");

                        hbox.getChildren().clear(); // Clear existing content

                        // Add type label
                        hbox.getChildren().add(t);

                        // Add region for flexible spacing
                        Region region = new Region();
                        HBox.setHgrow(region, Priority.ALWAYS); // Allow region to grow horizontally
                        hbox.getChildren().add(region);

                        // Add contenu text area
                        hbox.getChildren().add(c);

                        // Check for responses and add button if present
                        if (!rs.getAllDataId(rec.getId_reclamation()).isEmpty()) {
                            button.setVisible(true);
                            button.setStyle("-fx-padding: 10px;");
                            hbox.getChildren().add(button);

                            button.setOnAction(event -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mesReponses.fxml"));
                                try {
                                    Parent root = loader.load();
                                    MesReponses repControl = loader.getController();
                                    repControl.setReponses(rec.getId_reclamation());
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else {
                            button.setStyle("-fx-padding: 10px;");
                            hbox.getChildren().add(button);
                            button.setVisible(false);
                        }

                        setGraphic(hbox);
                    }
                }
            });





            myListView.setFixedCellSize(150);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onFilterAtt(ActionEvent event) {
        if (filterClicked.equals("en attente")){
            filterAttBtn.setStyle("-fx-background-color: #2350d9;");
            filterClicked = "All";
            showReclamation("All");
        }
        else {
            filterClicked = "en attente";

            filterAttBtn.setStyle("-fx-background-color: #71afff; ");
            filterClotBtn.setStyle("-fx-background-color: #2350d9;");
            filterTraitBtn.setStyle("-fx-background-color: #2350d9;");

            showReclamation("en attente");
        }

    }

    @FXML
    void onFilterClot(ActionEvent event) {
        if (filterClicked.equals("Cloturee")){
            filterClotBtn.setStyle("-fx-background-color: #2350d9;");
            filterClicked = "All";
            showReclamation("All");
        }
        else {
            filterClicked = "Cloturee";
            filterClotBtn.setStyle("-fx-background-color: #71afff;");
            filterTraitBtn.setStyle("-fx-background-color: #2350d9;");
            filterAttBtn.setStyle("-fx-background-color: #2350d9;");

            showReclamation("Cloturee");
        }

    }

    @FXML
    void onFilterTrait(ActionEvent event) {
        if (filterClicked.equals("en cours de traitement")){
            filterTraitBtn.setStyle("-fx-background-color: #2350d9;");
            filterClicked = "All";
            showReclamation("All");
        }
        else {
            filterClicked = "en cours de traitement";
            filterTraitBtn.setStyle("-fx-background-color: #71afff;");
            filterAttBtn.setStyle("-fx-background-color: #2350d9;");
            filterClotBtn.setStyle("-fx-background-color: #2350d9;");
            showReclamation("en cours de traitement");
        }

    }

    @FXML
    private void home1(ActionEvent event) throws IOException {

        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Frontmodifier.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }

}