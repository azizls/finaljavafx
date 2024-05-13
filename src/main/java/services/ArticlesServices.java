package services;

import entities.Articles;
import entities.reactions;
import interfaces.Iservices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticlesServices implements Iservices<Articles> {




    @Override
    public void addEntity(Articles articles) {
        String requete = "insert INTO Articles (nom,description,prix,image,contact,quantite) Values (?,?,?,?,?,?)";
        try {

            PreparedStatement st= MyConnection.getInstance().getCnx().prepareStatement(requete);
            st.setString(1,articles.getNom());
            st.setString(2,articles.getDescription());
            st.setInt(3,articles.getPrix());
            st.setString(4,articles.getImage());
            st.setInt(5,articles.getContact());
            st.setInt(6,articles.getQuantite());
            try {
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void updateEntity(Articles articles) {
        String requete = "UPDATE Articles SET nom=?, description=?, prix=?, image=?, contact=?, quantité=? WHERE id=?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ps.setString(1, articles.getNom());
            ps.setString(2, articles.getDescription());
            ps.setInt(3, articles.getPrix());
            ps.setString(4, articles.getImage());
            ps.setInt(5, articles.getContact());
            ps.setInt(6, articles.getQuantite());


            ps.setInt(7, articles.getId()); // Supposons que votre classe Articles ait une méthode getId() pour obtenir l'identifiant de l'article à mettre à jour
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEntity(Articles articles) {
        String requete = "DELETE FROM Articles WHERE id=?";
        try {
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ps.setInt(1, articles.getId()); // Supposons que votre classe Articles ait une méthode getId() pour obtenir l'identifiant de l'article à supprimer
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Articles> getAllData() {
        List<Articles> data = new ArrayList<>();
        String requete = "SELECT * FROM Articles";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                int prix = rs.getInt("prix");
                int contact = rs.getInt("contact");
                String image = rs.getString("image");
                int quantite = rs.getInt("quantite");
                // Créer un objet Articles à partir des données récupérées
                Articles article = new Articles(id, nom, description, prix, contact, image,quantite);
                // Ajouter l'article à la liste
                data.add(article);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public void updateQuantite(int idArticle){
        String requete = "SELECT quantite from articles where id = ?";
        try{
            PreparedStatement st = MyConnection.getInstance().getCnx().prepareStatement(requete);
            st.setInt(1,idArticle);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int oldNbr = rs.getInt("quantite");
                int newNbr = oldNbr - 1;
                if(newNbr >= 0){
                    String requete2 = "UPDATE articles set quantite = ? where id = ?";
                    PreparedStatement st2 = MyConnection.getInstance().getCnx().prepareStatement(requete2);
                    st2.setInt(1,newNbr);
                    st2.setInt(2,idArticle);
                    st2.executeUpdate();
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public void updatequantite1(int idArticle){
        String requete = "SELECT quantite from articles where id = ?";
        try{
            PreparedStatement st = MyConnection.getInstance().getCnx().prepareStatement(requete);
            st.setInt(1,idArticle);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int oldNbr = rs.getInt("quantite");
                int newNbr = oldNbr + 1;
                if(newNbr >= 0){
                    String requete2 = "UPDATE articles set quantite = ? where id = ?";
                    PreparedStatement st2 = MyConnection.getInstance().getCnx().prepareStatement(requete2);
                    st2.setInt(1,newNbr);
                    st2.setInt(2,idArticle);
                    st2.executeUpdate();
                }

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public Articles getByID(int id) {
        String requete = "SELECT * FROM ARTICLES WHERE id = ?";
        try {
            PreparedStatement stm = MyConnection.getInstance().getCnx().prepareStatement(requete);
            stm.setInt(1,id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                int idA = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                int prix = rs.getInt("prix");
                int contact = rs.getInt("contact");
                String image = rs.getString("image");
                int quantite = rs.getInt("quantite");
                // Créer un objet Articles à partir des données récupérées
                return new Articles(idA, nom, description, prix, contact, image,quantite);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new Articles();
    }

}
