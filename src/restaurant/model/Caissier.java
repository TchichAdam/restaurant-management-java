package restaurant.model;

public class Caissier extends Employe {
  public Caissier(int id, String nom) {
        super(id, nom);
    }
    

    public Caissier() {
}



    @Override
    public void travailler() {
        System.out.println("Le caissier gère les paiements");
    }

    public void encaisser(double montant) {
        System.out.println("Paiement de " + montant + " DH effectué");
    }

    public void afficherFacture() {
        System.out.println("Facture imprimée");
    }


    @Override
    public String toString() {
        return "Caissier [id=" + id + ", nom=" + nom + "]";
    }
}
