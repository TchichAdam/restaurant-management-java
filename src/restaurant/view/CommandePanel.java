package restaurant.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import restaurant.controller.CommandeController;
import restaurant.model.Commande;

/**
 * Classe representant le panneau de gestion des commandes
 * Permet de creer, modifier, supprimer des commandes et ajouter des plats
 */
public class CommandePanel extends JPanel {
    // Controleur pour la gestion des commandes
    private final CommandeController controller = new CommandeController();
    // Modele pour le tableau (colonnes: ID Commande, Date, Total)
    private final DefaultTableModel tableModel =
        new DefaultTableModel(new Object[] {"ID Commande", "Date", "Total"}, 0);
    // Tableau pour afficher les commandes
    private final JTable table = new JTable(tableModel);
    // Champs de saisie
    private final JTextField idCommandeField = new JTextField();
    private final JTextField idClientField = new JTextField();
    private final JTextField numeroTableField = new JTextField();
    private final JTextField idEmployeField = new JTextField();
    private final JTextField idPlatField = new JTextField();
    private final JTextField quantiteField = new JTextField();

    /**
     * Constructeur - Initialise le panneau et ses composants
     */
    public CommandePanel() {
        // Configurer le layout et les bordures
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creer le panneau superieur avec formulaire et boutons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(buildFormPanel(), BorderLayout.CENTER);
        topPanel.add(buildActionPanel(), BorderLayout.SOUTH);

        // Ajouter les composants
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire commande"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Ajouter les champs
        addField(panel, gbc, 0, 0, "ID Commande", idCommandeField);
        addField(panel, gbc, 1, 0, "ID Client", idClientField);
        addField(panel, gbc, 2, 0, "Numero Table", numeroTableField);
        addField(panel, gbc, 3, 0, "ID Employe", idEmployeField);

        addField(panel, gbc, 0, 1, "ID Plat", idPlatField);
        addField(panel, gbc, 1, 1, "Quantite", quantiteField);

        return panel;
    }

    /**
     * Ajoute un champ au formulaire
     * @param panel le panneau cible
     * @param gbc les contraintes
     * @param x position x
     * @param y position y
     * @param label le label du champ
     * @param field le champ de saisie
     */
    private void addField(JPanel panel, GridBagConstraints gbc, int x, int y, String label, JTextField field) {
        gbc.gridx = x;
        gbc.gridy = y * 2;
        gbc.weightx = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = y * 2 + 1;
        field.setColumns(12);
        panel.add(field, gbc);
    }

    /**
     * Construction du panneau d'actions
     * @return le panneau avec les boutons
     */
    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

        // Creer les boutons
        JButton creerBtn = new JButton("Creer");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        JButton afficherBtn = new JButton("Afficher tout");
        JButton ajouterPlatBtn = new JButton("Ajouter plat");
        JButton totalBtn = new JButton("Calculer total");
        JButton viderBtn = new JButton("Vider");

        // Associer les actions aux boutons
        creerBtn.addActionListener(e -> creerCommande());
        modifierBtn.addActionListener(e -> modifierCommande());
        supprimerBtn.addActionListener(e -> supprimerCommande());
        afficherBtn.addActionListener(e -> chargerCommandes());
        ajouterPlatBtn.addActionListener(e -> ajouterPlat());
        totalBtn.addActionListener(e -> calculerTotal());
        viderBtn.addActionListener(e -> viderChamps());

        // Ajouter les boutons au panneau
        panel.add(creerBtn);
        panel.add(modifierBtn);
        panel.add(supprimerBtn);
        panel.add(afficherBtn);
        panel.add(ajouterPlatBtn);
        panel.add(totalBtn);
        panel.add(viderBtn);

        return panel;
    }

    /**
     * Remplit le tableau avec la liste des commandes
     */
    private void chargerCommandes() {
        List<Commande> commandes = controller.getToutesLesCommandes();
        tableModel.setRowCount(0);
        for (Commande commande : commandes) {
            tableModel.addRow(new Object[] {
                commande.getIdCommande(),
                commande.getDate(),
                controller.calculerTotalCommande(commande.getIdCommande())
            });
        }
    }

    /**
     * Rafraichit les donnees - Charge toutes les commandes depuis la base
     */
    public void refreshData() {
        chargerCommandes();
    }

    /**
     * Cree une nouvelle commande
     * Recupere les donnees des champs et appelle le controleur
     */
    private void creerCommande() {
        try {
            int idCommande = controller.creerCommande(
                Integer.parseInt(idClientField.getText()),
                Integer.parseInt(numeroTableField.getText()),
                Integer.parseInt(idEmployeField.getText())
            );
            idCommandeField.setText(String.valueOf(idCommande));
            chargerCommandes();
            message("Commande creee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Modifie une commande existante
     * Met a jour les informations de la commande selectionnee
     */
    private void modifierCommande() {
        try {
            controller.modifierCommande(
                Integer.parseInt(idCommandeField.getText()),
                Integer.parseInt(idClientField.getText()),
                Integer.parseInt(numeroTableField.getText()),
                Integer.parseInt(idEmployeField.getText())
            );
            chargerCommandes();
            message("Commande modifiee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Supprime la commande selectionnee
     */
    private void supprimerCommande() {
        try {
            if (idCommandeField.getText().trim().isEmpty()) {
                throw new IllegalStateException("Selectionnez d'abord une commande a supprimer.");
            }
            controller.supprimerCommande(Integer.parseInt(idCommandeField.getText()));
            chargerCommandes();
            viderChamps();
            message("Commande supprimee avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Ajoute un plat a la commande
     * @param idCommande l'ID de la commande
     * @param idPlat l'ID du plat
     * @param quantite la quantite
     */
    private void ajouterPlat() {
        try {
            controller.ajouterPlatACommande(
                Integer.parseInt(idCommandeField.getText()),
                Integer.parseInt(idPlatField.getText()),
                Integer.parseInt(quantiteField.getText())
            );
            chargerCommandes();
            message("Plat ajoute a la commande.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Calcule le total de la commande selectionnee
     */
    private void calculerTotal() {
        try {
            double total = controller.calculerTotalCommande(Integer.parseInt(idCommandeField.getText()));
            JOptionPane.showMessageDialog(this, "Total de la commande : " + total + " DH");
            chargerCommandes();
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
        // Recuperer l'ID de la commande et charger les details
        idCommandeField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        Commande commande = controller.getCommandeParId(Integer.parseInt(idCommandeField.getText()));
        if (commande != null) {
            idCommandeField.setText(String.valueOf(commande.getIdCommande()));
        }
    }

    /**
     * Vide tous les champs du formulaire
     */
    private void viderChamps() {
        idCommandeField.setText("");
        idClientField.setText("");
        numeroTableField.setText("");
        idEmployeField.setText("");
        idPlatField.setText("");
        quantiteField.setText("");
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
}