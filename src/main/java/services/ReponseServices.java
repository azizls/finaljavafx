package services;

import entities.Reponse;
import entities.reactions;
import entities.user;
import interfaces.Iservices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReponseServices implements Iservices<Reponse> {


    UserService US = new UserService();
    ReclamationServices RS = new ReclamationServices();


    MailService mailService = new MailService();


    @Override
    public void addEntity(Reponse reponse) {
        String requete = "INSERT INTO reponse (id_reclamation, contenu) VALUES (?, ?)";
        try {
            // Préparation de la requête SQL avec PreparedStatement
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            // Définition des valeurs des paramètres de la requête
            pst.setInt(1, reponse.getId_reclamation());
            pst.setString(2, reponse.getContenu());

            // Exécution de la requête d'insertion
            int rowsAffected = pst.executeUpdate();

            // Vérification si la requête a affecté des lignes
            if (rowsAffected > 0) {
                // Récupération de l'utili;sateur associé à la réclamation
                System.out.println(RS.getReclamationById(reponse.getId_reclamation())+"***");
                user u = US.readById(RS.getReclamationById(reponse.getId_reclamation()).getId_user());
                System.out.println("***"+RS.getReclamationById(reponse.getId_reclamation()).getId_user());
                // Vérification si l'utilisateur est null avant d'envoyer l'email
                if (u != null) {
                    String userEmail = u.getEmail();
                    System.out.println(userEmail);
                    String message = "Traitement Reclamations\nUne reponse a été envoyée à votre réclamation. Merci pour votre patience.";
                    mailService.sendEmail(userEmail, "Traitement Reclamations", message);
                } else {
                    System.out.println("L'utilisateur associé à la réclamation est null.");
                }

                System.out.println("Réponse ajoutée avec succès");
            } else {
                System.out.println("Échec de l'ajout de la réponse");
            }

        } catch (SQLException e) {
            // Gestion des exceptions
            System.out.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }



    @Override
    public void updateEntity(Reponse reponse) {
        String requete = "UPDATE reponse SET id_reclamation=?, contenu=? WHERE id_reponse=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, reponse.getId_reclamation());
            pst.setString(2, reponse.getContenu());
            pst.setInt(3, reponse.getId_reponse());
            pst.executeUpdate();
            System.out.println("Réponse mise à jour avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(Reponse reponse) {
        String requete = "DELETE FROM reponse WHERE id_reponse=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, reponse.getId_reponse());
            pst.executeUpdate();
            System.out.println("Réponse supprimée avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reponse> getAllData() {
        List<Reponse> reponses = new ArrayList<>();
        String requete = "SELECT * FROM reponse";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId_reponse(rs.getInt("id_reponse"));
                reponse.setId_reclamation(rs.getInt("id_reclamation"));
                reponse.setContenu(rs.getString("contenu"));

                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(reponses);
        return reponses;
    }

    @Override
    public void update(reactions r) {

    }

    @Override
    public void delete(reactions r) {

    }

    @Override
    public List<reactions> readAll() {
        return null;
    }

    @Override
    public reactions readById(int id) {
        return null;
    }

    @Override
    public reactions get_annonce(int id) {
        return null;
    }

    @Override
    public void insert(reactions t) {

    }

    public List<Reponse> getAllDataId(int id_reclam) {
        List<Reponse> reponses = new ArrayList<>();
        String requete = "SELECT * FROM reponse WHERE id_reclamation="+id_reclam;
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId_reponse(rs.getInt("id_reponse"));
                reponse.setId_reclamation(rs.getInt("id_reclamation"));
                reponse.setContenu(rs.getString("contenu"));
                reponse.setDate_rep(rs.getDate("date_rep"));

                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reponses;
    }}