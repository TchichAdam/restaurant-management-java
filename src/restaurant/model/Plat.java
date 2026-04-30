package restaurant.model;

public class Plat {
    private int idPlat;
    private String nom;
    private double prix;
    private String categorie;

    public Plat(int idPlat, String nom, double prix, String categorie) {
        this.idPlat = idPlat;
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
    }

   

    public Plat() {
    }
    



    public int getIdPlat() {
        return idPlat;
    }



    public String getNom() {
        return nom;
    }



    public double getPrix() {
        return prix;
    }



    public String getCategorie() {
        return categorie;
    }

    


    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }



    public void setNom(String nom) {
        this.nom = nom;
    }



    public void setPrix(double prix) {
        this.prix = prix;
    }



    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }


    //class object
    @Override
    public String toString() {
        return "Plat [idPlat=" + idPlat + ", nom=" + nom + ", prix=" + prix + ", categorie=" + categorie + "]";
    }



    public void afficherPlat() {
        System.out.println(nom + " (" + categorie + ") : " + prix + " DH");
    }
}

