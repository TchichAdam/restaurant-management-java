package restaurant.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.dao.EmployeDAO;
import restaurant.model.Employe;

/**
 * Controleur pour la gestion des employes
 * Fait le lien entre la vue et le DAO
 */
public class EmployeController {
    private final EmployeDAO employeDAO;

    /**
     * Constructeur - Initialise le DAO
     */
    public EmployeController() {
        this.employeDAO = new EmployeDAO();
    }

    /**
     * Ajoute un nouvel employe
     * @param employe l'employe a ajouter
     * @param role le role de l'employe
     */
    public void ajouterEmploye(Employe employe, String role) {
        try {
            employeDAO.insert(employe, role);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'ajout de l'employe.", e);
        }
    }

    /**
     * Recupere tous les employes
     * @return liste des employes
     */
    public List<Employe> getTousLesEmployes() {
        try {
            return employeDAO.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des employes.", e);
        }
    }

    /**
     * Filtre les employes par role
     * @param role le role a rechercher
     * @return liste des employes de ce role
     */
    public List<Employe> getEmployesParRole(String role) {
        try {
            return employeDAO.findByRole(role);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du filtrage des employes.", e);
        }
    }

    /**
     * Recherche un employe par ID
     * @param id l'ID de l'employe
     * @return l'employe trouve ou null
     */
    public Employe getEmployeParId(int id) {
        try {
            return employeDAO.findById(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche de l'employe.", e);
        }
    }

    /**
     * Met a jour un employe
     * @param employe l'employe avec les nouvelles donnees
     * @param role le nouveau role
     */
    public void modifierEmploye(Employe employe, String role) {
        try {
            employeDAO.update(employe, role);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la mise a jour de l'employe.", e);
        }
    }

    /**
     * Supprime un employe
     * @param id l'ID de l'employe a supprimer
     */
    public void supprimerEmploye(int id) {
        try {
            employeDAO.delete(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la suppression de l'employe.", e);
        }
    }

    /**
     * Recherche des employes par nom
     * @param motCle le nom a rechercher
     * @return liste des employes correspondants
     */
    public List<Employe> rechercherParNom(String motCle) {
        List<Employe> resultat = new ArrayList<>();
        String filtre = motCle == null ? "" : motCle.trim().toLowerCase();
        for (Employe employe : getTousLesEmployes()) {
            if (employe.getNom() != null && employe.getNom().toLowerCase().contains(filtre)) {
                resultat.add(employe);
            }
        }
        return resultat;
    }
}