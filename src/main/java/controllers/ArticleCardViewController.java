package controllers;

import entities.Articles;
import entities.reactions;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import services.ReactionsService;

public class ArticleCardViewController {

    @FXML
    private ImageView button_dislike;

    @FXML
    private ImageView button_like;

    @FXML
    private Label label_dislike;

    @FXML
    private Label label_like;

    @FXML
    private Text contact;

    @FXML
    private Circle imgContainer;

    @FXML
    private Text nomProduit;

    @FXML
    private Text prixArticle;

    private Articles articles;
    private Marketplace marketplace;

    private boolean hasLiked = false;
    private boolean hasDisliked = false;

    public void setData(Articles articles, Marketplace marketplace) {
        String img = articles.getImage().replace("[", "");
        String imgPath = img.replace("]", "");
        System.out.println(imgPath);
        imgContainer.setFill(new ImagePattern(new Image("file:/" + imgPath))); // pour charger l'image dans le cercle
        nomProduit.setText(articles.getNom());
        prixArticle.setText(articles.getPrix() + " TND");
        contact.setText(String.valueOf(articles.getContact()));
        this.articles = articles;
        this.marketplace = marketplace;

        // Mettre à jour les likes et les dislikes
        updateReactionsCount();
    }

    @FXML
    public void detailClicked(MouseEvent event) {
        marketplace.showDetails(articles);
    }

    @FXML
    private void addlike(MouseEvent event) {
        ReactionsService reactionsService = new ReactionsService();

        reactions reaction = new reactions();
        reaction.setId_article(articles.getId());
        reaction.setId_user(home.userID);
        reaction.setType_react("like");

        try {
            // Vérifier si l'utilisateur a déjà réagi
            boolean userHasReacted = reactionsService.checkUserReaction(home.userID, articles.getId());

            if (userHasReacted) {
                // Supprimer la réaction précédente
                reactionsService.deleteReaction(home.userID, articles.getId());
            }

            // Ajouter la nouvelle réaction
            reactionsService.addEntity(reaction);

            // Mettre à jour l'affichage
            updateReactionsCount();

            // Mettre à jour le statut des réactions
            hasLiked = true;
            hasDisliked = false;
        } catch (Exception e) {
            showAlert("Une erreur s'est produite lors de l'ajout du like.");
        }
    }

    @FXML
    private void adddislike(MouseEvent event) {
        ReactionsService reactionsService = new ReactionsService();

        reactions reaction = new reactions();
        reaction.setId_article(articles.getId());
        reaction.setId_user(home.userID);
        reaction.setType_react("dislike");

        try {
            // Vérifier si l'utilisateur a déjà réagi
            boolean userHasReacted = reactionsService.checkUserReaction(home.userID, articles.getId());

            if (userHasReacted) {
                // Supprimer la réaction précédente
                reactionsService.deleteReaction(home.userID, articles.getId());
            }

            // Ajouter la nouvelle réaction
            reactionsService.addEntity(reaction);

            // Mettre à jour l'affichage
            updateReactionsCount();

            // Mettre à jour le statut des réactions
            hasLiked = false;
            hasDisliked = true;
        } catch (Exception e) {
            showAlert("Une erreur s'est produite lors de l'ajout du dislike.");
        }
    }


    private void removeLike() {
        ReactionsService reactionsService = new ReactionsService();
        reactionsService.deleteReaction(articles.getId(), home.userID);
        hasLiked = false; // Mettre à jour la variable hasLiked après avoir supprimé le like
    }

    private void removeDislike() {
        ReactionsService reactionsService = new ReactionsService();
        reactionsService.deleteReaction(articles.getId(), home.userID);
        hasDisliked = false; // Mettre à jour la variable hasDisliked après avoir supprimé le dislike
    }


    private void updateReactionsCount() {
        ReactionsService reactionsService = new ReactionsService();
        int likesCount = reactionsService.getLikesCount(articles.getId());
        int dislikesCount = reactionsService.getDislikesCount(articles.getId());

        // Mettre à jour l'affichage des likes et des dislikes
        label_like.setText("Likes: " + likesCount);
        label_dislike.setText("Dislikes: " + dislikesCount);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
