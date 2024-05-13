package services;

import entities.reactions;
import interfaces.Iservices;
import utils.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReactionsService implements Iservices<reactions> {

    private Connection conn;

    public ReactionsService() {
        conn = MyConnection.getInstance().getCnx();
    }

    @Override
    public void addEntity(reactions r) {
        String requete = "INSERT INTO reactions (id_article, id_user, type_react) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(requete)) {
            pst.setInt(1, r.getId_article());
            pst.setInt(2, r.getId_user());
            pst.setString(3, r.getType_react());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateEntity(reactions reactions) {

    }

    @Override
    public void deleteEntity(reactions reactions) {

    }

    @Override
    public List<reactions> getAllData() {
        return null;
    }

    @Override
    public void update(reactions r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(reactions r) {
        String requete = "DELETE FROM reactions WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(requete)) {
            pst.setInt(1, r.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<reactions> readAll() {
        List<reactions> reactionsList = new ArrayList<>();
        String requete = "SELECT * FROM reactions";
        try (PreparedStatement pst = conn.prepareStatement(requete); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                reactions r = new reactions();
                r.setId(rs.getInt("id"));
                r.setId_article(rs.getInt("id_article"));
                r.setId_user(rs.getInt("id_user"));
                r.setType_react(rs.getString("type_react"));
                reactionsList.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reactionsList;
    }

    @Override
    public reactions readById(int id) {
        String requete = "SELECT * FROM reactions WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(requete)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    reactions r = new reactions();
                    r.setId(rs.getInt("id"));
                    r.setId_article(rs.getInt("id_article"));
                    r.setId_user(rs.getInt("id_user"));
                    r.setType_react(rs.getString("type_react"));
                    return r;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public reactions get_annonce(int id) {
        return null;
    }

    @Override
    public void insert(reactions t) {

    }


    public int getLikesCount(int articleId) {
        String query = "SELECT COUNT(*) AS likesCount FROM reactions WHERE id_article = ? AND type_react = 'like'";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, articleId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("likesCount");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0; // En cas d'erreur ou si aucun like n'est trouvé, retourne 0
    }


    public int getDislikesCount(int articleId) {
        String query = "SELECT COUNT(*) AS dislikesCount FROM reactions WHERE id_article = ? AND type_react = 'dislike'";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, articleId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("dislikesCount");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0; // En cas d'erreur ou si aucun dislike n'est trouvé, retourne 0
    }



    // Méthode pour vérifier si un utilisateur a réagi à un article donné
    public boolean checkUserReaction(int id_user, int id_article) {
        String query = "SELECT * FROM reactions WHERE id_user = ? AND id_article = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id_user);
            pst.setInt(2, id_article);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Méthode pour récupérer la réaction d'un utilisateur sur un article donné
    public reactions getReaction(int userId, int articleId) {
        String query = "SELECT * FROM reactions WHERE id_user = ? AND id_article = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, articleId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    reactions r = new reactions();
                    r.setId(rs.getInt("id"));
                    r.setId_article(rs.getInt("id_article"));
                    r.setId_user(userId);
                    r.setType_react(rs.getString("type_react"));
                    return r;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Méthode pour supprimer une réaction d'un utilisateur sur un article donné
    public void deleteReaction(int id_user, int id_article) {
        String requete = "DELETE FROM reactions WHERE id_user = ? AND id_article = ?";
        try (PreparedStatement pst = conn.prepareStatement(requete)) {
            pst.setInt(1, id_user);
            pst.setInt(2, id_article);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReactionsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
