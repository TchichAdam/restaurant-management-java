package restaurant.model;

/**
 * Classe representant un serveur du restaurant
 * Herite de la classe Employe
 */
public class Serveur extends Employe {
    /**
     * Constructeur avec parametres
     * @param id l'ID du serveur
     * @param nom le nom du serveur
     */
    public Serveur(int id, String nom) {
        super(id, nom);
    }

    /**
     * Definir le travail du serveur
     * Le serveur prend les commandes des clients
     */
    @Override
    public void travailler() {
        System.out.println("Le serveur prend les commandes");
    }

    /**
     * Affiche les details du serveur
     */
    @Override
    public void afficherDetails() {
        System.out.println("Serveur [id=" + id + ", nom=" + nom + "]");
    }

    /**
     * Methode pour prendre une commande
     */
    public void prendreCommande() {
        System.out.println("Commande prise");
    }
}