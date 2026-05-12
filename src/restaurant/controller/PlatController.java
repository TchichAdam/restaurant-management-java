package restaurant.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import restaurant.dao.PlatDAO;
import restaurant.model.Plat;

/**
 * Controleur pour la gestion des plats
 * Fait le lien entre la vue et le DAO
 */
public class PlatController {
    private final PlatDAO platDAO;

    /**
     * Constructeur - Initialise le DAO
     */
    public PlatController() {
        this.platDAO = new PlatDAO();
    }

    /**
     * Ajoute un nouveau plat
     * @param plat le plat a ajouter
     */
    public void ajouterPlat(Plat plat) {
        try {
            platDAO.insert(plat);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de l'ajout du plat.", e);
        }
    }

    /**
     * Recupere tous les plats
     * @return liste des plats
     */
    public List<Plat> getTousLesPlats() {
        try {
            return platDAO.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du chargement des plats.", e);
        }
    }

    /**
     * Filtre les plats par categorie
     * @param categorie la categorie a rechercher
     * @return liste des plats de cette categorie
     */
    public List<Plat> getPlatsParCategorie(String categorie) {
        try {
            return platDAO.findByCategorie(categorie);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors du filtrage des plats.", e);
        }
    }

    /**
     * Recherche un plat par ID
     * @param id l'ID du plat
     * @return le plat trouve ou null
     */
    public Plat getPlatParId(int id) {
        try {
            return platDAO.findById(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la recherche du plat.", e);
        }
    }

    /**
     * Met a jour un plat
     * @param plat le plat avec les nouvelles donnees
     */
    public void modifierPlat(Plat plat) {
        try {
            platDAO.update(plat);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la mise a jour du plat.", e);
        }
    }

    /**
     * Supprime (desactive) un plat
     * @param id l'ID du plat a supprimer
     */
    public void supprimerPlat(int id) {
        try {
            platDAO.delete(id);
        } catch (SQLException e) {
            throw new IllegalStateException("Erreur lors de la suppression du plat.", e);
        }
    }

    /**
     * Recherche des plats par nom
     * @param motCle le nom a rechercher
     * @return liste des plats correspondants
     */
    public List<Plat> rechercherParNom(String motCle) {
        List<Plat> resultat = new ArrayList<>();
        String filtre = motCle == null ? "" : motCle.trim().toLowerCase();
        for (Plat plat : getTousLesPlats()) {
            if (plat.getNom() != null && plat.getNom().toLowerCase().contains(filtre)) {
                resultat.add(plat);
            }
        }
        return resultat;
    }
}