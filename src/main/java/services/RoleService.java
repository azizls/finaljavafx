package services;

import entities.role;
import interfaces.IService;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RoleService implements IService<role> {

    private Connection conn;

    public RoleService() {
        conn= MyConnection.getInstance().getCnx();
    }

    @Override
    public void insert(role t) {
        String requete="insert into role (type) values (?)";
        try {
            PreparedStatement usr=conn.prepareStatement(requete);
            usr.setString(1,t.getType());
            usr.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(role t) {
        String requete="delete from role where id_role = "+t.getId_role();
        try {
            Statement st=conn.createStatement();
            st.executeUpdate(requete);
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(role t) {
        String requete = "update role set  type=? where id_role="+t.getId_role();
        try {
            PreparedStatement pst = conn.prepareStatement(requete);

            pst.setString(1,t.getType());
            pst.executeUpdate();
            System.out.println("role mise à jour");
        } catch (SQLException ex) {
            System.out.println("erreur lors de la mise à jour de le role" + ex.getMessage());
        }
    }

    @Override
    public List<role> readAll() {
        String requete ="select * from role";
        List<role> list=new ArrayList<>();
        try {
            Statement st=conn.createStatement();
            ResultSet rs= st.executeQuery(requete);
            while(rs.next()){
                role t=new role
                        ( rs.getInt("id_role"),rs.getString("type"));
                list.add(t);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public role readById(int id) {
        String requete ="select * from role where id_role="+id;
        role r=new role();

        try {
            Statement st=conn.createStatement();
            ResultSet rs= st.executeQuery(requete);
            while(rs.next()){
                r.setId_role(rs.getInt("id_role"));
                r.setType(rs.getString("type"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r;
    }

    @Override
    public role get_annonce(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
