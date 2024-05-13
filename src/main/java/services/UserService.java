package services;

import controllers.home;
import entities.role;
import entities.user;
import interfaces.IService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService implements IService<user> {

    private Connection conn;

    public UserService() {
        conn= MyConnection.getInstance().getCnx();
    }


    @Override
    public void insert(user t) {
        try {
            home.codeConfirmation = generate(8);
            String codeConfirmation = home.codeConfirmation;

            String requete = "insert into user (nom,prenom,age,email,mdp,numero,adresse,image,id_role,code_confirmation,is_enabled) values (?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement usr = conn.prepareStatement(requete);
            usr.setString(1, t.getNom());
            usr.setString(2, t.getPrenom());
            usr.setInt(3, t.getAge());
            usr.setString(4, t.getEmail());
            usr.setString(5, t.getMdp());
            usr.setInt(6, t.getNumero());
            usr.setString(7, t.getAdresse());
            usr.setString(8, t.getImage());
            usr.setInt(9, t.getId_role().getId_role());
            usr.setString(10, codeConfirmation);
            usr.setInt(11, t.getIs_enabled());
            usr.executeUpdate();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Utilisateur ajouté avec succès !");
            alert.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void delete(user t) {
        String requete="delete from user where id_user = "+t.getId_user();
        try {
            Statement st=conn.createStatement();
            st.executeUpdate(requete);
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete1(int id) {

        String requete = "DELETE FROM user WHERE id_user = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(requete);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(user t) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET nom = ?, prenom = ?, age = ?, email = ?, mdp = ?, numero = ?, adresse = ?, image = ? WHERE id_user = ?");
            ps.setString(1, t.getNom());
            ps.setString(2, t.getPrenom());
            ps.setInt(3, t.getAge());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getMdp());
            ps.setInt(6, t.getNumero());
            ps.setString(7, t.getAdresse());
            ps.setString(8, t.getImage());
            ps.setInt(9, t.getId_user());
            ps.executeUpdate();


        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




    @Override
    public List<user> readAll() {
        String requete = "SELECT user.*, role.id_role, role.type FROM user INNER JOIN role ON user.id_role = role.id_role";
        List<user> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                role r = new role();
                r.setId_role(rs.getInt("id_role"));
                r.setType(rs.getString("type"));
                user t;
                t = new user(rs.getInt("id_user"), rs.getString("nom"),rs.getString("prenom"),rs.getInt("age"),rs.getString("email"),rs.getString("mdp"),rs.getInt("numero"),rs.getString("adresse"),rs.getString("image"),r);
                list.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }




    @Override
    public user readById(int id_user) {
        String requete;
        requete = "SELECT user.*, role.id_role, role.type FROM user INNER JOIN role ON user.id_role = user.id_role where id_user=" + id_user;

        user t = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                role r = new role();
                r.setId_role(rs.getInt("id_role"));
                r.setType(rs.getString("type"));

                t = new user(rs.getInt("id_user"), rs.getString("nom"),rs.getString("prenom"),rs.getInt("age"),rs.getString("email"),rs.getString("mdp"),rs.getInt("numero"),rs.getString("adresse"),rs.getString("image"),r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return t;
    }

    public user readByEmail(String email) {
        user t = null;
        String query = "SELECT user.*, role.id_role, role.type FROM user INNER JOIN role ON user.id_role = role.id_role WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                role r = new role();
                r.setId_role(rs.getInt("id_role"));
                r.setType(rs.getString("type"));
                t = new user(rs.getInt("id_user"), rs.getString("nom"),rs.getString("prenom"),rs.getInt("age"),rs.getString("email"),rs.getString("mdp"),rs.getInt("numero"),rs.getString("adresse"),rs.getString("image"),r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }
    public user readByNom(String nom) {
        String requete = "SELECT user.*, role.id_role, role.type FROM user INNER JOIN role ON user.id_role = role.id_role WHERE nom='" + nom + "'";
        user t = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            if (rs.next()) {
                role r = new role();
                r.setId_role(rs.getInt("id_role"));
                r.setType(rs.getString("type"));
                t = new user(rs.getInt("id_user"), rs.getString("nom"), rs.getString("prenom"), rs.getInt("age"), rs.getString("email"), rs.getString("mdp"), rs.getInt("numero"
                        + ""), rs.getString("adresse"), rs.getString("image"), r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }

    public void setMotDePasse(int id_user, String hashmdp) {
        String query = "UPDATE user SET mdp = ? WHERE id_user = ?";
        try (
                Connection conn = MyConnection.getInstance().getCnx();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hashmdp);
            ps.setInt(2, id_user);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }

    @Override
    public user get_annonce(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }







}

