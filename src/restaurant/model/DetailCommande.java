package restaurant.model;

/**
 * Classe representant un detail de commande (un plat commande)
 * Contient les informations sur un plat specifique dans une commande
 * Implements l'interface Affichable
 */
public class DetailCommande implements Affichable {
    // Identifiant unique du detail
    private int idDetail;
    // ID de la commande parente
    private int idCommande;
    // ID du plat commande
    private int idPlat;
    // Quantite commandee
    private int quantite;
    // Prix unitaire au moment de la commande
    private double prixUnitaire;

    /**
     * Constructeur avec parametres
     * @param idDetail l'ID du detail
     * @param idCommande l'ID de la commande
     * @param idPlat l'ID du plat
     * @param quantite la quantite
     * @param prixUnitaire le prix unitaire
     */
    public DetailCommande(int idDetail, int idCommande, int idPlat, int quantite, double prixUnitaire) {
        this.idDetail = idDetail;
        this.idCommande = idCommande;
        this.idPlat = idPlat;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Retourne l'ID du detail
     * @return l'ID
     */
    public int getIdDetail() {
        return idDetail;
    }

    /**
     * Definit l'ID du detail
     * @param idDetail le nouvel ID
     */
    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    /**
     * Retourne l'ID de la commande
     * @return l'ID de la commande
     */
    public int getIdCommande() {
        return idCommande;
    }

    /**
     * Definit l'ID de la commande
     * @param idCommande le nouvel ID
     */
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    /**
     * Retourne l'ID du plat
     * @return l'ID du plat
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
     * Retourne la quantite
     * @return la quantite
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Definit la quantite
     * @param quantite la nouvelle quantite
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * Retourne le prix unitaire
     * @return le prix
     */
    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    /**
     * Definit le prix unitaire
     * @param prixUnitaire le nouveau prix
     */
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Calcule le sous-total (quantite * prix unitaire)
     * @return le sous-total
     */
    public double calculerSousTotal() {
        return quantite * prixUnitaire;
    }

    /**
     * Retourne une representation texte du detail
     * @return une chaine decrivant le detail
     */
    @Override
    public String toString() {
        return "DetailCommande [idDetail=" + idDetail + ", idCommande=" + idCommande + ", idPlat=" + idPlat + ", quantite=" + quantite + ", prixUnitaire=" + prixUnitaire + "]";
    }

    /**
     * Affiche les details du detail de commande
     */
    @Override
    public void afficherDetails() {
        System.out.println(toString());
    }
}