package restaurant.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe pour charger la configuration de la base de donnees
 * Lit les parametres depuis le fichier database.properties
 */
public class ConfigLoader {
    // Properties pour stocker les parametres
    private static final Properties properties = new Properties();

    // Bloc statique pour charger les properties au demarrage
    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Fichier database.properties introuvable");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de database.properties", e);
        }
    }

    /**
     * Recupere une valeur par sa cle
     * @param key la cle
     * @return la valeur
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Recupere l'URL de connexion
     * @return l'URL JDBC
     */
    public static String getUrl() {
        return get("db.url");
    }

    /**
     * Recupere le nom d'utilisateur
     * @return le username
     */
    public static String getUser() {
        return get("db.user");
    }

    /**
     * Recupere le mot de passe
     * @return le mot de passe
     */
    public static String getPassword() {
        return get("db.password");
    }
}