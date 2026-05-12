package restaurant.controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import restaurant.dao.CommandeDAO;
import restaurant.model.Commande;

/**
 * Controleur pour la gestion des commandes
 * Fait le lien entre la vue et le DAO
 */
public class CommandeController {
    private final CommandeDAO commandeDAO;

    /**
     * Constructeur - Initialise le DAO
     */
    public CommandeController() {
        this.commandeDAO = new CommandeDAO();
    }

    /**
     * Cree une nouvelle commande
     * @param idClient l'ID du client
     * @param numeroTable le numero de la table
     * @param idEmploye l'ID de l'employe
     * @return l'ID de la commande creee
     */
    public int creerCommande(int idClient, int numeroTable, int idEmploye) {
        try {
            return commandeDAO.insert(idClient, numeroTable, idEmploye);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la creation de la commande.", e);
        }
    }

    /**
     * Ajoute un plat a une commande existante
     * @param idCommande l'ID de la commande
     * @param idPlat l'ID du plat
     * @param quantite la quantite
     */
    public void ajouterPlatACommande(int idCommande, int idPlat, int quantite) {
        try {
            commandeDAO.ajouterPlat(idCommande, idPlat, quantite);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'ajout du plat a la commande.", e);
        }
    }

    /**
     * Calcule le total d'une commande
     * @param idCommande l'ID de la commande
     * @return le total
     */
    public double calculerTotalCommande(int idCommande) {
        try {
            return commandeDAO.calculerTotal(idCommande);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du calcul du total de la commande.", e);
        }
    }

    /**
     * Recupere toutes les commandes
     * @return liste des commandes
     */
    public List<Commande> getToutesLesCommandes() {
        try {
            return commandeDAO.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des commandes.", e);
        }
    }

    /**
     * Recherche une commande par ID
     * @param idCommande l'ID de la commande
     * @return la commande trouvee ou null
     */
    public Commande getCommandeParId(int idCommande) {
        try {
            return commandeDAO.findById(idCommande);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche de la commande.", e);
        }
    }

    /**
     * Met a jour une commande
     * @param idCommande l'ID de la commande
     * @param idClient le nouveau client
     * @param numeroTable le nouveau numero de table
     * @param idEmploye le nouvel employe
     */
    public void modifierCommande(int idCommande, int idClient, int numeroTable, int idEmploye) {
        try {
            commandeDAO.update(idCommande, idClient, numeroTable, idEmploye);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la mise a jour de la commande.", e);
        }
    }

    /**
     * Supprime une commande
     * @param idCommande l'ID de la commande a supprimer
     */
    public void supprimerCommande(int idCommande) {
        try {
            commandeDAO.delete(idCommande);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la suppression de la commande.", e);
        }
    }

    /**
     * Charge les commandes dans un modele de tableau
     * @param model le modele a remplir
     */
    public void chargerCommandes(DefaultTableModel model) {
        try {
            commandeDAO.afficherCommandes(model);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'affichage des commandes.", e);
        }
    }
}