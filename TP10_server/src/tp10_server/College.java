/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.XML;

/**
 * Cette classe instancie un collège. Il contient une liste de classes, 
 * et une liste d'élèves. 
 */
public class College {

    //Différents niveaux du collège
    public String[] niveaux = {"6", "5", "4", "3"};
    //Différentes sections
    public String[] sections = {"A", "B", "C", "D", "E", "F"};
    public List<Classe> listClasse = new ArrayList<>();
    public List<Eleve> listEleve = new ArrayList<>();
    public int nbEleves = (20 * sections.length) * niveaux.length;

    /**
     * Constructeur permettant la création des classes selon les tableaux
     * niveaux et sections.
     * Il permet également de creer le nombre exacte d'élèves et les répartie
     * dans chaque classe
     */
    public College() {
        for (String n : niveaux) {
            for (String s : sections) {
                listClasse.add(new Classe(n, s));
            }
        }
        for (int i = 0; i < this.nbEleves; i++) {
            listEleve.add(new Eleve("nom" + i, "prenom" + i));
        }

        //listEleve.forEach(e -> System.out.println(e.toString()));
        int cpt = 0;
        for (Classe c : listClasse) {
            if (c.getListEleves().size() != 20) {
                for (int i = cpt; i < cpt + 20; i++) {
                    listEleve.get(i).setListMatieres(c.getNiveau());
                    c.addToList(listEleve.get(i));
                }
                cpt = cpt + 20;
            }
        }

        //listClasse.forEach(c -> System.out.println(c.toString()));
    }
    /**
     * Extrait un Element jdom contenant une note
     * @param n Instance de note
     * @return un Element jdom contenant une note
     */
    public Element extractNote(Note n) {
        Element note = new Element("note").setText("" + n.getNote());
        return note;
    }

    /**
     * Extrait un Element jdom contenant une matière 
     * @param m Instance de matière
     * @return un Element jdom contenant une matière
     */
    public Element extractMatiere(Matiere m) {
        Element matiere = new Element("matiere");
        Element nom = new Element("nom").setText(m.getNom());
        Element notes = new Element("notes");
        for (Note n : m.getListNote()) {
            notes.addContent(extractNote(n));
        }
        matiere.addContent(nom);
        matiere.addContent(notes);
        return matiere;
    }

    /**
     * Extrait un Element jdom contenant un eleve
     * @param e Instance de eleve
     * @return un Element jdom contenant un eleve
     */
    public Element extractEleve(Eleve e) {
        Element eleve = new Element("eleve");
        Element nom = new Element("nom").setText(e.getNom());
        Element prenom = new Element("prenom").setText(e.getPrenom());
        Element matieres = new Element("matieres");
        for (Matiere m : e.getListMatiere()) {
            matieres.addContent(extractMatiere(m));
        }

        eleve.addContent(nom);
        eleve.addContent(prenom);
        eleve.addContent(matieres);

        return eleve;
    }

    /**
     *  Extrait un Element jdom contenant une classe
     * @param c Instance de classe
     * @return un Element jdom contenant une classe
     */
    public Element extractClass(Classe c) {
        Element classe = new Element("classe");
        Element niveau = new Element("niveau").setText(c.getNiveau());
        Element section = new Element("section").setText(c.getSection());
        Element eleves = new Element("eleves");
        for (Eleve e : c.getListEleves()) {
            eleves.addContent(extractEleve(e));
        }
        classe.addContent(niveau);
        classe.addContent(section);
        classe.addContent(eleves);
        return classe;
    }

    /**
     * Extrait en XML un Document jdom contenant une classe et tous ses Elements
     * @see extractClass
     * @see extractEleve
     * @see extractMatiere
     * @see extractNote
     */
    public void extractCollegeXML() {
        Document doc = new Document();
        Element racine = new Element("college");
        Element classes = new Element("classes");
        for (Classe c : listClasse) {
            classes.addContent(extractClass(c));
        }
        racine.addContent(classes);
        doc.setRootElement(racine);

        saveXML(doc);

    }

    /**
     * Extrait en JSON un Document jdom contenant une classe et tous ses Elements
     * @see extractClass
     * @see extractEleve
     * @see extractMatiere
     * @see extractNote
     */
    public void extractCollegeJSON() {
        Document doc = new Document();
        Element racine = new Element("college");
        Element classes = new Element("classes");
        for (Classe c : listClasse) {
            classes.addContent(extractClass(c));
        }
        racine.addContent(classes);
        doc.setRootElement(racine);

        saveJSON(doc);

    }

