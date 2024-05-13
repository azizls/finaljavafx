package services;
import controllers.home;
import entities.EspacePartenaire;
import entities.reactions;
import interfaces.Iservices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class EspaceServices implements Iservices<EspacePartenaire>  {
    public void addEntity(EspacePartenaire espacePartenaire) {
        String requete = "INSERT INTO EspacePartenaire(nom, localisation, type, description, photos,id_categorie,id_user) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, espacePartenaire.getNom());
            pst.setString(2, espacePartenaire.getLocalisation());
            pst.setString(3, espacePartenaire.getType());
            pst.setString(4, espacePartenaire.getDescription());
            // Convertir la liste de photos en une chaîne séparée par des virgules
            String photosStr = String.join(",", espacePartenaire.getPhotos());
            pst.setString(5, photosStr);
            pst.setInt(6,recupererDernierIdCategorie());
            pst.setInt(7, home.userID);

            pst.executeUpdate();
            System.out.println("Espace Partenaire ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(EspacePartenaire espacePartenaire) {
        String req = "UPDATE EspacePartenaire SET nom = ?, localisation = ?, type = ?, description = ?, photos = ? , accepted = ? WHERE id_espace = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, espacePartenaire.getNom());
            ps.setString(2, espacePartenaire.getLocalisation());
            ps.setString(3, espacePartenaire.getType());
            ps.setString(4, espacePartenaire.getDescription());
// Set the 5th parameter for photos
            String photosStr = String.join(",", espacePartenaire.getPhotos());
            ps.setString(5, photosStr);
// Set the 6th parameter for accepted
            ps.setBoolean(6, espacePartenaire.isAccepted());
// Set the 7th parameter for id_espace
            ps.setInt(7, espacePartenaire.getId_espace());

            ps.executeUpdate();
            System.out.println("Espace Partenaire modifié avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(EspacePartenaire espacePartenaire) {
        String req = "DELETE FROM EspacePartenaire WHERE id_espace = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, espacePartenaire.getId_espace());
            ps.executeUpdate();
            System.out.println("Espace Partenaire supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<EspacePartenaire> getAllData() {
        List<EspacePartenaire> data = new ArrayList<>();
        String requete = "SELECT * FROM EspacePartenaire";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                EspacePartenaire espacePartenaire = new EspacePartenaire();
                espacePartenaire.setId_espace(rs.getInt("id_espace"));
                espacePartenaire.setNom(rs.getString("nom"));
                espacePartenaire.setLocalisation(rs.getString("localisation"));
                espacePartenaire.setType(rs.getString("type"));
                espacePartenaire.setDescription(rs.getString("description"));
                espacePartenaire.setId_categorie(rs.getInt("id_categorie"));

                // Convertir la chaîne de photos en une liste
                String photosStr = rs.getString("photos");
                List<String> photos = new ArrayList<>();
                if (photosStr != null && !photosStr.isEmpty()) {
                    String[] photosArr = photosStr.split(",");
                    for (String photo : photosArr) {
                        photos.add(photo);
                    }
                }
                espacePartenaire.setPhotos(photos);
                data.add(espacePartenaire);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
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

    public int recupererDernierIdCategorie() {
        int DernierCategorie = 0;
        try {
            String requete = "SELECT MAX(id_categorie) FROM categorie";
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DernierCategorie = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return DernierCategorie;
    }
    public boolean isLocalisationUnique(String localisation) {
        boolean unique = true;
        String requete = "SELECT COUNT(*) FROM EspacePartenaire WHERE localisation = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, localisation);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    unique = count == 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return unique;
    }

}