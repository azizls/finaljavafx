package entities;

import java.util.List;

public class Panier {
    private int id_panier;
    private int id_usr;
    private int nbr_articles;
    private int id_article;
    private List<Articles> articles;

    public Panier() {
    }

    public Panier(int id_panier, int id_usr, int nbr_articles, int id_article) {
        this.id_panier = id_panier;
        this.id_usr = id_usr;
        this.nbr_articles = nbr_articles;
        this.id_article = id_article;
    }

    public Panier(int id_usr, int nbr_articles, int id_article) {

        this.id_usr = id_usr;
        this.nbr_articles = nbr_articles;
        this.id_article = id_article;
    }

    public Panier(int id_article) {
        this.id_article = id_article;
    }


    public int getId_panier() {
        return id_panier;
    }

    public void setId_panier(int id_panier) {
        this.id_panier = id_panier;
    }

    public int getId_usr() {
        return id_usr;
    }

    public void setId_usr(int id_usr) {
        this.id_usr = id_usr;
    }

    public int getNbr_articles() {
        return nbr_articles;
    }

    public void setNbr_articles(int nbr_articles) {
        this.nbr_articles = nbr_articles;
    }

    public int getId_article() {
        return id_article;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }

    public Panier(int nbr_articles, int id_article) {
        this.nbr_articles = nbr_articles;
        this.id_article = id_article;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id_panier=" + id_panier +
                ", id_usr=" + id_usr +
                ", nbr_articles=" + nbr_articles +
                ", id_article=" + id_article +
                ", articles=" + articles +
                '}';
    }
}
