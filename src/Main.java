import javax.swing.SwingUtilities;

import restaurant.view.MainFrame;

/**
 * Classe principale du programme
 * Point d'entree de l'application de gestion de restaurant
 */
public class Main {
    /**
     * Methode principale - Point d'entree du programme
     * Demarre l'application Swing dans l'EDT (Event Dispatch Thread)
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        // Utiliser SwingUtilities pour demarrer dans l'EDT (bonne pratique)
        SwingUtilities.invokeLater(() -> {
            // Creer et afficher la fenetre principale
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}