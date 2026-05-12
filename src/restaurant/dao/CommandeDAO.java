package restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import restaurant.model.Commande;

public class CommandeDAO {

    /**
     * Recuper le prochain ID disponible pour une nouvelle commande
     * @return le prochain ID (MAX + 1) ou 1 si la table est vide
     */
    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id_commande) FROM Commande";
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
     * Cree une nouvelle commande dans la base de donnees
     * @param idClient l'ID du client
     * @param numeroTable le numero de la table
     * @param idEmploye l'ID de l'employe (serveur)
     * @return l'ID de la commande creee
     */
    public int insert(int idClient, int numeroTable, int idEmploye) throws SQLException {
        int nextId = getNextId();
        Connection con = DatabaseConnection.getConnection();
        try {
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Commande ON");
            }
            int idTable = getIdTable(con, numeroTable);
            String sql = "INSERT INTO Commande (id_commande, id_client, id_table, id_employe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setInt(2, idClient);
                ps.setInt(3, idTable);
                ps.setInt(4, idEmploye);
                ps.executeUpdate();
            }
            try (Statement st = con.createStatement()) {
                st.execute("SET IDENTITY_INSERT Commande OFF");
            }
            new TableRestoDAO().reserver(numeroTable);
            System.out.println("Commande creee (id=" + nextId + ")");
            return nextId;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Recupere toutes les commandes de la base de donnees
     * @return liste de toutes les commandes
     */
    public List<Commande> findAll() throws SQLException {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT id_commande, date_commande FROM Commande";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("id_commande"));
                commande.setDate(rs.getTimestamp("date_commande"));
                list.add(commande);
            }
        }
        return list;
    }

    public Commande findById(int idCommande) throws SQLException {
        String sql = "SELECT id_commande, date_commande FROM Commande WHERE id_commande = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("id_commande"));
                commande.setDate(rs.getTimestamp("date_commande"));
                return commande;
            }
        }
        return null;
    }

    public void update(int idCommande, int idClient, int numeroTable, int idEmploye) throws SQLException {
        String sql = "UPDATE Commande SET id_client = ?, id_table = ?, id_employe = ? WHERE id_commande = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int idTable = getIdTable(con, numeroTable);
            ps.setInt(1, idClient);
            ps.setInt(2, idTable);
            ps.setInt(3, idEmploye);
            ps.setInt(4, idCommande);
            ps.executeUpdate();
            System.out.println("Commande mise a jour (id=" + idCommande + ")");
        }
    }

    public void delete(int idCommande) throws SQLException {
        String detailSql = "DELETE FROM DetailCommande WHERE id_commande = ?";
        String commandeSql = "DELETE FROM Commande WHERE id_commande = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            Integer idClient = getIdClientByCommande(con, idCommande);
            Integer numeroTable = getNumeroTableByCommande(con, idCommande);
            try (PreparedStatement psDetail = con.prepareStatement(detailSql);
                 PreparedStatement psCommande = con.prepareStatement(commandeSql)) {
                psDetail.setInt(1, idCommande);
                psDetail.executeUpdate();

                psCommande.setInt(1, idCommande);
                int affected = psCommande.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Aucune commande trouvee avec l'id " + idCommande + ".");
                }
            }
            if (numeroTable != null) {
                libererTable(con, numeroTable);
            }
            if (idClient != null && countCommandesByClient(con, idClient) == 0) {
                deleteClient(con, idClient);
            }
            deleteOrphanClients(con);
            System.out.println("Commande supprimee (id=" + idCommande + ")");
        }
    }

    public void ajouterPlat(int idCommande, int idPlat, int quantite) throws SQLException {
        String prixSql = "SELECT prix FROM Plat WHERE id_plat = ?";
        double prix = 0;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(prixSql)) {
            ps.setInt(1, idPlat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prix = rs.getDouble("prix");
            }
        }

        String sql = "INSERT INTO DetailCommande (id_commande, id_plat, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, idPlat);
            ps.setInt(3, quantite);
            ps.setDouble(4, prix);
            ps.executeUpdate();
            System.out.println("Plat ajoute a la commande.");
        }
    }

    public double calculerTotal(int idCommande) throws SQLException {
        String sql = "SELECT SUM(quantite * prix_unitaire) AS total FROM DetailCommande WHERE id_commande = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }

    public void afficherCommandes(DefaultTableModel model) throws SQLException {
        String sql = "SELECT * FROM vw_CommandesDetail";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id_commande"),
                    rs.getString("client_nom"),
                    rs.getInt("numero_table"),
                    rs.getString("employe_nom"),
                    rs.getString("date_commande")
                });
            }
        }
    }

    private int getIdTable(Connection con, int numero) throws SQLException {
        String sql = "SELECT id_table FROM TableRestaurant WHERE numero_table = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_table");
                }
            }
        }
        throw new SQLException("Table numero " + numero + " introuvable.");
    }

    private Integer getNumeroTableByCommande(Connection con, int idCommande) throws SQLException {
        String sql = "SELECT tr.numero_table "
                   + "FROM Commande c "
                   + "JOIN TableRestaurant tr ON c.id_table = tr.id_table "
                   + "WHERE c.id_commande = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("numero_table");
            }
        }
        return null;
    }

    private Integer getIdClientByCommande(Connection con, int idCommande) throws SQLException {
        String sql = "SELECT id_client FROM Commande WHERE id_commande = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_client");
            }
        }
        return null;
    }

    private int countCommandesByClient(Connection con, int idClient) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Commande WHERE id_client = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    private void deleteOrphanClients(Connection con) throws SQLException {
        String sql = "DELETE FROM Client WHERE id_client NOT IN (SELECT DISTINCT id_client FROM Commande WHERE id_client IS NOT NULL)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private void libererTable(Connection con, int numeroTable) throws SQLException {
        String sql = "UPDATE TableRestaurant SET etat='Disponible' WHERE numero_table=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroTable);
            ps.executeUpdate();
        }
    }

    private void deleteClient(Connection con, int idClient) throws SQLException {
        String sql = "DELETE FROM Client WHERE id_client = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.executeUpdate();
        }
    }
}
