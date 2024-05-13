package entities;

public class Articles {
    private int id;
    private String nom;
    private String description;
    private int prix;
    private int contact;
    private int quantite;
    private String Image;


    

    public Articles() {
    }

    public Articles(int id, String nom, String description, int prix, int contact, String Image,int quantite) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.contact = contact;
        this.Image = Image;
        this.quantite = quantite;

    }
    public Articles( String nom, String description, int prix, int contact, String Image,int quantite) {
    
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.contact = contact;
        this.Image = Image;
        this.quantite = quantite;

    }


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getPrix() {
        return prix;
    }

    public int getContact() {
        return contact;
    }

    public String getImage() {
        return Image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", contact=" + contact +
                ", quantite=" + quantite +
                ", Image='" + Image + '\'' +
                '}';
    }
}
