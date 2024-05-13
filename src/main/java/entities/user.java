package entities;

import javafx.scene.control.TextField;

public class user {


    private int id_user ;
    private String nom ;
    private String prenom ;
    private String email ;



    private int is_enabled ;
    private String mdp ;
    private int numero ;
    private String adresse ;
    private int age ;
    private  String image  ;
    public role id_role ;
    private TextField emailTextField;

    public user(int id_user, String nom, String email) {
        this.id_user = id_user;
        this.nom = nom;
        this.email = email;
    }



    public user() {
    }
    public user(int id_user) {
        this.id_user = id_user;
    }




    public user(int id_user, String image) {

        this.id_user = id_user;
        this.image = image;
    }
    public user(String nom, String prenom, int age, String email, String mdp, int numero, String adresse, role id_role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age = age;
        this.id_role = id_role;

    }

    public user(String nom, String prenom,int age, String email, String mdp, int numero, String adresse,int id_user) {
        this.id_user = id_user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age = age;
    }





    public user(String nom, String prenom, int age,String email, String mdp, int numero, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age = age;


    }

    public user(String nom, String prenom, String email, int is_enabled, String mdp, int numero, String adresse, int age, String image, role id_role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.is_enabled = is_enabled;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age = age;
        this.image = image;
        this.id_role = id_role;

    }

    //    public user(int id_user,String nom, String prenom, int age,String email, String mdp, int numero, String adresse ) {
//        this.nom = nom;
//        this.prenom = prenom;
//        this.email = email;
//        this.mdp = mdp;
//        this.numero = numero;
//        this.adresse = adresse;
//        this.age = age;
//        this.id_user=id_user;
//    }
//my const :
    public user(int id_user, String nom, String prenom,int age,String email, String mdp, int numero, String adresse,String image,role id_role) {
        this.id_user = id_user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age     =   age;
        this.image  = image ;
        this.id_role = id_role;

    }



    public user(String nom, String prenom,int age, String email, String mdp, int numero, String adresse, String image, role id_role,int is_enabled) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.numero = numero;
        this.adresse = adresse;
        this.age = age;
        this.image = image;
        this.id_role = id_role;
        this.is_enabled=is_enabled;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public role getId_role() {
        return id_role;
    }

    public void setId_role(role id_role) {
        this.id_role = id_role;
    }

    @Override
    public String toString() {
        return "user{" + "id_user=" + id_user + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", mdp=" + mdp + ", numero=" + numero + ", adresse=" + adresse + ", age=" + age + ", image=" + image + ", id_role=" + id_role + '}';
    }

//    public void setNomImage(String name) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public void setEmailTextField(TextField emailTextField) {
        this.emailTextField = emailTextField;
    }

    public int getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(int is_enabled) {
        this.is_enabled = is_enabled;
    }
}
