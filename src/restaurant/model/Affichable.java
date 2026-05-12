package restaurant.model;

/**
 * Interface pour les objets affichables
 * Toute classe qui implente cette interface doit definir afficherDetails()
 */
public interface Affichable {
    /**
     * Affiche les details de l'objet
     * Esta methoded sera appelee pour afficher les informations
     */
    void afficherDetails();
}