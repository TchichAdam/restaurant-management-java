package restaurant.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe representant une commande du restaurant
 * Contient les plats commandes par un client
 * Implements l'interface Affichable
 */
public class Commande implements Affichable {
    // Identifiant unique de la commande
    private int idCommande;
    // Date de la commande
    private Date date;
    // Liste des plats commandes
    private ArrayList<Plat> plats;

    /**
     * Constructeur avec ID
     * Initialise la date courante et la liste des plats
     * @param idCommande l'ID de la commande
     */
    public Commande(int idCommande) {
        this.idCommande = idCommande;
        this.date = new Date();
        this.plats = new ArrayList<>();
    }

    /**
     * Constructeur par defaut
     */
    public Commande() {
    }

    /**
     * Retourne l'ID de la commande
     * @return l'ID
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
     * Retourne la date de la commande
     * @return la date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Definit la date de la commande
     * @param date la nouvelle date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retourne la liste des plats
     * @return la liste des plats
     */
    public ArrayList<Plat> getPlats() {
        return plats;
    }

    /**
     * Definit la liste des plats
     * @param plats la nouvelle liste
     */
    public void setPlats(ArrayList<Plat> plats) {
        this.plats = plats;
    }

    /**
     * Ajoute un plat a la commande
     * @param p le plat a ajouter
     */
    public void ajouterPlat(Plat p) {
        plats.add(p);
    }

    /**
     * Supprime un plat de la commande
     * @param p le plat a supprimer
     */
    public void supprimerPlat(Plat p) {
        plats.remove(p);
    }

    /**
     * Calcule le total de la commande
     * Fait la somme des prix de tous les plats
     * @return le total
     */
    public double calculerTotal() {
        double total = 0;
        for (Plat p : plats) {
            total += p.getPrix();
        }
        return total;
    }

    /**
     * Affiche les details de la commande
     * Affiche l'ID, les plats et le total
     */
    public void afficherCommande() {
        System.out.println("Commande #" + idCommande);
        for (Plat p : plats) {
            p.afficherPlat();
        }
        System.out.println("Total = " + calculerTotal());
    }

    /**
     * Affiche les details de la commande
     */
    @Override
    public void afficherDetails() {
        afficherCommande();
    }
}