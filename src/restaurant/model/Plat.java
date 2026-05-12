package restaurant.model;

/**
 * Classe representant un plat du restaurant
 * Implements l'interface Affichable pour afficher les details
 */
public class Plat implements Affichable {
    // Identifiant unique du plat
    private int idPlat;
    // Nom du plat
    private String nom;
    // Prix du plat
    private double prix;
    // Categorie du plat (entree, plat, dessert, etc.)
    private String categorie;

    /**
     * Constructeur avec parametres
     * @param idPlat l'ID du plat
     * @param nom le nom
     * @param prix le prix
     * @param categorie la categorie
     */
    public Plat(int idPlat, String nom, double prix, String categorie) {
        this.idPlat = idPlat;
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
    }

    /**
     * Constructeur par defaut
     */
    public Plat() {
    }

    /**
     * Retourne l'ID du plat
     * @return l'ID
     */
    public int getIdPlat() {
        return idPlat;
    }

    /**
     * Definit l'ID du plat
     * @param idPlat le nouvel ID
     */
    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    /**
     * Retourne le nom du plat
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Definit le nom du plat
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prix du plat
     * @return le prix
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Definit le prix du plat
     * @param prix le nouveau prix
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * Retourne la categorie du plat
     * @return la categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Definit la categorie du plat
     * @param categorie la nouvelle categorie
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Retourne une representation texte du plat
     * @return une chaine decrivant le plat
     */
    @Override
    public String toString() {
        return "Plat [idPlat=" + idPlat + ", nom=" + nom + ", prix=" + prix + ", categorie=" + categorie + "]";
    }

    /**
     * Affiche les informations du plat
     * Affiche le nom, la categorie et le prix
     */
    public void afficherPlat() {
        System.out.println(nom + " (" + categorie + ") : " + prix + " DH");
    }

    /**
     * Affiche les details du plat
     */
    @Override
    public void afficherDetails() {
        afficherPlat();
    }
}