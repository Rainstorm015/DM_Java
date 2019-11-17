/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe instancie un Eleve. Elle contient un nom, un prénom et
 * une liste de matière
 */
public class Eleve {

    public String nom;
    public String prenom;
    public List<Matiere> listMatiere = new ArrayList<>();

    public Eleve(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Permet d'ajouter les matières dans une liste en fonction du niveau de
     * l'élève. Elle permet également l'ajout aléatoire des matières facultatives
     * en appelant une autre méthode.
     * @param niveau String contenant le niveau
     */
    public void setListMatieres(String niveau) {
        String[] matieres = {
            "mathematiques",
            "francais",
            "anglais",
            "histoire-geographie",
            "physique",
            "sciences naturelles",
            "arts",
            "musique",
            "sport",
            "langue vivante"
        };

        for (String s : matieres) {
            if (niveau.equals("6")) {
                if (!s.equals("langue vivante") && !s.equals("physique")) {
                    listMatiere.add(new Matiere(s));
                }
            } else {
                listMatiere.add(new Matiere(s));
            }
        }

        int facultatif = 0 + (int) (Math.random() * ((2 - 0) + 1));

        for (int i = 0; i < facultatif; i++) {
            listMatiere.add(randomAleaMatiere());
        }

    }

    /**
     * Cette méthode renvoi une matiere facultative en testant au préalable sa 
     * présence dans la liste des matières. Dans le cas où elle est présente, la
     * méthode se rappelle pour en trouver une autre
     * @return un objet de type Matiere
     */
    public Matiere randomAleaMatiere() {
        String[] matieresAlea = {
            "latin",
            "grec",
            "anglais avance"
        };
        int indice = 0 + (int) (Math.random() * ((matieresAlea.length - 0)));
        boolean alreadyExists = false;
        for (Matiere m : listMatiere) {
            if (m.getNom().equals(matieresAlea[indice])) {
                alreadyExists = true;
            }
        }

        if (alreadyExists == true) {
            return randomAleaMatiere();
        } else {
            return new Matiere(matieresAlea[indice]);
        }        
    }

    public List<Matiere> getListMatiere() {
        return listMatiere;
    }
    
    @Override
    public String toString() {
        return "{" + "nom=" + nom + ", prenom=" + prenom + ", Matieres=" + listMatiere.toString() + '}';
    }

}
