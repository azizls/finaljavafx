package entities;

import java.util.Objects;

public class Personne {
    private int id ;
    private String Nom;
    private String Prenom;
    private String Email;
    private String Password;
    private String Datedenaissance;


    public Personne(String rebai, String saber, String mail, String s) {
    }

    public Personne(int id, String Nom, String Prenom,String Email,String Password,String Datedenaissance) {
        this.id = id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email = Email;
        this.Password= Password;
        this.Datedenaissance= Datedenaissance;
    }

    public Personne( String Nom, String Prenom,String Email,String Password,String Datedenaissance) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email=Email;
        this.Password=Password;
        this.Datedenaissance=Datedenaissance;
    }
    public Personne() {}



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDatedenaissance() {
        return Datedenaissance;
    }

    public void setDatedenaissance(String datedenaissance) {
        Datedenaissance = datedenaissance;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", Nom='" + Nom + '\'' +
                ", prenom='" + Prenom + '\'' +
                ", Email='" + Email + '\'' +
                ", Datedenaissance='" + Datedenaissance + '\'' +


                '}';
    }
    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personne other = (Personne) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.Nom, other.Nom)) {
            return false;
        }

        if (!Objects.equals(this.Prenom, other.Prenom)) {
            return false;
        }

        if (!Objects.equals(this.Email, other.Email)) {
            return false;
        }
        return Objects.equals(this.Password, other.Password);
    }
    // Méthode pour afficher les informations de la personne
    public void afficherInformations() {
        System.out.println("Nom : " + Nom);
        System.out.println("Prénom : " + Prenom);
        System.out.println("Email : " + Email);
        System.out.println("Date de naissance : " + Datedenaissance);
    }


    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }
}
