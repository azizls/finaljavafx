package controllers;


import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.FilterService;
import services.ReclamationServices;

import java.net.URL;
import java.util.ResourceBundle;

public class Ajouter {




    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bouton;

    @FXML
    private TextArea reclamation;

    @FXML
    private ChoiceBox<String> type;

    FilterService filterService = new FilterService();

    @FXML
    void onClick(ActionEvent event) {
        if (type.getValue() == null || reclamation.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.show();
        }
        String filtredText = FilterService.filterProfanity(reclamation.getText());
        if (FilterService.containsBadWord(filtredText)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Votre reclamation contient un mot inapproprié" +
                    "\n"+filtredText);
            alert.show();
        }
        else {

            Reclamation R = new Reclamation(1, home.userID, type.getValue(), reclamation.getText());
            ReclamationServices RS = new ReclamationServices();
            RS.addEntity(R);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Reclamation envoyée avec succés");
            alert.show();
            Stage stage = (Stage) bouton.getScene().getWindow();


// Closing the current stage
            stage.close();


        }
    }

    @FXML
    void initialize() {
        assert bouton != null : "fx:id=\"bouton\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert reclamation != null : "fx:id=\"reclamation\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert type != null : "fx:id=\"type\" was not injected: check your FXML file 'ajouter.fxml'.";
        reclamation.setWrapText(true);
        type.getItems().addAll("Evenement","Espace","MarketPlace");

    }



}

