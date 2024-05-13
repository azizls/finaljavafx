package services;

import entities.Categorie;
import entities.reactions;
import interfaces.Iservices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CategorieServices implements Iservices<Categorie> {
    public void addEntity(Categorie categorie) {
        String requete = "INSERT INTO Categorie(espaceCouvert, espaceEnfants, espaceFumeur, serviceRestauration) VALUES (?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setBoolean(1, categorie.isEspaceCouvert());
            pst.setBoolean(2, categorie.isEspaceEnfants());
            pst.setBoolean(3, categorie.isEspaceFumeur());
            pst.setBoolean(4, categorie.isServiceRestauration());
            pst.executeUpdate();
            System.out.println("Catégorie ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(Categorie categorie) {
        String req = "UPDATE Categorie SET espaceCouvert = ?, espaceEnfants = ?, espaceFumeur = ?, serviceRestauration = ? WHERE id_categorie = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
            ps.setBoolean(1, categorie.isEspaceCouvert());
            ps.setBoolean(2, categorie.isEspaceEnfants());
            ps.setBoolean(3, categorie.isEspaceFumeur());
            ps.setBoolean(4, categorie.isServiceRestauration());
            ps.setInt(5, categorie.getId_categorie());
            ps.executeUpdate();
            System.out.println("Catégorie modifiée avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(Categorie categorie) {
        String req = "DELETE FROM Categorie WHERE id_categorie = ?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, categorie.getId_categorie());
            ps.executeUpdate();
            System.out.println("Catégorie supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Categorie> getAllData() {
        List<Categorie> data = new ArrayList<>();
        String requete = "SELECT * FROM Categorie";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setId_categorie(rs.getInt("id_categorie"));
                categorie.setEspaceCouvert(rs.getBoolean("espaceCouvert"));
                categorie.setEspaceEnfants(rs.getBoolean("espaceEnfants"));
                categorie.setEspaceFumeur(rs.getBoolean("espaceFumeur"));
                categorie.setServiceRestauration(rs.getBoolean("serviceRestauration"));
                data.add(categorie);
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

}