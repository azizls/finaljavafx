/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import entities.role;
import entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import services.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.ResourceBundle;


public class InscriptionController implements Initializable {

    @FXML
    private TextField text_nom;
    @FXML
    private TextField text_prenom;
    @FXML
    private TextField text_age;
    @FXML
    public TextField text_email;
    @FXML
    private TextField text_mdp;
    @FXML
    private TextField text_adresse;
    @FXML
    private Button button_apply;
    @FXML
    private TextField text_num;
    @FXML
    private Button text_buton_image;
    @FXML
    private TextField text_image;
    @FXML
    private ImageView chargerImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void sauvgarder(ActionEvent event) throws IOException, MessagingException {
        role r1 = new role (2,"user");
        String nom = text_nom.getText();
        String prenom = text_prenom.getText();
        String email = text_email.getText();
        String mdp = text_mdp.getText();
        int num = Integer.parseInt(text_num.getText());
        String adresse = text_adresse.getText();
        int age = Integer.parseInt(text_age.getText());
        String image = text_buton_image.getText();
        String nomImage =text_image.getText();
        int is_enabled = 1 ;
        String hashedcode = controllers.Motdepass.hashPassword(mdp);
      
        try {
        
        if (age < 18 || age > 150) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("L'âge est invalide. Veuillez saisir un âge valide compris entre 18 et 150 ans.");
            alert.showAndWait();
            return;
        }
            } catch (NumberFormatException e) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez saisir un nombre valide pour l'âge.");
        alert.showAndWait();
        return;
         }          
    
         try {
    int numero = Integer.parseInt(text_num.getText());
    String numeroStr = String.valueOf(numero);
    if (!numeroStr.matches("[259]\\d{7}")) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("Le numéro de téléphone est invalide. Veuillez saisir un numéro de téléphone valide commençant par 2, 5 ou 9 et ayant une longueur de 8 caractères.");
        alert.showAndWait();
        text_num.setText("");
        return;
    }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un nombre valide pour le numéro de téléphone.");
            alert.showAndWait();
            text_num.setText("");
            return;
        }
            String password = text_mdp.getText();
            if (!password.matches("^(?=.*[A-Z]).{8,}$")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Le mot de passe est invalide. Veuillez saisir un mot de passe contenant au moins 8 caractères avec au moins une lettre majuscule.");
                alert.showAndWait();
                text_mdp.setText("");
                return;
            }
            String emailRegex = "\\w+\\.?\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}";
            if (!text_email.getText().matches(emailRegex)) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("L'adresse email est invalide. Veuillez saisir une adresse email valide (ex: nom_utilisateur@domaine.com).");
                alert.showAndWait();
                text_email.setText("");
                return;
            }
         File selectedFile1 = new File(text_image.getText());
       user u = new user(nom,prenom,age,email,mdp,num,adresse,selectedFile1.getName() ,r1,is_enabled);


        
        UserService userService = new UserService();
        userService.insert(u);
         // Send confirmation email to the user
        
         String codeConfirmation = home.codeConfirmation ;
         envoyerEmail(email,codeConfirmation);

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Confirmation");
//        alert.setHeaderText(null);
//        alert.setContentText("Utilisateur ajouté avec succès !");
//        alert.showAndWait();
        
        
     
        String destinationPath = "C:\\Users\\lando\\Desktop\\JDBC3A3\\src\\main\\resources\\";

// Récupérer le fichier sélectionné
        File selectedFile = new File(text_image.getText());

// Créer un nouveau fichier dans le dossier de destination avec le même nom que le fichier sélectionné
        File destinationFile = new File(destinationPath + selectedFile.getName());

// Copier le fichier sélectionné dans le dossier de destination
        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

// Définir le nom de l'image dans l'utilisateur
        u.setImage(selectedFile.getName());
        
        
        
        
        
                Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));
                Scene scene = new Scene(root);
                 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 stage.setScene(scene);
                 stage.show();

        // Réinitialiser le formulaire
        text_nom.setText("");
        text_prenom.setText("");
        text_age.setText("");
        text_email.setText("");
        text_mdp.setText("");
        text_num.setText("");
        text_adresse.setText("");
        text_image.setText("");
        }
        
public void envoyerEmail(String email,String codeConfirmation) throws MessagingException {

    final String username = "alaeddine.aouf@esprit.tn";
    final String password = "7984651320Aa";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    Message message = new MimeMessage(session);

    try {
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        message.setSubject("Confirmation d'inscription");

        message.setText("Bonjour,\n\n"
                + "Nous vous confirmons votre inscription à notre site. Voici votre code de confirmation : " + codeConfirmation);

        Transport.send(message);

        System.out.println("Email sent successfully.");

    } catch (MessagingException e) {
        throw new RuntimeException(e);
    }
}

    @FXML
    private void image(ActionEvent event) {
         String email = text_email.getText();
         FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une image");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.gif"),
        new ExtensionFilter("Tous les fichiers", "*.*")
    );
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
        
       
        String imagename = selectedFile.getAbsolutePath();
        text_image.setText(imagename);
    }
    }
    }
    

