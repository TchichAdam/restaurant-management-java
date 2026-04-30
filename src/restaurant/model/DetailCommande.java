package restaurant.model;

public class DetailCommande {
    private int idDetail;
    private int idCommande;
    private int idPlat;
    private int quantite;
    private double prixUnitaire;

    public DetailCommande(int idDetail, int idCommande, int idPlat, int quantite, double prixUnitaire) {
        this.idDetail = idDetail;
        this.idCommande = idCommande;
        this.idPlat = idPlat;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double calculerSousTotal() {
        return quantite * prixUnitaire;
    }

    @Override
    public String toString() {
        return "DetailCommande [idDetail=" + idDetail + ", idCommande=" + idCommande + ", idPlat=" + idPlat + ", quantite=" + quantite + ", prixUnitaire=" + prixUnitaire + "]";
    }
}
