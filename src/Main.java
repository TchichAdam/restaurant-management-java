import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Connection settings for the local SQL Server database.
        String url = "jdbc:sqlserver://localhost:1433;database=GestionRestaurant;encrypt=true;trustServerCertificate=true";
        // SQL Server login username.
        String user = "restaurant_user";
        // SQL Server login password.
        String password = "123456";

        try {
            // Explicitly load the SQL Server JDBC driver. w jdbc its a way to  talk to java
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Open the database connection and query the Client table.
            try (Connection con = DriverManager.getConnection(url, user, password);
            //outil pour query ou pour ecrire
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM Client")) {
                // Confirm that the connection succeeded.
                System.out.println("Connected successfully");

                // Print each client row returned by the query.
                while (rs.next()) {
                    System.out.println(
                        rs.getInt("id_client") + " - " +
                        rs.getString("nom")
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            // Explain that the JDBC driver was not found in the project classpath.
            System.out.println("SQL Server JDBC driver not found. Check lib/mssql-jdbc-13.4.0.jre11.jar.");
            e.printStackTrace();
        } catch (SQLException e) {
            // Show a clearer SQL Server connection error message.
            System.out.println("SQL Server connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
