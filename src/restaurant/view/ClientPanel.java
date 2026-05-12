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

import restaurant.controller.ClientController;
import restaurant.model.Client;

/**
 * Classe representant le panneau de gestion des clients
 * Permet d'ajouter, modifier, supprimer et rechercher des clients
 */
public class ClientPanel extends JPanel {
    // Controleur pour la gestion des clients
    private final ClientController controller = new ClientController();
    // Modele pour le tableau (colonnes: ID, Nom, Telephone, Adresse)
    private final DefaultTableModel tableModel =
        new DefaultTableModel(new Object[] {"ID", "Nom", "Telephone", "Adresse"}, 0);
    // Tableau pour afficher les clients
    private final JTable table = new JTable(tableModel);
    // Champs de saisie
    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();
    private final JTextField telephoneField = new JTextField();
    private final JTextField adresseField = new JTextField();
    private final JTextField rechercheField = new JTextField();

    /**
     * Constructeur - Initialise le panneau et ses composants
     */
    public ClientPanel() {
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
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire client"));

        // Ajouter les labels et champs
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Nom"));
        panel.add(new JLabel("Telephone"));
        panel.add(new JLabel("Adresse"));
        panel.add(idField);
        panel.add(nomField);
        panel.add(telephoneField);
        panel.add(adresseField);
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
        ajouterBtn.addActionListener(e -> ajouterClient());
        modifierBtn.addActionListener(e -> modifierClient());
        supprimerBtn.addActionListener(e -> supprimerClient());
        rechercherBtn.addActionListener(e -> chargerClients(controller.rechercherParNom(rechercheField.getText())));
        afficherBtn.addActionListener(e -> chargerClients(controller.getTousLesClients()));
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
     * Remplit le tableau avec la liste des clients
     * @param clients la liste des clients a afficher
     */
    private void chargerClients(List<Client> clients) {
        tableModel.setRowCount(0);
        for (Client client : clients) {
            tableModel.addRow(new Object[] {
                client.getIdClient(),
                client.getNom(),
                client.getTelephone(),
                client.getAdresse()
            });
        }
    }

    /**
     * Rafraichit les donnees - Charge tous les clients depuis la base
     */
    public void refreshData() {
        chargerClients(controller.getTousLesClients());
    }

    /**
     * Ajoute un nouveau client
     * Recupere les donnees des champs et appelle le controleur
     */
    private void ajouterClient() {
        // Valider les champs avant l'ajout
        if (!validerChamps()) return;
        try {
            // Creer le client avec les donnees du formulaire
            Client client = new Client();
            client.setNom(nomField.getText().trim());
            client.setTelephone(telephoneField.getText().trim());
            client.setAdresse(adresseField.getText().trim());
            // Appeler le controleur pour l'ajouter
            controller.ajouterClient(client);
            // Rafraichir le tableau
            chargerClients(controller.getTousLesClients());
            viderChamps();
            message("Client ajoute avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Modifie un client existant
     * Met a jour les informations du client selectionne
     */
    private void modifierClient() {
        // Valider les champs
        if (!validerChamps()) return;
        if (idField.getText().trim().isEmpty()) {
            erreur(new IllegalStateException("Selectionnez un client a modifier."));
            return;
        }
        try {
            Client client = new Client();
            client.setIdClient(Integer.parseInt(idField.getText().trim()));
            client.setNom(nomField.getText().trim());
            client.setTelephone(telephoneField.getText().trim());
            client.setAdresse(adresseField.getText().trim());
            controller.modifierClient(client);
            chargerClients(controller.getTousLesClients());
            message("Client modifie avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Supprime le client selectionne
     */
    private void supprimerClient() {
        if (idField.getText().trim().isEmpty()) {
            erreur(new IllegalStateException("Selectionnez d'abord un client a supprimer."));
            return;
        }
        try {
            controller.supprimerClient(Integer.parseInt(idField.getText().trim()));
            chargerClients(controller.getTousLesClients());
            viderChamps();
            message("Client supprime avec succes.");
        } catch (Exception ex) {
            erreur(ex);
        }
    }

    /**
     * Valide les champs du formulaire
     * Verifie que le nom est rempli et le telephone est valide
     * @return true si les champs sont valides
     */
    private boolean validerChamps() {
        // Le nom est obligatoire
        if (nomField.getText().trim().isEmpty()) {
            erreur(new IllegalStateException("Le nom est obligatoire."));
            return false;
        }
        // Verifier le format du telephone (chiffres uniquement)
        String telephone = telephoneField.getText().trim();
        if (!telephone.isEmpty() && !telephone.matches("\\d{10,15}")) {
            erreur(new IllegalStateException("Le telephone doit contenir uniquement des chiffres (10-15 chiffres)."));
            return false;
        }
        return true;
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
telephoneField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
adresseField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    /**
     * Vide tous les champs du formulaire
     */
    private void viderChamps() {
        idField.setText("");
        nomField.setText("");
        telephoneField.setText("");
        adresseField.setText("");
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