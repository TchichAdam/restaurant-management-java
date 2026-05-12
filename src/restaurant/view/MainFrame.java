package restaurant.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Classe representant la fenetre principale de l'application
 * Contient les onglets pour chaque entite (clients, plats, employes, tables, commandes)
 */
public class MainFrame extends JFrame {
    // Panels pour chaque section
    private final ClientPanel clientPanel;
    private final PlatPanel platPanel;
    private final EmployePanel employePanel;
    private final TablePanel tablePanel;
    private final CommandePanel commandePanel;

    /**
     * Constructeur - Initialise la fenetre et ses composants
     */
    public MainFrame() {
        // Configurer la fenetre
        setTitle("Gestion de Restaurant");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(UIStyles.BG);

        // Creer le panneau d'en-tete (hero)
        JPanel hero = new JPanel(new GridLayout(2, 1, 0, 4));
        hero.setBackground(UIStyles.HEADER);
        hero.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 14, 18));
        JLabel title = UIStyles.createHeroTitle("Application de Gestion de Restaurant");
        JLabel subtitle = UIStyles.createHeroSubtitle("Suivi des clients, plats, employes, tables et commandes");
        hero.add(title);
        hero.add(subtitle);
        add(hero, BorderLayout.NORTH);

        // Initialiser tous les panels
        clientPanel = new ClientPanel();
        platPanel = new PlatPanel();
        employePanel = new EmployePanel();
        tablePanel = new TablePanel();
        commandePanel = new CommandePanel();

        // Creer les onglets
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clients", clientPanel);
        tabs.addTab("Plats", platPanel);
        tabs.addTab("Employes", employePanel);
        tabs.addTab("Tables", tablePanel);
        tabs.addTab("Commandes", commandePanel);
        // Rafraichir les donnees quand l'onglet change
        tabs.addChangeListener(e -> refreshSelectedTab(tabs.getSelectedIndex()));

        add(tabs, BorderLayout.CENTER);
        UIStyles.stylePanelTree(this.getContentPane());
    }

    /**
     * Rafraichit les donnees de l'onglet selectionne
     * @param index l'index de l'onglet selectionne
     */
    private void refreshSelectedTab(int index) {
        switch (index) {
            case 0 -> clientPanel.refreshData();
            case 1 -> platPanel.refreshData();
            case 2 -> employePanel.refreshData();
            case 3 -> tablePanel.refreshData();
            case 4 -> commandePanel.refreshData();
            default -> {
            }
        }
    }
}