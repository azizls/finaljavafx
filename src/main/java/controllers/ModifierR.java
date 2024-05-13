package controllers;

import entities.Reponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ReponseServices;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierR {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button EnvoyerBtn;

    @FXML
    private TextArea Reponse;

    @FXML
    private Label idLabel;

    @FXML
    private TextField id_Reclam;

    @FXML
    void onClick(ActionEvent event) {
        if (id_Reclam.getText() == null || Reponse.getText() == null ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.show();
        }
        else {
            Reponse R = new Reponse(Integer.parseInt(idLabel.getText()), Integer.parseInt(id_Reclam.getText()),Reponse.getText());
            ReponseServices RS = new ReponseServices();
            RS.updateEntity(R);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Reclamation modifiée avec succés");
            alert.show();
            // Assuming you have a reference to the current stage
            Stage stage = (Stage) EnvoyerBtn.getScene().getWindow();


// Closing the current stage
            stage.close();
        }


    }

    public void setReponse(Reponse R){

        idLabel.setText(String.valueOf(R.getId_reponse()));
        id_Reclam.setText(String.valueOf(R.getId_reclamation()));
        Reponse.setText(String.valueOf(R.getContenu()));
        Reponse.setWrapText(true);
        idLabel.setVisible(false);
        id_Reclam.setVisible(false);
    }


    @FXML
    void initialize() {
        assert EnvoyerBtn != null : "fx:id=\"EnvoyerBtn\" was not injected: check your FXML file 'modifierR.fxml'.";
        assert Reponse != null : "fx:id=\"Reponse\" was not injected: check your FXML file 'modifierR.fxml'.";
        assert idLabel != null : "fx:id=\"idLabel\" was not injected: check your FXML file 'modifierR.fxml'.";
        assert id_Reclam != null : "fx:id=\"id_Reclam\" was not injected: check your FXML file 'modifierR.fxml'.";
        Reponse.setWrapText(true);

    }

}