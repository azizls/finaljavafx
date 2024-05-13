package controllers;

import entities.Articles;
import entities.Panier;
import entities.Sms;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import services.ArticlesServices;
import services.PanierServices;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Marketplace implements Initializable {
    @FXML
    private Button back_button;

    @FXML
    private Button back_button1;

    @FXML
    private VBox articlesVBox;
    @FXML
    private Text descriptionDetail;
    @FXML
    private Text nomProduit;
    @FXML
    private ImageView pdfimage;

    @FXML
    private Circle imgDetailContainer;
    @FXML
    private AnchorPane detailsContainer;
    @FXML
    private AnchorPane panierContainer;
    @FXML
    private VBox panierVBox;
    private List<Articles> articles;
    private Articles currentArticle;
    @FXML
    private Text sommePrixText;
    @FXML
    private Text sommePrixText1;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panierContainer.setVisible(false);
        detailsContainer.setVisible(false);
        ArticlesServices articlesServices = new ArticlesServices();
        articles = articlesServices.getAllData();
        showArticles(articles);
    }
    public void showArticles(List<Articles> list){
        articlesVBox.getChildren().clear();
        for (Articles a : list){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/articleCardView.fxml"));
                Parent root = fxmlLoader.load();
                ArticleCardViewController controller = fxmlLoader.getController();
              controller.setData(a,this);
                articlesVBox.getChildren().add(root);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void showDetails(Articles a){
        nomProduit.setText(a.getNom());
        descriptionDetail.setText(a.getDescription());
        String img = a.getImage().replace("[","");
        String imagePath = img.replace("]","");
        imgDetailContainer.setFill(new ImagePattern(new Image("file:/"+imagePath)));
        detailsContainer.setVisible(true);
        currentArticle = a;
    }
    @FXML
    public void goBack(MouseEvent event){

        panierContainer.setVisible(false);
        detailsContainer.setVisible(false);
    }
    @FXML
    public void ajoutPanier(MouseEvent event){
        Panier panier = new Panier(home.userID,1,currentArticle.getId());
        PanierServices panierServices = new PanierServices();
        panierServices.addEntity(panier);
        ArticlesServices articlesServices = new ArticlesServices();
        articlesServices.updateQuantite(currentArticle.getId());
        Sms s=new Sms();
        s.SmsSend("54821391");
    }
    @FXML
    public void recherche(KeyEvent search){
        TextInputControl textInputControl = (TextInputControl) search.getSource();
        String searchInput = textInputControl.getText();
        if (!searchInput.isEmpty()){
            showArticles(articles.stream().filter( a -> a.getNom().contains(searchInput)).collect(Collectors.toList()));
        } else
            showArticles(articles);
    }
    /*
    @FXML
    public void panierBtnClicked(MouseEvent event){
        panierVBox.getChildren().clear();
        PanierServices panierServices = new PanierServices();
        List<Panier> paniers = panierServices.getPanierByUser(1);
        for(Panier p : paniers){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/panierCardView.fxml"));
                Parent root = fxmlLoader.load();
                PanierCardViewController controller = fxmlLoader.getController();
                controller.setData(p,this);
                panierVBox.getChildren().add(root);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        panierContainer.setVisible(true);

        int idUtilisateur = 1; // ID de l'utilisateur connecté (remplacez-le par l'ID de l'utilisateur actuel)
        PanierServices panierService = new PanierServices();
        int sommePrix = panierServices.calculerSommePrixArticlesDansPanier(idUtilisateur);
        sommePrixText.setText("LA Somme des prix dans le panier est " + sommePrix + " TND");




    }
    @FXML
    public void panierBtnClicked(MouseEvent event){
        panierVBox.getChildren().clear();
        PanierServices panierServices = new PanierServices();
        List<Panier> paniers = panierServices.getPanierByUser(1);

        // Calculer la somme des prix
        int sommePrix = panierServices.calculerSommePrixArticlesDansPanier(1);

        // Appliquer la remise de 10% si le nombre d'articles dans le panier est supérieur à 2
        if (paniers.size() > 2) {
            sommePrix *= 0.9; // Appliquer une remise de 10%
        }

        // Afficher la somme des prix dans le panier
        sommePrixText1.setText("La somme des prix dans le panier après remise est " + sommePrix + " TND");

        // Afficher les articles dans le panier
        for(Panier p : paniers){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/panierCardView.fxml"));
                Parent root = fxmlLoader.load();
                PanierCardViewController controller = fxmlLoader.getController();
                controller.setData(p,this);
                panierVBox.getChildren().add(root);
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        panierContainer.setVisible(true);
    }

*/
    public void panierBtnClicked(MouseEvent event){
        panierVBox.getChildren().clear();
        PanierServices panierServices = new PanierServices();
        List<Panier> paniers = panierServices.getPanierByUser(home.userID);

        // Calculer la somme des prix avant remise
        int sommePrixAvantRemise = panierServices.calculerSommePrixArticlesDansPanier(home.userID);

        // Appliquer la remise de 10% si le nombre d'articles dans le panier est supérieur à 2
        int sommePrixApresRemise = sommePrixAvantRemise;
        if (paniers.size() > 2) {
            sommePrixApresRemise *= 0.9; // Appliquer une remise de 10%
        }

        // Afficher la somme des prix dans le panier avant et après remise
        sommePrixText.setText("La somme des prix dans le panier avant remise est " + sommePrixAvantRemise + " TND");
        sommePrixText1.setText("La somme des prix dans le panier après remise est " + sommePrixApresRemise + " TND");

        // Afficher les articles dans le panier
        for(Panier p : paniers){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/panierCardView.fxml"));
                Parent root = fxmlLoader.load();
                PanierCardViewController controller = fxmlLoader.getController();
                controller.setData(p,this);
                panierVBox.getChildren().add(root);
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        panierContainer.setVisible(true);
        int idUtilisateur = 23; // ID de l'utilisateur connecté (remplacez-le par l'ID de l'utilisateur actue
    }
    @FXML
    void exportTopdf(MouseEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            if (printerJob.showPrintDialog(panierVBox.getScene().getWindow())) {
                PageLayout pageLayout = printerJob.getPrinter().getDefaultPageLayout();
                double scaleX = pageLayout.getPrintableWidth() / panierVBox.getBoundsInParent().getWidth();
                double scaleY = pageLayout.getPrintableHeight() / panierVBox.getBoundsInParent().getHeight();
                double scale = Math.min(scaleX, scaleY);
                Scale printScale = new Scale(scale, scale);
                panierVBox.getTransforms().add(printScale);
                boolean success = printerJob.printPage(panierVBox);
                if (success) {
                    showSuccessMessage("Le PDF a été téléchargé avec succès !");
                    printerJob.endJob();
                } else {
                    showErrorMessage("Une erreur s'est produite lors du téléchargement du PDF.");
                }

                panierVBox.getTransforms().remove(printScale); // Réinitialiser la transformation après l'impression
            }

        }


    }
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void evenement(ActionEvent event) throws IOException {

        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource(".fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }

    @FXML
    private void espacepartenaire(ActionEvent event) throws IOException {

    }

    @FXML
    private void monprofil(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Frontmodifier.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
    }

    @FXML
    private void reclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mesReclamations.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
           // showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors du chargement de la page de réclamation.");
        }
    }



    @FXML
    private void produit(ActionEvent event) throws IOException {

        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Marketplace.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }


    @FXML
    void back(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Frontmodifier.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);

    }

}


