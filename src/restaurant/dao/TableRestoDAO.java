package restaurant.dao;

import restaurant.model.TableResto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableRestoDAO {

    /**
     * Recuper le prochain numero disponible pour une nouvelle table
     * @return le prochain numero (MAX + 1) ou 1 si la table est vide
     */
    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(numero_table) FROM TableRestaurant";
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
     * Ajoute une nouvelle table dans la base de donnees
     * @param t la table a ajouter
     */
    public void insert(TableResto t) throws SQLException {
        // Generer automatiquement le numero si non fourni
        if (t.getNumero() == 0) {
            t.setNumero(getNextId());
        }
        String sql = "INSERT INTO TableRestaurant (numero_table, capacite, etat) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getNumero());
            ps.setInt(2, t.getCapacite());
            ps.setString(3, normalizeEtat(t.getEtat()));
            ps.executeUpdate();
            System.out.println("Table ajoutee : n°" + t.getNumero());
        }
    }

    /**
     * Recupere toutes les tables de la base de donnees
     * @return liste de toutes les tables
     */
    public List<TableResto> findAll() throws SQLException {
        List<TableResto> list = new ArrayList<>();
        String sql = "SELECT * FROM TableRestaurant";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Creer un objet TableResto pour chaque ligne
                TableResto t = new TableResto(
                    rs.getInt("numero_table"),
                    rs.getInt("capacite"),
                    normalizeEtat(rs.getString("etat"))
                );
                list.add(t);
            }
        }
        return list;
    }

    /**
     * Recupere toutes les tables libres
     * @return liste des tables disponibles
     */
    public List<TableResto> findLibres() throws SQLException {
        List<TableResto> list = new ArrayList<>();
        String sql = "SELECT * FROM TableRestaurant WHERE etat IN ('Disponible', 'libre')";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new TableResto(
                    rs.getInt("numero_table"),
                    rs.getInt("capacite"),
                    normalizeEtat(rs.getString("etat"))
                ));
            }
        }
        return list;
    }

    /**
     * Recherche une table par son numero
     * @param numero le numero de la table a rechercher
     * @return la table trouvee ou null
     */
    public TableResto findByNumero(int numero) throws SQLException {
        String sql = "SELECT * FROM TableRestaurant WHERE numero_table = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TableResto(
                    rs.getInt("numero_table"),
                    rs.getInt("capacite"),
                    normalizeEtat(rs.getString("etat"))
                );
            }
        }
        return null;
    }

    /**
     * Met a jour les informations d'une table
     * @param t la table avec les nouvelles informations
     */
    public void update(TableResto t) throws SQLException {
        String sql = "UPDATE TableRestaurant SET capacite = ?, etat = ? WHERE numero_table = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getCapacite());
            ps.setString(2, normalizeEtat(t.getEtat()));
            ps.setInt(3, t.getNumero());
            ps.executeUpdate();
            System.out.println("Table mise a jour : n°" + t.getNumero());
        }
    }

    /**
     * Supprime une table de la base de donnees
     * @param numero le numero de la table a supprimer
     */
    public void delete(int numero) throws SQLException {
        String sql = "DELETE FROM TableRestaurant WHERE numero_table = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Aucune table trouvee avec le numero " + numero + ".");
            }
            System.out.println("Table supprimee : n°" + numero);
        }
    }

    /**
     * Reserve une table ( Change son etat a "Occupee")
     * @param numero le numero de la table a reserver
     */
    public void reserver(int numero) throws SQLException {
        String sql = "UPDATE TableRestaurant SET etat='Occupee' WHERE numero_table=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ps.executeUpdate();
            System.out.println("Table n°" + numero + " reservee.");
        }
    }

    /**
     * Libere une table (Change son etat a "Disponible")
     * @param numero le numero de la table a liberer
     */
    public void liberer(int numero) throws SQLException {
        String sql = "UPDATE TableRestaurant SET etat='Disponible' WHERE numero_table=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ps.executeUpdate();
            System.out.println("Table n°" + numero + " liberee.");
        }
    }

    /**
     * Normalise l'etat d'une table (uniformise les valeurs)
     * @param etat l'etat a normaliser
     * @return l'etat normalise
     */
    private String normalizeEtat(String etat) {
        // Retourner "Disponible" si null
        if (etat == null) {
            return "Disponible";
        }
        String value = etat.trim().toLowerCase();
        if (value.equals("libre") || value.equals("disponible")) {
            return "Disponible";
        }
        if (value.equals("occupee") || value.equals("occupée") || value.contains("occup")) {
            return "Occupee";
        }
        return etat;
    }
}