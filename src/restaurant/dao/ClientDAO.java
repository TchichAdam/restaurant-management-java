package restaurant.dao;

import restaurant.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    /**
     * Recuper le prochain ID disponible pour un nouveau client
     * @return le prochain ID (MAX + 1) ou 1 si la table est vide
     */
    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id_client) FROM Client";
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
     * Ajoute un nouveau client dans la base de donnees
     * @param c le client a ajouter
     */
    public void insert(Client c) throws SQLException {
        // Generer automatiquement l'ID si non fourni
        if (c.getIdClient() == 0) {
            c.setIdClient(getNextId());
        }
        try (Connection con = DatabaseConnection.getConnection()) {
            // Activer l'insertion manuelle de l'ID
            String enableId = "SET IDENTITY_INSERT Client ON";
            try (Statement st = con.createStatement()) {
                st.execute(enableId);
            }
            // Preparer et executes la requete d'insertion
            String sql = "INSERT INTO Client (id_client, nom, telephone, adresse) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, c.getIdClient());
                ps.setString(2, c.getNom());
                ps.setString(3, c.getTelephone());
                ps.setString(4, c.getAdresse());
                ps.executeUpdate();
                System.out.println("Client ajoute : " + c.getNom());
            }
            // Desactiver l'insertion manuelle de l'ID
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Client OFF");
            }
        }
    }

    /**
     * Recupere tous les clients de la base de donnees
     * @return liste de tous les clients
     */
    public List<Client> findAll() throws SQLException {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Creer un objet Client pour chaque ligne
                Client c = new Client();
                c.setIdClient(rs.getInt("id_client"));
                c.setNom(rs.getString("nom"));
                c.setTelephone(rs.getString("telephone"));
                c.setAdresse(rs.getString("adresse"));
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Recherche un client par son ID
     * @param id l'ID du client a rechercher
     * @return le client trouve ou null
     */
    public Client findById(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id_client = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setIdClient(rs.getInt("id_client"));
                c.setNom(rs.getString("nom"));
                c.setTelephone(rs.getString("telephone"));
                c.setAdresse(rs.getString("adresse"));
                return c;
            }
        }
        return null;
    }

    /**
     * Met a jour les informations d'un client
     * @param c le client avec les nouvelles informations
     */
    public void update(Client c) throws SQLException {
        String sql = "UPDATE Client SET nom=?, telephone=?, adresse=? WHERE id_client=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getTelephone());
            ps.setString(3, c.getAdresse());
            ps.setInt(4, c.getIdClient());
            ps.executeUpdate();
            System.out.println("Client mis a jour : " + c.getNom());
        }
    }

    /**
     * Supprime un client de la base de donnees
     * @param id l'ID du client a supprimer
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id_client=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Aucun client trouve avec l'id " + id + ".");
            }
            System.out.println("Client supprime (id=" + id + ")");
        }
    }

    /**
     * Recherche des clients par nom (recherche partielle)
     * @param nom le nom a rechercher
     * @return liste des clients correspondants
     */
    public List<Client> searchByName(String nom) throws SQLException {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM Client WHERE LOWER(nom) LIKE LOWER(?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client c = new Client();
                    c.setIdClient(rs.getInt("id_client"));
                    c.setNom(rs.getString("nom"));
                    c.setTelephone(rs.getString("telephone"));
                    c.setAdresse(rs.getString("adresse"));
                    list.add(c);
                }
            }
        }
        return list;
    }
}