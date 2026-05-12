package restaurant.model;

/**
 * Classe representant une table du restaurant
 * Implements l'interface Affichable pour afficher les details
 */
public class TableResto implements Affichable {
    // Numero de la table
    private int numero;
    // Nombre de places
    private int capacite;
    //Etat (Disponible ou Occupee)
    private String etat;

    /**
     * Constructeur avec parametres
     * @param numero le numero de la table
     * @param capacite le nombre de places
     * @param etat l'etat actuel
     */
    public TableResto(int numero, int capacite, String etat) {
        this.numero = numero;
        this.capacite = capacite;
        this.etat = etat;
    }

    /**
     * Retourne le numero de la table
     * @return le numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Definit le numero de la table
     * @param numero le nouveau numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Retourne la capacite
     * @return le nombre de places
     */
    public int getCapacite() {
        return capacite;
    }

    /**
     * Definit la capacite
     * @param capacite le nouveau nombre de places
     */
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    /**
     * Retourne l'etat de la table
     * @return l'etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * Definit l'etat de la table
     * @param etat le nouvel etat
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * Reserve la table (Change l'etat a "Occupee")
     */
    public void reserver() {
        etat = "Occupee";
    }

    /**
     * Libere la table (Change l'etat a "Disponible")
     */
    public void liberer() {
        etat = "Disponible";
    }

    /**
     * Affiche les details de la table
     */
    @Override
    public void afficherDetails() {
        System.out.println("Table [numero=" + numero + ", capacite=" + capacite + ", etat=" + etat + "]");
    }
}