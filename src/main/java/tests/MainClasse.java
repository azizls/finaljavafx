package tests;

import entities.Articles;
import services.ArticlesServices;

public class MainClasse {
    public static void main(String[] args) {

        Articles p = new Articles(1, "r", "Saber", 123, 265354, "https://www.futura-sciences.com/tech/dossiers/technologie-photo-numerique-capteur-image-773/",4);
        ArticlesServices ps = new ArticlesServices();
        ps.addEntity(p);

        System.out.println(ps.getAllData());

    }
}