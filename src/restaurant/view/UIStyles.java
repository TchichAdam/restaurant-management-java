package restaurant.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

/**
 * Classe utilitaire pour appliquer les styles a l'interface utilisateur
 * Definir les couleurs, polices et styles generaux
 */
public final class UIStyles {
    // Couleurs du theme
    public static final Color BG = new Color(247, 242, 235);
    public static final Color CARD = new Color(255, 252, 247);
    public static final Color INK = new Color(46, 36, 28);
    public static final Color MUTED = new Color(112, 94, 78);
    public static final Color ACCENT = new Color(195, 94, 45);
    public static final Color ACCENT_DARK = new Color(137, 61, 27);
    public static final Color LINE = new Color(221, 206, 190);
    public static final Color HEADER = new Color(238, 228, 216);

    // Polices
    private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Constructeur prive - Classe utilitaire
     */
    private UIStyles() {
    }

    /**
     * Applique les styles a un composant et ses enfants
     * @param component le composant a styliser
     */
    public static void stylePanelTree(Component component) {
        // Styliser les panneaux
        if (component instanceof JPanel panel) {
            panel.setOpaque(true);
            panel.setBackground(BG);
            if (panel.getBorder() instanceof TitledBorder titledBorder) {
                Border line = BorderFactory.createLineBorder(LINE, 1, true);
                Border empty = BorderFactory.createEmptyBorder(12, 12, 12, 12);
                panel.setBorder(BorderFactory.createCompoundBorder(line, empty));
                TitledBorder replacement = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(LINE, 1, true),
                    titledBorder.getTitle()
                );
                replacement.setTitleColor(ACCENT_DARK);
                replacement.setTitleFont(TITLE_FONT);
                panel.setBorder(BorderFactory.createCompoundBorder(replacement, empty));
                panel.setBackground(CARD);
            }
        }

        // Styliser les labels
        if (component instanceof JLabel label) {
            label.setForeground(INK);
            label.setFont(LABEL_FONT);
            label.setHorizontalAlignment(label.getHorizontalAlignment() == SwingConstants.CENTER
                ? SwingConstants.CENTER : SwingConstants.LEFT);
        }

        // Styliser les champs de texte
        if (component instanceof JTextField field) {
            field.setFont(BODY_FONT);
            field.setForeground(INK);
            field.setBackground(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }

        // Styliser les ComboBox
        if (component instanceof JComboBox<?> comboBox) {
            comboBox.setFont(BODY_FONT);
            comboBox.setForeground(INK);
            comboBox.setBackground(Color.WHITE);
        }

        // Styliser les boutons
        if (component instanceof JButton button) {
            button.setFocusPainted(false);
            button.setFont(BUTTON_FONT);
            button.setForeground(Color.WHITE);
            button.setBackground(ACCENT);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_DARK, 1, true),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)
            ));
        }

        // Styliser les tableaux
        if (component instanceof JTable table) {
            table.setFont(BODY_FONT);
            table.setRowHeight(28);
            table.setGridColor(LINE);
            table.setSelectionBackground(new Color(219, 170, 135));
            table.setSelectionForeground(INK);
            table.setBackground(Color.WHITE);
            JTableHeader header = table.getTableHeader();
            header.setFont(LABEL_FONT);
            header.setBackground(HEADER);
            header.setForeground(INK);
        }

        // Styliser les ScrollPane
        if (component instanceof JScrollPane scrollPane) {
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.setBorder(BorderFactory.createLineBorder(LINE, 1, true));
        }

        // Styliser les TabbedPane
        if (component instanceof JTabbedPane tabbedPane) {
            tabbedPane.setBackground(BG);
            tabbedPane.setForeground(INK);
            tabbedPane.setFont(LABEL_FONT);
        }

        // Appliquer la couleur du texte
        if (component instanceof JComponent jComponent) {
            jComponent.setForeground(INK);
        }

        // Appliquer recursivement aux composants enfants
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                stylePanelTree(child);
            }
        }
    }

    /**
     * Cree un label de titre principal
     * @param text le texte du titre
     * @return le JLabel cree
     */
    public static JLabel createHeroTitle(String text) {
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(INK);
        return title;
    }

    /**
     * Cree un label de sous-titre
     * @param text le texte du sous-titre
     * @return le JLabel cree
     */
    public static JLabel createHeroSubtitle(String text) {
        JLabel subtitle = new JLabel(text, SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        return subtitle;
    }

    /**
     * Extrait le message d'erreur le plus pertinent
     * @param error l'exception
     * @return le message d'erreur
     */
    public static String getErrorMessage(Throwable error) {
        Throwable current = error;
        // Remonter jusqu'a la cause racine
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() != null ? current.getMessage() : error.getMessage();
    }
}