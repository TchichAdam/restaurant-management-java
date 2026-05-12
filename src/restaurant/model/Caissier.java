package restaurant.model;

/**
 * Classe representant un caissier du restaurant
 * Herite de la classe Employe
 */
public class Caissier extends Employe {
    /**
     * Constructeur avec parametres
     * @param id l'ID du caissier
     * @param nom le nom du caissier
     */
    public Caissier(int id, String nom) {
        super(id, nom);
    }

    /**
     * Constructeur par defaut
     */
    public Caissier() {
    }

    /**
     * Definir le travail du caissier
     * Le caissier gere les paiements
     */
    @Override
    public void travailler() {
        System.out.println("Le caissier gere les paiements");
    }

    /**
     * Affiche les details du caissier
     */
    @Override
    public void afficherDetails() {
        System.out.println("Caissier [id=" + id + ", nom=" + nom + "]");
    }

    /**
     * Methode pour encaisser un montant
     * @param montant le montant a encaisser
     */
    public void encaisser(double montant) {
        System.out.println("Paiement de " + montant + " DH effectue");
    }

    /**
     * Methode pour afficher la facture
     */
    public void afficherFacture() {
        System.out.println("Facture imprimee");
    }

    /**
     * Retourne une representation texte du caissier
     * @return une chaine decrivant le caissier
     */
    @Override
    public String toString() {
        return "Caissier [id=" + id + ", nom=" + nom + "]";
    }
}