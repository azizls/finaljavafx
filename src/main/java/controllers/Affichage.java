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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.ReclamationServices;
import utils.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class Affichage {
    @FXML
    private Button home;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;




    @FXML
    private TableColumn<Reclamation, String> Col_nomUser;

    @FXML
    private TableColumn<Reclamation, String> Col_type;

    @FXML
    private TableColumn<Reclamation, String> Col_contenu;

    @FXML
    private TableColumn Col_Repond ;


    @FXML
    private TableView<Reclamation> ReclamationsTable;

    @FXML
    private Button ajoutBtn;

    @FXML
    private Button modifBtn;

    @FXML
    private Button supBtn;

    @FXML
    private Button editButton;

    ObservableList<Reclamation> ListeR;
    public ObservableList<Reclamation> getAllData() throws SQLException{
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String requete = "SELECT * FROM reclamation";
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
    @FXML
    void initialize() {
        assert Col_contenu != null : "fx:id=\"Col_contenu\" was not injected: check your FXML file 'affichage.fxml'.";
        assert Col_nomUser != null : "fx:id=\"Col_nomUser\" was not injected: check your FXML file 'affichage.fxml'.";
        assert Col_type != null : "fx:id=\"Col_type\" was not injected: check your FXML file 'affichage.fxml'.";
        assert ReclamationsTable != null : "fx:id=\"ReclamationsTable\" was not injected: check your FXML file 'affichage.fxml'.";
        assert ajoutBtn != null : "fx:id=\"ajoutBtn\" was not injected: check your FXML file 'affichage.fxml'.";
        assert modifBtn != null : "fx:id=\"modifBtn\" was not injected: check your FXML file 'affichage.fxml'.";
        assert supBtn != null : "fx:id=\"supBtn\" was not injected: check your FXML file 'affichage.fxml'.";

        showReclamations();
    }

    public void showReclamations(){


        Col_nomUser.setCellValueFactory(cellData -> {
            return new javafx.beans.value.ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return "aziz";
                }
            };
        });
        Col_type.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("type"));
        Col_contenu.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("contenu"));

        Callback<TableColumn<Reclamation, String>,TableCell<Reclamation,String>> cellFactory = (param) -> {
            final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                //override updateItem method
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
//ensure that cell is created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
//Now we can create the action button
                        final Button editButton = new Button("Repondre");
//attach listener on button, what to do when clicked
                        editButton.setOnAction(event -> {
                            Reclamation Rec = getTableView().getItems().get(getIndex());

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterR.fxml"));
                            try {
                                Parent root = loader.load();
                                controllers.AjouterR moRdControl = loader.getController();
                                moRdControl.setReponse(Rec);
                                moRdControl.setReclamation(Rec);
                                Stage stage = new Stage();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.showAndWait();
                                showReclamations();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        //Now we can create the action button
                        final Button repButton = new Button("->Reponses");
//attach listener on button, what to do when clicked

                        Reclamation Rec = getTableView().getItems().get(getIndex());
                        MesReponses repo = new MesReponses();
                        if (!repo.getAllData(Rec.getId_reclamation()).isEmpty()) {
                        repButton.setOnAction(event -> {

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mesReponsesAdmin.fxml"));
                                try {
                                    System.out.println("aaa");
                                    Parent root2 = loader.load();
                                    MesReponses repControl = loader.getController();
                                    repControl.setReponses(Rec.getId_reclamation());
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root2));
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                        });
                        repButton.setTranslateX(5);
                        final HBox buttonsPane = new HBox(editButton,repButton);
                        setGraphic(buttonsPane);
                        setText(null);
                    }
                        else {
                            repButton.setTranslateX(5);
                            final HBox buttonsPane = new HBox(editButton,repButton);
                            repButton.setVisible(false);
                            setGraphic(buttonsPane);
                            setText(null);
                        }
//remember to set the created button to cell


                    }
                }

                ;

            };
        return cell;
        };
        Col_Repond.setCellFactory(cellFactory);

        try {
            ListeR=getAllData();
            ReclamationsTable.setItems(ListeR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickAjout(ActionEvent event) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/ajouter.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            showReclamations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onClickModif(ActionEvent event) {

            Reclamation R = this.ReclamationsTable.getSelectionModel().getSelectedItem();
            if (R == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Vous devez selectionner une reclamation afin de la modifier!");
                alert.show();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Confirmation");
                alert1.setHeaderText("Voulez vous vraiment modifier la reclamation N°?"+this.ReclamationsTable.getSelectionModel().getSelectedItem().getId_reclamation());
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
                    showReclamations();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    void onClickSupp(ActionEvent event) {
        Reclamation R = this.ReclamationsTable.getSelectionModel().getSelectedItem();
        if(R == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Vous devez selectionner une reclamation afin de la supprimer!");
            alert.show();
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setTitle("Confirmation");
            alert1.setHeaderText("Voulez vous vraiment supprimer la reclamation N°?" + this.ReclamationsTable.getSelectionModel().getSelectedItem().getId_reclamation());
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
                this.showReclamations();
            }
        }

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
