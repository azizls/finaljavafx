package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entities.user;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.UserService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontmodifierController implements Initializable {

    @FXML
    private TextField textNom;
    @FXML
    private TextField textPrenom;
    @FXML
    private TextField textAge;
    @FXML
    private TextField textMdp;
    @FXML
    private TextField textAdresse;
    @FXML
    private TextField textNum;
    @FXML
    private Button btnModifier;
    @FXML
    private ImageView imageView;

    @FXML
    private ImageView qrcode;

    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userService = new UserService();
        int utilisateurConnecte = home.userID;
        user utilisateur = userService.readById(utilisateurConnecte);

        textNom.setText(utilisateur.getNom());
        textPrenom.setText(utilisateur.getPrenom());
        textNum.setText(String.valueOf(utilisateur.getNumero()));
        textAdresse.setText(utilisateur.getAdresse());
        textMdp.setText(utilisateur.getMdp());
        textAge.setText(String.valueOf(utilisateur.getAge()));

        String dossierImages = "C:\\Users\\lando\\Desktop\\JDBC3A3\\src\\main\\resources\\";
        String nomImage = utilisateur.getImage();
        String cheminImage = dossierImages + nomImage;
        Image img = new Image(new File(cheminImage).toURI().toString());
        imageView.setImage(img);

        try {
            generateQRCode(utilisateur.getNom(), utilisateur.getPrenom());
        } catch (Exception ex) {
            Logger.getLogger(FrontmodifierController.class.getName()).log(Level.SEVERE, null, ex);
            // Gérer l'échec de la création du code QR ici
        }
    }

    @FXML
    private void modifier(ActionEvent event) throws IOException {
        int idUtilisateur = home.userID;
        user utilisateur = userService.readById(idUtilisateur);
        String mdp = textMdp.getText();

        utilisateur.setNom(textNom.getText());
        utilisateur.setPrenom(textPrenom.getText());
        utilisateur.setNumero(Integer.parseInt(textNum.getText()));
        utilisateur.setAdresse(textAdresse.getText());
        utilisateur.setMdp(mdp);
        utilisateur.setAge(Integer.parseInt(textAge.getText()));

        userService.update(utilisateur);
        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "L'utilisateur a été modifié avec succès !");
    }




    private void generateQRCode(String nom, String prenom) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String information = "Nom: " + nom + "\nPrénom: " + prenom;
        int width = 300;
        int height = 300;

        BitMatrix byteMatrix = qrCodeWriter.encode(information, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = byteMatrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                bufferedImage.setRGB(i, j, color);
            }
        }

        ImageIO.write(bufferedImage, "png", new File("qr_code.png"));
        Image img = new Image("qr_code.png");
        qrcode.setImage(img);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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

        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/AfficherClient.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
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
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors du chargement de la page de réclamation.");
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
}
