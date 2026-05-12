package restaurant.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.dao.ClientDAO;
import restaurant.model.Client;

/**
 * Controleur pour la gestion des clients
 * Fait le lien entre la vue et le DAO
 */
public class ClientController {
    private final ClientDAO clientDAO;

    /**
     * Constructeur - Initialise le DAO
     */
    public ClientController() {
        this.clientDAO = new ClientDAO();
    }

    /**
     * Ajoute un nouveau client
     * @param client le client a ajouter
     */
    public void ajouterClient(Client client) {
        try {
            clientDAO.insert(client);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'ajout du client.", e);
        }
    }

    /**
     * Recupere tous les clients
     * @return liste des clients
     */
    public List<Client> getTousLesClients() {
        try {
            return clientDAO.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des clients.", e);
        }
    }

    /**
     * Recherche un client par ID
     * @param id l'ID du client
     * @return le client trouve ou null
     */
    public Client getClientParId(int id) {
        try {
            return clientDAO.findById(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche du client.", e);
        }
    }

    /**
     * Met a jour un client
     * @param client le client avec les nouvelles donnees
     */
    public void modifierClient(Client client) {
        try {
            clientDAO.update(client);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la mise a jour du client.", e);
        }
    }

    /**
     * Supprime un client
     * @param id l'ID du client a supprimer
     */
    public void supprimerClient(int id) {
        try {
            clientDAO.delete(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la suppression du client.", e);
        }
    }

    /**
     * Recherche des clients par nom
     * @param motCle le nom a rechercher
     * @return liste des clients correspondants
     */
    public List<Client> rechercherParNom(String motCle) {
        try {
            String filtre = (motCle == null) ? "" : motCle.trim();
            return clientDAO.searchByName(filtre);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche du client.", e);
        }
    }
}