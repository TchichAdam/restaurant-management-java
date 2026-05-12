package restaurant.dao;

import restaurant.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    /**
     * Recuper le prochain ID disponible pour un nouvel employe
     * @return le prochain ID (MAX + 1) ou 1 si la table est vide
     */
    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id_employe) FROM Employe";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int max = rs.getInt(1);
                return (rs.wasNull()) ? 1 : max + 1;
            }
        }
        return 1;
    }

    /**
     * Ajoute un nouvel employe dans la base de donnees
     * @param e l'employe a ajouter
     * @param role le role de l'employe (Serveur, Cuisinier, Caissier)
     */
    public void insert(Employe e, String role) throws SQLException {
        // Generer automatiquement l'ID si non fourni
        if (e.getId() == 0) {
            e.setId(getNextId());
        }
        try (Connection con = DatabaseConnection.getConnection()) {
            // Activer l'insertion manuelle de l'ID
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Employe ON");
            }
            // Preparer et executer la requete d'insertion
            String sql = "INSERT INTO Employe (id_employe, nom, role, salaire) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, e.getId());
                ps.setString(2, e.getNom());
                ps.setString(3, role);
                ps.setDouble(4, 0);
                ps.executeUpdate();
                System.out.println("Employe ajoute : " + e.getNom());
            }
            // Desactiver l'insertion manuelle de l'ID
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Employe OFF");
            }
        }
    }

    /**
     * Recupere tous les employes de la base de donnees
     * @return liste de tous les employes
     */
    public List<Employe> findAll() throws SQLException {
        List<Employe> list = new ArrayList<>();
        String sql = "SELECT * FROM Employe";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Creer un objet Employe pour chaque ligne
                list.add(buildEmploye(rs));
            }
        }
        return list;
    }

    /**
     * Recherche des employes par role
     * @param role le role a rechercher
     * @return liste des employes de ce role
     */
    public List<Employe> findByRole(String role) throws SQLException {
        List<Employe> list = new ArrayList<>();
        String sql = "SELECT * FROM Employe WHERE role = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildEmploye(rs));
            }
        }
        return list;
    }

    /**
     * Recherche un employe par son ID
     * @param id l'ID de l'employe a rechercher
     * @return l'employe trouve ou null
     */
    public Employe findById(int id) throws SQLException {
        String sql = "SELECT * FROM Employe WHERE id_employe = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildEmploye(rs);
            }
        }
        return null;
    }

    /**
     * Met a jour les informations d'un employe
     * @param e l'employe avec les nouvelles informations
     * @param role le nouveau role
     */
    public void update(Employe e, String role) throws SQLException {
        String sql = "UPDATE Employe SET nom = ?, role = ? WHERE id_employe = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, role);
            ps.setInt(3, e.getId());
            ps.executeUpdate();
            System.out.println("Employe mis a jour : " + e.getNom());
        }
    }

    /**
     * Supprime un employe de la base de donnees
     * @param id l'ID de l'employe a supprimer
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Employe WHERE id_employe = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Aucun employe trouve avec l'id " + id + ".");
            }
            System.out.println("Employe supprime (id=" + id + ")");
        }
    }

    /**
     * Construit un objet Employe a partir d'un ResultSet
     * @param rs le ResultSet contenant les donnees
     * @return l'objet Employe cree
     */
    private Employe buildEmploye(ResultSet rs) throws SQLException {
        // Lire le role et creer l'objet approprie
        String role = rs.getString("role");
        int id = rs.getInt("id_employe");
        String nom = rs.getString("nom");
        switch (role) {
            case "Serveur":
                return new Serveur(id, nom);
            case "Cuisinier":
                return new Cuisinier(id, nom);
            case "Caissier":
                return new Caissier(id, nom);
            default:
                return new Serveur(id, nom);
        }
    }
}