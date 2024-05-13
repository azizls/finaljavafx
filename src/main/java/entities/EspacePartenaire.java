package entities;
import java.util.List;
public class EspacePartenaire {
    private int id_espace;
    private int nbClick;
    private int id_user;
    private String nom;
    private String localisation;
    private String type;
    private String description;
    private List<String> photos;
    private int id_categorie;
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public EspacePartenaire() {

    }

    public EspacePartenaire(int id_espace) {
        this.id_espace = id_espace;
    }

    public EspacePartenaire(String nom, String localisation, String type, String description, List<String> photos, int id_categorie) {
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.id_categorie = id_categorie;
    }

    public EspacePartenaire(int id_espace, String nom, String localisation, String type, String description, List<String> photos) {
        this.id_espace = id_espace;
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
    }



    public EspacePartenaire(String nom, String localisation, String type, String description, List<String> photos) {
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
    }

    public EspacePartenaire(int id_espace, String nom, String localisation, String type, String description, List<String> photos, int id_categorie) {
        this.id_espace = id_espace;
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.id_categorie = id_categorie;
    }

    public int getId_espace() {
        return id_espace;
    }

    public void setId_espace(int id_espace) {
        this.id_espace = id_espace;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public int getNbClick() {
        return nbClick;
    }

    public void setNbClick(int nbClick) {
        this.nbClick = nbClick;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public EspacePartenaire(int id_espace, String nom, String localisation, String type, String description, List<String> photos, int id_categorie, boolean accepted) {
        this.id_espace = id_espace;
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.id_categorie = id_categorie;
        this.accepted = accepted;
    }

    public EspacePartenaire(String nom, String localisation, String type, String description, List<String> photos, int id_categorie, boolean accepted) {
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.id_categorie = id_categorie;
        this.accepted = accepted;
    }

    public EspacePartenaire(int id_espace, int nbClick, int id_user, String nom, String localisation, String type, String description, List<String> photos, int id_categorie, boolean accepted) {
        this.id_espace = id_espace;
        this.nbClick = nbClick;
        this.id_user = id_user;
        this.nom = nom;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.photos = photos;
        this.id_categorie = id_categorie;
        this.accepted = accepted;
    }


    @Override
    public String toString() {
        return "EspacePartenaire{" +
                "id_espace=" + id_espace +
                ", nbClick=" + nbClick +
                ", id_user=" + id_user +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", id_categorie=" + id_categorie +
                ", accepted=" + accepted +
                '}';
    }
}
