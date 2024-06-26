package controllers;


import entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.UserService;
import utils.MyConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SeconnecterController implements Initializable {

    @FXML
    private Button button_sinscrire;
    @FXML
    private TextField text_email;
    @FXML
    private PasswordField text_mdp;
    @FXML
    private Button button_connecter;
    private Connection connection;

    public static int userID;
    @FXML
    private ImageView imageleft;
    @FXML
    private ImageView imageleft2;
    @FXML
    private Label titleLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void sinscrire(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/inscription.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void seconnecter(ActionEvent event) throws SQLException, IOException  {
        connection=MyConnection.getInstance().getCnx();
        String email = text_email.getText();
        String mdp = text_mdp.getText();

        if (email.isEmpty() || mdp.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir l'email et le mot de passe.");
            alert.showAndWait();
            return;
        }
        try {
            String requete = "SELECT * FROM user WHERE email = ? and mdp = ?";
            PreparedStatement statement = connection.prepareStatement(requete);
            statement.setString(1, email);
            statement.setString(2, mdp);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // User found, check password

                // Passwords match
                int id = resultSet.getInt("id_user");
                System.out.println(id);
                int idRole = resultSet.getInt("id_role");
                if (idRole == 1) {
                    //  int id = resultSet.getInt("id_user");
                    home.userID = resultSet.getInt("id_user");
                    home.email = resultSet.getString("email");
                    home.id_role = resultSet.getInt("id_role");
                    home.imagee = resultSet.getString("image");

                    Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText(null);
                    alert.setContentText("Vous êtes maintenant connecté!");
                    alert.showAndWait();

                    home.userID = resultSet.getInt("id_user");
                    home.email = resultSet.getString("email");
                    home.id_role = resultSet.getInt("id_role");
                    home.imagee = resultSet.getString("image");

                    Parent root = FXMLLoader.load(getClass().getResource("/Frontmodifier.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
//         else {
//            // Passwords don't match
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Erreur");
//            alert.setHeaderText(null);
//            alert.setContentText("Mot de passe incorrect.");
//            alert.showAndWait();
//        }
            } else {
                // User not found
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Email non trouvé.");
                alert.showAndWait();
            }
            resultSet.close();


        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de se connecter à la base de données.");
            alert.showAndWait();
        }
    }
    @FXML
    private void mdp_oubliers(ActionEvent event) throws SQLException, MessagingException {
        String email = text_email.getText();
        if (email.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir votre adresse email.");
            alert.showAndWait();
            return;
        }
        UserService userService = new UserService();
        user utilisateur = userService.readByEmail(email);
        if (utilisateur == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Cette adresse email n'existe pas.");
            alert.showAndWait();
            return;
        }

        String nouveauMotDePasse = genererMotDePasseAleatoire(8);
        //String hashmdp = gui.Motdepass.hashPassword(nouveauMotDePasse);
        userService.setMotDePasse(utilisateur.getId_user(), nouveauMotDePasse);
        envoyerEmail(email, nouveauMotDePasse);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Un nouveau mot de passe a été envoyé à votre adresse email.");
        alert.showAndWait();

    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String genererMotDePasseAleatoire(int longueur) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(longueur);
        for (int i = 0; i < longueur; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    final String username = "alaeddine.aouf@esprit.tn";
    final String password = "7984651320Aa";




    public void envoyerEmail(String email, String nouveauMotDePasse) throws MessagingException {

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
            message.setFrom(new InternetAddress("alaeddine.aouf@esprit.tn"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            message.setSubject("Récupération de mot de passe");

            message.setText("Bonjour ,\n"
                    + " \n"
                    + "Vous avez demandé de récupérer le mot de passe lié à votre compte. Votre nouveau mot de passe est: "
                    + nouveauMotDePasse);

            String msg= "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                    "<head>\n" +
                    "<!--[if gte mso 9]>\n" +
                    "<xml>\n" +
                    "  <o:OfficeDocumentSettings>\n" +
                    "    <o:AllowPNG/>\n" +
                    "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                    "  </o:OfficeDocumentSettings>\n" +
                    "</xml>\n" +
                    "<![endif]-->\n" +
                    "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                    "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                    "  <title></title>\n" +
                    "  \n" +
                    "    <style type=\"text/css\">\n" +
                    "      table, td { color: #000000; } a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_image_1 .v-src-width { width: auto !important; } #u_content_image_1 .v-src-max-width { max-width: 55% !important; } #u_content_image_2 .v-src-width { width: auto !important; } #u_content_image_2 .v-src-max-width { max-width: 60% !important; } #u_content_text_1 .v-container-padding-padding { padding: 30px 30px 30px 20px !important; } #u_content_button_1 .v-container-padding-padding { padding: 10px 20px !important; } #u_content_button_1 .v-size-width { width: 100% !important; } #u_content_button_1 .v-text-align { text-align: left !important; } #u_content_button_1 .v-padding { padding: 15px 40px !important; } #u_content_text_3 .v-container-padding-padding { padding: 30px 30px 80px 20px !important; } #u_content_text_5 .v-text-align { text-align: center !important; } #u_content_text_4 .v-text-align { text-align: center !important; } }\n" +
                    "@media only screen and (min-width: 570px) {\n" +
                    "  .u-row {\n" +
                    "    width: 550px !important;\n" +
                    "  }\n" +
                    "  .u-row .u-col {\n" +
                    "    vertical-align: top;\n" +
                    "  }\n" +
                    "\n" +
                    "  .u-row .u-col-50 {\n" +
                    "    width: 275px !important;\n" +
                    "  }\n" +
                    "\n" +
                    "  .u-row .u-col-100 {\n" +
                    "    width: 550px !important;\n" +
                    "  }\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "@media (max-width: 570px) {\n" +
                    "  .u-row-container {\n" +
                    "    max-width: 100% !important;\n" +
                    "    padding-left: 0px !important;\n" +
                    "    padding-right: 0px !important;\n" +
                    "  }\n" +
                    "  .u-row .u-col {\n" +
                    "    min-width: 320px !important;\n" +
                    "    max-width: 100% !important;\n" +
                    "    display: block !important;\n" +
                    "  }\n" +
                    "  .u-row {\n" +
                    "    width: calc(100% - 40px) !important;\n" +
                    "  }\n" +
                    "  .u-col {\n" +
                    "    width: 100% !important;\n" +
                    "  }\n" +
                    "  .u-col > div {\n" +
                    "    margin: 0 auto;\n" +
                    "  }\n" +
                    "}\n" +
                    "body {\n" +
                    "  margin: 0;\n" +
                    "  padding: 0;\n" +
                    "}\n" +
                    "\n" +
                    "table,\n" +
                    "tr,\n" +
                    "td {\n" +
                    "  vertical-align: top;\n" +
                    "  border-collapse: collapse;\n" +
                    "}\n" +
                    "\n" +
                    "p {\n" +
                    "  margin: 0;\n" +
                    "}\n" +
                    "\n" +
                    ".ie-container table,\n" +
                    ".mso-container table {\n" +
                    "  table-layout: fixed;\n" +
                    "}\n" +
                    "\n" +
                    "* {\n" +
                    "  line-height: inherit;\n" +
                    "}\n" +
                    "\n" +
                    "a[x-apple-data-detectors='true'] {\n" +
                    "  color: inherit !important;\n" +
                    "  text-decoration: none !important;\n" +
                    "}\n" +
                    "\n" +
                    "</style>\n" +
                    "  \n" +
                    "  \n" +
                    "\n" +
                    "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Crimson+Text:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->\n" +
                    "\n" +
                    "</head>\n" +
                    "\n" +
                    "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbeeb8;color: #000000\">\n" +
                    "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                    "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                    "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbeeb8;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "  <tbody>\n" +
                    "  <tr style=\"vertical-align: top\">\n" +
                    "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                    "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbeeb8;\"><![endif]-->\n" +
                    "    \n" +
                    "\n" +
                    "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\n" +
                    "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n" +
                    "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                    "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\n" +
                    "      \n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n" +
                    "  \n" +
                    "<table id=\"u_content_image_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 10px 33px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                    "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                    "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                    "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                    "      \n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                    "  \n" +
                    "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "    <tbody>\n" +
                    "      <tr style=\"vertical-align: top\">\n" +
                    "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "          <span>&#160;</span>\n" +
                    "        </td>\n" +
                    "      </tr>\n" +
                    "    </tbody>\n" +
                    "  </table>\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                    "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n" +
                    "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                    "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\n" +
                    "      \n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"542\" style=\"width: 542px;padding: 0px;border-top: 4px solid #d9d8d8;border-left: 4px solid #d9d8d8;border-right: 4px solid #d9d8d8;border-bottom: 4px solid #d9d8d8;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 4px solid #d9d8d8;border-left: 4px solid #d9d8d8;border-right: 4px solid #d9d8d8;border-bottom: 4px solid #d9d8d8;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                    "  \n" +
                    "<table id=\"u_content_image_2\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "<table id=\"u_content_text_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 30px 30px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "  <div class=\"v-text-align\" style=\"color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                    "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-family: 'Crimson Text', serif; font-size: 14px; line-height: 19.6px;\"><strong><span style=\"font-size: 22px; line-height: 30.8px;\">VIRAGECOM</span></strong></span></p>\n" +
                    "<p style=\"font-size: 14px; line-height: 140%;\">&nbsp;</p>\n" +
                    "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 24px; line-height: 25.2px; font-family: 'Crimson Text', serif;\">You have requested to reset your password </span></p>\n" +
                    "\n" +
                    "  </div>\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "<table id=\"u_content_button_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "<div class=\"v-text-align\" align=\"left\">\n" +
                    "  <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;font-family:arial,helvetica,sans-serif;\"><tr><td class=\"v-text-align\" style=\"font-family:arial,helvetica,sans-serif;\" align=\"left\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"https://unlayer.com\" style=\"height:47px; v-text-anchor:middle; width:456px;\" arcsize=\"6.5%\" strokecolor=\"#ced4d9\" strokeweight=\"3px\" fillcolor=\"#91a5e2\"><w:anchorlock/><center style=\"color:#000000;font-family:arial,helvetica,sans-serif;\"><![endif]-->\n" +
                    "    <a target=\"_blank\" class=\"v-size-width\" style=\"box-sizing: border-box;display: inline-block;font-family:arial,helvetica,sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #000000; background-color: #91a5e2; border-radius: 3px;-webkit-border-radius: 3px; -moz-border-radius: 3px; width:100%; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;border-top-color: #ced4d9; border-top-style: solid; border-top-width: 3px; border-left-color: #ced4d9; border-left-style: solid; border-left-width: 3px; border-right-color: #ced4d9; border-right-style: solid; border-right-width: 3px; border-bottom-color: #ced4d9; border-bottom-style: solid; border-bottom-width: 3px;\">\n" +
                    "      <span class=\"v-padding\" style=\"display:block;padding:15px 40px;line-height:120%;\"><span style=\"font-size: 28px; line-height: 16.8px;\">"+nouveauMotDePasse+"</span></span>\n" +
                    "    </a>\n" +
                    "  <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "<table id=\"u_content_text_3\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 30px 80px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "  <div class=\"v-text-align\" style=\"color: #333333; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                    "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; font-family: 'Crimson Text', serif;\">We cannot simply send you your old password. A unique Code to reset your password has been generated for you. To reset your password, Copy the code.</span></p>\n" +
                    "\n" +
                    "  </div>\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                    "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                    "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                    "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                    "      \n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"550\" style=\"width: 550px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 550px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                    "  \n" +
                    "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                    "  <tbody>\n" +
                    "    <tr>\n" +
                    "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:50px 10px 30px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                    "        \n" +
                    "<div align=\"center\">\n" +
                    "  <div style=\"display: table; max-width:170px;\">\n" +
                    "  <!--[if (mso)|(IE)]><table width=\"170\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"center\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:170px;\"><tr><![endif]-->\n" +
                    "  \n" +
                    "  \n" +
                    "    \n" +
                    "\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </tbody>\n" +
                    "</table>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<div class=\"u-row-container\" style=\"padding: 0px 0px 11px;background-color: transparent\">\n" +
                    "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 550px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                    "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\n" +
                    "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px 0px 11px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:550px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                    "      \n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"275\" style=\"width: 275px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 275px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "<!--[if (mso)|(IE)]><td align=\"center\" width=\"275\" style=\"width: 275px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                    "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 275px;display: table-cell;vertical-align: top;\">\n" +
                    "  <div style=\"width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                    "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                    "  \n" +
                    "\n" +
                    "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                    "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "\n" +
                    "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                    "    </td>\n" +
                    "  </tr>\n" +
                    "  </tbody>\n" +
                    "  </table>\n" +
                    "  <!--[if mso]></div><![endif]-->\n" +
                    "  <!--[if IE]></div><![endif]-->\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
            message.setContent(msg,"text/html");

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; // re-throw the exception so that the caller can handle it
        }
    }

}

