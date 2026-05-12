package restaurant.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe pour gerer la connexion a la base de donnees SQL Server
 * Utilise le pattern Singleton pour une seule connexion
 */
public class DatabaseConnection {
    // Instance unique de la connexion
    private static Connection instance = null;

    /**
     * Recupere l'instance de connexion
     * Cree la connexion si elle n'existe pas ou si elle est fermee
     * @return l'objet Connection
     * @throws SQLException si erreur de connexion
     */
    public static Connection getConnection() throws SQLException {
        // Verifier si la connexion existe et est ouverte
        if (instance == null || instance.isClosed()) {
            try {
                // Charger le driver JDBC
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // Creer la connexion avec les parametres du fichier de configuration
                instance = DriverManager.getConnection(
                    ConfigLoader.getUrl(),
                    ConfigLoader.getUser(),
                    ConfigLoader.getPassword()
                );
                System.out.println("Connexion etablie.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC introuvable.", e);
            }
        }
        // Verifier a nouveau si la connexion est fermee (peut avoir ete fermee par externe)
        Connection conn = instance;
        if (conn.isClosed()) {
            // Recréer la connexion
            conn = DriverManager.getConnection(
                ConfigLoader.getUrl(),
                ConfigLoader.getUser(),
                ConfigLoader.getPassword()
            );
            instance = conn;
        }
        return conn;
    }
}