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

import restaurant.controller.EmployeController;
import restaurant.model.Caissier;
import restaurant.model.Cuisinier;
import restaurant.model.Employe;
import restaurant.model.Serveur;

/**
 * Classe representant le panneau de gestion des employes
 * Permet d'ajouter, modifier, supprimer et rechercher des employes
 */
public class EmployePanel extends JPanel {
    // Controleur pour la gestion des employes
    private final EmployeController controller = new EmployeController();
    // Modele pour le tableau (colonnes: ID, Nom, Role)
    private final DefaultTableModel tableModel =
        new DefaultTableModel(new Object[] {"ID", "Nom", "Role"}, 0);
    // Tableau pour afficher les employes
    private final JTable table = new JTable(tableModel);
    // Champs de saisie
    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();
    // ComboBox pour selectionner le role
    private final JComboBox<String> roleBox = new JComboBox<>(new String[] {"Serveur", "Cuisinier", "Caissier"});
    private final JTextField rechercheField = new JTextField();

    /**
     * Constructeur - Initialise le panneau et ses composants
     */
    public EmployePanel() {
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
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire employe"));

        // Ajouter les labels et champs
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Nom"));
        panel.add(new JLabel("Role"));
        panel.add(new JLabel(""));
        panel.add(idField);
        panel.add(nomField);
        panel.add(roleBox);
        panel.add(new JLabel(""));
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
        JButton filtrerBtn = new JButton("Filtrer role");
        JButton afficherBtn = new JButton("Afficher tout");

        // Associer les actions aux boutons
        ajouterBtn.addActionListener(e -> ajouterEmploye());
        modifierBtn.addActionListener(e -> modifierEmploye());
        supprimerBtn.addActionListener(e -> supprimerEmploye());
        rechercherBtn.addActionListener(e -> chargerEmployes(controller.rechercherParNom(rechercheField.getText())));
        filtrerBtn.addActionListener(e -> chargerEmployes(controller.getEmployesParRole((String) roleBox.getSelectedItem())));
        afficherBtn.addActionListener(e -> chargerEmployes(controller.getTousLesEmployes()));

        // Ajouter les boutons au panneau
        panel.add(ajouterBtn);
        panel.add(modifierBtn);
        panel.add(supprimerBtn);
        panel.add(rechercherBtn);
        panel.add(filtrerBtn);
        panel.add(afficherBtn);

        return panel;
    }

    /**
     * Remplit le tableau avec la liste des employes
     * @param employes la liste des employes a afficher
     */
    private void chargerEmployes(List<Employe> employes) {
        tableModel.setRowCount(0);
        for (Employe employe : employes) {
            tableModel.addRow(new Object[] {
                employe.getId(),
                employe.getNom(),
                employe.getClass().getSimpleName()
            });
        }
    }

    /**
     * Rafraichit les donnees - Charge tous les employes depuis la base
     */
    public void refreshData() {
        chargerEmployes(controller.getTousLesEmployes());
    }

    /**
     * Ajoute un nouvel employe
     * Recupere les donnees des champs et appelle le controleur
     */
    private void ajouterEmploye() {
        try {
            // Recuperer le role selectionne
            String role = (String) roleBox.getSelectedItem();
            // Creer l'employe avec le bon type
            Employe employe = buildEmploye(0, nomField.getText(), role);
            // Appeler le controleur pour l'ajouter
            controller.ajouterEmploye(employe, role);
            // Rafraichir le tableau
            chargerEmployes(controller.getTousLesEmployes());
            viderChamps();
            message("Employe ajoute avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Modifie un employe existant
     * Met a jour les informations de l'employe selectionne
     */
    private void modifierEmploye() {
        try {
            String role = (String) roleBox.getSelectedItem();
            Employe employe = buildEmploye(Integer.parseInt(idField.getText()), nomField.getText(), role);
            controller.modifierEmploye(employe, role);
            chargerEmployes(controller.getTousLesEmployes());
            message("Employe modifie avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Supprime l'employe selectionne
     */
    private void supprimerEmploye() {
        try {
            if (idField.getText().trim().isEmpty()) {
                throw new IllegalStateException("Selectionnez d'abord un employe a supprimer.");
            }
            controller.supprimerEmploye(Integer.parseInt(idField.getText()));
            chargerEmployes(controller.getTousLesEmployes());
            viderChamps();
            message("Employe supprime avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Construit un objet Employe selon le role
     * @param id l'ID de l'employe
     * @param nom le nom
     * @param role le role (Serveur, Cuisinier, Caissier)
     * @return l'objet Employe cree
     */
    private Employe buildEmploye(int id, String nom, String role) {
        // Creer le bon type d'employe selon le role
        if ("Cuisinier".equals(role)) {
            return new Cuisinier(id, nom);
        }
        if ("Caissier".equals(role)) {
            return new Caissier(id, nom);
        }
        return new Serveur(id, nom);
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
        roleBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 2)));
    }

    /**
     * Vide tous les champs du formulaire
     */
    private void viderChamps() {
        idField.setText("");
        nomField.setText("");
        rechercheField.setText("");
        roleBox.setSelectedIndex(0);
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