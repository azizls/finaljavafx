package controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.ReclamationServices;
import services.ReponseServices;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterR {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button envoyerBtn;

    @FXML
    private TextArea reponse;
    @FXML
    private Label idlabel ;

    private Reclamation reclamation;


    ReclamationServices RS = new ReclamationServices();

    @FXML
    void initialize() {
        assert envoyerBtn != null : "fx:id=\"envoyerBtn\" was not injected: check your FXML file 'ajouterR.fxml'.";
        assert idlabel != null : "fx:id=\"idlabel\" was not injected: check your FXML file 'ajouterR.fxml'.";
        assert reponse != null : "fx:id=\"reponse\" was not injected: check your FXML file 'ajouterR.fxml'.";

        idlabel.setText("0");
        reponse.setWrapText(true);
    }
    public void setReponse(Reclamation R) {

        idlabel.setText(String.valueOf(R.getId_reclamation()));
    }
    @FXML
    void onClick(ActionEvent event) {
        if (reponse.getText() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.show();
        }
        else {
            Reponse R = new Reponse(1,Integer.parseInt(idlabel.getText()),reponse.getText());
            ReponseServices rs = new ReponseServices();
            try {
                rs.addEntity(R);
                RS.updateStatus(reclamation,"en cours de traitement");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Reponse envoyée avec succés");
                alert.show();
                // Assuming you have a reference to the current stage
                Stage stage = (Stage) envoyerBtn.getScene().getWindow();


// Closing the current stage
                stage.close();


            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }

    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }
}
