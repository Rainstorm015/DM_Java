/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette Classe instancie une classe contenant un niveau, une section 
 * et une liste d'élèves de type Eleve
 */
public class Classe {
    public String niveau;
    public String section;
    public List<Eleve> listEleves = new ArrayList<>();

    public Classe(String niveau, String section) {
        this.niveau = niveau;
        this.section = section;
    }
    
    public Classe(String niveau, String section, List<Eleve> liste) {
        this.niveau = niveau;
        this.section = section;
        this.listEleves = liste;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Eleve> getListEleves() {
        return listEleves;
    }

    public void setListEleves(List<Eleve> listEleves) {
        this.listEleves = listEleves;
    }
    
    public void addToList(Eleve e){
        this.listEleves.add(e);
    }

    @Override
    public String toString() {
        return "Classe{" + "niveau=" + niveau + ", section=" + section + ", Eleves=" + listEleves + '}';
    }

    

    
}
