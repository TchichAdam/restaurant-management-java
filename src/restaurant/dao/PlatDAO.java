package restaurant.dao;

import restaurant.model.Plat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO {

    /**
     * Recuper le prochain ID disponible pour un nouveau plat
     * @return le prochain ID (MAX + 1) ou 1 si la table est vide
     */
    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id_plat) FROM Plat";
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
     * Ajoute un nouveau plat dans la base de donnees
     * @param p le plat a ajouter
     */
    public void insert(Plat p) throws SQLException {
        // Generer automatiquement l'ID si non fourni
        if (p.getIdPlat() == 0) {
            p.setIdPlat(getNextId());
        }
        try (Connection con = DatabaseConnection.getConnection()) {
            // Activer l'insertion manuelle de l'ID
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Plat ON");
            }
            // Preparer et executer la requete d'insertion
            String sql = "INSERT INTO Plat (id_plat, nom_plat, categorie, prix, disponible) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, p.getIdPlat());
                ps.setString(2, p.getNom());
                ps.setString(3, p.getCategorie());
                ps.setDouble(4, p.getPrix());
                ps.setBoolean(5, true);
                ps.executeUpdate();
                System.out.println("Plat ajoute : " + p.getNom());
            }
            // Desactiver l'insertion manuelle de l'ID
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Plat OFF");
            }
        }
    }

    /**
     * Recupere tous les plats disponibles
     * @return liste de tous les plats
     */
    public List<Plat> findAll() throws SQLException {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT * FROM Plat WHERE disponible = 1";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Creer un objet Plat pour chaque ligne
                Plat p = new Plat();
                p.setIdPlat(rs.getInt("id_plat"));
                p.setNom(rs.getString("nom_plat"));
                p.setCategorie(rs.getString("categorie"));
                p.setPrix(rs.getDouble("prix"));
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Recherche des plats par categorie
     * @param categorie la categorie a rechercher
     * @return liste des plats de cette categorie
     */
    public List<Plat> findByCategorie(String categorie) throws SQLException {
        List<Plat> list = new ArrayList<>();
        String sql = "SELECT * FROM Plat WHERE categorie = ? AND disponible = 1";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categorie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Plat p = new Plat();
                p.setIdPlat(rs.getInt("id_plat"));
                p.setNom(rs.getString("nom_plat"));
                p.setCategorie(rs.getString("categorie"));
                p.setPrix(rs.getDouble("prix"));
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Recherche un plat par son ID
     * @param id l'ID du plat a rechercher
     * @return le plat trouve ou null
     */
    public Plat findById(int id) throws SQLException {
        String sql = "SELECT * FROM Plat WHERE id_plat = ? AND disponible = 1";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Plat p = new Plat();
                p.setIdPlat(rs.getInt("id_plat"));
                p.setNom(rs.getString("nom_plat"));
                p.setCategorie(rs.getString("categorie"));
                p.setPrix(rs.getDouble("prix"));
                return p;
            }
        }
        return null;
    }

    /**
     * Met a jour les informations d'un plat
     * @param p le plat avec les nouvelles informations
     */
    public void update(Plat p) throws SQLException {
        String sql = "UPDATE Plat SET nom_plat = ?, categorie = ?, prix = ? WHERE id_plat = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getCategorie());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getIdPlat());
            ps.executeUpdate();
            System.out.println("Plat mis a jour : " + p.getNom());
        }
    }

    /**
     * Desactive un plat (suppression logique)
     * @param id l'ID du plat a desactiver
     */
    public void delete(int id) throws SQLException {
        String sql = "UPDATE Plat SET disponible = 0 WHERE id_plat = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Aucun plat trouve avec l'id " + id + ".");
            }
            System.out.println("Plat desactive (id=" + id + ")");
        }
    }
}