/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe instancie un objet de type Matiere. Il contient un nom, une
 * liste de Notes et un boolean pour savoir s'il la matière est optionnelle
 */
public class Matiere {

    public String nom;
    public boolean estOptionnelle = false;
    public List<Note> listNote = new ArrayList<>();

    /**
     * Le constructeur génère la matière en lui donnant un nom. Il génère 
     * également le nombre de notes nécessaire dans la liste de notes
     * @param nom 
     */
    public Matiere(String nom) {
        this.nom = nom;
        switch(nom){
            case "latin" : 
                this.estOptionnelle = true;
                break;
            case "grec":
                this.estOptionnelle = true;
                break;
            case "anglais avance":
                this.estOptionnelle = true;
                break;
        }
        int cpt;
        if(nom.equals("sport") || nom.equals("musique")){
            cpt = 2;
        } else {
            cpt = 3;
        }
        
        for(int i = 0 ; i < cpt ; i++){
            listNote.add(new Note());
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isEstOptionnelle() {
        return estOptionnelle;
    }

    public void setEstOptionnelle(boolean estOptionnelle) {
        this.estOptionnelle = estOptionnelle;
    }

    public List<Note> getListNote() {
        return listNote;
    }

    
    @Override
    public String toString() {
        return "{" + nom + ", notes=" + listNote.toString() + '}';
    }

    
}
