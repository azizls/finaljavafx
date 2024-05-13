package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    private ImageView imageview;
    private TextField txt_role;
    @FXML
    private Button btn;
    @FXML
    private TextField txt_code;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    private void apply(ActionEvent event) throws IOException {
        // récupérer la valeur du code stocké dans la base de données
        String codeStocke = home.codeConfirmation;

        // récupérer le code entré par l'utilisateur
        String codeUtilisateur = txt_code.getText();
    
        // comparer les codes
        if (codeUtilisateur.equals(codeStocke)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("welcome");
            alert.setHeaderText("welcome to viragecom !!");
            alert.showAndWait();
            
            Parent root = FXMLLoader.load(getClass().getResource("/seconnecter.fxml"));
                Scene scene = new Scene(root);
                 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 stage.setScene(scene);
                 stage.show();
        } else {
            
           Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Code incorrect");
        alert.showAndWait();
        txt_code.setText("");
        }
    }
}
