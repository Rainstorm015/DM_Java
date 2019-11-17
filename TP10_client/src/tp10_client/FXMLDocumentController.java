/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp10_client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author legra
 */
public class FXMLDocumentController implements Initializable {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    @FXML
    private Button button;

    @FXML
    private BarChart barchart1;

    @FXML
    private BarChart barchart2;

    @FXML
    private BarChart barchart3;

    @FXML
    private BarChart barchart4;

    @FXML
    private ComboBox combo1;

    @FXML
    private ComboBox combo2;

    @FXML
    private ComboBox combo3;

    @FXML
    private ComboBox combo4;

    @FXML
    private ComboBox combo5;

    @FXML
    private ComboBox combo6;

    @FXML
    private ComboBox combo7;

    @FXML
    private ComboBox combo8;

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
        "langue vivante",
        "latin",
        "grec",
        "anglais avance"
    };

    String[] classes = {"6", "5", "4", "3"};

    String[] epreuves = {"1", "2", "3"};

    /**
     * Récupère le changement effectué sur les combobox du premier graphique
     * @param event Evenement reçu
     */
    @FXML
    private void handleComboGraph1Action(ActionEvent event) {
        updateGraph1();
    }

    /**
     * Récupère le changement effectué sur les combobox du deuxième graphique
     * @param event Evenement reçu
     */
    @FXML
    private void handleComboGraph2Action(ActionEvent event) {
        updateGraph2();
    }

    /**
     * Récupère le changement effectué sur les combobox du troisième graphique
     * @param event Evenement reçu
     */
    @FXML
    private void handleComboGraph3Action(ActionEvent event) {
        updateGraph3();
    }

    /**
     * Récupère le changement effectué sur les combobox du quatrième graphique
     * @param event Evenement reçu
     */
    @FXML
    private void handleComboGraph4Action(ActionEvent event) {
        updateGraph4();
    }

    /**
     * Récupère le clique du boutton d'import. Démarre un file chooser et envoi
     * le contenu au serveur
     * @param event Evenement reçu
     */
    @FXML
    private void handleComboGraph5Action(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);
        String line = "";
        String fileContent = "";
        if (file != null) {
            String filename = file.getAbsolutePath();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                
                try {
                    while((line = reader.readLine()) != null){
                        fileContent = fileContent + line + ";";
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            sendRequest(fileContent);
        }
    }

    /**
     * Méthode permettant la mise à jour du graphique 1. Elle envoi au serveur
     * une requête sous forme csv et récupère la réponse qu'elle converti en XML
     * pour ensuite l'ajouter aux données du graphique
     */
    public void updateGraph1() {
        barchart1.getYAxis().setLabel("Note");
        String matiere = combo1.getValue().toString();
        String classe = combo2.getValue().toString();
        barchart1.getXAxis().setLabel("Classe de niveau " + classe);
        barchart1.setTitle("Moyenne de " + matiere + " par classe");
        BarChart.Series<String, Number> s1 = new BarChart.Series<>();
        s1.setName("ISEN");
        Document doc = convertToXML(sendRequest("getMoyenneByClasseAndMatiere;" + classe + ";" + matiere));
        List<Element> listDonnees = doc.getRootElement().getChildren();
        for (Element e : listDonnees) {
            String x = e.getChildText("x");
            String yText = e.getChildText("y");
            double y = Double.parseDouble(yText);
            s1.getData().add(new XYChart.Data<>(x, y));
        }

        barchart1.setData(FXCollections.observableArrayList(s1));

    }

    /**
     * Méthode permettant la mise à jour du graphique 2. Elle envoi au serveur
     * une requête sous forme csv et récupère la réponse qu'elle converti en XML
     * pour ensuite l'ajouter aux données du graphique
     */
    public void updateGraph2() {
        barchart2.getYAxis().setLabel("Nombre d'eleves");
        String matiere = combo4.getValue().toString();
        String classe = combo3.getValue().toString();
        int epreuve = Integer.parseInt(combo5.getValue().toString()) - 1;
        barchart2.getXAxis().setLabel("Classe de niveau " + classe);
        barchart2.setTitle("Gaussienne de " + matiere + " par niveau");
        BarChart.Series<String, Number> s1 = new BarChart.Series<>();
        s1.setName("ISEN");
        Document doc = convertToXML(sendRequest("getNbNotesParEpreuve;" + classe + ";" + matiere + ";" + epreuve));
        List<Element> listDonnees = doc.getRootElement().getChildren();
        for (Element e : listDonnees) {
            String x = e.getChildText("x");
            double y = Double.parseDouble(e.getChildText("y"));
            s1.getData().add(new XYChart.Data<>(x, y));
        }

        barchart2.setData(FXCollections.observableArrayList(s1));
    }

    /**
     * Méthode permettant la mise à jour du graphique 3. Elle envoi au serveur
     * une requête sous forme csv et récupère la réponse qu'elle converti en XML
     * pour ensuite l'ajouter aux données du graphique
     */
    public void updateGraph3() {
        barchart3.getYAxis().setLabel("Nombre d'eleves");
        String matiere = combo7.getValue().toString();
        String classe = combo6.getValue().toString();
        barchart3.getXAxis().setLabel("Classe de niveau " + classe);
        barchart3.setTitle("Gaussienne de " + matiere + " par niveau");
        BarChart.Series<String, Number> s1 = new BarChart.Series<>();
        s1.setName("ISEN");
        Document doc = convertToXML(sendRequest("getNbMoyennesParEpreuve;" + classe + ";" + matiere));
        List<Element> listDonnees = doc.getRootElement().getChildren();
        for (Element e : listDonnees) {
            String x = e.getChildText("x");
            double y = Double.parseDouble(e.getChildText("y"));
            s1.getData().add(new XYChart.Data<>(x, y));
        }

        barchart3.setData(FXCollections.observableArrayList(s1));
    }

    /**
     * Méthode permettant la mise à jour du graphique 4. Elle envoi au serveur
     * une requête sous forme csv et récupère la réponse qu'elle converti en XML
     * pour ensuite l'ajouter aux données du graphique
     */
    public void updateGraph4() {
        barchart4.getYAxis().setLabel("Nombre d'eleves");
        String classe = combo8.getValue().toString();
        barchart4.getXAxis().setLabel("Classe de niveau " + classe);
        barchart4.setTitle("Gaussienne des moyennes generales pour le niveau " + classe);
        BarChart.Series<String, Number> s1 = new BarChart.Series<>();
        s1.setName("ISEN");
        Document doc = convertToXML(sendRequest("getNbMoyennesGenerale;" + classe));
        List<Element> listDonnees = doc.getRootElement().getChildren();
        for (Element e : listDonnees) {
            String x = e.getChildText("x");
            double y = Double.parseDouble(e.getChildText("y"));
            s1.getData().add(new XYChart.Data<>(x, y));
        }
        barchart4.setData(FXCollections.observableArrayList(s1));
    }

    /**
     * Méthode qui converti un String en JSON puis en XML pour permettre l'ajout
     * dans els graphiques. Le passage au Json a été obligatoire afin d'éviter 
     * la perte de données lors de l'envoi par les sockets
     * @param s String contenant les données
     * @return un Document jdom
     */
    public Document convertToXML(String s) {
        JSONObject json = new JSONObject(s);
        String xml = XML.toString(json);
        Document doc = null;
        try {
            doc = new SAXBuilder().build(new StringReader(xml));
        } catch (JDOMException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }

    /**
     * Cette méthode initialise la connexion au serveur via un Socket,
     * les inputStream et les outputStream
     */
    public void initConnect() {
        try {
            socket = new Socket("localhost", 8080);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cette méthode ferme le socket
     */
    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cette méthode initialise un socket, envoi la commande et attend la réponse
     * puis ferme la connexion.
     * @param input Commande envoyé par les méthodes de mise à jour des graphs
     * @return la réponse du serveur
     */
    public String sendRequest(String input) {
        initConnect();
        String response = null;
        out.println(input);
        out.println("Test");
        try {
            response = in.readLine();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        closeSocket();
        return response;
    }

    /**
     * Initialisation des éléments graphique
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //c = new College();
        combo1.getItems().addAll(matieres);
        combo1.getSelectionModel().select(0);

        combo4.getItems().addAll(matieres);
        combo4.getSelectionModel().select(0);

        combo2.getItems().addAll(classes);
        combo2.getSelectionModel().select(0);

        combo3.getItems().addAll(classes);
        combo3.getSelectionModel().select(0);

        combo5.getItems().addAll(epreuves);
        combo5.getSelectionModel().select(0);

        combo6.getItems().addAll(classes);
        combo6.getSelectionModel().select(0);

        combo7.getItems().addAll(matieres);
        combo7.getSelectionModel().select(0);

        combo8.getItems().addAll(classes);
        combo8.getSelectionModel().select(0);

        //System.out.println(combo1.getValue().toString() + " " + combo2.getValue().toString());
        updateGraph1();
        updateGraph2();
        updateGraph3();
        updateGraph4();
    }

}
