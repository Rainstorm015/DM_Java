/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

/**
 * Cette classe instancie un objet de type Note contenant un double.
 */
public class Note {
    public double note;
    
    /**
     * Génère aléatoirement un double.
     */
    public Note(){        
        this.note = (double) Math.round((0 + Math.random() * (20 - 0)) * 100) / 100;;
    }
    
    public Note(double d){
        this.note = d;
    }

    public double getNote() {
        return note;
    }
    
    

    @Override
    public String toString() {
        return "" + note + "" ;
    }
    
}
