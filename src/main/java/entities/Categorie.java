package entities;

public class Categorie {

    private int id_categorie;
    private boolean espaceCouvert;
    private boolean espaceEnfants;
    private boolean espaceFumeur;
    private boolean serviceRestauration;

    public Categorie() {
    }

    public Categorie(boolean espaceCouvert, boolean espaceEnfants, boolean espaceFumeur, boolean serviceRestauration) {
        this.espaceCouvert = espaceCouvert;
        this.espaceEnfants = espaceEnfants;
        this.espaceFumeur = espaceFumeur;
        this.serviceRestauration = serviceRestauration;
    }

    public Categorie(int id_categorie, boolean espaceCouvert, boolean espaceEnfants, boolean espaceFumeur, boolean serviceRestauration) {
        this.id_categorie = id_categorie;
        this.espaceCouvert = espaceCouvert;
        this.espaceEnfants = espaceEnfants;
        this.espaceFumeur = espaceFumeur;
        this.serviceRestauration = serviceRestauration;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public boolean isEspaceCouvert() {
        return espaceCouvert;
    }

    public void setEspaceCouvert(boolean espaceCouvert) {
        this.espaceCouvert = espaceCouvert;
    }

    public boolean isEspaceEnfants() {
        return espaceEnfants;
    }

    public void setEspaceEnfants(boolean espaceEnfants) {
        this.espaceEnfants = espaceEnfants;
    }

    public boolean isEspaceFumeur() {
        return espaceFumeur;
    }

    public void setEspaceFumeur(boolean espaceFumeur) {
        this.espaceFumeur = espaceFumeur;
    }

    public boolean isServiceRestauration() {
        return serviceRestauration;
    }

    public void setServiceRestauration(boolean serviceRestauration) {
        this.serviceRestauration = serviceRestauration;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id_categorie=" + id_categorie +
                ", espaceCouvert=" + espaceCouvert +
                ", espaceEnfants=" + espaceEnfants +
                ", espaceFumeur=" + espaceFumeur +
                ", serviceRestauration=" + serviceRestauration +
                '}';
    }
}
