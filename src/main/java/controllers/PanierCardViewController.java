package controllers;

import entities.Articles;
import entities.Panier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import services.ArticlesServices;
import services.PanierServices;

import java.awt.*;

public class PanierCardViewController {

    @FXML
    private Text contact;

    @FXML
    private Circle imgContainer;

    @FXML
    private Text nomProduit;

    @FXML
    private Text prixArticle;
    @FXML
    private Button change;
    private Panier panier;
    private Marketplace marketplace;
    private Articles article;
    int id_user = home.userID;

    public void setData(Panier p, Marketplace marketplace){
        ArticlesServices articlesServices = new ArticlesServices();
        article = articlesServices.getByID(p.getId_article());
        nomProduit.setText(article.getNom());
        prixArticle.setText(article.getPrix()+" TND");
        contact.setText(String.valueOf(article.getContact()));
        String img = article.getImage().replace("[","");
        String imgPath = img.replace("]","");
        imgContainer.setFill(new ImagePattern(new Image("file:/"+imgPath)));
        this.panier = p;
        this.marketplace = marketplace;
    }

    @FXML
    public void deletePanierClicked(MouseEvent event){
        PanierServices servicePanier = new PanierServices();
        servicePanier.deleteEntity(panier);
        ArticlesServices articlesServices = new ArticlesServices();
        articlesServices.updatequantite1(article.getId());
        marketplace.panierBtnClicked(null);


    }


    @FXML
    private boolean isInDinar = true; // Variable pour suivre l'état actuel de l'affichage du prix

    @FXML
    void changeCurrency(ActionEvent event) {
        double conversionRate = 0.33; // Taux de conversion de TND en USD

        // Récupérer le prix actuel du produit depuis le texte prixArticle
        String prixString = prixArticle.getText();
        double prix = Double.parseDouble(prixString.replaceAll("[^\\d.]", ""));

        // Vérifier l'état actuel de l'affichage du prix
        if (isInDinar) {
            // Convertir le prix en dollars
            double nouveauPrix = prix * conversionRate;

            // Mettre à jour le texte du prix avec le nouveau prix en dollars
            prixArticle.setText(String.format("%.2f USD", nouveauPrix));
        } else {
            // Convertir le prix en dinar
            double nouveauPrix = prix / conversionRate;

            // Mettre à jour le texte du prix avec le nouveau prix en dinar
            prixArticle.setText(String.format("%.2f TND", nouveauPrix));
        }

        // Inverser l'état de l'affichage du prix pour le prochain clic
        isInDinar = !isInDinar;
    }


}
