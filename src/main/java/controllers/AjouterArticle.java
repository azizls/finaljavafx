package controllers;

import entities.Articles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.ArticlesServices;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterArticle {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField contacttextfield;

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField imagetextfield;

    @FXML
    private TextField nomtextfield;

    @FXML
    private TextField prixtextfield;
    @FXML
    private TextField id_paniertextfield;
    @FXML
    private TextField quantitetextfield1;

    @FXML
    void AjouterArticle(ActionEvent event) {
        Articles articles =new Articles(nomtextfield.getText(),descriptiontextfield.getText(),Integer.parseInt(prixtextfield.getText()),Integer.parseInt(contacttextfield.getText()),imagetextfield.getText(),Integer.parseInt(contacttextfield.getText()));
        ArticlesServices articlesServices=new ArticlesServices();
        try {
            articlesServices.addEntity(articles);
          Alert alert=new Alert(Alert.AlertType.INFORMATION);
          alert.setContentText("l'utilisateur a éte ajouter aver succés");
          alert.show();
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void initialize() {

    }

}
