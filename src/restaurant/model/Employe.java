package restaurant.model;

/**
 * Classe abstraite representant un employe du restaurant
 * Serve de base pour Serveur, Cuisinier et Caissier
 * Implements l'interface Affichable
 */
public abstract class Employe implements Affichable {
    // Identifiant unique de l'employe
    protected int id;
    // Nom de l'employe
    protected String nom;

    /**
     * Constructeur avec parametres
     * @param id l'ID de l'employe
     * @param nom le nom
     */
    public Employe(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    /**
     * Constructeur par defaut
     */
    public Employe() {
    }

    /**
     * Retourne l'ID de l'employe
     * @return l'ID
     */
    public int getId() {
        return id;
    }

    /**
     * Definit l'ID de l'employe
     * @param id le nouvel ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de l'employe
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Methode abstraite - chaque employe definit son travail
     */
    public abstract void travailler();
}