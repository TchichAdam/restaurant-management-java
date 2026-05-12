package restaurant.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import restaurant.controller.PlatController;
import restaurant.model.Plat;

/**
 * Classe representant le panneau de gestion des plats
 * Permet d'ajouter, modifier, supprimer et rechercher des plats
 */
public class PlatPanel extends JPanel {
    // Controleur pour la gestion des plats
    private final PlatController controller = new PlatController();
    // Modele pour le tableau (colonnes: ID, Nom, Prix, Categorie)
    private final DefaultTableModel tableModel =
        new DefaultTableModel(new Object[] {"ID", "Nom", "Prix", "Categorie"}, 0);
    // Tableau pour afficher les plats
    private final JTable table = new JTable(tableModel);
    // Champs de saisie
    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();
    private final JTextField prixField = new JTextField();
    private final JTextField categorieField = new JTextField();
    private final JTextField rechercheField = new JTextField();

    /**
     * Constructeur - Initialise le panneau et ses composants
     */
    public PlatPanel() {
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
        JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire plat"));

        // Ajouter les labels et champs
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Nom"));
        panel.add(new JLabel("Prix"));
        panel.add(new JLabel("Categorie"));
        panel.add(idField);
        panel.add(nomField);
        panel.add(prixField);
        panel.add(categorieField);
        panel.add(new JLabel("Recherche nom"));
        panel.add(rechercheField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        return panel;
    }

    /**
     * Construction du panneau d'actions
     * @return le panneau avec les boutons
     */
    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 8, 8));

        // Creer les boutons
        JButton ajouterBtn = new JButton("Ajouter");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        JButton rechercherBtn = new JButton("Rechercher");
        JButton afficherBtn = new JButton("Afficher tout");
        JButton viderBtn = new JButton("Vider");

        // Associer les actions aux boutons
        ajouterBtn.addActionListener(e -> ajouterPlat());
        modifierBtn.addActionListener(e -> modifierPlat());
        supprimerBtn.addActionListener(e -> supprimerPlat());
        rechercherBtn.addActionListener(e -> chargerPlats(controller.rechercherParNom(rechercheField.getText())));
        afficherBtn.addActionListener(e -> chargerPlats(controller.getTousLesPlats()));
        viderBtn.addActionListener(e -> viderChamps());

        // Ajouter les boutons au panneau
        panel.add(ajouterBtn);
        panel.add(modifierBtn);
        panel.add(supprimerBtn);
        panel.add(rechercherBtn);
        panel.add(afficherBtn);
        panel.add(viderBtn);

        return panel;
    }

    /**
     * Remplit le tableau avec la liste des plats
     * @param plats la liste des plats a afficher
     */
    private void chargerPlats(List<Plat> plats) {
        tableModel.setRowCount(0);
        for (Plat plat : plats) {
            tableModel.addRow(new Object[] {
                plat.getIdPlat(),
                plat.getNom(),
                plat.getPrix(),
                plat.getCategorie()
            });
        }
    }

    /**
     * Rafraichit les donnees - Charge tous les plats depuis la base
     */
    public void refreshData() {
        chargerPlats(controller.getTousLesPlats());
    }

    /**
     * Ajoute un nouveau plat
     * Recupere les donnees des champs et appelle le controleur
     */
    private void ajouterPlat() {
        try {
            // Creer le plat avec les donnees du formulaire
            Plat plat = new Plat();
            plat.setNom(nomField.getText());
            plat.setPrix(Double.parseDouble(prixField.getText()));
            plat.setCategorie(categorieField.getText());
            // Appeler le controleur pour l'ajouter
            controller.ajouterPlat(plat);
            // Rafraichir le tableau
            chargerPlats(controller.getTousLesPlats());
            viderChamps();
            message("Plat ajoute avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Modifie un plat existant
     * Met a jour les informations du plat selectionne
     */
    private void modifierPlat() {
        try {
            Plat plat = new Plat();
            plat.setIdPlat(Integer.parseInt(idField.getText()));
            plat.setNom(nomField.getText());
            plat.setPrix(Double.parseDouble(prixField.getText()));
            plat.setCategorie(categorieField.getText());
            controller.modifierPlat(plat);
            chargerPlats(controller.getTousLesPlats());
            message("Plat modifie avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Supprime le plat selectionne (desactivation logique)
     */
    private void supprimerPlat() {
        try {
            if (idField.getText().trim().isEmpty()) {
                throw new IllegalStateException("Selectionnez d'abord un plat a supprimer.");
            }
            controller.supprimerPlat(Integer.parseInt(idField.getText()));
            chargerPlats(controller.getTousLesPlats());
            viderChamps();
            message("Plat supprime avec succes.");
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
        idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        nomField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        prixField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        categorieField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    /**
     * Vide tous les champs du formulaire
     */
    private void viderChamps() {
        idField.setText("");
        nomField.setText("");
        prixField.setText("");
        categorieField.setText("");
        rechercheField.setText("");
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