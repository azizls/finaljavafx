package entities;

import java.sql.Date;

public class Reclamation {
    private int id_reclamation;
    private int id_user;
    private String type;
    private String contenu;
    private Date date_env;
    private String status;



    // Constructor

    public Reclamation(){

    }
    public Reclamation(int id_reclamation, int id_user, String type, String contenu) {
        this.id_reclamation = id_reclamation;
        this.id_user = id_user;
        this.type = type;
        this.contenu = contenu;
    }

    // Getter methods
    public int getId_reclamation() {
        return id_reclamation;
    }

    public int getId_user() {
        return id_user;
    }

    public String getType() {
        return type;
    }

    public String getContenu() {
        return contenu;
    }

    // Setter methods
    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate_env() {
        return date_env;
    }

    public void setDate_env(Date date_env) {
        this.date_env = date_env;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "Reclamation{" +
                "id_reclamation=" + id_reclamation +
                ", id_user=" + id_user +
                ", type='" + type + '\'' +
                ", contenu='" + contenu + '\'' +
                '}';
    }
}

