package entities;

public class reactions {
    private int id;
    private int id_article;
    private int id_user;
    private String type_react;

    public reactions() {
    }

    public reactions(int id, int id_article, int id_user, String type_react) {
        this.id = id;
        this.id_article = id_article;
        this.id_user = id_user;
        this.type_react = type_react;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_article() {
        return id_article;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getType_react() {
        return type_react;
    }

    public void setType_react(String type_react) {
        this.type_react = type_react;
    }
}
