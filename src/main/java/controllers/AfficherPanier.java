package controllers;

import entities.Articles;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.List;

public class AfficherPanier {

    @FXML
    private VBox pannierVbox;
    @FXML
    private VBox articlesVBox;
    @FXML
    private Text descriptionDetail;
    @FXML
    private Text nomProduit;
    @FXML
    private Circle imgDetailContainer;
    private List<Articles> articles;
    private Articles currentArticle;


}
