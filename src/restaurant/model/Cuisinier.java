package restaurant.model;

/**
 * Classe representant un cuisinier du restaurant
 * Herite de la classe Employe
 */
public class Cuisinier extends Employe {

    /**
     * Constructeur avec parametres
     * @param id l'ID du cuisinier
     * @param nom le nom du cuisinier
     */
    public Cuisinier(int id, String nom) {
        super(id, nom);
    }

    /**
     * Definir le travail du cuisinier
     * Le cuisinier prepare les plats commandés
     */
    @Override
    public void travailler() {
        System.out.println("Le cuisinier prepare les plats");
    }

    /**
     * Affiche les details du cuisinier
     */
    @Override
    public void afficherDetails() {
        System.out.println("Cuisinier [id=" + id + ", nom=" + nom + "]");
    }

    /**
     * Methode pour preparer un plat
     */
    public void preparerPlat() {
        System.out.println("Plat prepare");
    }
}