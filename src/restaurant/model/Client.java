package restaurant.model;

/**
 * Classe representant un client du restaurant
 * Implements l'interface Affichable pour afficher les details
 */
public class Client implements Affichable {
    // Identifiant unique du client
    private int idClient;
    // Nom du client
    private String nom;
    // Numero de telephone
    private String telephone;
    // Adresse du client
    private String adresse;

    /**
     * Constructeur avec parametres
     * @param idClient l'ID du client
     * @param nom le nom
     * @param telephone le telephone
     * @param adresse l'adresse
     */
    public Client(int idClient, String nom, String telephone, String adresse) {
        this.idClient = idClient;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    /**
     * Constructeur par defaut
     */
    public Client() {
    }

    /**
     * Retourne l'ID du client
     * @return l'ID
     */
    public int getIdClient() {
        return idClient;
    }

    /**
     * Retourne le nom du client
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le telephone
     * @return le numero de telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Retourne l'adresse
     * @return l'adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Definit l'ID du client
     * @param idClient le nouvel ID
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * Definit le nom du client
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Definit le telephone
     * @param telephone le nouveau numero
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Definit l'adresse
     * @param adresse la nouvelle adresse
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Retourne une representation texte du client
     * @return une chaine decrivant le client
     */
    @Override
    public String toString() {
        return "Client [id=" + idClient + ", nom=" + nom + ", tel=" + telephone + ", adresse=" + adresse + "]";
    }

    /**
     * Affiche les details du client dans la console
     */
    @Override
    public void afficherDetails() {
        System.out.println(toString());
    }
}