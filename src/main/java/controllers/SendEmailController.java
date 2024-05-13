/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author USER
 */
public class SendEmailController implements Initializable {

    @FXML
    private TextField toField;
    @FXML
    private TextField subjectField;
    @FXML
    private Button sendButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField messageField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String message ="message";
     

//...

// Obtention de la date actuelle
LocalDate currentDate = LocalDate.now();

// Affichage de la date dans le format JJ/MM/YYYY
String formattedDate = currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue() + "/" + currentDate.getYear();


      
        messageField.setText("La numero Numéro XXX du Client Foulen Ben Foulen a été éditée le  "+formattedDate );
        subjectField.setText("Traitment de reclamation " );
    }    

    @FXML
    private void handleSendButtonAction(ActionEvent event) {
        // Get the email address and password for the account you will use to send the email
    String username = "maleksamtini@gmail.com";
    String password = "kspqhjlpqsmaxmee";

    // Set the SMTP server address and port for your email service provider
    String smtpHost = "smtp.gmail.com";
    int smtpPort = 587;

    // Get the recipient, subject, and message for the email
    String to = toField.getText();
    String subject = subjectField.getText();
    String message = messageField.getText();

    // Create a properties object to hold the SMTP server settings
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);

    // Create a Session object to authenticate the email account
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        // Create a MimeMessage object to represent the email message
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(username));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mimeMessage.setSubject(subject);
        mimeMessage.setText(message);

        // Send the email message
        Transport.send(mimeMessage);

        // Update the status label to indicate that the email was sent successfully
        statusLabel.setText("Email sent successfully");
    } catch (MessagingException e) {
        // Update the status label to indicate that there was an error sending the email
        statusLabel.setText("Error sending email");
        e.printStackTrace();
    }

    }

    private void sendEmail() {
    // Récupérer l'adresse email et le mot de passe pour le compte que vous utiliserez pour envoyer l'email
    String username = "votre-adresse-email";
    String password = "votre-mot-de-passe-email";

    // Définir l'adresse et le port du serveur SMTP pour votre fournisseur de services email
    String smtpHost = "smtp.votre-fournisseur-de-services-email.com";
    int smtpPort = 587;

    // Définir le destinataire, l'objet et le message pour l'email
    String to = toField.getText();
    String subject = subjectField.getText();
    String message = messageField.getText();

    // Créer un objet Properties pour contenir les paramètres du serveur SMTP
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);

    // Créer un objet Session pour authentifier le compte email
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        // Créer un objet MimeMessage pour représenter le message email
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(username));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mimeMessage.setSubject(subject);
        mimeMessage.setText(message);

        // Envoyer le message email
        Transport.send(mimeMessage);

        // Mettre à jour le label de statut pour indiquer que l'email a été envoyé avec succès
        statusLabel.setText("Email envoyé avec succès");
    } catch (MessagingException e) {
        // Mettre à jour le label de statut pour indiquer qu'il y a eu une erreur lors de l'envoi de l'email
        statusLabel.setText("Erreur lors de l'envoi de l'email");
        e.printStackTrace();
    }
}


  

    
    
}