    /**
     * Enregistre un document jdom sur la machine
     * @param doc Document jdom
     */
    public void saveXML(Document doc) {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter("file.xml"));
            System.out.println("File Saved!");
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Convertit un Document jdom en JSONObject
     * @param doc Document jdom
     * @return un JSONObject
     */
    public JSONObject saveJSON(Document doc) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(doc);

        return XML.toJSONObject(xmlString);
    }

    /**
     * Affiche en console toutes les notes des élèves appartenant à un niveau
     * et une section.
     * @param niveau String contenant un niveau
     * @param section String contenant une section
     */
    public void getClasseNote(String niveau, String section) {
        List<Eleve> newListEleve = new ArrayList<>();
        List<Note> newListNote = new ArrayList<>();
        for (Classe c : listClasse) {
            if (c.getNiveau().equals(niveau) && c.getSection().equals(section)) {
                newListEleve = c.getListEleves();
            }
        }

        for (Eleve e : newListEleve) {
            for (Matiere m : e.getListMatiere()) {
                for (Note n : m.getListNote()) {
                    newListNote.add(n);
                }
            }
        }

        newListNote.forEach(n -> System.out.println(n));
    }

    /**
     * Affiche en console toutes les notes d'un élève
     * @param nom String contenant un nom
     * @param prenom String contenant un prenom
     */
    public void getEleveNote(String nom, String prenom) {
        List<Note> newListNote = new ArrayList<>();
        for (Classe c : listClasse) {
            for (Eleve e : c.getListEleves()) {
                if (e.getNom().equals(nom) && e.getPrenom().equals(prenom)) {
                    for (Matiere m : e.getListMatiere()) {
                        for (Note n : m.getListNote()) {
                            newListNote.add(n);
                        }
                    }
                }
            }

        }

        newListNote.forEach(n -> System.out.println(n));
    }

    /**
     * Affiche en console toutes les notes de tous les élèves d'un niveau
     * @param niveau String contenant un niveau
     */
    public void getClasseNiveauNote(String niveau) {
        List<Eleve> newListEleve = new ArrayList<>();
        List<Note> newListNote = new ArrayList<>();
        for (Classe c : listClasse) {
            if (c.getNiveau().equals(niveau)) {
                newListEleve = c.getListEleves();
            }
        }

        for (Eleve e : newListEleve) {
            for (Matiere m : e.getListMatiere()) {
                for (Note n : m.getListNote()) {
                    newListNote.add(n);
                }
            }
        }

        newListNote.forEach(n -> System.out.println(n));
    }

    /**
     * Retourne la valeur maximale d'une liste de notes
     * @param liste Liste de notes
     * @return  valeur maximale
     */
    public double getMaxFromList(List<Note> liste) {
        double result;
        result = liste.get(0).getNote();
        for (Note n : liste) {
            if (n.getNote() > result) {
                result = n.getNote();
            }
        }
        return result;
    }

    /**
     * Retourne la valeur minimale d'une liste de notes
     * @param liste Liste de notes
     * @return  valeur minimale
     */
    public double getMinFromList(List<Note> liste) {
        double result = 0.0;
        result = liste.get(0).getNote();
        for (Note n : liste) {
            if (n.getNote() < result) {
                result = n.getNote();
            }
        }
        return result;
    }

    /**
     * Affiche en console toutes les notes données en argument
     * @param mathNote note de mathématiques
     * @param frNote note de français
     * @param anNote note d'anglais
     * @param hgNote note d'histoire géographie
     * @param phNote note de physique
     * @param snNote note de science naturelle
     * @param artNote note d'art
     * @param musicNote note de musique
     * @param sportNote note de sport
     * @param lanViNote note de langue vivante
     * @param latNote note de latin
     * @param greNote note de grec
     * @param anavNote note d'anglais avancé
     */
    public void displayByDouble(double mathNote, double frNote, double anNote,
            double hgNote, double phNote, double snNote, double artNote,
            double musicNote, double sportNote, double lanViNote,
            double latNote, double greNote, double anavNote) {
        System.out.println("mathematiques : " + mathNote);
        System.out.println("francais : " + frNote);
        System.out.println("anglais : " + anNote);
        System.out.println("histoire-geographie : " + hgNote);
        System.out.println("physique : " + phNote);
        System.out.println("sciences naturelles : " + snNote);
        System.out.println("arts : " + artNote);
        System.out.println("musique : " + musicNote);
        System.out.println("sport : " + sportNote);
        System.out.println("langue vivante : " + lanViNote);
        System.out.println("latin : " + latNote);
        System.out.println("grec : " + greNote);
        System.out.println("anglais avance : " + anavNote);

    }

    /**
     * Récupère toutes les notes maximale d'un élève et les affiche en console
     */
    public void getMax() {
        List<Note> mathNote = new ArrayList<>();
        List<Note> frNote = new ArrayList<>();
        List<Note> anNote = new ArrayList<>();
        List<Note> hgNote = new ArrayList<>();
        List<Note> phNote = new ArrayList<>();
        List<Note> snNote = new ArrayList<>();
        List<Note> artNote = new ArrayList<>();
        List<Note> musicNote = new ArrayList<>();
        List<Note> sportNote = new ArrayList<>();
        List<Note> lanViNote = new ArrayList<>();
        List<Note> latNote = new ArrayList<>();
        List<Note> greNote = new ArrayList<>();
        List<Note> anavNote = new ArrayList<>();

        for (Classe c : listClasse) {
            System.out.println("===== " + c.getNiveau() + c.getSection() + " =====");
            for (Eleve e : c.getListEleves()) {
                for (Matiere m : e.getListMatiere()) {
                    for (Note n : m.getListNote()) {
                        switch (m.getNom()) {
                            case "mathematiques":
                                mathNote.add(n);
                                break;
                            case "francais":
                                frNote.add(n);
                                break;
                            case "anglais":
                                anNote.add(n);
                                break;
                            case "histoire-geographie":
                                hgNote.add(n);
                                break;
                            case "physique":
                                phNote.add(n);
                                break;
                            case "sciences naturelles":
                                snNote.add(n);
                                break;
                            case "arts":
                                artNote.add(n);
                                break;
                            case "musique":
                                musicNote.add(n);
                                break;
                            case "sport":
                                sportNote.add(n);
                                break;
                            case "langue vivante":
                                lanViNote.add(n);
                                break;
                            case "latin":
                                latNote.add(n);
                                break;
                            case "grec":
                                greNote.add(n);
                                break;
                            case "anglais avance":
                                anavNote.add(n);
                                break;

                        }

                    }
                }
            }
            double math = getMaxFromList(mathNote);
            double fr = getMaxFromList(frNote);
            double an = getMaxFromList(anNote);
            double hg = getMaxFromList(hgNote);
            double ph = getMaxFromList(phNote);
            double sn = getMaxFromList(snNote);
            double art = getMaxFromList(artNote);
            double music = getMaxFromList(musicNote);
            double sport = getMaxFromList(sportNote);
            double lanVi = getMaxFromList(lanViNote);
            double lat = getMaxFromList(latNote);
            double gre = getMaxFromList(greNote);
            double anav = getMaxFromList(anavNote);

            displayByDouble(math, fr, an, hg, ph, sn, art, music, sport, lanVi,
                    lat, gre, anav);
            mathNote = new ArrayList<>();
            frNote = new ArrayList<>();
            anNote = new ArrayList<>();
            hgNote = new ArrayList<>();
            phNote = new ArrayList<>();
            snNote = new ArrayList<>();
            artNote = new ArrayList<>();
            musicNote = new ArrayList<>();
            sportNote = new ArrayList<>();
            lanViNote = new ArrayList<>();
            latNote = new ArrayList<>();
            greNote = new ArrayList<>();
            anavNote = new ArrayList<>();
        }
    }

    /**
     * Récupère toutes les notes minimale d'un élève et les affiche en console
     */
    public void getMin() {
        List<Note> mathNote = new ArrayList<>();
        List<Note> frNote = new ArrayList<>();
        List<Note> anNote = new ArrayList<>();
        List<Note> hgNote = new ArrayList<>();
        List<Note> phNote = new ArrayList<>();
        List<Note> snNote = new ArrayList<>();
        List<Note> artNote = new ArrayList<>();
        List<Note> musicNote = new ArrayList<>();
        List<Note> sportNote = new ArrayList<>();
        List<Note> lanViNote = new ArrayList<>();
        List<Note> latNote = new ArrayList<>();
        List<Note> greNote = new ArrayList<>();
        List<Note> anavNote = new ArrayList<>();

        for (Classe c : listClasse) {
            System.out.println("===== " + c.getNiveau() + c.getSection() + " =====");
            for (Eleve e : c.getListEleves()) {
                for (Matiere m : e.getListMatiere()) {
                    for (Note n : m.getListNote()) {
                        switch (m.getNom()) {
                            case "mathematiques":
                                mathNote.add(n);
                                break;
                            case "francais":
                                frNote.add(n);
                                break;
                            case "anglais":
                                anNote.add(n);
                                break;
                            case "histoire-geographie":
                                hgNote.add(n);
                                break;
                            case "physique":
                                phNote.add(n);
                                break;
                            case "sciences naturelles":
                                snNote.add(n);
                                break;
                            case "arts":
                                artNote.add(n);
                                break;
                            case "musique":
                                musicNote.add(n);
                                break;
                            case "sport":
                                sportNote.add(n);
                                break;
                            case "langue vivante":
                                lanViNote.add(n);
                                break;
                            case "latin":
                                latNote.add(n);
                                break;
                            case "grec":
                                greNote.add(n);
                                break;
                            case "anglais avance":
                                anavNote.add(n);
                                break;

                        }

                    }
                }
            }
            double math = getMinFromList(mathNote);
            double fr = getMinFromList(frNote);
            double an = getMinFromList(anNote);
            double hg = getMinFromList(hgNote);
            double ph = getMinFromList(phNote);
            double sn = getMinFromList(snNote);
            double art = getMinFromList(artNote);
            double music = getMinFromList(musicNote);
            double sport = getMinFromList(sportNote);
            double lanVi = getMinFromList(lanViNote);
            double lat = getMinFromList(latNote);
            double gre = getMinFromList(greNote);
            double anav = getMinFromList(anavNote);

            displayByDouble(math, fr, an, hg, ph, sn, art, music, sport, lanVi,
                    lat, gre, anav);
            mathNote = new ArrayList<>();
            frNote = new ArrayList<>();
            anNote = new ArrayList<>();
            hgNote = new ArrayList<>();
            phNote = new ArrayList<>();
            snNote = new ArrayList<>();
            artNote = new ArrayList<>();
            musicNote = new ArrayList<>();
            sportNote = new ArrayList<>();
            lanViNote = new ArrayList<>();
            latNote = new ArrayList<>();
            greNote = new ArrayList<>();
            anavNote = new ArrayList<>();
        }
    }

    /**
     * Calcule la moyenne à partie d'une liste de notes
     * @param note Liste de type Note
     * @return la moyenne de la liste
     */
    public double getMoyenneFromList(List<Note> note) {
        double result = 0.0;
        for (Note n : note) {
            result = result + n.getNote();
        }

        return result / note.size();
    }

    /**
     * Calcule la moyenne à partie d'une liste de doucle
     * @param note Liste de type double
     * @return la moyenne de la liste
     */
    public double getMoyenneFromListOfDouble(List<Double> note) {
        double result = 0.0;
        for (double d : note) {
            result = result + d;
        }

        return result / note.size();
    }

    /**
     * Récupère et affiche les moyennes par matière et par élève
     */
    public void getMoyenne() {
        List<Note> mathNote = new ArrayList<>();
        List<Note> frNote = new ArrayList<>();
        List<Note> anNote = new ArrayList<>();
        List<Note> hgNote = new ArrayList<>();
        List<Note> phNote = new ArrayList<>();
        List<Note> snNote = new ArrayList<>();
        List<Note> artNote = new ArrayList<>();
        List<Note> musicNote = new ArrayList<>();
        List<Note> sportNote = new ArrayList<>();
        List<Note> lanViNote = new ArrayList<>();
        List<Note> latNote = new ArrayList<>();
        List<Note> greNote = new ArrayList<>();
        List<Note> anavNote = new ArrayList<>();

        for (Classe c : listClasse) {
            System.out.println("===== " + c.getNiveau() + c.getSection() + " =====");
            for (Eleve e : c.getListEleves()) {
                for (Matiere m : e.getListMatiere()) {
                    for (Note n : m.getListNote()) {
                        switch (m.getNom()) {
                            case "mathematiques":
                                mathNote.add(n);
                                break;
                            case "francais":
                                frNote.add(n);
                                break;
                            case "anglais":
                                anNote.add(n);
                                break;
                            case "histoire-geographie":
                                hgNote.add(n);
                                break;
                            case "physique":
                                phNote.add(n);
                                break;
                            case "sciences naturelles":
                                snNote.add(n);
                                break;
                            case "arts":
                                artNote.add(n);
                                break;
                            case "musique":
                                musicNote.add(n);
                                break;
                            case "sport":
                                sportNote.add(n);
                                break;
                            case "langue vivante":
                                lanViNote.add(n);
                                break;
                            case "latin":
                                latNote.add(n);
                                break;
                            case "grec":
                                greNote.add(n);
                                break;
                            case "anglais avance":
                                anavNote.add(n);
                                break;

                        }

                    }
                }
            }
            double math = getMoyenneFromList(mathNote);
            double fr = getMoyenneFromList(frNote);
            double an = getMoyenneFromList(anNote);
            double hg = getMoyenneFromList(hgNote);
            double ph = getMoyenneFromList(phNote);
            double sn = getMoyenneFromList(snNote);
            double art = getMoyenneFromList(artNote);
            double music = getMoyenneFromList(musicNote);
            double sport = getMoyenneFromList(sportNote);
            double lanVi = getMoyenneFromList(lanViNote);
            double lat = getMoyenneFromList(latNote);
            double gre = getMoyenneFromList(greNote);
            double anav = getMoyenneFromList(anavNote);

            displayByDouble(math, fr, an, hg, ph, sn, art, music, sport, lanVi,
                    lat, gre, anav);
        }
    }

    /**
     * Récupère la moyenne d'une classe en fonction d'une matière
     * @param classe classe de type classe
     * @param matiere String contenant le nom d'une matière
     * @return la moyenne de la classe
     */
    public double getMoyenneByClasseAndMatiere(Classe classe, String matiere) {
        List<Note> notes = new ArrayList<>();
        Double moyenneNotes = 0.0;
        for (Classe c : listClasse) {
            if (c == classe) {
                for (Eleve e : c.getListEleves()) {
                    for (Matiere m : e.getListMatiere()) {
                        if (m.getNom().equals(matiere)) {
                            for (Note n : m.getListNote()) {
                                notes.add(n);

                            }
                        }

                    }
                }
            }

            moyenneNotes = getMoyenneFromList(notes);
            if (moyenneNotes.isNaN()) {
                moyenneNotes = 0.0;
            }
        }
        return moyenneNotes;
    }

    /**
     * Récupère la médiane d'une liste de notes
     * @param liste Liste de type note
     * @return retourne la valeur médiane
     */
    public double getMedianeByList(List<Note> liste) {
        List<Double> newList = new ArrayList<>();
        liste.forEach(s -> newList.add(s.getNote()));
        Collections.sort(newList);
        if (liste.size() % 2 == 0) {
            return liste.get(liste.size() / 2).getNote();
        } else {
            return liste.get((liste.size() / 2) + 1).getNote();
        }
    }

    /**
     * Récupère et affiche en console la valeur médiane de chaque étudiant de 
     * chaque matière
     */
    public void getMediane() {
        List<Note> mathNote = new ArrayList<>();
        List<Note> frNote = new ArrayList<>();
        List<Note> anNote = new ArrayList<>();
        List<Note> hgNote = new ArrayList<>();
        List<Note> phNote = new ArrayList<>();
        List<Note> snNote = new ArrayList<>();
        List<Note> artNote = new ArrayList<>();
        List<Note> musicNote = new ArrayList<>();
        List<Note> sportNote = new ArrayList<>();
        List<Note> lanViNote = new ArrayList<>();
        List<Note> latNote = new ArrayList<>();
        List<Note> greNote = new ArrayList<>();
        List<Note> anavNote = new ArrayList<>();

        for (Classe c : listClasse) {
            System.out.println("===== " + c.getNiveau() + c.getSection() + " =====");
            for (Eleve e : c.getListEleves()) {
                for (Matiere m : e.getListMatiere()) {
                    for (Note n : m.getListNote()) {
                        switch (m.getNom()) {
                            case "mathematiques":
                                mathNote.add(n);
                                break;
                            case "francais":
                                frNote.add(n);
                                break;
                            case "anglais":
                                anNote.add(n);
                                break;
                            case "histoire-geographie":
                                hgNote.add(n);
                                break;
                            case "physique":
                                phNote.add(n);
                                break;
                            case "sciences naturelles":
                                snNote.add(n);
                                break;
                            case "arts":
                                artNote.add(n);
                                break;
                            case "musique":
                                musicNote.add(n);
                                break;
                            case "sport":
                                sportNote.add(n);
                                break;
                            case "langue vivante":
                                lanViNote.add(n);
                                break;
                            case "latin":
                                latNote.add(n);
                                break;
                            case "grec":
                                greNote.add(n);
                                break;
                            case "anglais avance":
                                anavNote.add(n);
                                break;

                        }

                    }
                }
            }
            double math = getMedianeByList(mathNote);
            double fr = getMedianeByList(frNote);
            double an = getMedianeByList(anNote);
            double hg = getMedianeByList(hgNote);
            double ph = getMedianeByList(phNote);
            double sn = getMedianeByList(snNote);
            double art = getMedianeByList(artNote);
            double music = getMedianeByList(musicNote);
            double sport = getMedianeByList(sportNote);
            double lanVi = getMedianeByList(lanViNote);
            double lat = getMedianeByList(latNote);
            double gre = getMedianeByList(greNote);
            double anav = getMedianeByList(anavNote);

            displayByDouble(math, fr, an, hg, ph, sn, art, music, sport, lanVi,
                    lat, gre, anav);
        }
    }

    /**
     * Met à jour les notes d'une matière en fonction d'une ligne d'un fichier 
     * txt. 
     * @param list List contenant les 3 élélement d'une ligne du txt
     * @param matiere String du nom de la matière
     * @param classe String contenant le niveau et la section
     */
    public void updateMatiere(List<String> list, String matiere, String classe) {
        String matiereToUpdate = "";
        switch (matiere) {
            case "MATHEMATIQUES":
                matiereToUpdate = "mathematiques";
                break;
            case "FRANCAIS":
                matiereToUpdate = "francais";
                break;
            case "ANGLAIS":
                matiereToUpdate = "anglais";
                break;
            case "HISTOIREGEO":
                matiereToUpdate = "histoire-geographie";
                break;
            case "PHYSIQUE":
                matiereToUpdate = "physique";
                break;
            case "SCIENCENAT":
                matiereToUpdate = "sciences naturelles";
                break;
            case "ARTS":
                matiereToUpdate = "arts";
                break;
            case "MUSIQUE":
                matiereToUpdate = "musique";
                break;
            case "SPORT":
                matiereToUpdate = "sport";
                break;
            case "LANGUEVIVANTE":
                matiereToUpdate = "langue vivante";
                break;
            case "LATIN":
                matiereToUpdate = "latin";
                break;
            case "GREC":
                matiereToUpdate = "grec";
                break;
            case "ANGLAISAVANCE":
                matiereToUpdate = "anglais avance";
                break;
        }
        for (Classe c : listClasse) {
            if (classe.split("")[0].equals(c.getNiveau()) && classe.split("")[1].equals(c.getSection())) {
                for (Eleve e : c.getListEleves()) {
                    for (String s : list) {
                        if (e.getNom().equals(s.split(" ")[0]) && e.getPrenom().equals(s.split(" ")[1])) {
                            for (Matiere m : e.getListMatiere()) {
                                if (m.getNom().equals(matiereToUpdate)) {
                                    m.getListNote().add(new Note(Double.parseDouble(s.split(" ")[2])));
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    /**
     * Lit un fichier file.txt et ajoute les notes. Termine par l'extraction du
     * XML.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void importFromFile() throws FileNotFoundException, IOException {
        String filename = "file.txt";
        String classe = "";
        String matiere = "";
        List<String> eleves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 2) {
                    System.out.println("new classe : " + line);
                    classe = line;
                    matiere = "";
                    eleves = new ArrayList<String>();
                }
                if ((line.split(" ").length == 1 && line.length() > 2) && !line.equals(matiere)) {
                    if (!matiere.equals("")) {
                        updateMatiere(eleves, matiere, classe);
                    }
                    System.out.println("new matiere : " + line);
                    matiere = line;
                }
                if (line.split(" ").length > 1) {
                    System.out.println("new eleve : " + line);
                    eleves.add(line);
                }

            }
        }
        extractCollegeXML();
    }

    /**
     * Récupère le nombre d'élève ayant une note entre min et max pour une 
     * matière et une classe donnée.
     * @param classe classe de type Classe
     * @param matiere String contenant le nom d'une matière
     * @param epreuve indice d'une épreuve
     * @param min minimum de l'écart
     * @param max maximum de l'écart
     * @return retourne le nombre d'élève
     */
    public int getNbNotesParEpreuve(Classe classe, String matiere, int epreuve, int min, int max) {
        int number = 0;
        for (Eleve e : classe.listEleves) {
            for (Matiere m : e.getListMatiere()) {
                if (m.getNom().equals(matiere)) {
                    double note = m.getListNote().get(epreuve).getNote();
                    if (note >= min && note < max) {
                        number = number + 1;
                    }
                }
            }

        }
        //System.out.println("number : " + number);
        return number;
    }

    /**
     * Récupère le nombre d'élève ayant une moyenne entre min et max pour une 
     * matière et une classe donnée.
     * @param classe classe de type Classe
     * @param matiere String contenant le nom d'une matière
     * @param min minimum de l'écart
     * @param max maximum de l'écart
     * @return retourne le nombre d'élève
     */
    public int getNbMoyennesParEpreuve(Classe classe, String matiere, int min, int max) {
        int number = 0;
        for (Eleve e : classe.listEleves) {
            for (Matiere m : e.getListMatiere()) {
                if (m.getNom().equals(matiere)) {
                    double note = getMoyenneFromList(m.getListNote());
                    if (note >= min && note < max) {
                        number = number + 1;
                    }
                }
            }

        }
        return number;
    }

    /**
     * Récupère la moyenne d'une matière
     * @param m matiere de type Matiere
     * @return retourne la moyenne
     */
    public double getMoyenneByMatiere(Matiere m) {
        return getMoyenneFromList(m.getListNote());
    }

    /**
     * Récupère la moyenne générale d'une élève (sans les matières facultatives)
     * @param e élève de type Eleve
     * @return la moyenne des matières
     */
    public double getMoyenneGeneraleByEleve(Eleve e) {
        List<Double> notes = new ArrayList<>();
        for (Matiere m : e.getListMatiere()) {
            if (m.isEstOptionnelle() == false) {
                notes.add(getMoyenneByMatiere(m));
            }
        }
        return getMoyenneFromListOfDouble(notes);
    }

    /**
     * Récupère les points qui seront ajoutés à la moyenne d'un élève
     * @param e élève de type Eleve
     * @return le nombre de points à rajouter
     */
    public double getPointMatiereFacultatives(Eleve e) {
        List<Double> notes = new ArrayList<>();
        double points = 0.0;
        for (Matiere m : e.getListMatiere()) {
            if (m.isEstOptionnelle() == true) {
                notes.add(getMoyenneByMatiere(m));
            }
        }
        Double moyenne = getMoyenneFromListOfDouble(notes);

        if (moyenne > 10) {
            points = (moyenne - 10) * 0.1;
        }
        return points;
    }

    /**
     * Récupère le nombre de moyenne générale entre min et max
     * @param c classe de type Classe
     * @param min minimum de l'écart
     * @param max maximum de l'écart
     * @return le nombre d'élèves
     */
    public int getNbMoyennesGenerale(Classe c, int min, int max) {
        int number = 0;
        for (Eleve e : c.listEleves) {
            double moyenne = getMoyenneGeneraleByEleve(e) + getPointMatiereFacultatives(e);
            if (moyenne >= min && moyenne < max) {
                number = number + 1;
            }
        }

        return number;
    }

    /**
     * Converti un document jdom en json pour ensuite le convertir en String.
     * Permet d'éviter les problèmes d'envoi par socket
     * @param doc Document jdom
     * @return L'objet converti en string
     * @throws IOException
     * @throws JDOMException 
     */
    public String docToString(Document doc) throws IOException, JDOMException {
        JSONObject json = saveJSON(doc);

        return json.toString();
    }

    /**
     * Creer un Element jdom contenant l'indice x et y permettant l'ajout dans 
     * un graphique.
     * @param x Element x
     * @param y Element y
     * @return retourne un Element donnee
     */
    public Element extractDonnee(String x, String y) {
        Element donnee = new Element("donnee");
        Element xData = new Element("x").setText(x);
        Element yData = new Element("y").setText(y);

        donnee.addContent(xData);
        donnee.addContent(yData);
        return donnee;
    }

    /**
     * Récupère les moyennes par classe et par matière puis les converti en 
     * String
     * @param niveau String contenant le niveau
     * @param matiere String contenant la matière
     * @return La String contenant toutes les données
     */
    public String extractgetMoyenneByClasseAndMatiere(String niveau, String matiere) {
        Document doc = new Document();
        Element root = new Element("donnees");
        String x = "";
        String moyenne = "";
        for (Classe cl : getListClasse()) {
            if (cl.getNiveau().equals(niveau)) {
                for (int i = 0; i < 20; i++) {
                    x = cl.getNiveau() + cl.getSection();
                    moyenne = "" + getMoyenneByClasseAndMatiere(cl, matiere);
                    root.addContent(extractDonnee(x, moyenne));
                }

            }
        }

        doc.setRootElement(root);
        String result = "";
        try {
            result = docToString(doc);
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Récupère le nombre de moyennes par niveau, par matière, et par épreuve
     * Les données sont ensuite converties en String
     * @param niveau String contenant le niveau
     * @param matiere String contenant la matière
     * @param epreuve String contenant l'indice de l'épreuve
     * @return retourne les données en String
     */
    public String extractgetNbNotesParEpreuve(String niveau, String matiere, String epreuve) {
        Document doc = new Document();
        Element root = new Element("donnees");
        String x = "";
        String moyenne = "";
        for (Classe cl : getListClasse()) {
            if (cl.getNiveau().equals(niveau)) {
                for (int i = 0; i < 20; i++) {
                    x = i + "-" + (i + 1);
                    moyenne = "" + getNbNotesParEpreuve(cl, matiere, Integer.parseInt(epreuve), i, i + 1);
                    root.addContent(extractDonnee(x, moyenne));
                }

            }
        }

        doc.setRootElement(root);
        String result = "";
        try {
            result = docToString(doc);
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Récupère le nombre de moyennes par niveau, et par matière
     * Les données sont ensuite converties en String
     * @param niveau String contenant le niveau
     * @param matiere String contenant la matière
     * @return retourne les données en String
     */
    public String extractNbMoyenneParEpreuve(String niveau, String matiere) {
        Document doc = new Document();
        Element root = new Element("donnees");
        String x = "";
        String moyenne = "";
        for (Classe cl : getListClasse()) {
            if (cl.getNiveau().equals(niveau)) {
                for (int i = 0; i < 20; i++) {
                    x = i + "-" + (i + 1);
                    moyenne = "" + getNbMoyennesParEpreuve(cl, matiere, i, i + 1);
                    root.addContent(extractDonnee(x, moyenne));
                }

            }
        }

        doc.setRootElement(root);
        String result = "";
        try {
            result = docToString(doc);
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Récupère le nombre de moyennes générales par niveau
     * Les données sont ensuite converties en String
     * @param niveau String contenant le niveau
     * @return retourne les données en String
     */
    public String extractgetNbMoyennesGenerale(String niveau) {
        Document doc = new Document();
        Element root = new Element("donnees");
        String x = "";
        String moyenne = "";
        for (Classe cl : getListClasse()) {
            if (cl.getNiveau().equals(niveau)) {
                for (int i = 0; i < 20; i++) {
                    x = i + "-" + (i + 1);
                    moyenne = "" + getNbMoyennesGenerale(cl, i, i + 1);
                    root.addContent(extractDonnee(x, moyenne));
                }

            }
        }

        doc.setRootElement(root);
        String result = "";
        try {
            result = docToString(doc);
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Permet de définir quelles données renvoyer en fonction de la commande 
     * reçu via le socket
     * @param command String contenant la commande du client
     * @return les données correspondantes
     */
    public String extractDataForClient(String command) {
        String result = "";
        String[] cmd = command.split(";");
        switch (cmd[0]) {
            case "getMoyenneByClasseAndMatiere":
                result = extractgetMoyenneByClasseAndMatiere(cmd[1], cmd[2]);
                break;
            case "getNbNotesParEpreuve":
                result = extractgetNbNotesParEpreuve(cmd[1], cmd[2], cmd[3]);
                break;
            case "getNbMoyennesParEpreuve":
                result = extractNbMoyenneParEpreuve(cmd[1], cmd[2]);
                break;
            case "getNbMoyennesGenerale":
                result = extractgetNbMoyennesGenerale(cmd[1]);
                break;
            default: {
                try {
                    saveStringToFile(command);
                    importFromString();
                } catch (IOException ex) {
                    Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

        }

        return result;
    }

    /**
     * Récupère le fichier txt envoyé par le client et l'enregistre dans un fichier
     * @param command String envoyée par le client
     */
    public void saveStringToFile(String command) {
        String filename = "filename.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            String[] lines = command.split(";");
            for (String s : lines) {
                writer.write(s + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(College.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Lit un fichier txt pour le parser et importer les notes
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void importFromString() throws FileNotFoundException, IOException {
        String filename = "filename.txt";
        String classe = "";
        String matiere = "";
        List<String> eleves = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 2) {
                    classe = line;
                    matiere = "";
                    eleves = new ArrayList<String>();
                }
                if ((line.split(" ").length == 1 && line.length() > 2) && !line.equals(matiere)) {
                    if (!matiere.equals("")) {
                        updateMatiere(eleves, matiere, classe);
                    }
                    matiere = line;
                }
                if (line.split(" ").length > 1) {
                    eleves.add(line);
                }

            }
            extractCollegeXML();
        }

    }

    /*
    Getters et setters classiques
    */
    
    public String[] getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(String[] niveaux) {
        this.niveaux = niveaux;
    }

    public String[] getSections() {
        return sections;
    }

    public void setSections(String[] sections) {
        this.sections = sections;
    }

    public List<Classe> getListClasse() {
        return listClasse;
    }

    public void setListClasse(List<Classe> listClasse) {
        this.listClasse = listClasse;
    }

    public int getNbEleves() {
        return nbEleves;
    }

    public void setNbEleves(int nbEleves) {
        this.nbEleves = nbEleves;
    }

}
