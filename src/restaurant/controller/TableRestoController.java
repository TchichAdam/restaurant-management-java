package restaurant.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.dao.TableRestoDAO;
import restaurant.model.TableResto;

/**
 * Controleur pour la gestion des tables
 * Fait le lien entre la vue et le DAO
 */
public class TableRestoController {
    private final TableRestoDAO tableRestoDAO;

    /**
     * Constructeur - Initialise le DAO
     */
    public TableRestoController() {
        this.tableRestoDAO = new TableRestoDAO();
    }

    /**
     * Ajoute une nouvelle table
     * @param table la table a ajouter
     */
    public void ajouterTable(TableResto table) {
        try {
            tableRestoDAO.insert(table);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'ajout de la table.", e);
        }
    }

    /**
     * Calcule le prochain numero de table disponible
     * @return le prochain numero
     */
    public int getNextNumero() {
        try {
            return tableRestoDAO.getNextId();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du calcul du prochain numero.", e);
        }
    }

    /**
     * Recupere toutes les tables
     * @return liste des tables
     */
    public List<TableResto> getToutesLesTables() {
        try {
            return tableRestoDAO.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des tables.", e);
        }
    }

    /**
     * Recupere les tables disponibles
     * @return liste des tables libres
     */
    public List<TableResto> getTablesLibres() {
        try {
            return tableRestoDAO.findLibres();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des tables libres.", e);
        }
    }

    /**
     * Met a jour une table
     * @param table la table avec les nouvelles donnees
     */
    public void modifierTable(TableResto table) {
        try {
            tableRestoDAO.update(table);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la mise a jour de la table.", e);
        }
    }

    /**
     * Supprime une table
     * @param numeroTable le numero de la table a supprimer
     */
    public void supprimerTable(int numeroTable) {
        try {
            tableRestoDAO.delete(numeroTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la suppression de la table.", e);
        }
    }

    /**
     * Reserve une table
     * @param numeroTable le numero de la table a reserver
     */
    public void reserverTable(int numeroTable) {
        try {
            tableRestoDAO.reserver(numeroTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la reservation de la table.", e);
        }
    }

    /**
     * Libere une table
     * @param numeroTable le numero de la table a liberer
     */
    public void libererTable(int numeroTable) {
        try {
            tableRestoDAO.liberer(numeroTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la liberation de la table.", e);
        }
    }

    /**
     * Recherche une table par numero
     * @param numeroTable le numero de la table
     * @return la table trouvee ou null
     */
    public TableResto getTableParNumero(int numeroTable) {
        try {
            return tableRestoDAO.findByNumero(numeroTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche de la table.", e);
        }
    }

    /**
     * Recherche des tables par etat
     * @param etat l'etat a rechercher
     * @return liste des tables correspondantes
     */
    public List<TableResto> rechercherParEtat(String etat) {
        List<TableResto> resultat = new ArrayList<>();
        String filtre = etat == null ? "" : etat.trim().toLowerCase();
        for (TableResto table : getToutesLesTables()) {
            if (table.getEtat() != null && table.getEtat().toLowerCase().contains(filtre)) {
                resultat.add(table);
            }
        }
        return resultat;
    }
}