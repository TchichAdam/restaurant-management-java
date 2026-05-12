package restaurant.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import restaurant.controller.TableRestoController;
import restaurant.model.TableResto;

/**
 * Classe representant le panneau de gestion des tables
 * Permet d'ajouter, modifier, supprimer, reserver et liberer des tables
 */
public class TablePanel extends JPanel {
    // Controleur pour la gestion des tables
    private final TableRestoController controller = new TableRestoController();
    // Modele pour le tableau (colonnes: Numero, Capacite,Etat)
    private final DefaultTableModel tableModel =
        new DefaultTableModel(new Object[] {"Numero", "Capacite", "Etat"}, 0);
    // Tableau pour afficher les tables
    private final JTable table = new JTable(tableModel);
    // Champs de saisie
    private final JTextField numeroField = new JTextField();
    private final JTextField capaciteField = new JTextField();
    // ComboBox pour selectionner l'etat
    private final JComboBox<String> etatBox = new JComboBox<>(new String[] {"Disponible", "Occupee"});

    /**
     * Constructeur - Initialise le panneau et ses composants
     */
    public TablePanel() {
        // Configurer le layout et les bordures
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajouter les differents composants
        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildActionPanel(), BorderLayout.SOUTH);

        // Configurer le tableau
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Remplir les champs quand une ligne est selectionnee
        table.getSelectionModel().addListSelectionListener(e -> remplirChampsDepuisTable());

        // Charger les donnees initiales
        refreshData();
        // Appliquer le style
        UIStyles.stylePanelTree(this);
    }

    /**
     * Construction du panneau de formulaire
     * @return le panneau avec les champs
     */
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire table"));

        // Ajouter les labels et champs
        panel.add(new JLabel("Numero"));
        panel.add(new JLabel("Capacite"));
        panel.add(new JLabel("Etat"));
        panel.add(new JLabel(""));
        panel.add(numeroField);
        panel.add(capaciteField);
        panel.add(etatBox);
        panel.add(new JLabel(""));

        return panel;
    }

    /**
     * Construction du panneau d'actions
     * @return le panneau avec les boutons
     */
    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 7, 8, 8));

        // Creer les boutons
        JButton ajouterBtn = new JButton("Ajouter");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        JButton reserverBtn = new JButton("Reserver");
        JButton libererBtn = new JButton("Liberer");
        JButton libresBtn = new JButton("Tables libres");
        JButton afficherBtn = new JButton("Afficher tout");

        // Associer les actions aux boutons
        ajouterBtn.addActionListener(e -> ajouterTable());
        modifierBtn.addActionListener(e -> modifierTable());
        supprimerBtn.addActionListener(e -> supprimerTable());
        reserverBtn.addActionListener(e -> reserverTable());
        libererBtn.addActionListener(e -> libererTable());
        libresBtn.addActionListener(e -> chargerTables(controller.getTablesLibres()));
        afficherBtn.addActionListener(e -> chargerTables(controller.getToutesLesTables()));

        // Ajouter les boutons au panneau
        panel.add(ajouterBtn);
        panel.add(modifierBtn);
        panel.add(supprimerBtn);
        panel.add(reserverBtn);
        panel.add(libererBtn);
        panel.add(libresBtn);
        panel.add(afficherBtn);

        return panel;
    }

    /**
     * Remplit le tableau avec la liste des tables
     * @param tables la liste des tables a afficher
     */
    private void chargerTables(List<TableResto> tables) {
        tableModel.setRowCount(0);
        for (TableResto tableResto : tables) {
            tableModel.addRow(new Object[] {
                tableResto.getNumero(),
                tableResto.getCapacite(),
                normalizeEtat(tableResto.getEtat())
            });
        }
    }

    /**
     * Rafraichit les donnees - Charge toutes les tables depuis la base
     */
    public void refreshData() {
        chargerTables(controller.getToutesLesTables());
    }

    /**
     * Ajoute une nouvelle table
     * Recupere les donnees des champs et appelle le controleur
     * Le numero est auto-genere si le champ est vide
     */
    private void ajouterTable() {
        try {
            // Generer automatiquement le numero si non fourni
            int numero = numeroField.getText().trim().isEmpty() 
                ? controller.getNextNumero() 
                : Integer.parseInt(numeroField.getText());
            TableResto tableResto = new TableResto(
                numero,
                Integer.parseInt(capaciteField.getText()),
                (String) etatBox.getSelectedItem()
            );
            // Appeler le controleur pour l'ajouter
            controller.ajouterTable(tableResto);
            // Rafraichir le tableau
            chargerTables(controller.getToutesLesTables());
            viderChamps();
            message("Table ajoutee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Modifie une table existante
     * Met a jour les informations de la table selectionnee
     */
    private void modifierTable() {
        try {
            TableResto tableResto = new TableResto(
                Integer.parseInt(numeroField.getText()),
                Integer.parseInt(capaciteField.getText()),
                (String) etatBox.getSelectedItem()
            );
            controller.modifierTable(tableResto);
            chargerTables(controller.getToutesLesTables());
            message("Table modifiee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Supprime la table selectionnee
     */
    private void supprimerTable() {
        try {
            if (numeroField.getText().trim().isEmpty()) {
                throw new IllegalStateException("Selectionnez d'abord une table a supprimer.");
            }
            controller.supprimerTable(Integer.parseInt(numeroField.getText()));
            chargerTables(controller.getToutesLesTables());
            viderChamps();
            message("Table supprimee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Reserve la table selectionnee
     * Change son etat a "Occupee"
     */
    private void reserverTable() {
        try {
            controller.reserverTable(Integer.parseInt(numeroField.getText()));
            chargerTables(controller.getToutesLesTables());
            message("Table reservee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Libere la table selectionnee
     * Change son etat a "Disponible"
     */
    private void libererTable() {
        try {
            controller.libererTable(Integer.parseInt(numeroField.getText()));
            chargerTables(controller.getToutesLesTables());
            message("Table liberee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Remplit les champs depuis la ligne selectionnee dans le tableau
     */
    private void remplirChampsDepuisTable() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        // Recuperer les valeurs du tableau
        numeroField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        capaciteField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        etatBox.setSelectedItem(normalizeEtat(String.valueOf(tableModel.getValueAt(row, 2))));
    }

    /**
     * Vide tous les champs du formulaire
     */
    private void viderChamps() {
        numeroField.setText("");
        capaciteField.setText("");
        etatBox.setSelectedIndex(0);
        table.clearSelection();
    }

    /**
     * Affiche un message a l'utilisateur
     * @param texte le message a afficher
     */
    private void message(String texte) {
        JOptionPane.showMessageDialog(this, texte);
    }

    /**
     * Affiche une erreur a l'utilisateur
     * @param ex l'exception a afficher
     */
    private void erreur(Exception ex) {
        JOptionPane.showMessageDialog(this, UIStyles.getErrorMessage(ex), "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Normalise l'etat d'une table
     * @param etat l'etat a normaliser
     * @return l'etat normalise
     */
    private String normalizeEtat(String etat) {
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
        return "Disponible";
    }
}